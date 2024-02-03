package org.example.hackatonapi.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.example.hackatonapi.config.BotConfig;
import org.example.hackatonapi.menu.ActionMenu;
import org.example.hackatonapi.menu.CurrencyMenu;
import org.example.hackatonapi.menu.DateMenu;
import org.example.hackatonapi.model.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private static HashMap<Long, State> map = new HashMap<>();
    final BotConfig config;

    static final String HELP_TEXT = "hackaton java team 12bot" +
            "Type /start to see welcome message";


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
//                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    SendMessage message = ActionMenu.getMenu();

                    message.setText("\"Привет! Чтобы воспользоваться функционалом \" +\n" +
                            "                \"бота сперва выбери банк из меню снизу\"");
                    message.setChatId(chatId);
//                    CurrencyMenu.getMenu();
                    sendMessage(message);
                }
                case "Беларусбанк" -> {
                    map.get(chatId).setBankName("Беларусбанк");
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setText("Беларусбанк");
                    message.setChatId(chatId);
                    sendMessage(message);
                }

                case "Альфа банк" -> {
                    map.get(chatId).setBankName("Альфа банк");
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setText("Альфа банк");
                    message.setChatId(chatId);
                    sendMessage(message);
                }

                case "НБРБ" -> {
                    map.get(chatId).setBankName("НБРБ");
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setText("НБРБ");
                    message.setChatId(chatId);
                    sendMessage(message);
                }

                case "USD" -> {
                    map.get(chatId).setCurrencyName("USD");
                    SendMessage message = DateMenu.getMenu();
                    message.setChatId(chatId);
                    message.setText("u");
                    sendMessage(message);
                }
                case "EUR" -> {
                    map.get(chatId).setCurrencyName("EUR");
                    SendMessage message = DateMenu.getMenu();
                    message.setChatId(chatId);
                    message.setText("e");
                    sendMessage(message);
                }
                case "RUB" -> {
                    map.get(chatId).setCurrencyName("RUB");
                    SendMessage message = DateMenu.getMenu();
                    message.setChatId(chatId);
                    message.setText("r");
                    sendMessage(message);
                }

                case "Курс на выбранный день" -> {}
                case "Курс на текущий день" -> {}
                default -> {
                    sendMessage(null);
                }
            }
        }
    }


    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }
    }
}
