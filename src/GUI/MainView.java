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

import Client.network.TCPClient;
import Common.models.Color;
import Common.models.Coordinates;
import Common.models.Location;
import Common.models.Person;
import Common.models.StudyGroup;

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
            
            this.service = new StudyGroupService(client, login, password, userUid, userId);
            this.dialog = new StudyGroupDialog();
            
            setSizeFull();
            configureGrid();
            configureFilter();
            configureButtons();
            configureDialog();
            
            // Create header with logout button
            HorizontalLayout header = new HorizontalLayout();
            header.setWidthFull();
            header.setJustifyContentMode(JustifyContentMode.END);
            
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
        grid.setColumns("id", "name", "studentsCount", "expelledStudents", "transferredStudents", "formOfEducation");
        
        // Add custom columns for complex objects
        grid.addColumn(group -> group.getCoordinates().getX() + ", " + group.getCoordinates().getY())
            .setHeader("Coordinates")
            .setSortable(true);
            
        grid.addColumn(group -> group.getCreationDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
            .setHeader("Creation Date")
            .setSortable(true);
            
        grid.addColumn(group -> {
            Person admin = group.getGroupAdmin();
            return admin != null ? admin.getName() : "N/A";
        }).setHeader("Admin Name")
          .setSortable(true);

        // Enable sorting for all columns
        grid.getColumns().forEach(col -> col.setSortable(true));
        
        // Enable selection
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
} 