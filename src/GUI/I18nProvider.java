package GUI;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.text.MessageFormat;
import java.util.*;

@Component
public class I18nProvider implements I18NProvider {
    private static final long serialVersionUID = 1L;
    private final List<Locale> locales = Collections.unmodifiableList(Arrays.asList(
        Locale.ENGLISH,
        new Locale("ru"),
        new Locale("be"),
        new Locale("it"),
        new Locale("es", "DO")
    ));

    private final MessageSource messageSource;

    public I18nProvider(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        LoggerFactory.getLogger(I18nProvider.class.getName()).debug("Getting translation for key: " + key + " with locale: " + locale);

        if (key == null) {
            LoggerFactory.getLogger(I18nProvider.class.getName())
                .warn("Got lang request for key with null value!");
            return "";
        }

        // Сначала пробуем получить перевод для указанной локали
        try {
            String translation = messageSource.getMessage(key, params, locale);
            if (translation != null && !translation.isEmpty()) {
                return translation;
            }
        } catch (NoSuchMessageException e) {
            LoggerFactory.getLogger(I18nProvider.class.getName())
                .debug("No translation found for key: " + key + " with locale: " + locale);
        }

        // Если перевод не найден и это не английский язык, пробуем получить перевод для английского
        if (!locale.equals(Locale.ENGLISH)) {
            try {
                String translation = messageSource.getMessage(key, params, Locale.ENGLISH);
                if (translation != null && !translation.isEmpty()) {
                    return translation;
                }
            } catch (NoSuchMessageException e) {
                LoggerFactory.getLogger(I18nProvider.class.getName())
                    .debug("No translation found for key: " + key + " with locale: ENGLISH");
            }
        }

        // Если перевод все еще не найден, возвращаем ключ
        return key;
    }
} 