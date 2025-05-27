package GUI;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import Client.network.TCPClient;
import Common.models.Coordinates;
import Common.models.FormOfEducation;
import Common.models.Location;
import Common.models.Person;
import Common.models.StudyGroup;
import GUI.components.CommandsComponent;
import GUI.components.DistributionChartComponent;
import GUI.components.GroupDetailsComponent;
import GUI.components.HeaderComponent;
import GUI.components.TicTacToeComponent;
import GUI.components.ToolbarComponent;

@Route("main")
public class MainView extends VerticalLayout {
    private final Grid<StudyGroup> grid = new Grid<>(StudyGroup.class);
    private final TextField filterText = new TextField();
    private final Button addButton = new Button();
    private final Button editButton = new Button();
    private final Button removeButton = new Button();
    private final Button clearButton = new Button();
    private final Button infoButton = new Button();
    private final Button headButton = new Button();
    private final Button removeHeadButton = new Button();
    private final Button averageTransferredButton = new Button();
    private final Button countByFormButton = new Button();
    private final Button printAdminAscButton = new Button();
    private final Button logoutButton = new Button();
    private final Div distributionChart = new Div();
    
    private StudyGroupService service;
    private StudyGroupDialog dialog;
    private TCPClient client;
    private String currentUserLogin;
    private I18NProvider i18NProvider;
    private LanguageSwitcher languageSwitcher;

    // Components
    private HeaderComponent headerComponent;
    private ToolbarComponent toolbarComponent;
    private CommandsComponent commandsComponent;
    private DistributionChartComponent distributionChartComponent;
    private TicTacToeComponent ticTacToeComponent;

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
            
