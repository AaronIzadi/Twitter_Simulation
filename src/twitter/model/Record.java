package twitter.model;


public class Record implements Comparable {

    private long accountId;
    private Time timeRecord;
    private int type;
    private RecordType recordType;
    public static final int DEFAULT = 0;
    public static final int LIKE_RECORD = 1;
    public static final int RETWEET_RECORD = 2;


    public Record(){}

    public Record(long accountId, Time timeRecord, int type) {
        this.accountId = accountId;
        this.timeRecord = timeRecord;
        this.type = type;
        setRecordType(type);
    }

    public long getAccountId() {
        return accountId;
    }

    public RecordType getRecordType() {
        return recordType;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setTimeRecord(Time timeRecord) {
        this.timeRecord = timeRecord;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setRecordType(int type) {
        switch (type) {
            case 2:
                this.recordType = RecordType.RETWEET;
                break;
            case 1:
                this.recordType = RecordType.LIKE;
                break;
            default:
                this.recordType = RecordType.DEFAULT;
                break;
        }
    }

    public static Record valueOf(String recordInString){
        String[] all = recordInString.split(" ");
        Time time = Time.valueOf(all[1]);
        Record record = new Record();

        record.setAccountId(Long.parseLong(all[0]));
        record.setTimeRecord(time);
        record.setRecordType(Integer.parseInt(all[2]));
        record.setType(Integer.parseInt(all[2]));

        return record;
    }

    @Override
    public String toString() {
        String record = "";
        record = accountId + " " + timeRecord.toString() + " " + type;
        return record;
    }


    @Override
    public int compareTo(Object o) {
        Record rec = (Record) o;
        return this.timeRecord.compareTo(rec.timeRecord);
    }
}
