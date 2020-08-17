package com.gnevanov;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class ReplyKeyboardBuilder {

    private Long chatId;
    private String text;

    private List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row = null;

    private ReplyKeyboardBuilder() {
    }

    public static ReplyKeyboardBuilder create() {
        return new ReplyKeyboardBuilder();
    }

    public static ReplyKeyboardBuilder create(Long chatId) {
        ReplyKeyboardBuilder builder = new ReplyKeyboardBuilder();
        builder.setChatId(chatId);
        return builder;
    }

    public ReplyKeyboardBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public ReplyKeyboardBuilder row() {
        this.row = new KeyboardRow();
        return this;
    }

    public ReplyKeyboardBuilder button(String text, String callbackData) {
        row.add(text);
        return this;
    }

    public ReplyKeyboardBuilder endRow() {
        this.keyboard.add(this.row);
        this.row = null;
        return this;
    }

    public SendMessage build() {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(text);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        keyboardMarkup.setKeyboard(keyboard);
        message.setReplyMarkup(keyboardMarkup);

        return message;
    }


}
