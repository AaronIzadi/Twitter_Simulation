package twitter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TimeTest {

    @Test
    public void valueOfStoresIndependentTimes() {
        Time first = Time.valueOf("2023 12 6 22 44 42");
        Time second = Time.valueOf("2024 1 2 3 4 5");

        assertEquals(2023, first.getYear());
        assertEquals(12, first.getMonth());
        assertEquals(6, first.getDay());
        assertEquals(2024, second.getYear());
        assertEquals(1, second.getMonth());
    }

    @Test
    public void compareToOrdersNewestLast() {
        Time older = Time.valueOf("2023 12 6 10 0 0");
        Time newer = Time.valueOf("2023 12 6 11 0 0");

        assertTrue(older.compareTo(newer) < 0);
        assertTrue(newer.compareTo(older) > 0);
    }
}
