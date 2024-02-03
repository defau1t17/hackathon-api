package org.example.hackatonapi.menu;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class CurrencyMenu{

    public static SendMessage getMenu() {
        SendMessage message = new SendMessage();
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("USD");
        keyboardRows.add(row);

        row = new KeyboardRow();

        row.add("EUR");
        row.add("RUB");
        keyboardRows.add(row);

        row = new KeyboardRow();
        row.add("Выбрать другой банк");
//        row.add("Выбрать другую дату");
        keyboardRows.add(row);

        keyboardMarkup.setKeyboard(keyboardRows);

        message.setReplyMarkup(keyboardMarkup);

        return  message;
    }
}
