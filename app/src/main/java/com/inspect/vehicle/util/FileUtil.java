package com.inspect.vehicle.util;

import com.inspect.vehicle.impl.IContactsImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

//文件系统工具类
public class FileUtil implements IContactsImpl {

    public static final String apkName="veh_inspect.apk";

    public static void createFileSys() {
        for (String path : Path.ARRAY) {
            File file = new File(path);
            createFiles(file);
        }
    }

    private static boolean createFiles(File file) {
        return !file.exists() && file.mkdirs();
    }

    public static void createFile(String path, String fileName, String content) {
        try {
            File file = new File(path, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            //
        }
    }

    public static void saveDeviceData(String content) {
        createFile("dataPath", "device.db", content);
    }

    public static String getDeviceData() {
        return readFileContent("dataPath", "device.db");
    }

    public static String readFileContent(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String readLine;
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * @param file Eg:"F:\ss\新建文本文档.txt"
     * @ 删除文件
     */
    public static boolean deleteFile(File file) {
        try {
            return !file.exists() || file.delete();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean deleteFile(String path) {
        return deleteFile(new File(path));
    }

    public static boolean deleteFile(String path, String fileName) {
        return deleteFile(new File(path, fileName));
    }

    /**
     * 删除文件及所在文件夹
     */
    public static void deleteFileWithDir(File file) {
        if (file.exists()) { //指定文件是否存在
            if (file.isFile()) { //该路径名表示的文件是否是一个标准文件
                deleteFile(file); //删除该文件
            } else if (file.isDirectory()) { //该路径名表示的文件是否是一个目录（文件夹）
                File[] files = file.listFiles(); //列出当前文件夹下的所有文件
                for (File f : files) {
                    deleteFileWithDir(f); //递归删除
                    //Log.d("fileName", f.getName()); //打印文件名
                }
            }
            deleteFile(file);
            //删除文件夹（song,art,lyric）
        }
    }

    public static boolean deleteEmptyDir(File file) {
        try {
            if (file.isDirectory()) {
                File[] childFiles = file.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                for (File f : childFiles) {
                    deleteEmptyDir(f);
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
