package com.path.fusion.fusion;

import android.content.Context;
import android.os.Environment;
import android.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Melissa on 3/2/2015.
 */
public class FileManager {

    private static HashMap<Pair<String,String>, String> fusionList = new HashMap<Pair<String, String>, String>();
    private static Set<String> uniqueSet = new TreeSet<String>();
    private static FileManager instance = null;
    private static File file;
    private static File fileUnique;
    private static String fileName = "/fusionData.dat";
    private static String fileNameUnique = "/fusionUnique.dat";


    protected FileManager(){
    }

    public static FileManager getInstance(){
        if(instance == null){
            instance = new FileManager();
            file = new File(Environment.getExternalStorageDirectory(), fileName);
            fileUnique = new File(Environment.getExternalStorageDirectory(), fileNameUnique);
            try {
                file.createNewFile();
                fileUnique.createNewFile();
                openFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public static void openFile() {
        ObjectInputStream ois = null;
        try {
            //recover file
            ois = new ObjectInputStream(new FileInputStream(file));
            fusionList = (HashMap<Pair<String, String>, String>) ois.readObject();
            ois.close();

            ois = new ObjectInputStream(new FileInputStream(fileUnique));
            uniqueSet = (TreeSet<String>) ois.readObject();
            ois.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(fusionList);
            oos.flush();
            oos.close();

            oos = new ObjectOutputStream(new FileOutputStream(fileUnique));
            oos.writeObject(uniqueSet);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getUnique(){
        return new ArrayList<String>(uniqueSet);
    }

    public void addToMap(String pairLeft, String pairRight, String value){
        fusionList.put(new Pair(pairLeft, pairRight), value);
        uniqueSet.add(pairLeft);
        uniqueSet.add(pairRight);
//        uniqueSet.add(value);
        writeFile();
    }

    public String getValue(String pairLeft, String pairRight){
        return fusionList.get(new Pair(pairLeft, pairRight));
    }

    public void destroyInstance(){
        instance = null;
    }

    public void deleteFile(){
        file.delete();
        fileUnique.delete();
    }

    public ArrayList<String> generatePaths(String initial, String result){
        return new ArrayList<String>();
    }

    public HashMap<Pair<String, String>, String> getFusionList() {
        return fusionList;
    }

    public void setFusionList(HashMap<Pair<String, String>, String> fusionList) {
        this.fusionList = fusionList;
    }

    public static void setInstance(FileManager instance) {
        FileManager.instance = instance;
    }

    public static File getFile() {
        return file;
    }

    public static void setFile(File file) {
        FileManager.file = file;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        FileManager.fileName = fileName;
    }

}
