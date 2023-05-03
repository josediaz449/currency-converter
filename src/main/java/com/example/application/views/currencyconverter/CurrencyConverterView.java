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
        mainLayout.setAlignItems(Alignment.CENTER);
        converterLayout.setAlignItems(Alignment.BASELINE);

        fromComboBox = new ComboBox<>("Currency");
        fromComboBox.setItems(ExchangeData.getCurrencies());
        fromComboBox.setItemLabelGenerator(Currency::toString);
        fromComboBox.setRequired(true);
        fromComboBox.setClearButtonVisible(true);
        fromComboBox.setRequiredIndicatorVisible(true);

        toComboBox = new ComboBox<>("Currency");
        toComboBox.setItems(ExchangeData.getCurrencies());
        toComboBox.setItemLabelGenerator(Currency::toString);
        toComboBox.setRequired(true);
        toComboBox.setClearButtonVisible(true);
        toComboBox.setRequiredIndicatorVisible(true);
        

        fromField = new NumberField();
        fromField.setLabel("Enter Amount");
        fromField.setMin(0);

        toField = new NumberField();
        toField.setLabel("Converted Amount");
        toField.setMin(0);

        fromComboBox.addValueChangeListener(cur->{
            if(!toComboBox.isEmpty()&&!fromComboBox.isEmpty()){
                currentConversion = ExchangeData.getConversion(cur.getValue().getSymbol(), toComboBox.getValue().getSymbol());
                fromField.clear();
                toField.clear();
            }
        });
        toComboBox.addValueChangeListener(cur->{
            if(!fromComboBox.isEmpty()&&!toComboBox.isEmpty()){
                currentConversion = ExchangeData.getConversion(fromComboBox.getValue().getSymbol(), cur.getValue().getSymbol());
                fromField.clear();
                toField.clear();
            }
        });
        fromField.addValueChangeListener(val->{
            if(!fromField.isEmpty()) {
                toField.setValue(new BigDecimal(val.getValue() * currentConversion).setScale(2, RoundingMode.HALF_DOWN).doubleValue());
            }
        });

        Icon lumoIcon = new Icon("lumo", "arrow-right");

        switchButton = new Button("Switch Currencies",new Icon(VaadinIcon.EXCHANGE));


        converterLayout.add(fromField,fromComboBox,lumoIcon,toField,toComboBox,switchButton);

        mainLayout.add(h1,converterLayout);

        add(mainLayout);
    }

}
