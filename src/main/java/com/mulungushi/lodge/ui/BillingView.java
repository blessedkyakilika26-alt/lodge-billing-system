package com.mulungushi.lodge.ui;

import java.util.List;

import com.mulungushi.lodge.billing.BillingService;
import com.mulungushi.lodge.billing.BillingState;
import com.mulungushi.lodge.billing.RoomType;
import com.mulungushi.lodge.model.Bill;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** Main screen: capture a guest stay, calculate the bill, and list saved bills. */
public class BillingView extends BorderPane {

    /** Icon glyphs from Segoe MDL2 Assets, bundled with Windows 10+. */
    private static final String ICON_PERSON = "\uE77B";
    private static final String ICON_RECEIPT = "\uE8A5";
    private static final String ICON_DELETE = "\uE74D";

    private final BillingState state;
    private final TableView<Bill> table = new TableView<>();

    private final TextField nameInput = new TextField();
    private final ComboBox<String> roomCombo = new ComboBox<>(FXCollections.observableArrayList(
            RoomType.STANDARD.displayName(),
            RoomType.DELUXE.displayName(),
            RoomType.FAMILY.displayName()));
    private final TextField nightsInput = new TextField();
    private final TextField extrasInput = new TextField("0");
    private final TextField paidInput = new TextField("0");
    private final TextArea notesInput = new TextArea();
    private final Label resultStatus = new Label();

    public BillingView(String username, BillingState state, Runnable onLogout) {
        this.state = state;

        getStyleClass().add("screen-root");
        roomCombo.getSelectionModel().select(RoomType.STANDARD.displayName());

        setTop(buildHeader(username, onLogout));
        setCenter(buildContent());
    }

    /** Dark app bar plus the icon toolbar underneath it. */
    private VBox buildHeader(String username, Runnable onLogout) {
        Label title = new Label("Lodge Billing Lab");
        title.getStyleClass().add("appbar-title");

        Label fileMenu = new Label("File");
        fileMenu.getStyleClass().add("menu-item");

        Label helpMenu = new Label("Help");
        helpMenu.getStyleClass().add("menu-item");

        Label userLabel = new Label("Logged in: " + username);
        userLabel.getStyleClass().add("logged-in");

        Button logoutBtn = new Button("Log Out");
        logoutBtn.getStyleClass().add("btn-logout");
        logoutBtn.setOnAction(e -> onLogout.run());

        Region appbarSpacer = new Region();
        HBox.setHgrow(appbarSpacer, Priority.ALWAYS);

        HBox appbar = new HBox(20, title, appbarSpacer, fileMenu, helpMenu, userLabel, logoutBtn);
        appbar.getStyleClass().add("appbar");
        appbar.setAlignment(Pos.CENTER_LEFT);

        HBox toolbar = new HBox(6,
                toolButton(ICON_PERSON, "New guest", this::resetForm),
                toolButton(ICON_RECEIPT, "View receipt",
                        () -> showStatus("Receipt viewer — a later lab step.", false)),
                toolButton(ICON_DELETE, "Delete selected bill", this::deleteSelectedBill));
        toolbar.getStyleClass().add("toolbar");
        toolbar.setAlignment(Pos.CENTER_LEFT);

        return new VBox(appbar, toolbar);
    }

    private VBox buildContent() {
        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(26, 28, 32, 28));

        Label headerTitle = new Label("Lodge Billing Lab");
        headerTitle.getStyleClass().add("screen-title");

        Label subHeader = new Label("ICT261 Lecture 3 | Mr E Nyirenda | Mulungushi University");
        subHeader.getStyleClass().add("subtitle");

        HBox middleLayout = new HBox(40, buildFormGrid(), buildPriceInfoBox());

        Button calculateBtn = new Button("Calculate");
        calculateBtn.getStyleClass().add("btn-ghost");
        calculateBtn.setOnAction(e -> calculateBill());

