package org.example.hackatonapi.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hackatonapi.config.BotConfig;
import org.example.hackatonapi.menu.ActionMenu;
import org.example.hackatonapi.menu.CurrencyMenu;
import org.example.hackatonapi.menu.DateMenu;
import org.example.hackatonapi.model.State;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.services.ApiService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static final HashMap<Long, State> map = new HashMap<>();
    final BotConfig config;
    private final ApiService apiService;

    public TelegramBot(BotConfig config, ApiService apiService) {
        this.config = config;
        this.apiService = apiService;
        List<BotCommand> listOfCommands = Arrays.asList(
                new BotCommand("/start", "get a welcome message"),
                new BotCommand("/mydata", "get your data store"),
                new BotCommand("/help", "how to use this bot"),
                new BotCommand("/settings", "set your preferences")
        );

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            System.err.println("Error setting bot's command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (chatId == null) {
                System.err.println("Received an update with null chatId.");
                return;
            }

            if (!map.containsKey(chatId)) {
                map.put(chatId, new State());
            }

            switch (messageText) {
                case "/start" -> {
                    SendMessage message = ActionMenu.getMenu();
                    message.setText("Привет! Чтобы воспользоваться функционалом бота сперва выбери банк из меню снизу");
                    message.setChatId(chatId);
                    sendMessage(message);
                }
                case "Беларусбанк" -> sendMessage(setBankState("BELBANK", chatId));

                case "Альфа банк" -> sendMessage(setBankState("ALFA", chatId));

                case "НБРБ" -> sendMessage(setBankState("NBRB", chatId));

                case "USD", "RUB", "EUR" -> sendMessage(setCurrencyState(messageText, chatId));

                case "Выбрать другой банк" -> {
                    SendMessage message = ActionMenu.getMenu();
                    message.setChatId(chatId);
                    message.setText(messageText);
                    sendMessage(message);
                }
                case "Выбрать другую валюту" -> {
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setChatId(chatId);
                    message.setText(messageText);
                    sendMessage(message);
                }

                case "Курс на текущий день" -> {
                    CurrencyDTO currencyRateForDate = apiService.getCurrencyRateForDate(map.get(chatId).getCurrencyName(), map.get(chatId).getBankName(), LocalDate.now().toString());

                    SendMessage message = new SendMessage();
                    message.setChatId(chatId);
                    message.setText("Курс покупки: " + currencyRateForDate.getOffBuyRate() +
                            "\nКурс продажи: " + currencyRateForDate.getOffSellRate());
                    sendMessage(message);
                }
                case "Курс на выбранный день" -> {
                    SendMessage promptMessage = new SendMessage();
                    promptMessage.setChatId(chatId);
                    promptMessage.setText("Введите дату в формате YYYY-MM-DD (например, 2024-02-03):");
                    sendMessage(promptMessage);

                    String inputDate = update.getMessage().getText().trim();

                    try {
                        LocalDate selectedDate = LocalDate.parse(inputDate);
                        CurrencyDTO currencyRateForSelectedDate = apiService.getCurrencyRateForDate(
                                map.get(chatId).getCurrencyName(),
                                map.get(chatId).getBankName(),
                                String.valueOf(selectedDate)
                        );

                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);
                        message.setText("Курс покупки: " + currencyRateForSelectedDate.getOffBuyRate() +
                                "\nКурс продажи: " + currencyRateForSelectedDate.getOffSellRate());
                        sendMessage(message);
                    } catch (DateTimeParseException e) {
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(chatId);
                        errorMessage.setText("Некорретная информация");
                        sendMessage(errorMessage);
                    } catch (Exception e) {
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(chatId);
                        errorMessage.setText("Runtime error");
                        sendMessage(errorMessage);
                    }
                }

                case "Статистика" -> {
                    byte[] png = apiService.getStatisticsAsPNG(map.get(chatId).getCurrencyName(), map.get(chatId).getBankName(), "2024-01-31", LocalDate.now().toString());

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);

                    try {
                        BufferedImage image = byteArrayToImage(png);

                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", os);
                        InputStream is = new ByteArrayInputStream(os.toByteArray());

                        sendPhoto.setPhoto(new InputFile(is, "your_filename.png"));
                        execute(sendPhoto);

                    } catch (IOException | TelegramApiException e) {
                        System.err.println("Error sending photo: " + e.getMessage());
                    }
                }
                default -> {
                    SendMessage promptMessage = new SendMessage();
                    promptMessage.setChatId(chatId);
                    promptMessage.setText("Введите команду или дату в формате YYYY-MM-DD (например, 2024-02-03), чтобы получить курс на текущую дату:");
                    sendMessage(promptMessage);

                    String inputDate = update.getMessage().getText().trim();

                    try {
                        LocalDate selectedDate = LocalDate.parse(inputDate);
                        CurrencyDTO currencyRateForSelectedDate = apiService.getCurrencyRateForDate(
                                map.get(chatId).getCurrencyName(),
                                map.get(chatId).getBankName(),
                                String.valueOf(selectedDate)
                        );

                        SendMessage message = new SendMessage();
                        message.setChatId(chatId);
                        message.setText("Курс покупки: " + currencyRateForSelectedDate.getOffBuyRate() +
                                "\nКурс продажи: " + currencyRateForSelectedDate.getOffSellRate());
                        sendMessage(message);
                    } catch (DateTimeParseException e) {
                        SendMessage errorMessage = new SendMessage();
                        errorMessage.setChatId(chatId);
                        errorMessage.setText("Вы ввели некорректную команду");
                        sendMessage(errorMessage);
                    }
                }
            }
        }
    }

    private static BufferedImage byteArrayToImage(byte[] byteData) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
        return ImageIO.read(bis);
    }

    private SendMessage setCurrencyState(String messageText, Long chatId) {
        map.get(chatId).setCurrencyName(messageText);
        SendMessage message = DateMenu.getMenu();
        message.setChatId(chatId);
        message.setText(messageText);
        sendMessage(message);
        return message;
    }

    private SendMessage setBankState(String messageText, Long chatId) {
        map.get(chatId).setBankName(messageText);
        SendMessage message = CurrencyMenu.getMenu();
        message.setText(messageText);
        message.setChatId(chatId);
        return message;
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}
