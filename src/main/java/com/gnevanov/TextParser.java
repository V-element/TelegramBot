package com.gnevanov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TextParser {
    private String cvURL;
    private int workNumber = 0;

    public int getWorkNumber() {
        return workNumber;
    }

    public void setWorkNumber(int workNumber) {
        this.workNumber = workNumber;
    }

    public String getInfoAboutAuthor(TelegramBotProperties properties, String currentLanguage) {
        workNumber = 0;
        StringBuilder authorInfo = new StringBuilder();
        setCvURL(properties, currentLanguage);
        if (currentLanguage.equals("RU")) {
            authorInfo.append("Информация об авторе:\n");
        } else {
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
            return getErrorMessage(currentLanguage);
        }
    }

    public String getWorkExperience(TelegramBotProperties properties, String currentLanguage, boolean onlyTitle) {
        StringBuilder workExperience = new StringBuilder();
        setCvURL(properties, currentLanguage);
        try {
            Document doc = Jsoup.connect(cvURL).get();
            if (onlyTitle) {
                Elements authorWorkExperience = doc.select("span.resume-block__title-text_sub");
                workExperience.append("<b>").append(authorWorkExperience.get(0).text()).append("</b>").append("\n");
            } else {
                workNumber = Math.max(workNumber, 0);
                Elements experienceBlocks = doc.select("div.bloko-column.bloko-column_xs-4.bloko-column_s-6.bloko-column_m-7.bloko-column_l-10");
                while (workNumber > experienceBlocks.size() + 1 ||
                        experienceBlocks.get(workNumber).select("div.resume-block-container[data-qa=resume-block-education-item]").size() != 0) {
                    workNumber--;
                }
                Element workBlock = experienceBlocks.get(workNumber);
                Element timeWorkBlock = doc.select("div.bloko-column.bloko-column_xs-4.bloko-column_s-2.bloko-column_m-2.bloko-column_l-2").get(workNumber);
                workExperience.append(timeWorkBlock.ownText()).append("\n");
                if (currentLanguage.equals("EN")) {
                    workExperience.append("<b>Company: </b>").append(workBlock.select("div.resume-block__sub-title").get(0).text()).append("\n");
                    workExperience.append("<b>Position: </b>").append(workBlock.select("div.resume-block__sub-title[data-qa=resume-block-experience-position]").get(0).text()).append("\n");
                    workExperience.append("<b>Responsibilities and achievements: </b>\n").append(workBlock.select("div[data-qa=resume-block-experience-description]").get(0).text()).append("\n");
                } else {
                    workExperience.append("<b>Организация: </b>").append(workBlock.select("div.resume-block__sub-title").get(0).text()).append("\n");
                    workExperience.append("<b>Должность: </b>").append(workBlock.select("div.resume-block__sub-title[data-qa=resume-block-experience-position]").get(0).text()).append("\n");
                    workExperience.append("<b>Обязанности и достижения: </b>\n").append(workBlock.select("div[data-qa=resume-block-experience-description]").get(0).text()).append("\n");
                }
            }
            return workExperience.toString();
        } catch (IOException e) {
            return getErrorMessage(currentLanguage);
        }
    }

    public String getContactInformation(TelegramBotProperties properties, String currentLanguage) {
        workNumber = 0;
        StringBuilder contactInformation = new StringBuilder();
        setCvURL(properties, currentLanguage);
        if (currentLanguage.equals("RU")) {
            contactInformation.append("Контактная информация:\n");
        } else {
            contactInformation.append("Contact information:\n");
        }

        contactInformation.append("Telegram: ").append(properties.getProperty("telegram")).append("\n");

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
            return getErrorMessage(currentLanguage);
        }
    }

    private void setCvURL(TelegramBotProperties properties, String currentLanguage){
        if (currentLanguage.equals("RU")) {
            cvURL = properties.getProperty("cvUrlRus");
        } else {
            cvURL = properties.getProperty("cvUrlEng");
        }
    }

    private String getErrorMessage(String currentLanguage) {
        if (currentLanguage.equals("EN")) {
            return "Information is temporarily unavailable.\nPlease try again later";
        } else {
            return "Информация временно недоступна.\nПопробуйте позже";
        }
    }
}
