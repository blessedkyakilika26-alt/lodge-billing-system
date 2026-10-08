package com.mulungushi.lodge;

import com.mulungushi.lodge.Bill.Bill;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage primaryStage;
    private final ObservableList<Bill> savedBills = FXCollections.observableArrayList();
    private int billCounter = 1;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Lodge Billing Lab");
        showLoginScreen();
        primaryStage.show();
    }

    private void showLoginScreen() {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setMaxWidth(350);
        card.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 5);");

        Label title = new Label("Lodge System Login");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));

        TextField userField = new TextField();
        userField.setPromptText("Username");

        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");

        Label errorMsg = new Label();
        errorMsg.setStyle("-fx-text-fill: red;");

        Button loginBtn = new Button("Log In");
        loginBtn.setMaxWidth(Double.MAX_VALUE);
        loginBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8px;");

        loginBtn.setOnAction(e -> {
            if (!userField.getText().trim().isEmpty()) {
                showMainBillingScreen(userField.getText().trim());
            } else {
                errorMsg.setText("Please enter a valid username.");
            }
        });

        card.getChildren().addAll(title, userField, passField, loginBtn, errorMsg);

        StackPane root = new StackPane(card);
        root.setStyle("-fx-background-color: #f1f5f9;");
        primaryStage.setScene(new Scene(root, 900, 650));
    }

    private void showMainBillingScreen(String username) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #ffffff;");

        HBox topMenu = new HBox(20);
        topMenu.setPadding(new Insets(10, 20, 10, 20));
        topMenu.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");

        Hyperlink newGuestLink = new Hyperlink("New guest");
        Hyperlink viewReceiptLink = new Hyperlink("View receipt");
        Hyperlink deleteBillLink = new Hyperlink("Delete selected bill");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label userLabel = new Label("Logged in: " + username);
        Button logoutBtn = new Button("Log Out");
        logoutBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> showLoginScreen());

        topMenu.getChildren().addAll(newGuestLink, viewReceiptLink, deleteBillLink, spacer, userLabel, logoutBtn);
        root.setTop(topMenu);

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        Label headerTitle = new Label("Lodge Billing Lab");
        headerTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        Label subHeader = new Label("ICT261 Lecture 3   Mr E Nyirenda   Mulungushi University");
        subHeader.setFont(Font.font("Segoe UI", 12));
        subHeader.setStyle("-fx-text-fill: #64748b;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(10);

        TextField nameInput = new TextField();
        ComboBox<String> roomCombo = new ComboBox<>(FXCollections.observableArrayList("Standard", "Deluxe", "Family"));
        roomCombo.getSelectionModel().select("Standard");

        TextField nightsInput = new TextField();
        TextField extrasInput = new TextField("0");
        TextField paidInput = new TextField("0");
        TextArea notesInput = new TextArea();
        notesInput.setPrefRowCount(2);

        formGrid.add(new Label("Guest name"), 0, 0);
        formGrid.add(nameInput, 1, 0);
        formGrid.add(new Label("Room type"), 0, 1);
        formGrid.add(roomCombo, 1, 1);
        formGrid.add(new Label("Nights"), 0, 2);
        formGrid.add(nightsInput, 1, 2);
        formGrid.add(new Label("Extras in K"), 0, 3);
        formGrid.add(extrasInput, 1, 3);
        formGrid.add(new Label("Amount paid in K"), 0, 4);
        formGrid.add(paidInput, 1, 4);
        formGrid.add(new Label("Notes"), 0, 5);
        formGrid.add(notesInput, 1, 5);

        VBox infoBox = new VBox(8);
        infoBox.setPadding(new Insets(15));
        infoBox.setStyle("-fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 5;");

        Label priceTitle = new Label("Practice room prices");
        priceTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        Label p1 = new Label("Standard: K 350.00 per night");
        Label p2 = new Label("Deluxe: K 550.00 per night");
        Label p3 = new Label("Family: K 750.00 per night");
        Label discInfo = new Label("\n5 nights or more: 5% off the room charge.\nExtras get no discount. No tax in this lab.\nSaved bills last only until you close the app.");
        discInfo.setStyle("-fx-text-fill: #475569;");

        infoBox.getChildren().addAll(priceTitle, p1, p2, p3, discInfo);

        HBox middleLayout = new HBox(40, formGrid, infoBox);

        Button calculateBtn = new Button("Calculate");
        Button saveBtn = new Button("Save bill");
        saveBtn.setStyle("-fx-background-color: #2563eb; -fx-text-fill: white;");

        Label resultStatus = new Label();
        resultStatus.setFont(Font.font("Segoe UI", FontWeight.BOLD, 13));

        HBox actionBox = new HBox(10, calculateBtn, saveBtn);

        TableView<Bill> table = new TableView<>(savedBills);

        TableColumn<Bill, String> colId = new TableColumn<>("Bill");
        colId.setCellValueFactory(data -> data.getValue().billIdProperty());

        TableColumn<Bill, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(data -> data.getValue().guestNameProperty());

        TableColumn<Bill, String> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(data -> data.getValue().roomTypeProperty());

        TableColumn<Bill, Number> colNights = new TableColumn<>("Nights");
        colNights.setCellValueFactory(data -> data.getValue().nightsProperty());

        TableColumn<Bill, Number> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(data -> data.getValue().totalProperty());

        TableColumn<Bill, Number> colPaid = new TableColumn<>("Paid");
        colPaid.setCellValueFactory(data -> data.getValue().paidProperty());

        TableColumn<Bill, Number> colBalance = new TableColumn<>("Balance");
        colBalance.setCellValueFactory(data -> data.getValue().balanceProperty());

        table.getColumns().addAll(colId, colGuest, colRoom, colNights, colTotal, colPaid, colBalance);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(180);

        calculateBtn.setOnAction(e -> {
            try {
                int nights = Integer.parseInt(nightsInput.getText().trim());
                double extras = Double.parseDouble(extrasInput.getText().trim());
                double paid = Double.parseDouble(paidInput.getText().trim());

                double rate = switch (roomCombo.getValue()) {
                    case "Deluxe" -> 550.0;
                    case "Family" -> 750.0;
                    default -> 350.0;
                };

                double roomTotal = rate * nights;
                if (nights >= 5) roomTotal *= 0.95;

                double grandTotal = roomTotal + extras;
                double balance = grandTotal - paid;

                resultStatus.setText(String.format("Calculated Total: K %.2f, Balance: K %.2f", grandTotal, balance));
            } catch (NumberFormatException ex) {
                resultStatus.setText("Error: Enter valid numbers for Nights, Extras, and Paid.");
            }
        });

        saveBtn.setOnAction(e -> {
            try {
                String name = nameInput.getText().trim();
                if (name.isEmpty()) {
                    resultStatus.setText("Error: Guest name is required.");
                    return;
                }

                int nights = Integer.parseInt(nightsInput.getText().trim());
                double extras = Double.parseDouble(extrasInput.getText().trim());
                double paid = Double.parseDouble(paidInput.getText().trim());

                double rate = switch (roomCombo.getValue()) {
                    case "Deluxe" -> 550.0;
                    case "Family" -> 750.0;
                    default -> 350.0;
                };

                double roomTotal = rate * nights;
                if (nights >= 5) roomTotal *= 0.95;

                double grandTotal = roomTotal + extras;
                double balance = grandTotal - paid;

                String bId = String.format("B%03d", billCounter++);
                savedBills.add(new Bill(bId, name, roomCombo.getValue(), nights, grandTotal, paid, balance));

                resultStatus.setText(String.format("Saved %s. Balance: K %.2f", bId, balance));
            } catch (NumberFormatException ex) {
                resultStatus.setText("Error: Verify input fields before saving.");
            }
        });

        deleteBillLink.setOnAction(e -> {
            Bill selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) savedBills.remove(selected);
        });

        newGuestLink.setOnAction(e -> {
            nameInput.clear();
            nightsInput.clear();
            extrasInput.setText("0");
            paidInput.setText("0");
            notesInput.clear();
            resultStatus.setText("");
        });

        mainContent.getChildren().addAll(
                headerTitle, subHeader, middleLayout, actionBox, resultStatus,
                new Label("Saved bills"), table
        );

        root.setCenter(mainContent);
        primaryStage.setScene(new Scene(root, 900, 650));
    }

    public static void main(String[] args) {
        launch(args);
    }
}