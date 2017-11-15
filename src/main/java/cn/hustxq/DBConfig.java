package cn.hustxq;

/**
 * @Author hustxq.
 * @Date 2017/11/15 10:00
 */
public class DBConfig {
    private String url;
    private String user;
    private String password;
    private String table;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "DBConfig{" +
                "url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", table='" + table + '\'' +
                '}';
    }
}
