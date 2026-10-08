package com.mulungushi.lodge.billing;

public final class BillingService {

    public static final int DISCOUNT_THRESHOLD_NIGHTS = 5;
    public static final double DISCOUNT_FACTOR = 0.95;

    private BillingService() {
    }

    public record Quote(double roomTotal, double grandTotal, double balance) {
    }

    public static Quote calculate(RoomType roomType, int nights, double extras, double paid) {
        if (roomType == null) {
            throw new IllegalArgumentException("Room type is required.");
        }
        if (nights <= 0) {
            throw new IllegalArgumentException("Nights must be greater than 0.");
        }
        if (extras < 0) {
            throw new IllegalArgumentException("Extras cannot be negative.");
        }
        if (paid < 0) {
            throw new IllegalArgumentException("Amount paid cannot be negative.");
        }

        double roomTotal = roomType.ratePerNight() * nights;
        if (nights >= DISCOUNT_THRESHOLD_NIGHTS) {
            roomTotal *= DISCOUNT_FACTOR;
        }
        double grandTotal = roomTotal + extras;
        double balance = grandTotal - paid;
        return new Quote(roomTotal, grandTotal, balance);
    }

    public static Quote calculate(String roomDisplayName, int nights, double extras, double paid) {
        return calculate(RoomType.fromDisplayName(roomDisplayName), nights, extras, paid);
    }
}
