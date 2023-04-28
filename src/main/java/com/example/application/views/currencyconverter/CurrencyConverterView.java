package com.example.application.views.currencyconverter;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Currency Converter")
@Route(value = "empty")
@RouteAlias(value = "")
public class CurrencyConverterView extends VerticalLayout {
    static ComboBox fromComboBox;
    static ComboBox toComboBox;
    static NumberField fromField;
    static NumberField toField;
    public CurrencyConverterView() {
        VerticalLayout mainLayout = new VerticalLayout();
        H1 h1 = new H1("Currency Converter");
        HorizontalLayout converterLayout = new HorizontalLayout();
        converterLayout.setAlignItems(Alignment.BASELINE);

        fromComboBox = new ComboBox<>("Currency");
        //fromComboBox.setItems(ExchangeData.getCurrencies("USD"));
        //fromComboBox.setItemLabelGenerator(Currency::getName);
        fromComboBox.isRequired();

        toComboBox = new ComboBox<>("Currency");
        //toComboBox.setItems(ExchangeData.getCurrencies("USD"));
        //toComboBox.setItemLabelGenerator(Currency::getName);
        toComboBox.isRequired();

        fromField = new NumberField();
        fromField.setLabel("Enter Amount");
        fromField.setValue(1.00);

        toField = new NumberField();
        toField.setLabel("Converted Amount");

        Icon lumoIcon = new Icon("lumo", "arrow-right");

        converterLayout.add(fromField,fromComboBox,lumoIcon,toField,toComboBox);

        mainLayout.add(h1,converterLayout);

        add(mainLayout);
    }

}
