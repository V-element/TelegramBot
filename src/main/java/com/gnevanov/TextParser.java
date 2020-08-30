package com.gnevanov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TextParser {
    private static String cvURL;

    public static String getInfoAboutAuthor(TelegramBotProperties properties, String currentLanguage) {

        StringBuilder authorInfo = new StringBuilder();
        if (currentLanguage.equals("RU")) {
            cvURL = properties.getProperty("cvUrlRus");
            authorInfo.append("Информация об авторе:\n");
        } else {
            cvURL = properties.getProperty("cvUrlEng");
            authorInfo.append("Information about author:\n");
        }
        try {
            Document doc = Jsoup.connect(cvURL).get();
            Elements authorName = doc.select("h2.bloko-header-1[data-qa=resume-personal-name]");
            authorInfo.append(authorName.get(0).text()).append("\n");
            Elements authorPosition = doc.select("span.resume-block__title-text[data-qa=resume-block-title-position]");
            authorInfo.append(authorPosition.get(0).text());
            return authorInfo.toString();
        } catch (IOException e) {
            return currentLanguage.equals("RU") ? "Информация временно недоступна" : "Information is temporarily unavailable";
        }
    }

    public static String getWorkExperience(TelegramBotProperties properties, String currentLanguage) {
        StringBuilder workExperience = new StringBuilder();
        if (currentLanguage.equals("RU")) {
            cvURL = properties.getProperty("cvUrlRus");
        } else {
            cvURL = properties.getProperty("cvUrlEng");
        }
        try {
            Document doc = Jsoup.connect(cvURL).get();
            Elements authorWorkExperience = doc.select("span.resume-block__title-text_sub");
            workExperience.append(authorWorkExperience.get(0).text());

            //Elements experienceBlocks = doc.select("div.resume-block[data-qa=resume-block-experience]");
            /*for (Element exeprienceBlock: experienceBlocks) {
            }*/

            //xperienceBlocks.get(0).select("div.resume-block-item-gap").select("div.resume-block__sub-title");
            return workExperience.toString();
        } catch (IOException e) {
            return currentLanguage.equals("RU") ? "Информация временно недоступна" : "Information is temporarily unavailable";
        }
    }

    public static String getContactInformation(TelegramBotProperties properties, String currentLanguage) {
        StringBuilder contactInformation = new StringBuilder();
        if (currentLanguage.equals("RU")) {
            cvURL = properties.getProperty("cvUrlRus");
            contactInformation.append("Контактная информация:\n");
        } else {
            cvURL = properties.getProperty("cvUrlEng");
            contactInformation.append("Contact information:\n");
        }
        try {
            Document doc = Jsoup.connect(cvURL).get();
            Elements contactPhone = doc.select("div[data-qa=resume-contacts-phone]");
            Elements contactEmail = doc.select("div[data-qa=resume-contact-email]");
            Elements contactSkype = doc.select("span[data-qa=resume-personalsite-skype]");
            if (currentLanguage.equals("RU")) {
                contactInformation.append("Тел.: ").append(contactPhone.get(0).text()).append("\n");
            } else {
                contactInformation.append("Phone: ").append(contactPhone.get(0).text()).append("\n");
            }
            contactInformation.append("E-mail: ").append(contactEmail.select("a").get(0).text()).append("\n");
            contactInformation.append("Skype: ").append(contactSkype.get(0).text());
            return contactInformation.toString();
        } catch (IOException e) {
            return currentLanguage.equals("RU") ? "Информация временно недоступна" : "Information is temporarily unavailable";
        }
    }
}
