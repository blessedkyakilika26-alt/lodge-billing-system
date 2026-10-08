package com.mulungushi.lodge.ui;

import java.util.function.Consumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/** Login screen shown before the billing screen. Styled by midnight.css. */
public class LoginView extends StackPane {

    public LoginView(Consumer<String> onLogin) {
        getStyleClass().add("login-root");

        VBox card = new VBox(15);
        card.getStyleClass().add("login-card");
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(350);

        Label title = new Label("Lodge System Login");
        title.getStyleClass().add("login-title");

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Label errorMsg = new Label();
        errorMsg.getStyleClass().addAll("status", "error");

        Button loginBtn = new Button("Log In");
        loginBtn.getStyleClass().add("btn-primary");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            if (username.isEmpty()) {
                errorMsg.setText("Please enter a valid username.");
            } else {
                onLogin.accept(username);
            }
        });

        card.getChildren().addAll(title, userField, passField, loginBtn, errorMsg);
        getChildren().add(card);
    }
}
