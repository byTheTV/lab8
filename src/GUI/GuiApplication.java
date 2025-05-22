package GUI;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme("")
public class GuiApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(GuiApplication.class, args);
    }
} 