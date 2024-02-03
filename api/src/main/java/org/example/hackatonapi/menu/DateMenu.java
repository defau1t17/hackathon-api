package org.example.hackatonapi.menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class DateMenu {

    public static SendMessage getMenu() {
        SendMessage message = new SendMessage();
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        keyboardRows.add(createKeyboardRow("Курс на текущий день"));
        keyboardRows.add(createKeyboardRow("Курс на выбранный день"));
        keyboardRows.add(createKeyboardRow("Выбрать другой банк", "Выбрать другую валюту"));
        keyboardRows.add(createKeyboardRow("Статистика"));

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }

    private static KeyboardRow createKeyboardRow(String... buttons) {
        KeyboardRow row = new KeyboardRow();
        for (String button : buttons) {
            row.add(button);
        }
        return row;
    }
}
