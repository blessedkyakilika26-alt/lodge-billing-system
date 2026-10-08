package com.mulungushi.lodge;

import com.mulungushi.lodge.billing.BillingState;
import com.mulungushi.lodge.ui.BillingView;
import com.mulungushi.lodge.ui.LoginView;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** App shell: owns the window and session state, switches between LoginView and BillingView. */
public class MainApp extends Application {

    private static final int WINDOW_WIDTH = 900;
    private static final int WINDOW_HEIGHT = 650;

    private Stage primaryStage;
    private final BillingState billingState = new BillingState();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Lodge Billing Lab");
        showLoginScreen();
        primaryStage.show();
    }

    private void showLoginScreen() {
        setScene(new LoginView(this::showBillingScreen));
    }

    private void showBillingScreen(String username) {
        setScene(new BillingView(username, billingState, this::showLoginScreen));
    }

    private void setScene(Parent root) {
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("/com/mulungushi/lodge/midnight.css").toExternalForm());
        primaryStage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
