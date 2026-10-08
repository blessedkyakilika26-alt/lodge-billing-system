package model;

import javafx.beans.property.*;

public class Bill {
    private final StringProperty billId;
    private final StringProperty guestName;
    private final StringProperty roomType;
    private final IntegerProperty nights;
    private final DoubleProperty total;
    private final DoubleProperty paid;
    private final DoubleProperty balance;

    public Bill(String billId, String guestName, String roomType, int nights, double total, double paid, double balance) {
        this.billId = new SimpleStringProperty(billId);
        this.guestName = new SimpleStringProperty(guestName);
        this.roomType = new SimpleStringProperty(roomType);
        this.nights = new SimpleIntegerProperty(nights);
        this.total = new SimpleDoubleProperty(total);
        this.paid = new SimpleDoubleProperty(paid);
        this.balance = new SimpleDoubleProperty(balance);
    }

    public StringProperty billIdProperty() { return billId; }
    public StringProperty guestNameProperty() { return guestName; }
    public StringProperty roomTypeProperty() { return roomType; }
    public IntegerProperty nightsProperty() { return nights; }
    public DoubleProperty totalProperty() { return total; }
    public DoubleProperty paidProperty() { return paid; }
    public DoubleProperty balanceProperty() { return balance; }
}