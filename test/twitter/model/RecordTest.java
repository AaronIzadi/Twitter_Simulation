package twitter.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RecordTest {

    @Test
    public void valueOfParsesAccountIdAndType() {
        Record record = Record.valueOf("1 2023 12 6 22 44 42 0");

        assertEquals(1, record.getAccountId());
        assertEquals(RecordType.DEFAULT, record.getRecordType());
    }

    @Test
    public void valueOfRoundTripsToString() {
        Record original = new Record(1, Time.valueOf("2023 12 6 22 44 42"), Record.DEFAULT);
        Record restored = Record.valueOf(original.toString());

        assertEquals(original.toString(), restored.toString());
        assertEquals(original.getAccountId(), restored.getAccountId());
    }
}
