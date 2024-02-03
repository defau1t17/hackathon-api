package org.example.hackatonapi.service;

import lombok.extern.slf4j.Slf4j;
import org.example.hackatonapi.config.BotConfig;
import org.example.hackatonapi.menu.ActionMenu;
import org.example.hackatonapi.menu.CurrencyMenu;
import org.example.hackatonapi.menu.DateMenu;
import org.example.hackatonapi.model.State;
import org.example.hackatonapi.models.dto.CurrencyDTO;
import org.example.hackatonapi.services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static HashMap<Long, State> map = new HashMap<>();

    private static String inputUserDate = "2024-02-02";
    final BotConfig config;
    @Autowired
    private ApiService apiService;

    static final String HELP_TEXT = "hackaton java team 12bot. Type /start to see welcome message";


    public TelegramBot(BotConfig config) {
//        user = new User();
        this.config = config;
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "get your data store"));
        listOfCommands.add(new BotCommand("/help", "how to use this bot"));
        listOfCommands.add(new BotCommand("/settings", "set your preferences"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
//            log.error("Error setting bots command list: " + e.getMessage());
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
            if (chatId != null && !map.containsKey(chatId)) {
                map.put(chatId, new State());
            }


            switch (messageText) {
                case "/start" -> {
                    SendMessage message = ActionMenu.getMenu();
                    message.setText("Привет! Чтобы воспользоваться функционалом бота сперва выбери банк из меню снизу");
                    message.setChatId(chatId);
                    sendMessage(message);
                }
                case "Беларусбанк" -> {
                    sendMessage(setBankState("BELBANK", chatId));

                }

                case "Альфа банк" -> {
                    sendMessage(setBankState("ALFA", chatId));
                }

                case "НБРБ" -> {
                    sendMessage(setBankState("NBRB", chatId));
                }

                case "USD" -> {
                    sendMessage(setCurrencyState(messageText, chatId));
                }
                case "EUR" -> {
                    sendMessage(setCurrencyState(messageText, chatId));
                }
                case "RUB" -> {
                    sendMessage(setCurrencyState(messageText, chatId));
                }

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
                    System.out.println(LocalDate.now());
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

                    System.out.println(inputDate);

                    try {
                        LocalDate selectedDate = LocalDate.parse(inputDate);
                        System.out.println(selectedDate);
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
                        errorMessage.setText("");
                        sendMessage(errorMessage);
                    }
                }


                case "Статистика" -> {
                    System.out.println(inputUserDate);
                    byte[] png = apiService.getStatisticsAsPNG(map.get(chatId).getCurrencyName(), map.get(chatId).getBankName(), "2024-01-31", LocalDate.now().toString());

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setChatId(chatId);

                    try {
                        BufferedImage image = byteArrayToImage(png);

                        // Convert BufferedImage to Inputstream
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        ImageIO.write(image, "png", os);
                        InputStream is = new ByteArrayInputStream(os.toByteArray());

                        // Set the photo and send the message
                        sendPhoto.setPhoto(new InputFile(is, "your_filename.png"));
                        execute(sendPhoto);  // Assuming you have a method named execute to send the message

                        System.out.println("Изображение успешно отправлено");
                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }


//                    sendMessage(message);
                }
                default -> {
                    SendMessage promptMessage = new SendMessage();
                    promptMessage.setChatId(chatId);
                    promptMessage.setText("Введите дату в формате YYYY-MM-DD (например, 2024-02-03):");
                    sendMessage(promptMessage);

                    String inputDate = update.getMessage().getText().trim();
                    inputUserDate = inputDate;
                    System.out.println(inputDate);

                    try {
                        LocalDate selectedDate = LocalDate.parse(inputDate);
                        System.out.println(selectedDate);
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
        }
    }
}
