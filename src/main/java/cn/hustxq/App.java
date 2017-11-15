package cn.hustxq;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * Hello world!
 */
public class App {
    private static void help() {
        System.out.println("# Usage: \n"
                + "\t java -jar svnhelper.jar d:\\statsvn(统计生成如2017-10.html文件的父目录) \n"
        );
    }

    /*public static void main2(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load((new FileInputStream("out.properties")));
            System.out.println(properties.getProperty("test"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void main(String[] args) {
        if (args.length == 0) {
            help();
            return;
        }
        String srcDirPath = args[0];//"d:\\statsvn";//
        try {
            //  1.load Properties
            Properties pro = new Properties();
            pro.load(App.class.getResourceAsStream("/config.properties"));//放于jar包内
//            pro.load(new FileInputStream("svnhelper.properties"));//读jar包同级目录下的配置文件
            DBConfig config = new DBConfig();
            String tableName = pro.getProperty("systemName").trim();
            String url = pro.getProperty("url").trim();
//            System.out.println(url);
            String user = pro.getProperty("user").trim();
            String password = pro.getProperty("password").trim();
            config.setTable(tableName);
            config.setUrl(url);
            config.setUser(user);
            config.setPassword(password);
            System.out.println("配置信息读取中...");
//            System.out.println(config);
            //  2. get record
            Worker worker = new Worker();
            Collection<File> collection = worker.getFiles(srcDirPath);
            System.out.println("文件分析和统计中...");
            List<Record> list = worker.deal(collection);
//              3. insert into db
            DBHelper helper = DBHelper.getInstance();
            helper.insert(config,list);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }catch (Exception e){
//            System.out.println(e.getMessage());
            System.out.println("请准确填写配置信息!");
        }
    }
}
