package GUI;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.component.notification.Notification;

import Common.models.Color;
import Common.models.FormOfEducation;
import Common.models.Location;
import Common.models.Person;
import Common.models.StudyGroup;

public class StudyGroupDialog extends Dialog {
    private final FormLayout formLayout = new FormLayout();
    private final TextField name = new TextField("Name");
    private final IntegerField coordinatesX = new IntegerField("X Coordinate");
    private final IntegerField coordinatesY = new IntegerField("Y Coordinate");
    private final IntegerField studentsCount = new IntegerField("Students Count");
    private final IntegerField expelledStudents = new IntegerField("Expelled Students");
    private final IntegerField transferredStudents = new IntegerField("Transferred Students");
    private final ComboBox<FormOfEducation> formOfEducation = new ComboBox<>("Form of Education");
    
    // Group Admin fields
    private final TextField adminName = new TextField("Admin Name");
    private final ComboBox<Color> eyeColor = new ComboBox<>("Eye Color");
    private final TextField passportId = new TextField("Passport ID");
    private final IntegerField locationX = new IntegerField("Location X");
    private final IntegerField locationY = new IntegerField("Location Y");
    private final IntegerField locationZ = new IntegerField("Location Z");

    private final Button save = new Button("Save");
    private final Button close = new Button("Cancel");

    private final Binder<StudyGroup> binder = new BeanValidationBinder<>(StudyGroup.class);
    private StudyGroup studyGroup;

    public StudyGroupDialog() {
        addClassName("study-group-dialog");
        
        // Configure form fields
        formOfEducation.setItems(FormOfEducation.values());
        eyeColor.setItems(Color.values());
        
        // Configure numeric fields
        studentsCount.setMin(1);
        expelledStudents.setMin(0);
        transferredStudents.setMin(0);
        
        // Add fields to form
        formLayout.add(
            name,
            new HorizontalLayout(coordinatesX, coordinatesY),
            studentsCount,
            expelledStudents,
            transferredStudents,
            formOfEducation,
            adminName,
            eyeColor,
            passportId,
            new HorizontalLayout(locationX, locationY, locationZ),
            createButtonsLayout()
        );

        // Configure binder with converters
        binder.forField(name)
            .withValidator(name -> name != null && !name.trim().isEmpty(), "Name cannot be empty")
            .bind(StudyGroup::getName, StudyGroup::setName);
            
        binder.forField(coordinatesX)
            .withValidator(x -> x != null, "X coordinate cannot be null")
            .bind(group -> group.getCoordinates().getX().intValue(), 
                  (group, x) -> group.getCoordinates().setX(x.longValue()));
                  
        binder.forField(coordinatesY)
            .withValidator(y -> y != null, "Y coordinate cannot be null")
            .bind(group -> group.getCoordinates().getY().intValue(), 
                  (group, y) -> group.getCoordinates().setY(y.longValue()));
                  
        binder.forField(studentsCount)
            .withValidator(count -> count != null && count > 0, "Students count must be positive")
            .bind(group -> (int)group.getStudentsCount(), 
                  (group, count) -> group.setStudentsCount(count));
            
        binder.forField(expelledStudents)
            .withValidator(count -> count != null && count > 0, "Expelled students count must be positive")
            .bind(StudyGroup::getExpelledStudents, StudyGroup::setExpelledStudents);
            
        binder.forField(transferredStudents)
            .withValidator(count -> count != null && count > 0, "Transferred students count must be positive")
            .bind(StudyGroup::getTransferredStudents, StudyGroup::setTransferredStudents);
            
        binder.forField(formOfEducation)
            .bind(StudyGroup::getFormOfEducation, StudyGroup::setFormOfEducation);
            
        binder.forField(adminName)
            .withValidator(name -> name != null && !name.trim().isEmpty(), "Admin name cannot be empty")
            .bind(group -> group.getGroupAdmin() != null ? group.getGroupAdmin().getName() : null,
                  (group, name) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      group.getGroupAdmin().setName(name);
                  });
                  
        binder.forField(eyeColor)
            .withValidator(color -> color != null, "Eye color cannot be null")
            .bind(group -> group.getGroupAdmin() != null ? group.getGroupAdmin().getEyeColor() : null,
                  (group, color) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      group.getGroupAdmin().setEyeColor(color);
                  });
                  
        binder.forField(passportId)
            .withValidator(id -> id == null || (!id.trim().isEmpty() && id.length() <= 26), 
                          "Passport ID must be either null or a non-empty string with length <= 26")
            .bind(group -> group.getGroupAdmin() != null ? group.getGroupAdmin().getPassportID() : null,
                  (group, id) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      group.getGroupAdmin().setPassportID(id);
                  });
                  
        binder.forField(locationX)
            .withValidator(x -> x != null, "Location X cannot be null")
            .bind(group -> group.getGroupAdmin() != null && group.getGroupAdmin().getLocation() != null ? 
                          group.getGroupAdmin().getLocation().getX().intValue() : null,
                  (group, x) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      if (group.getGroupAdmin().getLocation() == null) {
                          group.getGroupAdmin().setLocation(new Location(0f, 0f, 0f));
                      }
                      group.getGroupAdmin().getLocation().setX(x.floatValue());
                  });
                  
        binder.forField(locationY)
            .withValidator(y -> y != null, "Location Y cannot be null")
            .bind(group -> group.getGroupAdmin() != null && group.getGroupAdmin().getLocation() != null ? 
                          group.getGroupAdmin().getLocation().getY().intValue() : null,
                  (group, y) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      if (group.getGroupAdmin().getLocation() == null) {
                          group.getGroupAdmin().setLocation(new Location(0f, 0f, 0f));
                      }
                      group.getGroupAdmin().getLocation().setY(y.floatValue());
                  });
                  
        binder.forField(locationZ)
            .withValidator(z -> z != null, "Location Z cannot be null")
            .bind(group -> group.getGroupAdmin() != null && group.getGroupAdmin().getLocation() != null ? 
                          group.getGroupAdmin().getLocation().getZ().intValue() : null,
                  (group, z) -> {
                      if (group.getGroupAdmin() == null) {
                          group.setGroupAdmin(new Person());
                      }
                      if (group.getGroupAdmin().getLocation() == null) {
                          group.getGroupAdmin().setLocation(new Location(0f, 0f, 0f));
                      }
                      group.getGroupAdmin().getLocation().setZ(z.floatValue());
                  });
        
        // Configure buttons
        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        // Add form to dialog
        add(formLayout);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        return new HorizontalLayout(save, close);
    }

    public void setStudyGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
        binder.setBean(studyGroup);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(studyGroup);
            // Don't set a temporary ID - let the server assign it
            fireEvent(new SaveEvent(this, studyGroup));
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    // Events
    public static abstract class StudyGroupDialogEvent extends ComponentEvent<StudyGroupDialog> {
        private final StudyGroup studyGroup;

        protected StudyGroupDialogEvent(StudyGroupDialog source, StudyGroup studyGroup) {
            super(source, false);
            this.studyGroup = studyGroup;
        }

        public StudyGroup getStudyGroup() {
            return studyGroup;
        }
    }

    public static class SaveEvent extends StudyGroupDialogEvent {
        SaveEvent(StudyGroupDialog source, StudyGroup studyGroup) {
            super(source, studyGroup);
        }
    }

    public static class CloseEvent extends StudyGroupDialogEvent {
        CloseEvent(StudyGroupDialog source) {
            super(source, null);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
} 