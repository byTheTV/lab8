package GUI.components;

import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;

public class CommandsComponent extends Div {
    private final Button infoButton;
    private final Button headButton;
    private final Button removeHeadButton;
    private final Button averageTransferredButton;
    private final Button countByFormButton;
    private final Button printAdminAscButton;
    private final I18NProvider i18NProvider;

    public CommandsComponent(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        
        getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("border-radius", "4px")
            .set("padding", "1rem")
            .set("margin", "0 1rem 1rem 1rem")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Initialize buttons
        infoButton = new Button();
        headButton = new Button();
        removeHeadButton = new Button();
        averageTransferredButton = new Button();
        countByFormButton = new Button();
        printAdminAscButton = new Button();

        // Configure buttons
        infoButton.setText(i18NProvider.getTranslation("main.info", getCurrentLocale()));
        headButton.setText(i18NProvider.getTranslation("main.head", getCurrentLocale()));
        removeHeadButton.setText(i18NProvider.getTranslation("main.removeHead", getCurrentLocale()));
        averageTransferredButton.setText(i18NProvider.getTranslation("main.averageTransferred", getCurrentLocale()));
        countByFormButton.setText(i18NProvider.getTranslation("main.countByForm", getCurrentLocale()));
        printAdminAscButton.setText(i18NProvider.getTranslation("main.printAdminAsc", getCurrentLocale()));

        // Create command groups
        HorizontalLayout commandGroups = new HorizontalLayout();
        commandGroups.setWidthFull();
        commandGroups.setSpacing(true);
        commandGroups.setJustifyContentMode(JustifyContentMode.BETWEEN);
        commandGroups.getStyle().set("flex-wrap", "wrap");

        // Collection info commands
        VerticalLayout infoCommands = new VerticalLayout();
        infoCommands.setSpacing(true);
        infoCommands.setPadding(false);
        infoCommands.add(new Span(i18NProvider.getTranslation("commands.collectionInfo", getCurrentLocale())));
        HorizontalLayout infoButtons = new HorizontalLayout(infoButton, headButton, removeHeadButton);
        infoButtons.setSpacing(true);
        infoCommands.add(infoButtons);
        infoCommands.getStyle().set("flex-grow", "1");
        infoButtons.setFlexGrow(1, infoButton, headButton, removeHeadButton);
        infoButtons.getStyle().set("width", "500px");

        // Statistics commands
        VerticalLayout statsCommands = new VerticalLayout();
        statsCommands.setSpacing(true);
        statsCommands.setPadding(false);
        statsCommands.add(new Span(i18NProvider.getTranslation("commands.statistics", getCurrentLocale())));
        HorizontalLayout statsButtons = new HorizontalLayout(averageTransferredButton, countByFormButton, printAdminAscButton);
        statsButtons.setSpacing(true);
        statsCommands.add(statsButtons);
        statsCommands.getStyle().set("flex-grow", "1");
        statsButtons.setFlexGrow(1, averageTransferredButton, countByFormButton, printAdminAscButton);
        statsButtons.getStyle().set("width", "500px");

        commandGroups.add(infoCommands, statsCommands);
        add(commandGroups);
    }

    public Button getInfoButton() {
        return infoButton;
    }

    public Button getHeadButton() {
        return headButton;
    }

    public Button getRemoveHeadButton() {
        return removeHeadButton;
    }

    public Button getAverageTransferredButton() {
        return averageTransferredButton;
    }

    public Button getCountByFormButton() {
        return countByFormButton;
    }

    public Button getPrintAdminAscButton() {
        return printAdminAscButton;
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 