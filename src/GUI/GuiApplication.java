package GUI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
public class GuiApplication implements AppShellConfigurator {
    @Override
    public void configurePage(AppShellSettings settings) {
        settings.setPageTitle("Lab8 Application");
    }

    public static void main(String[] args) {
        SpringApplication.run(GuiApplication.class, args);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasenames("classpath:messages", "messages");
        source.setDefaultEncoding("UTF-8");
        source.setCacheSeconds(0);
        source.setUseCodeAsDefaultMessage(true);
        source.setFallbackToSystemLocale(false);
        return source;
    }
} 