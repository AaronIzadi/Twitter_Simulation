package twitter.model;


public class Record implements Comparable{

    private final long accountId;
    private final Time timeRecord;
    private final int type;

    public static final int DEFAULT = 0;
    public static final int LIKE_RECORD = 1;
    public static final int RETWEET_RECORD = 2;


    public Record(long accountId, Time timeRecord, int type) {
        this.accountId = accountId;
        this.timeRecord = timeRecord;
        this.type = type;
    }

    public long getAccountId() { return accountId; }

    @Override
    public int compareTo(Object o) {
        Record rec = (Record)o;
        return this.timeRecord.compareTo(rec.timeRecord);
    }
}
