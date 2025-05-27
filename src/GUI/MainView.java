package GUI;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.component.UI;

import Client.network.TCPClient;
import Common.models.Color;
import Common.models.Coordinates;
import Common.models.Location;
import Common.models.Person;
import Common.models.StudyGroup;
import Common.models.FormOfEducation;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<StudyGroup> grid = new Grid<>(StudyGroup.class);
    private final TextField filterText = new TextField();
    private final Button addButton = new Button();
    private final Button editButton = new Button();
    private final Button removeButton = new Button();
    private final Button clearButton = new Button();
    
    private StudyGroupService service;
    private StudyGroupDialog dialog;
    private TCPClient client;
    private String currentUserLogin;
    private I18NProvider i18NProvider;
    private LanguageSwitcher languageSwitcher;

    public MainView(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        this.languageSwitcher = new LanguageSwitcher(i18NProvider);
        
        try {
            // Get client and credentials from session
            client = (TCPClient) VaadinSession.getCurrent().getAttribute("tcpClient");
            String login = (String) VaadinSession.getCurrent().getAttribute("login");
            String password = (String) VaadinSession.getCurrent().getAttribute("password");
            Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
            String userUid = (String) VaadinSession.getCurrent().getAttribute("userUid");
            
            if (client == null || login == null || password == null || userId == null || userUid == null) {
                getUI().ifPresent(ui -> ui.navigate(""));
                return;
            }
            
            this.currentUserLogin = login;
            this.service = new StudyGroupService(client, login, password, userUid, userId);
            this.dialog = new StudyGroupDialog();
            
            setSizeFull();
            configureGrid();
            configureFilter();
            configureButtons();
            configureDialog();
            
            // Create header with user info, language switcher and logout button
            HorizontalLayout header = new HorizontalLayout();
            header.setWidthFull();
            header.setJustifyContentMode(JustifyContentMode.BETWEEN);
            
            // Add user info
            H1 userInfo = new H1(i18NProvider.getTranslation("main.currentUser", getCurrentLocale(), login));
            userInfo.getStyle().set("margin", "0");
            header.add(userInfo);
            
            // Add language switcher
            header.add(languageSwitcher);
            
            Button logoutButton = new Button(i18NProvider.getTranslation("main.logout", getCurrentLocale()));
            logoutButton.addClickListener(e -> {
                if (service != null) {
                    service.close();
                }
                if (client != null) {
                    client.close();
                }
                VaadinSession.getCurrent().setAttribute("tcpClient", null);
                VaadinSession.getCurrent().setAttribute("login", null);
                VaadinSession.getCurrent().setAttribute("password", null);
                VaadinSession.getCurrent().setAttribute("userId", null);
                VaadinSession.getCurrent().setAttribute("userUid", null);
                getUI().ifPresent(ui -> ui.navigate(""));
            });
            
            header.add(logoutButton);

            // Create toolbar with filter and buttons
            HorizontalLayout toolbar = new HorizontalLayout(filterText, addButton, editButton, removeButton, clearButton);
            toolbar.setWidthFull();
            toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);

            // Add components to layout
            add(header, new H1(i18NProvider.getTranslation("main.title", getCurrentLocale())), toolbar, grid);
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            
            // Load initial data
            updateList();
        } catch (Exception e) {
            Notification.show(i18NProvider.getTranslation("notification.error", getCurrentLocale(), e.getMessage()), 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(""));
        }
    }

    private void configureGrid() {
        grid.setSizeFull();

        // Add click listener to show detailed information
        grid.addItemClickListener(event -> {
            StudyGroup group = event.getItem();
            showGroupDetails(group);
        });

        // Add class generator for row styling
        grid.setClassNameGenerator(group -> {
            if (group.getUserId() != null && group.getUserId().equals(service.getUserId())) {
                return "my-group";
            } else {
                return "other-group";
            }
        });

        // Add indicator column
        grid.addColumn(new ComponentRenderer<>(group -> {
            Span indicator = new Span();
            indicator.getStyle()
                    .set("display", "inline-block")
                    .set("width", "15px")
                    .set("height", "15px")
                    .set("border-radius", "50%")
                    .set("margin-right", "10px");

            if (group.getUserId() != null && group.getUserId().equals(service.getUserId())) {
                indicator.getStyle()
                        .set("background-color", "green")
                        .set("border", "2px solid darkgreen");
                indicator.setTitle(i18NProvider.getTranslation("grid.myGroup", getCurrentLocale()));
            } else {
                indicator.getStyle()
                        .set("background-color", "red")
                        .set("border", "2px solid darkred");
                indicator.setTitle(i18NProvider.getTranslation("grid.otherGroup", getCurrentLocale()));
            }
            return indicator;
        })).setFlexGrow(0).setWidth("40px");

        // Configure columns with translations
        grid.setColumns("id", "name", "studentsCount", "expelledStudents", "transferredStudents", "formOfEducation");
        grid.getColumnByKey("id").setHeader(i18NProvider.getTranslation("grid.id", getCurrentLocale()));
        grid.getColumnByKey("name").setHeader(i18NProvider.getTranslation("grid.name", getCurrentLocale()));
        grid.getColumnByKey("studentsCount").setHeader(i18NProvider.getTranslation("grid.studentsCount", getCurrentLocale()));
        grid.getColumnByKey("expelledStudents").setHeader(i18NProvider.getTranslation("grid.expelledStudents", getCurrentLocale()));
        grid.getColumnByKey("transferredStudents").setHeader(i18NProvider.getTranslation("grid.transferredStudents", getCurrentLocale()));
        grid.getColumnByKey("formOfEducation").setHeader(i18NProvider.getTranslation("grid.formOfEducation", getCurrentLocale()));

        // Custom columns
        grid.addColumn(group -> group.getCoordinates().getX() + ", " + group.getCoordinates().getY())
                .setHeader(i18NProvider.getTranslation("grid.coordinates", getCurrentLocale()))
                .setSortable(true);

        grid.addColumn(group -> group.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", getCurrentLocale())))
                .setHeader(i18NProvider.getTranslation("grid.creationDate", getCurrentLocale()))
                .setSortable(true);

        grid.addColumn(group -> {
                    Person admin = group.getGroupAdmin();
                    return admin != null ? admin.getName() : "N/A";
                }).setHeader(i18NProvider.getTranslation("grid.adminName", getCurrentLocale()))
                .setSortable(true);

        grid.getColumns().forEach(col -> col.setSortable(true));
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureFilter() {
        filterText.setPlaceholder(i18NProvider.getTranslation("main.filterPlaceholder", getCurrentLocale()));
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void configureButtons() {
        addButton.setText(i18NProvider.getTranslation("main.addGroup", getCurrentLocale()));
        editButton.setText(i18NProvider.getTranslation("main.edit", getCurrentLocale()));
        removeButton.setText(i18NProvider.getTranslation("main.remove", getCurrentLocale()));
        clearButton.setText(i18NProvider.getTranslation("main.clearAll", getCurrentLocale()));

        addButton.addClickListener(e -> {
            StudyGroup newGroup = new StudyGroup();
            newGroup.setName("New Group");
            newGroup.setCoordinates(new Coordinates(0L, 0L));
            newGroup.setStudentsCount(1);
            newGroup.setExpelledStudents(1);
            newGroup.setTransferredStudents(1);
            newGroup.setFormOfEducation(FormOfEducation.FULL_TIME_EDUCATION);
            
            Person admin = new Person();
            admin.setName("Admin");
            admin.setEyeColor(Common.models.Color.GREEN);
            admin.setLocation(new Location(0f, 0f, 0f));
            
            newGroup.setGroupAdmin(admin);
            dialog.setStudyGroup(newGroup);
            dialog.open();
        });

        editButton.addClickListener(e -> {
            StudyGroup selectedGroup = grid.getSelectedItems().stream().findFirst().orElse(null);
            if (selectedGroup != null) {
                try {
                    // Debug output
                    System.out.println("Selected group ID: " + selectedGroup.getId());
                    System.out.println("Selected group user ID: " + selectedGroup.getUserId());
                    System.out.println("Current user ID: " + service.getUserId());
                    
                    // Direct comparison of user IDs
                    if (selectedGroup.getUserId() != null && selectedGroup.getUserId().equals(service.getUserId())) {
                        dialog.setStudyGroup(selectedGroup);
                        dialog.open();
                    } else {
                        Notification.show("You can only edit groups that you created", 3000, Notification.Position.MIDDLE);
                    }
                } catch (Exception ex) {
                    Notification.show("Error checking group ownership: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Please select a group to edit", 3000, Notification.Position.MIDDLE);
            }
        });

        removeButton.addClickListener(e -> {
            List<StudyGroup> selectedGroups = grid.getSelectedItems().stream().toList();
            if (!selectedGroups.isEmpty()) {
                try {
                    for (StudyGroup group : selectedGroups) {
                        service.removeGroup(group.getId().longValue());
                    }
                    updateList();
                    Notification.show("Groups removed successfully", 3000, Notification.Position.MIDDLE);
                } catch (Exception ex) {
                    Notification.show("Error removing groups: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            } else {
                Notification.show("Please select groups to remove", 3000, Notification.Position.MIDDLE);
            }
        });

        clearButton.addClickListener(e -> {
            try {
                service.clearGroups();
                updateList();
                Notification.show("All groups cleared successfully", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error clearing groups: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
    }

    private void configureDialog() {
        dialog.addSaveListener(event -> {
            try {
                if (event.getStudyGroup().getId() == null) {
                    service.addGroup(event.getStudyGroup());
                } else {
                    service.updateGroup(event.getStudyGroup());
                }
                updateList();
                dialog.close();
                Notification.show("Group saved successfully", 3000, Notification.Position.MIDDLE);
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
        dialog.addCloseListener(event -> dialog.close());
    }

    private void updateList() {
        try {
            List<StudyGroup> groups = service.filterGroups(filterText.getValue());
            grid.setItems(groups);
        } catch (Exception e) {
            Notification.show("Error updating list: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void showGroupDetails(StudyGroup group) {
        Div details = new Div();
        details.getStyle()
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
        details.add(new H1(i18NProvider.getTranslation("details.title", getCurrentLocale())));
        details.add(new Div(i18NProvider.getTranslation("details.id", getCurrentLocale()) + ": " + group.getId()));
        details.add(new Div(i18NProvider.getTranslation("details.name", getCurrentLocale()) + ": " + group.getName()));
        details.add(new Div(i18NProvider.getTranslation("details.studentsCount", getCurrentLocale()) + ": " + group.getStudentsCount()));
        details.add(new Div(i18NProvider.getTranslation("details.expelledStudents", getCurrentLocale()) + ": " + group.getExpelledStudents()));
        details.add(new Div(i18NProvider.getTranslation("details.transferredStudents", getCurrentLocale()) + ": " + group.getTransferredStudents()));
        details.add(new Div(i18NProvider.getTranslation("details.formOfEducation", getCurrentLocale()) + ": " + group.getFormOfEducation()));
        details.add(new Div(i18NProvider.getTranslation("details.coordinates", getCurrentLocale()) + ": X=" + group.getCoordinates().getX() + ", Y=" + group.getCoordinates().getY()));
        details.add(new Div(i18NProvider.getTranslation("details.creationDate", getCurrentLocale()) + ": " + 
            group.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss", getCurrentLocale()))));
        
        if (group.getGroupAdmin() != null) {
            Person admin = group.getGroupAdmin();
            details.add(new Div(i18NProvider.getTranslation("details.groupAdmin", getCurrentLocale()) + ":"));
            details.add(new Div("  " + i18NProvider.getTranslation("details.adminName", getCurrentLocale()) + ": " + admin.getName()));
            details.add(new Div("  " + i18NProvider.getTranslation("details.eyeColor", getCurrentLocale()) + ": " + admin.getEyeColor()));
            if (admin.getLocation() != null) {
                String locationText = String.format("%s: X=%.2f, Y=%.2f, Z=%.2f",
                    i18NProvider.getTranslation("details.location", getCurrentLocale()),
                    admin.getLocation().getX(),
                    admin.getLocation().getY(),
                    admin.getLocation().getZ());
                details.add(new Div("  " + locationText));
            }
        }

        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set("right", "0")
            .set("bottom", "0")
            .set("background", "rgba(0,0,0,0.5)")
            .set("z-index", "999");

        overlay.addClickListener(e -> {
            overlay.removeFromParent();
            details.removeFromParent();
        });

        Button closeButton = new Button(i18NProvider.getTranslation("details.close", getCurrentLocale()));
        closeButton.addClickListener(e -> {
            overlay.removeFromParent();
            details.removeFromParent();
        });
        details.add(closeButton);

        getUI().ifPresent(ui -> {
            ui.add(overlay);
            ui.add(details);
        });
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 