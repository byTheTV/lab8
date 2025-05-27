package GUI;

import java.net.InetAddress;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import Client.network.AuthHandler;
import Client.network.TCPClient;
import Common.models.User;

@Route("")
public class LoginView extends VerticalLayout {
    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final Button loginButton = new Button("Login");
    private final Button registerButton = new Button("Register");

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Welcome to Lab8");
        title.getStyle().set("margin-bottom", "2em");

        username.setWidth("300px");
        password.setWidth("300px");
        loginButton.setWidth("300px");
        registerButton.setWidth("300px");

        loginButton.addClickListener(e -> {
            try {
                // Create TCP client and auth handler
                TCPClient client = new TCPClient(InetAddress.getByName("localhost"), 55555);
                AuthHandler authHandler = new AuthHandler(client);
                
                // Attempt authentication with provided credentials
                User user = authHandler.authenticate(username.getValue(), password.getValue());
                
                if (user != null) {
                    // Store client and credentials in session
                    VaadinSession.getCurrent().setAttribute("tcpClient", client);
                    VaadinSession.getCurrent().setAttribute("login", username.getValue());
                    VaadinSession.getCurrent().setAttribute("password", password.getValue());
                    
                    // Successful login
                    getUI().ifPresent(ui -> ui.navigate("main"));
                } else {
                    Notification.show("Invalid username or password", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                Notification.show("Error: " + ex.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        registerButton.addClickListener(e -> {
            // TODO: Implement registration view
            Notification.show("Registration not implemented yet", 3000, Notification.Position.MIDDLE);
        });

        add(
            title,
            username,
            password,
            loginButton,
            registerButton
        );
    }
} 