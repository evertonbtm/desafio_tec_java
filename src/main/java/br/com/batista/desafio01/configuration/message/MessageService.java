package br.com.batista.desafio01.configuration.message;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class MessageService {

    private static final Locale defaultLocale = Locale.US;

    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String key) {
        return messageSource.getMessage(key, null, defaultLocale);
    }

    public String getMessage(String key, String[] args) {
        return messageSource.getMessage(key, args, defaultLocale);
    }

    public String getMessage(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }

}
