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
    private static final TextParser textParser = new TextParser();
    private static String currentLanguage = "RU";

    public static void main(String[] args) throws TelegramApiRequestException {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();
        botsApi.registerBot(new TelegramBot());
        //System.out.println(textParser.getWorkExperience(properties, currentLanguage));
    }

    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message message = update.getMessage();
                String textMessage = message.getText();
                long chatID = message.getChatId();
                sendMessage(chatID, textMessage);
            } else if (update.hasCallbackQuery()) {
                sendMessage(update.getCallbackQuery().getMessage().getChatId(),update.getCallbackQuery().getData());
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

    private void sendMessage(long chatID, String textMessage) throws TelegramApiException {
        if (textMessage.startsWith("/start")) {
            execute(createReplyKeyboardMessage(chatID,"RU"));
        } else if (textMessage.startsWith("\uD83D\uDC64")) { //About author
            execute(new SendMessage().setChatId(chatID).setText(textParser.getInfoAboutAuthor(properties,currentLanguage)));
        } else if (textMessage.startsWith("\uD83D\uDCBC")) { //Work experience
            textParser.setWorkNumber(0);
            execute(new SendMessage().setChatId(chatID).setText(textParser.getWorkExperience(properties,currentLanguage,true)).setParseMode("HTML"));
            execute(createWorkExperienceInlineKeyboardMessage(chatID, textParser.getWorkExperience(properties,currentLanguage, false)));
        } else if (textMessage.startsWith("\uD83D\uDCF1")) { //Contact
            execute(new SendMessage().setChatId(chatID).setText(textParser.getContactInformation(properties,currentLanguage)));
        } else if (textMessage.startsWith("\uD83C\uDDF7")) { //Русский
            execute(createReplyKeyboardMessage(chatID,"RU"));
        } else if (textMessage.startsWith("\uD83C\uDDFA")) { //English
            execute(createReplyKeyboardMessage(chatID, "EN"));
        } else if (textMessage.startsWith("\u2B05")) {
            textParser.setWorkNumber(textParser.getWorkNumber()-1);
            execute(createWorkExperienceInlineKeyboardMessage(chatID, textParser.getWorkExperience(properties,currentLanguage, false)));
        } else if (textMessage.startsWith("\u27A1")) {
            textParser.setWorkNumber(textParser.getWorkNumber()+1);
            execute(createWorkExperienceInlineKeyboardMessage(chatID, textParser.getWorkExperience(properties,currentLanguage, false)));
        } else {
            execute(createMainInlineKeyboardMessage(chatID));
        }
    }

    public static SendMessage createReplyKeyboardMessage(long chatId, String language) {

        ReplyKeyboardBuilder replyKeyboardBuilder = ReplyKeyboardBuilder.create();
        replyKeyboardBuilder.setChatId(chatId);

        if (language.equals("EN")) {
            replyKeyboardBuilder.setText("\uD83D\uDC68\u200D\uD83D\uDCBB " +
                                            "Hello, I'm VelementBot!\nHow can I help you?");
            replyKeyboardBuilder.row().button("\uD83D\uDC64 About author").endRow()
                    .row().button("\uD83D\uDCBC Work experience").endRow()
                    .row().button("\uD83D\uDCF1 Contact").endRow()
                    .row().button("\uD83C\uDDF7\uD83C\uDDFA").endRow();
        } else {
            replyKeyboardBuilder.setText("\uD83D\uDC68\u200D\uD83D\uDCBB " +
                    "Здравствуйте, я VelementBot!\nЧем я могу вам помочь?");
            replyKeyboardBuilder.row().button("\uD83D\uDC64 Об авторе").endRow()
                    .row().button("\uD83D\uDCBC Опыт работы").endRow()
                    .row().button("\uD83D\uDCF1 Контакты").endRow()
                    .row().button("\uD83C\uDDFA\uD83C\uDDF8").endRow();
        }
        currentLanguage = language;

        return replyKeyboardBuilder.build();

    }

    public static SendMessage createMainInlineKeyboardMessage(long chatId) {
        InlineKeyboardBuilder inlineKeyboardBuilder = InlineKeyboardBuilder.create(chatId);
        if (currentLanguage.equals("EN")) {
            return inlineKeyboardBuilder.setText("Command doesn't support!\n" +
                                            "Choose command:").row()
                    .button("\uD83D\uDC64 About author", "\uD83D\uDC64 About author")
                    .endRow()
                    .row()
                    .button("\uD83D\uDCBC Work experience", "\uD83D\uDCBC Work experience")
                    .endRow()
                    .row()
                    .button("\uD83D\uDCF1 Contact", "\uD83D\uDCF1 Contact")
                    .endRow()
                    .build();
        } else {
            return inlineKeyboardBuilder.setText("Команда не поддерживается!\n" +
                                            "Выберите команду:").row()
                    .button("\uD83D\uDC64 Об авторе", "\uD83D\uDC64 Об авторе")
                    .endRow()
                    .row()
                    .button("\uD83D\uDCBC Опыт работы", "\uD83D\uDCBC Опыт работы")
                    .endRow()
                    .row()
                    .button("\uD83D\uDCF1 Контакты", "\uD83D\uDCF1 Контакты")
                    .endRow()
                    .build();
        }
    }

    public static SendMessage createWorkExperienceInlineKeyboardMessage(long chatId, String textMessage) {
        return InlineKeyboardBuilder.create(chatId).setText(textParser.getWorkExperience(properties,currentLanguage,false))
                .row()
                .button("\u2B05", "\u2B05")
                .button("\u27A1", "\u27A1")
                .endRow()
                .build()
                .setParseMode("HTML");
    }

}
