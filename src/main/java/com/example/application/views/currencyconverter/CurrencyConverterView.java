package com.example.application.views.currencyconverter;

import com.example.application.data.ExchangeData;
import com.example.application.model.Currency;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import static com.example.application.data.ExchangeData.initiateCurrencies;

@PageTitle("Currency Converter")
@Route(value = "empty")
@RouteAlias(value = "")
public class CurrencyConverterView extends VerticalLayout {
    static float currentConversion =1;
    static ComboBox<Currency> fromComboBox;
    static ComboBox<Currency> toComboBox;
    static NumberField fromField;
    static NumberField toField;
    public CurrencyConverterView() {
        initiateCurrencies();
        VerticalLayout mainLayout = new VerticalLayout();
        H1 h1 = new H1("Currency Converter");
        HorizontalLayout converterLayout = new HorizontalLayout();
        converterLayout.setAlignItems(Alignment.BASELINE);

        fromComboBox = new ComboBox<>("Currency");
        fromComboBox.setItems(ExchangeData.getCurrencies());
        fromComboBox.setItemLabelGenerator(Currency::toString);
        fromComboBox.isRequired();

        toComboBox = new ComboBox<>("Currency");
        toComboBox.setItems(ExchangeData.getCurrencies());
        toComboBox.setItemLabelGenerator(Currency::toString);
        toComboBox.isRequired();

        fromField = new NumberField();
        fromField.setLabel("Enter Amount");
        fromField.setValue(1.00);

        toField = new NumberField();
        toField.setLabel("Converted Amount");

        fromComboBox.addValueChangeListener(cur->{
            if(!toComboBox.isEmpty()){
                currentConversion = ExchangeData.getConversion(cur.getValue().getSymbol(), toComboBox.getValue().getSymbol());
            }
            else{
                //add alert
            }
        });
        toComboBox.addValueChangeListener(cur->{
            if(!fromComboBox.isEmpty()){
                currentConversion = ExchangeData.getConversion(cur.getValue().getSymbol(), toComboBox.getValue().getSymbol());
            }
            else{
                //add alert
            }
        });
        fromField.addValueChangeListener(val->{
            toField.setValue(val.getValue()*currentConversion);
        });

        Icon lumoIcon = new Icon("lumo", "arrow-right");

        converterLayout.add(fromField,fromComboBox,lumoIcon,toField,toComboBox);

        mainLayout.add(h1,converterLayout);

        add(mainLayout);
    }

}
