package com.gnevanov;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class TelegramBot extends TelegramLongPollingBot {
    private static final TelegramBotProperties properties = new TelegramBotProperties();

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new TelegramBot());
        //TextParser.getInfoAboutAuthor("https://spb.hh.ru/resume/683183d2ff0777f2fd0039ed1f78766c747a51");
    }

    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            //проверяем есть ли сообщение и текстовое ли оно
            if (update.hasMessage() && update.getMessage().hasText()) {
                String textMessage = update.getMessage().getText();
                if (textMessage.startsWith("/start")) {
                    execute(sendReplyKeyBoardMessage(update.getMessage().getChatId(),"RU"));
                } else if (textMessage.startsWith("\uD83D\uDC64")) { //About author
                    execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Информация об авторе:\n Гневанов Егор \n" + TextParser.getInfoAboutAuthor(properties.getProperty("cvURL"))));
                    //System.out.println(TextParser.getInfoAboutAuthor(properties.getProperty("cvURL")));
                } else if (textMessage.startsWith("\uD83D\uDCBC")) { //Work experience
                    execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Стаж работы:\n 7 лет 10 месяцев"));
                } else if (textMessage.startsWith("\uD83D\uDCF1")) { //Contact
                    execute(new SendMessage().setChatId(update.getMessage().getChatId()).setText("Контактная информация: \n тел.: +79650313367"));
                } else if (textMessage.startsWith("\uD83C\uDDF7")) { //Русский
                    execute(sendReplyKeyBoardMessage(update.getMessage().getChatId(),"RU"));
                } else if (textMessage.startsWith("\uD83C\uDDFA")) { //English
                    execute(sendReplyKeyBoardMessage(update.getMessage().getChatId(),"EN"));
                } else {
                    //Извлекаем объект входящего сообщения
                    Message inMessage = update.getMessage();
                    //Создаем исходящее сообщение
                    SendMessage outMessage = new SendMessage();
                    //Указываем в какой чат будем отправлять сообщение
                    //(в тот же чат, откуда пришло входящее сообщение)
                    outMessage.setChatId(inMessage.getChatId());
                    //Указываем текст сообщения
                    outMessage.setText(inMessage.getText());
                    //Отправляем сообщение
                    execute(outMessage);
                }

            } else if (update.hasCallbackQuery()) {
                try {
                    execute(new SendMessage().setText(
                            update.getCallbackQuery().getData())
                            .setChatId(update.getCallbackQuery().getMessage().getChatId()));
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return properties.getProperty("botUsername");
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return properties.getProperty("botToken");
    }

    public static SendMessage sendReplyKeyBoardMessage(long chatId, String language) {

        ReplyKeyboardBuilder replyKeyboardBuilder = ReplyKeyboardBuilder.create();
        replyKeyboardBuilder.setChatId(chatId);

        if (language.equals("EN")) {
            replyKeyboardBuilder.setText("\uD83D\uDC68\u200D\uD83D\uDCBB");
            replyKeyboardBuilder.row().button("\uD83D\uDC64 About author").endRow()
                    .row().button("\uD83D\uDCBC Work experience").endRow()
                    .row().button("\uD83D\uDCF1 Contact").endRow()
                    .row().button("\uD83C\uDDF7\uD83C\uDDFA").endRow();
        } else {
            replyKeyboardBuilder.setText("\uD83D\uDC68\u200D\uD83D\uDCBB");
            replyKeyboardBuilder.row().button("\uD83D\uDC64 Об авторе").endRow()
                    .row().button("\uD83D\uDCBC Опыт работы").endRow()
                    .row().button("\uD83D\uDCF1 Контакты").endRow()
                    .row().button("\uD83C\uDDFA\uD83C\uDDF8").endRow();
        }

        return replyKeyboardBuilder.build();

    }
}
