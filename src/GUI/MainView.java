package GUI;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {
    public MainView() {
        H1 title = new H1("Lab8 Application");
        Button button = new Button("Click me");
        button.addClickListener(e -> {
            System.out.println("Button clicked!");
        });
        
        add(title, button);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();
    }
} 