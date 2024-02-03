package org.example.hackatonapi.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.example.hackatonapi.config.BotConfig;
import org.example.hackatonapi.menu.ActionMenu;
import org.example.hackatonapi.menu.CurrencyMenu;
import org.example.hackatonapi.menu.Menu;
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
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private Menu currencyMenu;
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
            long chatId = update.getMessage().getChatId();
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
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setText("Беларусбанк");
                    message.setChatId(chatId);
                    sendMessage(message);
                }

                case "Альфа банк" -> {
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setChatId(chatId);
                    sendMessage(message);
                }

                case "НБРБ" -> {
                    SendMessage message = CurrencyMenu.getMenu();
                    message.setChatId(chatId);
                    sendMessage(message);
                }
                default -> {
                    sendMessage(null);
                }
            }
        }
    }

//    private void register(long chatId) {
//        SendMessage message = new SendMessage();
//        message.setChatId(chatId);
//        message.setText("Do you want really to register?");
//
//        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
//        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
//
//        var yesButton = new InlineKeyboardButton();
//        yesButton.setText("Yes");
//        yesButton.setCallbackData("YES_BUTTON");
//
//        var noButton = new InlineKeyboardButton();
//        noButton.setText("No");
//        noButton.setCallbackData("NO_BUTTON");
//
//        rowInLine.add(yesButton);
//        rowInLine.add(noButton);
//
//        rowsInLine.add(rowInLine);
//
//        markupInLine.setKeyboard(rowsInLine);
//
//        message.setReplyMarkup(markupInLine);
//
//        try {
//            execute(message);
//        } catch (TelegramApiException e) {
////            log.error("Error occurred: " + e.getMessage());
//        }
//    }

//    private void startCommandReceived(long chatId, String name) {
//        String answer = EmojiParser.parseToUnicode("Привет, " + name + "! Чтобы воспользоваться функционалом " +
//                "бота сперва выбери банк из меню снизу");
//        sendMessage(chatId, answer);
//    }

    private void sendMessage(SendMessage message) {
        try {
            System.out.println(message.getText());
            execute(message);
        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
        }
    }
}
