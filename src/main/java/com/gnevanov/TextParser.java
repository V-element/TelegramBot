package com.gnevanov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TextParser {

    public static String getInfoAboutAuthor(String cvURL) {
        try {
            Document doc = Jsoup.connect(cvURL).get();
            Elements listNews = doc.select("span.resume-block__title-text[data-qa=resume-block-title-position]");
            return listNews.select("span").get(0).text();
                //System.out.println(element.text());

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
