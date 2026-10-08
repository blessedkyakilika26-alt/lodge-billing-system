package com.mulungushi.lodge.billing;

import com.mulungushi.lodge.model.Bill;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * In-memory state for bills saved during the current app session.
 * Bills are lost when the app is closed.
 */
public class BillingState {

    private final ObservableList<Bill> savedBills = FXCollections.observableArrayList();
    private int nextBillNumber = 1;

    /** Bills saved so far, in insertion order. The list is observable for the table view. */
    public ObservableList<Bill> savedBills() {
        return savedBills;
    }

    /** Creates a bill with an auto-generated id, stores it, and returns it. */
    public Bill createBill(String guestName, String roomType, int nights,
                           double total, double paid, double balance) {
        String billId = String.format("B%03d", nextBillNumber++);
        Bill bill = new Bill(billId, guestName, roomType, nights, total, paid, balance);
        savedBills.add(bill);
        return bill;
    }

    public void removeBill(Bill bill) {
        savedBills.remove(bill);
    }
}
