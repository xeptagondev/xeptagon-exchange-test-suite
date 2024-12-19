//package UtilClasses;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//public class ConfigReader {
//    private static Properties properties;
//
//    static {
//        try {
//            FileInputStream fileInputStream = new FileInputStream("src/test/resources/config.properties");
//            properties = new Properties();
//            properties.load(fileInputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new RuntimeException("Failed to load config.properties file!");
//        }
//    }
//
//    public static String get(String key) {
//        return properties.getProperty(key);
//    }
//}
//
