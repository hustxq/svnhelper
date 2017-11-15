package cn.hustxq;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @Author hustxq.
 * @Date 2017/11/14 15:16
 */
public class Worker {
    private static final Logger logger = Logger.getLogger(Worker.class);

    public static void main(String[] args) {
//        System.out.println(((Elements)null).text());
        Worker worker = new Worker();
        String srcDirPath = "D:\\statsvn";
        Collection<File> collection = worker.getFiles(srcDirPath);
        List<Record> list = worker.deal(collection);
        for (Record s : list) {
            System.out.println(s.getRecord() + ",");
        }
        /*Collection<File> collection = worker.getFiles(srcDirPath);
        for (File f:collection){
            if (worker.targetFile(f)){
                System.out.println(f.getName());
            }
        }*/
        /*File file = new File(srcDirPath + File.separator + "worker.html");
        System.out.println(worker.targetFile(file));*/
        /*DBHelper helper = DBHelper.getInstance();
        helper.insert(50,list);*/
    }

    public List<Record> deal(Collection<File> collection) {
        List<Record> list = new ArrayList<Record>();
        for (File file : collection) {
            if (targetFile(file)) {
                try {
                    Document doc = Jsoup.parse(file, "utf-8");
                    Elements e = doc.select("dl.commitlist");
                    for (int c = 0; c < e.select("dt").size(); c++) {
                        Element te0 = e.select("dt").get(c);
                        Element te1 = e.select("dd").get(c);
                        Elements dt = te0.select("dt");
                        Elements author = dt.select(".author");
                        Elements date = dt.select(".date");
                        Elements reversion = dt.select(".revisionNumberOuter").select("span.revisionNumberInner");
                        Elements changes = te1.select("dd").select("p.commitdetails").select("strong");
                        Record record = new Record();
                        record.setAuthor(author.text());
                        record.setDate(date.text());
                        record.setRevision(reversion.text());
                        record.setChanges(changes.text());
                        list.add(record);
                    }
                } catch (IOException e) {
//                    e.printStackTrace();
                    logger.error(e.getMessage());
                } catch (Exception e) {
//                    e.printStackTrace();
                    logger.error(e.getMessage());
                }
            }
        }
        return list;
    }

    public Collection<File> getFiles(String srcDirPath) {
        //获取所有html文件
        File file = new File(srcDirPath);
        if (!file.exists()) {
            logger.info("目录不存在!");
            System.exit(-1);
        }
        Collection<File> collection = FileUtils.listFiles(file, new String[]{"html"}, true);
        /*System.out.println(collection.size());
        for (File f:collection){
            System.out.println(f.getName());
        }*/
        return collection;
    }

    private boolean targetFile(File file) {
        String name = file.getName();
//        System.out.println(name);
        if ("commitlog.html".equals(name)) {
            return true;
        } else if (name.matches("^\\d{4}\\D\\d{2}.html")) {
            return true;
        }
        return false;
    }
}