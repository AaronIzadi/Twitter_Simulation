package twitter.model;


public class Record implements Comparable {

    private final long accountId;
    private final Time timeRecord;
    private final RecordType type;
    public static final int DEFAULT = 0;
    public static final int LIKE_RECORD = 1;
    public static final int RETWEET_RECORD = 2;


    public Record(long accountId, Time timeRecord, int type) {
        this.accountId = accountId;
        this.timeRecord = timeRecord;
        switch (type) {
            case 2:
                this.type = RecordType.RETWEET;
                break;
            case 1:
                this.type = RecordType.LIKE;
                break;
            default:
                this.type = RecordType.DEFAULT;
                break;
        }
    }

    public long getAccountId() {
        return accountId;
    }

    public RecordType getType() {
        return type;
    }

    @Override
    public int compareTo(Object o) {
        Record rec = (Record) o;
        return this.timeRecord.compareTo(rec.timeRecord);
    }
}
