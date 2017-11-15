package cn.hustxq;

/**
 * @Author hustxq.
 * @Date 2017/11/14 17:16
 */
public class Record {
    String author;
    String date;
    String revision;
    String changes;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getChanges() {
        return changes;
    }

    public void setChanges(String changes) {
        this.changes = changes;
    }

    @Override
    public String toString() {
        return "Record{" +
                "author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", revision='" + revision + '\'' +
                ", changes='" + changes + '\'' +
                '}';
    }
    public String getRecord(){
        return "("+date+","+author+","+revision+","+changes+")";
    }
}