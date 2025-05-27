package GUI.components;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.i18n.I18NProvider;

import Common.models.FormOfEducation;
import Common.models.StudyGroup;

public class DistributionChartComponent extends Div {
    private final I18NProvider i18NProvider;

    public DistributionChartComponent(I18NProvider i18NProvider) {
        this.i18NProvider = i18NProvider;
        
        getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("border-radius", "4px")
            .set("padding", "1rem")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");
    }

    public void updateChart(List<StudyGroup> groups) {
        // Group by form of education
        Map<FormOfEducation, Long> distribution = groups.stream()
            .collect(Collectors.groupingBy(
                StudyGroup::getFormOfEducation,
                Collectors.counting()
            ));

        // Clear previous chart
        removeAll();

        // Add title
        H1 chartTitle = new H1(i18NProvider.getTranslation("chart.title", getCurrentLocale()));
        chartTitle.getStyle()
            .set("font-size", "1.2rem")
            .set("margin", "0")
            .set("text-align", "center");
        add(chartTitle);

        // Create chart container
        Div chartContainer = new Div();
        chartContainer.getStyle()
            .set("display", "flex")
            .set("flex-direction", "column")
            .set("gap", "1rem")
            .set("padding", "1rem")
            .set("background", "var(--lumo-base-color)")
            .set("border-radius", "4px")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        // Calculate total for percentages
        long total = distribution.values().stream().mapToLong(Long::longValue).sum();

        // Create chart items
        for (Map.Entry<FormOfEducation, Long> entry : distribution.entrySet()) {
            Div chartItem = new Div();
            chartItem.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("gap", "0.5rem")
                .set("padding", "1rem")
                .set("border-radius", "4px")
                .set("background", "var(--lumo-contrast-5pct)");

            // Header with color indicator and label
            Div header = new Div();
            header.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "0.5rem");

            // Color indicator
            Div colorIndicator = new Div();
            colorIndicator.getStyle()
                .set("width", "1rem")
                .set("height", "1rem")
                .set("border-radius", "50%")
                .set("background", getColorForForm(entry.getKey()));
            header.add(colorIndicator);

            // Label
            Span label = new Span(entry.getKey().toString());
            label.getStyle()
                .set("font-weight", "bold");
            header.add(label);

            // Progress bar
            double percentage = total > 0 ? (entry.getValue() * 100.0) / total : 0;
            Div progressBar = new Div();
            progressBar.getStyle()
                .set("width", "100%")
                .set("height", "1.5rem")
                .set("background", "var(--lumo-contrast-10pct)")
                .set("border-radius", "0.75rem")
                .set("overflow", "hidden");

            Div progress = new Div();
            progress.getStyle()
                .set("width", percentage + "%")
                .set("height", "100%")
                .set("background", getColorForForm(entry.getKey()))
                .set("transition", "width 0.3s ease-in-out");
            progressBar.add(progress);

            // Value and percentage
            Div valueInfo = new Div();
            valueInfo.getStyle()
                .set("display", "flex")
                .set("justify-content", "space-between")
                .set("font-size", "0.875rem")
                .set("color", "var(--lumo-secondary-text-color)");
            valueInfo.setText(String.format("%d (%.1f%%)", entry.getValue(), percentage));

            chartItem.add(header, progressBar, valueInfo);
            chartContainer.add(chartItem);
        }

        add(chartContainer);
    }

    private String getColorForForm(FormOfEducation form) {
        switch (form) {
            case DISTANCE_EDUCATION:
                return "#FF6B6B";
            case FULL_TIME_EDUCATION:
                return "#4ECDC4";
            case EVENING_CLASSES:
                return "#FFD93D";
            default:
                return "#95A5A6";
        }
    }

    private Locale getCurrentLocale() {
        return UI.getCurrent().getLocale();
    }
} 