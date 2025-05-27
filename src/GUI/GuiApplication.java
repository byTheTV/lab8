package GUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;

@SpringBootApplication
public class GuiApplication implements AppShellConfigurator {
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.setPageTitle("Lab8 Application");
    }

    public static void main(String[] args) {
        SpringApplication.run(GuiApplication.class, args);
    }
} 