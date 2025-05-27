package GUI.components;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.i18n.I18NProvider;

import Common.models.Person;
import Common.models.StudyGroup;

public class GroupDetailsComponent extends Div {
    private final I18NProvider i18NProvider;
    private final StudyGroup group;

    public GroupDetailsComponent(I18NProvider i18NProvider, StudyGroup group) {
        this.i18NProvider = i18NProvider;
        this.group = group;
        
        getStyle()
            .set("position", "fixed")
            .set("top", "50%")
            .set("left", "50%")
            .set("transform", "translate(-50%, -50%)")
            .set("background", "white")
            .set("padding", "20px")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 10px rgba(0,0,0,0.1)")
            .set("z-index", "1000")
            .set("min-width", "300px");

        // Create content with translations
        add(new H1(i18NProvider.getTranslation("details.title", getCurrentLocale())));
        add(new Div(i18NProvider.getTranslation("details.id", getCurrentLocale()) + ": " + group.getId()));
        add(new Div(i18NProvider.getTranslation("details.name", getCurrentLocale()) + ": " + group.getName()));
        add(new Div(i18NProvider.getTranslation("details.studentsCount", getCurrentLocale()) + ": " + group.getStudentsCount()));
        add(new Div(i18NProvider.getTranslation("details.expelledStudents", getCurrentLocale()) + ": " + group.getExpelledStudents()));
        add(new Div(i18NProvider.getTranslation("details.transferredStudents", getCurrentLocale()) + ": " + group.getTransferredStudents()));
        add(new Div(i18NProvider.getTranslation("details.formOfEducation", getCurrentLocale()) + ": " + group.getFormOfEducation()));
        add(new Div(i18NProvider.getTranslation("details.coordinates", getCurrentLocale()) + ": X=" + group.getCoordinates().getX() + ", Y=" + group.getCoordinates().getY()));
        add(new Div(i18NProvider.getTranslation("details.creationDate", getCurrentLocale()) + ": " + 
            group.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", getCurrentLocale()))));
        
        if (group.getGroupAdmin() != null) {
            Person admin = group.getGroupAdmin();
            add(new Div(i18NProvider.getTranslation("details.groupAdmin", getCurrentLocale()) + ":"));
            add(new Div("  " + i18NProvider.getTranslation("details.adminName", getCurrentLocale()) + ": " + admin.getName()));
            add(new Div("  " + i18NProvider.getTranslation("details.eyeColor", getCurrentLocale()) + ": " + admin.getEyeColor()));
            if (admin.getLocation() != null) {
                String locationText = String.format("%s: X=%.2f, Y=%.2f, Z=%.2f",
                    i18NProvider.getTranslation("details.location", getCurrentLocale()),
                    admin.getLocation().getX(),
                    admin.getLocation().getY(),
                    admin.getLocation().getZ());
                add(new Div("  " + locationText));
            }
        }

        // Add close button
        Button closeButton = new Button(i18NProvider.getTranslation("details.close", getCurrentLocale()));
        closeButton.addClickListener(e -> {
            getParent().ifPresent(parent -> {
                if (parent instanceof Div) {
                    Div overlay = (Div) parent;
                    overlay.removeFromParent();
                    removeFromParent();
                }
            });
        });
        add(closeButton);
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 