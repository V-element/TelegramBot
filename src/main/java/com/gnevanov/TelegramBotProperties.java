package com.gnevanov;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TelegramBotProperties {
    private Properties properties = new Properties();
    private static final String propFileName = "config.properties";

    public TelegramBotProperties() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
