package GUI;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinSession;

import java.util.Locale;

public class LanguageSwitcher extends Button {
    private final I18NProvider i18NProvider;
    private final Dialog dialog;
    private final VerticalLayout layout;

    public LanguageSwitcher(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        this.dialog = new Dialog();
        this.layout = new VerticalLayout();
        
        setText(i18NProvider.getTranslation("language.current", getCurrentLocale()));
        addClickListener(e -> dialog.open());
        
        dialog.add(layout);
        updateLanguageButtons();
    }

    private void updateLanguageButtons() {
        layout.removeAll();
        for (Locale locale : i18NProvider.getProvidedLocales()) {
            String langKey = locale.getLanguage();
            Button button = new Button(i18NProvider.getTranslation("language." + langKey, locale));
            button.addClickListener(e -> {
                if (langKey.equals("en")) {
                    setLocale(Locale.ENGLISH);
                } else {
                    setLocale(locale);
                }
                dialog.close();
            });
            layout.add(button);
        }
    }

    private void setLocale(Locale locale) {
        UI.getCurrent().setLocale(locale);
        VaadinSession.getCurrent().setLocale(locale);
        setText(i18NProvider.getTranslation("language.current", locale));
        UI.getCurrent().getPage().executeJs("window.location.reload();");
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 