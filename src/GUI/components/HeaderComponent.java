package GUI.components;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.i18n.I18NProvider;

import GUI.LanguageSwitcher;

public class HeaderComponent extends HorizontalLayout {
    private final I18NProvider i18NProvider;
    private final LanguageSwitcher languageSwitcher;
    private final String login;

    public HeaderComponent(I18NProvider i18NProvider, String login) {
        this.i18NProvider = i18NProvider;
        this.login = login;
        this.languageSwitcher = new LanguageSwitcher(i18NProvider);
        
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setAlignItems(Alignment.CENTER);
        getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("padding", "1rem")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
            .set("position", "sticky")
            .set("top", "0")
            .set("z-index", "100");
        
        // Add user info
        H1 userInfo = new H1(i18NProvider.getTranslation("main.currentUser", getCurrentLocale(), login));
        userInfo.getStyle()
            .set("margin", "0")
            .set("font-size", "1.2rem");
        add(userInfo);
        
        // Add language switcher
        add(languageSwitcher);
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 