            // Add dialog event listeners
            dialog.addSaveListener(event -> {
                try {
                    StudyGroup savedGroup = event.getStudyGroup();
                    if (savedGroup.getId() == null) {
                        // New group
                        service.addGroup(savedGroup);
                    } else {
                        // Existing group
                        service.updateGroup(savedGroup);
                    }
                    updateList();
                    dialog.close();
                } catch (Exception ex) {
                    Notification.show("Error saving group: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
                }
            });

            dialog.addCloseListener(event -> dialog.close());
            
            setSizeFull();
            setPadding(false);
            setSpacing(false);
            configureGrid();
            configureComponents();
            configureLogoutButton();
            
            // Create main content layout
            HorizontalLayout mainContent = new HorizontalLayout();
            mainContent.setWidthFull();
            mainContent.setHeightFull();
            mainContent.setSpacing(true);
            mainContent.setPadding(true);

            // Create left side content
            VerticalLayout leftContent = new VerticalLayout();
            leftContent.setWidth("70%");
            leftContent.setHeightFull();
            leftContent.setSpacing(true);
            leftContent.setPadding(false);
            leftContent.add(toolbarComponent, commandsComponent, grid);
            leftContent.setFlexGrow(1, grid);

            // Create right side content with chart
            VerticalLayout rightContent = new VerticalLayout();
            rightContent.setWidth("30%");
            rightContent.setHeightFull();
            rightContent.setSpacing(true);
            rightContent.setPadding(false);
            rightContent.add(distributionChartComponent);
            
            // Add Tic Tac Toe game
            ticTacToeComponent = new TicTacToeComponent(i18NProvider);
            ticTacToeComponent.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "4px")
                .set("padding", "1rem")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)")
                .set("margin-top", "1rem");
            rightContent.add(ticTacToeComponent);
            
            rightContent.getStyle()
                .set("background", "var(--lumo-base-color)")
                .set("border-radius", "4px")
                .set("padding", "1rem")
                .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

            // Add both contents to main layout
            mainContent.add(leftContent, rightContent);

            // Add components to layout
            add(headerComponent);
            add(mainContent);
            
            setAlignItems(Alignment.STRETCH);
            setJustifyContentMode(JustifyContentMode.START);
            getStyle()
                .set("background", "var(--lumo-contrast-5pct)")
                .set("min-height", "100vh");
            
            // Load initial data
            updateList();
        } catch (Exception e) {
            Notification.show(i18NProvider.getTranslation("notification.error", getCurrentLocale(), e.getMessage()), 3000, Notification.Position.MIDDLE);
            getUI().ifPresent(ui -> ui.navigate(""));
        }
    }

    private void configureComponents() {
        // Initialize components
        headerComponent = new HeaderComponent(i18NProvider, currentUserLogin);
        toolbarComponent = new ToolbarComponent(i18NProvider);
        commandsComponent = new CommandsComponent(i18NProvider);
        distributionChartComponent = new DistributionChartComponent(i18NProvider);

        // Configure toolbar buttons
        toolbarComponent.getAddButton().addClickListener(e -> {
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

        toolbarComponent.getEditButton().addClickListener(e -> {
            StudyGroup selectedGroup = grid.getSelectedItems().stream().findFirst().orElse(null);
            if (selectedGroup != null) {
                try {
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

        toolbarComponent.getRemoveButton().addClickListener(e -> {
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

        toolbarComponent.getClearButton().addClickListener(e -> {
            try {
                service.clearGroups();
                updateList();
                Notification.show("All groups cleared successfully", 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error clearing groups: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        // Configure filter
        toolbarComponent.getFilterText().setValueChangeMode(ValueChangeMode.LAZY);
        toolbarComponent.getFilterText().addValueChangeListener(e -> updateList());

        // Configure command buttons
        commandsComponent.getInfoButton().addClickListener(e -> {
            try {
                String info = service.getInfo();
                Notification.show(info, 5000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error getting info: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        commandsComponent.getHeadButton().addClickListener(e -> {
            try {
                StudyGroup head = service.getHead();
                if (head != null) {
                    showGroupDetails(head);
                } else {
                    Notification.show("Collection is empty", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("Error getting head element: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        commandsComponent.getRemoveHeadButton().addClickListener(e -> {
            try {
                StudyGroup removed = service.removeHead();
                if (removed != null) {
                    Notification.show("Removed head element: " + removed.getName(), 3000, Notification.Position.MIDDLE);
                    updateList();
                } else {
                    Notification.show("Collection is empty", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("Error removing head element: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        commandsComponent.getAverageTransferredButton().addClickListener(e -> {
            try {
                double average = service.getAverageOfTransferredStudents();
                Notification.show("Average of transferred students: " + average, 3000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error calculating average: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        commandsComponent.getCountByFormButton().addClickListener(e -> {
            try {
                String counts = service.getGroupCountingByFormOfEducation();
                Notification.show(counts, 5000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error counting groups: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        commandsComponent.getPrintAdminAscButton().addClickListener(e -> {
            try {
                String admins = service.getPrintFieldAscendingGroupAdmin();
                Notification.show(admins, 5000, Notification.Position.MIDDLE);
            } catch (Exception ex) {
                Notification.show("Error getting admin list: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });
    }

    private void configureLogoutButton() {
        logoutButton.setText(i18NProvider.getTranslation("main.logout", getCurrentLocale()));
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
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setHeight("calc(100vh - 300px)");
        grid.setMinHeight("400px");

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

    private void updateList() {
        try {
            List<StudyGroup> groups = service.filterGroups(toolbarComponent.getFilterText().getValue());
            grid.setItems(groups);
            distributionChartComponent.updateChart(groups);
        } catch (Exception e) {
            Notification.show("Error updating list: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
        }
    }

    private void showGroupDetails(StudyGroup group) {
        Div overlay = new Div();
        overlay.getStyle()
            .set("position", "fixed")
            .set("top", "0")
            .set("left", "0")
            .set("right", "0")
            .set("bottom", "0")
            .set("background", "rgba(0,0,0,0.5)")
            .set("z-index", "999");

        GroupDetailsComponent details = new GroupDetailsComponent(i18NProvider, group);
        overlay.add(details);

        getUI().ifPresent(ui -> {
            ui.add(overlay);
        });
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 