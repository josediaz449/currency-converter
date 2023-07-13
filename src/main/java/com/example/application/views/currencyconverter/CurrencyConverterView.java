package com.example.application.views.currencyconverter;

import com.example.application.data.ExchangeData;
import com.example.application.model.Currency;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.example.application.data.ExchangeData.initiateCurrencies;

@PageTitle("Currency Converter")
@Route(value = "empty")
@RouteAlias(value = "")
public class CurrencyConverterView extends VerticalLayout {
    static double currentConversion =1;
    static ComboBox<Currency> fromComboBox;
    static ComboBox<Currency> toComboBox;
    static NumberField fromField;
    static NumberField toField;
    static Button switchButton;
    public CurrencyConverterView() {
        initiateCurrencies();//get Currency list from API

        VerticalLayout mainLayout = new VerticalLayout();
        H1 h1 = new H1("Currency Converter");
        HorizontalLayout converterLayout = new HorizontalLayout();
        Icon lumoIcon = new Icon("lumo", "arrow-right");

        alignLayout(mainLayout, converterLayout);

        createFromComboBox();
        createToComboBox();
        createFromField();
        createToField();

        createSwitchButton();

        addComponentsToLayout(mainLayout, h1, converterLayout, lumoIcon);
    }

    private void addComponentsToLayout(VerticalLayout mainLayout, H1 h1, HorizontalLayout converterLayout, Icon lumoIcon) {
        converterLayout.add(fromField,fromComboBox, lumoIcon,toField,toComboBox,switchButton);
        mainLayout.add(h1, converterLayout);
        add(mainLayout);
    }

    private void createToField() {
        toField = new NumberField();
        configureNumberField(toField, "Converted Amount");
    }

    private void createToComboBox() {
        toComboBox = new ComboBox<>("Currency");
        configureComboBox(toComboBox);
        toComboBox.addValueChangeListener(cur->{
            if(comboBoxesAreNotEmpty(fromComboBox, toComboBox)){
                currentConversion = ExchangeData.getConversion(fromComboBox.getValue().getSymbol(), cur.getValue().getSymbol());
                toField.clear();
            }
        });
    }

    private void createFromComboBox() {
        fromComboBox = new ComboBox<>("Currency");
        configureComboBox(fromComboBox);
        fromComboBox.addValueChangeListener(cur->{
            if(comboBoxesAreNotEmpty(toComboBox, fromComboBox)){
                currentConversion = ExchangeData.getConversion(cur.getValue().getSymbol(), toComboBox.getValue().getSymbol());
                toField.clear();
            }
        });
    }

    private void createFromField() {
        fromField = new NumberField();
        fromField.addValueChangeListener(val-> calculateToField());
        configureNumberField(fromField,"Enter Amount");
    }

    private static void createSwitchButton() {
        switchButton = new Button("Switch Currencies",new Icon(VaadinIcon.EXCHANGE));
        switchButton.addClickListener(click-> switchCurrencies());
    }

    private static void switchCurrencies() {
        Currency from = fromComboBox.getValue();
        Currency to = toComboBox.getValue();
        fromComboBox.setValue(to);
        toComboBox.setValue(from);
        calculateToField();
    }

    private static boolean comboBoxesAreNotEmpty(ComboBox<Currency> toComboBox, ComboBox<Currency> fromComboBox) {
        return !toComboBox.isEmpty() && !fromComboBox.isEmpty();
    }

    private static void alignLayout(VerticalLayout mainLayout, HorizontalLayout converterLayout) {
        mainLayout.setAlignItems(Alignment.CENTER);
        converterLayout.setAlignItems(Alignment.BASELINE);
    }

    private void configureComboBox(ComboBox<Currency> comboBox){
        comboBox.setItems(ExchangeData.getCurrencies());
        comboBox.setItemLabelGenerator(Currency::toString);
        comboBox.setRequired(true);
        comboBox.setClearButtonVisible(true);
        comboBox.setRequiredIndicatorVisible(true);
    }
    private void configureNumberField(NumberField numberField,String label){
        numberField.setLabel(label);
        numberField.setMin(0);
    }

    private static void calculateToField() {
        if (!fromField.isEmpty()) {
            toField.setValue(new BigDecimal(fromField.getValue() * currentConversion).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
        }
    }

}
