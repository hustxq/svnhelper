package cn.hustxq;

/**
 * @Author hustxq.
 * @Date 2017/11/15 8:59
 */

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DBHelper {
    public static void main(String[] args) {
        int batchSize = 1000;
        int insertCount = 1000;
//        testRewriteBatchedStatements(batchSize,insertCount);
//        DBHelper.getInstance().insert();
        Properties pro = new Properties();
        try {
            pro.load(App.class.getResourceAsStream("/config.properties"));
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        DBConfig config = new DBConfig();
        String tableName = pro.getProperty("systemName").trim();
        String url = pro.getProperty("url").trim();
        String user = pro.getProperty("user").trim();
        String password = pro.getProperty("password").trim();
        config.setTable(tableName);
        config.setUrl(url);
        config.setUser(user);
        config.setPassword(password);
        System.out.println(config);
        helper.insert(config, null);
        /*int i = compare_date("1995-11-12 15:21:00", "1999-12-11 09:59:00");
        System.out.println("i==" + i);*/
    }

    private static DBHelper helper = new DBHelper();

    private DBHelper() {
        try {
            //调用Class.forName()方法加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
//            System.out.println("成功加载MySQL驱动！");
        } catch (ClassNotFoundException e1) {
//            e1.printStackTrace();
            System.out.println(e1.getMessage());
            System.out.println("找不到MySQL驱动!");
        }
    }

    public static DBHelper getInstance() {
//        if (null == helper) helper = new DBHelper();
        return helper;
    }

    public static void insert(DBConfig config, List<Record> list) {
//        long start = System.currentTimeMillis();
        try {
            doBatchedInsert(100, config, list);
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("[[异常]]:SQL出错!");
        }
//        long end = System.currentTimeMillis();
//        System.out.println("rewriteBatchedStatements:" + (end - start) + "ms");
    }

    private static void doBatchedInsert(int batchSize, DBConfig config, List<Record> list) throws SQLException {
        String url = config.getUrl();    //JDBC的URL
        //调用DriverManager对象的getConnection()方法，获得一个Connection对象
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, config.getUser(), config.getPassword());
//            System.out.print("成功连接到数据库！");
        } catch (SQLException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
            System.out.println("[[异常]]:请检查数据库配置信息!");
            return;
        }

//        check table exists
        ResultSet rs = connection.getMetaData().getTables(null, null, config.getTable(), null);
        boolean isExists = false;
        if (rs.next()) {
            isExists = true;
        }
//        System.out.println(isExists);
        String date = null;
        if (!isExists) { // if table is not exist.create table
            String creatsql = "CREATE TABLE " + config.getTable() + " (" +
                    "id INT (11) NOT NULL AUTO_INCREMENT," +
                    "author VARCHAR (60)," +
                    "date DATETIME ," +
                    "revision VARCHAR (60)," +
                    "changes INT (11)," +
                    "PRIMARY KEY (id)" +
                    ") ENGINE=INNODB DEFAULT CHARSET=utf8;";
            PreparedStatement stmt = connection.prepareStatement(creatsql);
            stmt.execute();
        } else {
            String sql = "SELECT MAX(DATE) FROM " + config.getTable();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rset = preparedStatement.executeQuery();
            if (rset.next()) {
                date = rset.getString(1);
            }
        }
//        System.out.println(date);
        int count = 0;
        if (list != null && list.size() > 0) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into "+config.getTable()+" (date,author,revision,changes) values (?,?,?,?)");
            for (int i = 0; i < list.size(); i++) {
                Record record = list.get(i);
                if (date != null && compare_date(date, record.getDate()) >= 0)
                    continue;
                preparedStatement.setString(1, record.getDate());
                preparedStatement.setString(2, record.getAuthor());
                preparedStatement.setString(3, record.getRevision());
                preparedStatement.setString(4, record.getChanges());
                preparedStatement.addBatch();
                if ((i + 1) % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
                count++;
            }
            preparedStatement.executeBatch();
        }
        if (count>0){
            System.out.println("新增记录"+count+"行.");
        }else {
            System.out.println("暂未新增记录");
        }
        System.out.println("Finished!");
        connection.close();
    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
//                System.out.println("dt1在dt2后");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
//                System.out.println("dt1在dt2前");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
//            exception.printStackTrace();
            System.out.println(exception.getMessage());
        }
        return 0;
    }
}
