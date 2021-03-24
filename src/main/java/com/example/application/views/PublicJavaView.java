package com.example.application.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "public-java")
@PageTitle("Public Java")
public class PublicJavaView extends FlexLayout {

    public PublicJavaView() {
        setFlexDirection(FlexDirection.COLUMN);
        setHeightFull();

        H1 header = new H1("Welcome to the Bank of Vaadin");
        header.getStyle().set("text-align", "center");
        add(header);
        Image image = new Image("bank.jpg", "Bank");
        image.getStyle().set("max-width", "100%").set("min-height", "0");
        add(image);
        add(new Paragraph("We are very great and have great amounts of money."));
    }

}