        Button saveBtn = new Button("Save bill");
        saveBtn.getStyleClass().add("btn-primary");
        saveBtn.setOnAction(e -> saveBill());

        resultStatus.getStyleClass().add("status");
        resultStatus.setMinHeight(22);

        HBox actionBox = new HBox(10, calculateBtn, saveBtn);

        Label savedTitle = new Label("Saved bills");
        savedTitle.getStyleClass().add("saved-title");

        mainContent.getChildren().addAll(
                headerTitle, subHeader, middleLayout, actionBox, resultStatus,
                savedTitle, buildBillsTable());
        return mainContent;
    }

    private GridPane buildFormGrid() {
        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(10);

        notesInput.setPrefRowCount(2);

        formGrid.add(formLabel("Guest name:"), 0, 0);
        formGrid.add(nameInput, 1, 0);
        formGrid.add(formLabel("Room type:"), 0, 1);
        formGrid.add(roomCombo, 1, 1);
        formGrid.add(formLabel("Nights:"), 0, 2);
        formGrid.add(nightsInput, 1, 2);
        formGrid.add(formLabel("Extras in K:"), 0, 3);
        formGrid.add(extrasInput, 1, 3);
        formGrid.add(formLabel("Amount paid in K:"), 0, 4);
        formGrid.add(paidInput, 1, 4);
        formGrid.add(formLabel("Notes:"), 0, 5);
        formGrid.add(notesInput, 1, 5);
        return formGrid;
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("form-label");
        GridPane.setHalignment(label, HPos.RIGHT);
        return label;
    }

    private VBox buildPriceInfoBox() {
        VBox infoBox = new VBox(8);
        infoBox.getStyleClass().add("panel");

        Label priceTitle = new Label("Practice room prices");
        priceTitle.getStyleClass().add("panel-title");
        infoBox.getChildren().add(priceTitle);

        for (RoomType type : RoomType.values()) {
            Label name = new Label(type.displayName() + ":");
            name.getStyleClass().add("panel-price-name");
            Label rate = new Label(String.format("K %.2f per night", type.ratePerNight()));
            infoBox.getChildren().add(new HBox(6, name, rate));
        }

        Label discountInfo = new Label(
                "\n5 nights or more: 5% off the room charge.\nExtras get no discount. No tax in this lab.\nSaved bills last only until you close the app.");
        discountInfo.getStyleClass().add("panel-note");
        discountInfo.setWrapText(true);
        infoBox.getChildren().add(discountInfo);
        return infoBox;
    }

    private TableView<Bill> buildBillsTable() {
        TableColumn<Bill, String> colId = new TableColumn<>("Bill");
        colId.setCellValueFactory(data -> data.getValue().billIdProperty());

        TableColumn<Bill, String> colGuest = new TableColumn<>("Guest");
        colGuest.setCellValueFactory(data -> data.getValue().guestNameProperty());

        TableColumn<Bill, String> colRoom = new TableColumn<>("Room");
        colRoom.setCellValueFactory(data -> data.getValue().roomTypeProperty());

        TableColumn<Bill, Number> colNights = new TableColumn<>("Nights");
        colNights.setCellValueFactory(data -> data.getValue().nightsProperty());
        colNights.setCellFactory(col -> numericCell());

        TableColumn<Bill, Number> colTotal = new TableColumn<>("Total");
        colTotal.setCellValueFactory(data -> data.getValue().totalProperty());
        colTotal.setCellFactory(col -> moneyCell(false));

        TableColumn<Bill, Number> colPaid = new TableColumn<>("Paid");
        colPaid.setCellValueFactory(data -> data.getValue().paidProperty());
        colPaid.setCellFactory(col -> moneyCell(false));

        TableColumn<Bill, Number> colBalance = new TableColumn<>("Balance");
        colBalance.setCellValueFactory(data -> data.getValue().balanceProperty());
        colBalance.setCellFactory(col -> moneyCell(true));

        Label placeholder = new Label("No saved bills yet — save your first bill above.");
        placeholder.getStyleClass().add("panel-note");
        table.setPlaceholder(placeholder);

        table.setItems(state.savedBills());
        table.getColumns().addAll(List.of(colId, colGuest, colRoom, colNights, colTotal, colPaid, colBalance));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        table.setPrefHeight(200);
        return table;
    }

    private static TableCell<Bill, Number> numericCell() {
        TableCell<Bill, Number> cell = new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.toString());
            }
        };
        cell.getStyleClass().add("num");
        return cell;
    }

    /** Money cell; when statusColored, owed balances are amber and settled ones green. */
    private static TableCell<Bill, Number> moneyCell(boolean statusColored) {
        TableCell<Bill, Number> cell = new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().removeAll("balance-owed", "balance-paid");
                if (empty || item == null) {
                    setText(null);
                    return;
                }
                double value = item.doubleValue();
                if (statusColored) {
                    getStyleClass().add(value > 0 ? "balance-owed" : "balance-paid");
                }
                setText(String.format("K %.2f", value));
            }
        };
        cell.getStyleClass().add("num");
        return cell;
    }

    private Button toolButton(String glyph, String text, Runnable action) {
        Label icon = new Label(glyph);
        icon.getStyleClass().add("tool-icon");
        Button button = new Button(text, icon);
        button.getStyleClass().add("tool-button");
        button.setOnAction(e -> action.run());
        return button;
    }

    private void calculateBill() {
        try {
            BillingService.Quote quote = quote(readForm());
            showStatus(String.format("Calculated Total: K %.2f, Balance: K %.2f",
                    quote.grandTotal(), quote.balance()), false);
        } catch (NumberFormatException ex) {
            showStatus("Error: Enter valid numbers for Nights, Extras, and Paid.", true);
        } catch (IllegalArgumentException ex) {
            showStatus("Error: " + ex.getMessage(), true);
        }
    }

    private void saveBill() {
        try {
            String name = nameInput.getText().trim();
            if (name.isEmpty()) {
                showStatus("Error: Guest name is required.", true);
                return;
            }

            FormInput form = readForm();
            BillingService.Quote quote = quote(form);
            Bill bill = state.createBill(name, roomCombo.getValue(), form.nights(),
                    quote.grandTotal(), form.paid(), quote.balance());

            showStatus(String.format("Saved %s: Balance: K %.2f", bill.getBillId(), quote.balance()), false);
        } catch (NumberFormatException ex) {
            showStatus("Error: Verify input fields before saving.", true);
        } catch (IllegalArgumentException ex) {
            showStatus("Error: " + ex.getMessage(), true);
        }
    }

    private BillingService.Quote quote(FormInput form) {
        return BillingService.calculate(roomCombo.getValue(), form.nights(), form.extras(), form.paid());
    }

    private FormInput readForm() {
        int nights = Integer.parseInt(nightsInput.getText().trim());
        double extras = Double.parseDouble(extrasInput.getText().trim());
        double paid = Double.parseDouble(paidInput.getText().trim());
        return new FormInput(nights, extras, paid);
    }

    private void showStatus(String message, boolean error) {
        resultStatus.setText(message);
        resultStatus.getStyleClass().removeAll("error");
        if (error) {
            resultStatus.getStyleClass().add("error");
        }
    }

    private void deleteSelectedBill() {
        Bill selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showStatus("Select a bill to delete first.", true);
            return;
        }
        state.removeBill(selected);
        showStatus("Deleted " + selected.getBillId() + ".", false);
    }

    private void resetForm() {
        nameInput.clear();
        nightsInput.clear();
        extrasInput.setText("0");
        paidInput.setText("0");
        notesInput.clear();
        showStatus("", false);
    }

    private record FormInput(int nights, double extras, double paid) {
    }
}
