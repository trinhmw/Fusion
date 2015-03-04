package com.path.fusion.fusion;

import android.os.Environment;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Melissa on 3/2/2015.
 */
public class FileManager {

    private static HashMap<Pair<String,String>, String> fusionList = new HashMap<>();
    private static HashMap<String, Vertex> uniqueHash = new HashMap<>();
    private static List<Edge> edges = new ArrayList<>();
    private static Set<Vertex> uniqueSet = new TreeSet<>();
    private static Set<String> uniqueString = new TreeSet<>();
    private static FileManager instance = null;
    private static File file;
    private static File fileUnique;
    private static File fileEdge;
    private static File fileUniqueHash;
    private static String fileName = "/fusionData.dat";
    private static String fileNameUnique = "/fusionUnique.dat";
    private static String fileNameEdges = "/fusionEdge.dat";
    private static String fileNameHash = "/fusionUniqueHash.dat";


    protected FileManager(){
    }

    public static FileManager getInstance(){
        if(instance == null){
            instance = new FileManager();
            file = new File(Environment.getExternalStorageDirectory(), fileName);
            fileUnique = new File(Environment.getExternalStorageDirectory(), fileNameUnique);
            fileEdge = new File(Environment.getExternalStorageDirectory(), fileNameEdges);
            fileUniqueHash = new File(Environment.getExternalStorageDirectory(), fileNameHash);
            try {
                file.createNewFile();
                fileUnique.createNewFile();
                fileEdge.createNewFile();
                fileUniqueHash.createNewFile();
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
            uniqueSet = (TreeSet<Vertex>) ois.readObject();
            ois.close();

            ois = new ObjectInputStream(new FileInputStream(fileEdge));
            edges = (ArrayList<Edge>) ois.readObject();
            ois.close();

            ois = new ObjectInputStream(new FileInputStream(fileUniqueHash));
            uniqueHash = (HashMap<String, Vertex>) ois.readObject();
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

            oos = new ObjectOutputStream(new FileOutputStream(fileEdge));
            oos.writeObject(edges);
            oos.flush();
            oos.close();

            oos = new ObjectOutputStream(new FileOutputStream(fileUniqueHash));
            oos.writeObject(uniqueHash);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getUnique(){
        ArrayList<String> stringUnique = new ArrayList<String>();
        for(Vertex item : uniqueSet){
            stringUnique.add(item.getName());
        }
        return new ArrayList<String>(stringUnique);
    }

    public void addToMap(String pairLeft, String pairRight, String value){
        fusionList.put(new Pair(pairLeft, pairRight), value);
        uniqueString.add(pairLeft);
        uniqueString.add(pairRight);
        uniqueString.add(value);
//        uniqueSet.add(value);
        writeFile();
    }

    public void uniqueStringToVertexSet(){
        Vertex v;
        for(String name : uniqueString){
            Log.d("uniqueStringToVertexSet",name);
            v = new Vertex(name,name);
            uniqueSet.add(v);
            uniqueHash.put(name, v);
        }
        writeFile();
    }

    public void generateEdge(Vertex source){
        String id;
        String destination;
        Vertex vertexDestination;
        Edge e;
        int weight = 1;
        for(Vertex next : uniqueSet){
//            destination = fusionList.get(new Pair(source.getName(),next.getName()));
//            id = source.getName() + "_" + destination;
            //If this combination exists
            if(
                (fusionList.containsKey(new Pair(source.getName(), next.getName()))) &&
                (fusionList.get(new Pair(source.getName(),next.getName())) != null) &&
                !(fusionList.get(new Pair(source.getName(),next.getName())).equals(""))) {
//                    id = source.getName() + "_" + next.getName();
//                    Log.d("generateEdge", id);
//                    e = new Edge(id, source, next, weight);
//                    edges.add(e);

                    destination = fusionList.get(new Pair(source.getName(),next.getName()));
                    vertexDestination = uniqueHash.get(destination);
                    id = next.getName() + "_" + vertexDestination.getName();
//                    Log.d("generateEdge", id);
                    e = new Edge(id, source, vertexDestination, weight);
                    edges.add(e);
            }
        }
    }

    public void generateAllEdges(){
        for (Vertex vertex : uniqueSet){
            generateEdge(vertex);
        }
        Log.d("generateAllEdges", "Generate all edges complete");
        writeFile();
    }

    public static List<Edge> getEdges() {
        return edges;
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
        fileEdge.delete();
        fileUniqueHash.delete();
    }

    public HashMap<Pair<String, String>, String> getFusionList() {
        return fusionList;
    }

    private void setFusionList(HashMap<Pair<String, String>, String> fusionList) {
        this.fusionList = fusionList;
    }

    public static HashMap<String, Vertex> getUniqueHash() {
        return uniqueHash;
    }

    private static void setInstance(FileManager instance) {
        FileManager.instance = instance;
    }

    public static File getFile() {
        return file;
    }

    private static void setFile(File file) {
        FileManager.file = file;
    }

    public static String getFileName() {
        return fileName;
    }

    private static void setFileName(String fileName) {
        FileManager.fileName = fileName;
    }

    public static Set<Vertex> getUniqueSet() {
        return uniqueSet;
    }

    public static ArrayList<Vertex> getUniqueArrayList() {
        return new ArrayList<Vertex>(uniqueSet);
    }

    private static void setUniqueSet(Set<Vertex> uniqueSet) {
        FileManager.uniqueSet = uniqueSet;
    }

    public static File getFileUnique() {
        return fileUnique;
    }

    private static void setFileUnique(File fileUnique) {
        FileManager.fileUnique = fileUnique;
    }

    public static String getFileNameUnique() {
        return fileNameUnique;
    }

    private static void setFileNameUnique(String fileNameUnique) {
        FileManager.fileNameUnique = fileNameUnique;
    }


}
