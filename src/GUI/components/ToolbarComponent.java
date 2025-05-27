package GUI.components;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.I18NProvider;

public class ToolbarComponent extends HorizontalLayout {
    private final TextField filterText;
    private final Button addButton;
    private final Button editButton;
    private final Button removeButton;
    private final Button clearButton;
    private final I18NProvider i18NProvider;

    public ToolbarComponent(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setAlignItems(Alignment.CENTER);
        getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("border-radius", "4px")
            .set("padding", "0.5rem")
            .set("margin", "1rem")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Initialize components
        filterText = new TextField();
        addButton = new Button();
        editButton = new Button();
        removeButton = new Button();
        clearButton = new Button();

        // Configure filter
        filterText.setPlaceholder(i18NProvider.getTranslation("main.filterPlaceholder", getCurrentLocale()));
        filterText.setClearButtonVisible(true);
        filterText.getStyle()
            .set("min-width", "300px")
            .set("margin-right", "1rem");

        // Configure buttons
        addButton.setText(i18NProvider.getTranslation("main.addGroup", getCurrentLocale()));
        editButton.setText(i18NProvider.getTranslation("main.edit", getCurrentLocale()));
        removeButton.setText(i18NProvider.getTranslation("main.remove", getCurrentLocale()));
        clearButton.setText(i18NProvider.getTranslation("main.clearAll", getCurrentLocale()));

        // Add components
        add(filterText);
        
        HorizontalLayout operations = new HorizontalLayout(addButton, editButton, removeButton, clearButton);
        operations.setSpacing(true);
        add(operations);
    }

    public TextField getFilterText() {
        return filterText;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 