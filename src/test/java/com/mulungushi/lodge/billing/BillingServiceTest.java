package com.mulungushi.lodge.billing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BillingServiceTest {

    @Test
    void standardThreeNightsNoDiscount() {
        BillingService.Quote q = BillingService.calculate(RoomType.STANDARD, 3, 0, 0);
        assertEquals(1050.0, q.roomTotal(), 0.001);
        assertEquals(1050.0, q.grandTotal(), 0.001);
        assertEquals(1050.0, q.balance(), 0.001);
    }

    @Test
    void fiveNightsGetsFivePercentOffRoomOnly() {
        // 5 x 350 = 1750 * 0.95 = 1662.5, + 100 extras = 1762.5
        BillingService.Quote q = BillingService.calculate(RoomType.STANDARD, 5, 100, 200);
        assertEquals(1662.5, q.roomTotal(), 0.001);
        assertEquals(1762.5, q.grandTotal(), 0.001);
        assertEquals(1562.5, q.balance(), 0.001);
    }

    @Test
    void deluxeRate() {
        BillingService.Quote q = BillingService.calculate("Deluxe", 2, 0, 1100);
        assertEquals(1100.0, q.grandTotal(), 0.001);
        assertEquals(0.0, q.balance(), 0.001);
    }

    @Test
    void rejectsBadInput() {
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate(RoomType.FAMILY, 0, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate(RoomType.FAMILY, -1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate(RoomType.FAMILY, 1, -5, 0));
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate(RoomType.FAMILY, 1, 0, -1));
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate((RoomType) null, 1, 0, 0));
        assertThrows(IllegalArgumentException.class, () -> BillingService.calculate("Unknown", 1, 0, 0));
    }
}
