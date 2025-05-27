package GUI;

import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private final Button addButton = new Button("Add Group");
    private final Button editButton = new Button("Edit");
    private final Button removeButton = new Button("Remove");
    private final Button clearButton = new Button("Clear All");
    
    private StudyGroupService service;
    private StudyGroupDialog dialog;
    private TCPClient client;
    private String currentUserLogin;

    public MainView() {
        try {
            // Get client and credentials from session
            client = (TCPClient) VaadinSession.getCurrent().getAttribute("tcpClient");
            String login = (String) VaadinSession.getCurrent().getAttribute("login");
            String password = (String) VaadinSession.getCurrent().getAttribute("password");
            Integer userId = (Integer) VaadinSession.getCurrent().getAttribute("userId");
            String userUid = (String) VaadinSession.getCurrent().getAttribute("userUid");
            
            if (client == null || login == null || password == null || userId == null || userUid == null) {
                // Not authenticated, redirect to login
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
            
            // Create header with user info and logout button
            HorizontalLayout header = new HorizontalLayout();
            header.setWidthFull();
            header.setJustifyContentMode(JustifyContentMode.BETWEEN);
            
            // Add user info
            H1 userInfo = new H1("Current user: " + login);
            userInfo.getStyle().set("margin", "0");
            header.add(userInfo);
            
            Button logoutButton = new Button("Logout");
            logoutButton.addClickListener(e -> {
                // Close client and clear session
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
            add(header, new H1("Study Groups"), toolbar, grid);
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            
            // Load initial data
            updateList();
        } catch (Exception e) {
            Notification.show("Error initializing view: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
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

        // Добавляем генератор классов для стилизации строк
        grid.setClassNameGenerator(group -> {
            if (group.getUserId() != null && group.getUserId().equals(service.getUserId())) {
                return "my-group"; // Класс для групп пользователя
            } else {
                return "other-group"; // Класс для групп других пользователей
            }
        });

        // Добавляем колонку с улучшенным индикатором (круг)
        grid.addColumn(new ComponentRenderer<>(group -> {
            Span indicator = new Span();
            indicator.getStyle()
                    .set("display", "inline-block")
                    .set("width", "15px") // Увеличиваем размер круга
                    .set("height", "15px")
                    .set("border-radius", "50%") // Делаем круглым
                    .set("margin-right", "10px"); // Отступ справа

            if (group.getUserId() != null && group.getUserId().equals(service.getUserId())) {
                indicator.getStyle()
                        .set("background-color", "green") // Зеленый для групп пользователя
                        .set("border", "2px solid darkgreen"); // Граница для выделения
                indicator.setTitle("Ваша группа"); // Подсказка
            } else {
                indicator.getStyle()
                        .set("background-color", "red") // Красный для других групп
                        .set("border", "2px solid darkred"); // Граница для выделения
                indicator.setTitle("Чужая группа"); // Подсказка
            }
            return indicator;
        })).setFlexGrow(0).setWidth("40px"); // Увеличиваем ширину колонки для нового размера круга

        // Основные колонки
        grid.setColumns("id", "name", "studentsCount", "expelledStudents", "transferredStudents", "formOfEducation");

        // Пользовательские колонки для сложных объектов
        grid.addColumn(group -> group.getCoordinates().getX() + ", " + group.getCoordinates().getY())
                .setHeader("Координаты")
                .setSortable(true);

        grid.addColumn(group -> group.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .setHeader("Дата создания")
                .setSortable(true);

        grid.addColumn(group -> {
                    Person admin = group.getGroupAdmin();
                    return admin != null ? admin.getName() : "Н/Д";
                }).setHeader("Имя админа")
                .setSortable(true);

        // Включаем сортировку для всех колонок
        grid.getColumns().forEach(col -> col.setSortable(true));

        // Включаем множественный выбор
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
    }

    private void configureFilter() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
    }

    private void configureButtons() {
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

        // Create content
        details.add(new H1("Group Details"));
        details.add(new Div("ID: " + group.getId()));
        details.add(new Div("Name: " + group.getName()));
        details.add(new Div("Students Count: " + group.getStudentsCount()));
        details.add(new Div("Expelled Students: " + group.getExpelledStudents()));
        details.add(new Div("Transferred Students: " + group.getTransferredStudents()));
        details.add(new Div("Form of Education: " + group.getFormOfEducation()));
        details.add(new Div("Coordinates: X=" + group.getCoordinates().getX() + ", Y=" + group.getCoordinates().getY()));
        details.add(new Div("Creation Date: " + group.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
        
        if (group.getGroupAdmin() != null) {
            Person admin = group.getGroupAdmin();
            details.add(new Div("Group Admin:"));
            details.add(new Div("  Name: " + admin.getName()));
            details.add(new Div("  Eye Color: " + admin.getEyeColor()));
            if (admin.getLocation() != null) {
                details.add(new Div("  Location: X=" + admin.getLocation().getX() + 
                    ", Y=" + admin.getLocation().getY() + 
                    ", Z=" + admin.getLocation().getZ()));
            }
        }

        // Add close button
        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> details.removeFromParent());
        details.add(closeButton);

        // Add overlay
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

        getUI().ifPresent(ui -> {
            ui.add(overlay);
            ui.add(details);
        });
    }
} 