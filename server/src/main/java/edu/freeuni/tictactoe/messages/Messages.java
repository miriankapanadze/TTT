package edu.freeuni.tictactoe.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Messages {

	private static Logger log = LoggerFactory.getLogger(Messages.class);

	private static Map<String, Properties> messages = new HashMap<>();

	private static final String defaultLocale = "ka";

	public static String get(String key) {
		return getI18n(key, defaultLocale);
	}

	public static String getI18n(String key, String langCode) {
		if (key != null) {
			Properties properties = messages.get(langCode);
			if (properties == null) {
				properties = new Properties();
				try {
					InputStream is = Messages.class.getResourceAsStream("/messages_" + langCode + ".properties");
					Reader bufferedReader = new InputStreamReader(is, "UTF-8");
					properties.load(bufferedReader);
					messages.put(langCode, properties);
				} catch (IOException ex) {
					log.error("Error reading messages", ex);
				}
			}
			return properties.getProperty(key, key);
		}
		return null;
	}

	public static String get(String key, Object... params) {
		return MessageFormat.format(get(key), params);
	}

	public static String getI18n(String key, String langCode, Object... params) {
		return MessageFormat.format(getI18n(key, langCode), params);
	}
}
