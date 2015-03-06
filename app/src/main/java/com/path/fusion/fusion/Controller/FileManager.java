package com.path.fusion.fusion.Controller;

import android.util.Log;
import android.util.Pair;

import com.path.fusion.fusion.Object.Edge;
import com.path.fusion.fusion.Object.Vertex;

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
    private static HashMap<Pair<String,String>, String> fusionList2 = new HashMap<Pair<String,String>,String>();
    private static HashMap<String, Vertex> uniqueHash = new HashMap<>();
    private static List<Edge> edges = new ArrayList<>();
    private static Set<Vertex> uniqueSet = new TreeSet<>();
    private static Set<String> uniqueString = new TreeSet<>();
    private static FileManager instance = null;
    private static File file;
    private static File file2;
    private static File fileUnique;
    private static File fileEdge;
    private static File fileUniqueHash;
    private static String fileName = "/fusionData.dat";
    private static String fileName2 = "/fusionData2.dat";
    private static String fileNameUnique = "/fusionUnique.dat";
    private static String fileNameEdges = "/fusionEdge.dat";
    private static String fileNameHash = "/fusionUniqueHash.dat";


    protected FileManager(){
    }

    public static FileManager getInstance(){
        if(instance == null){
            instance = new FileManager();
//            file = new File(Environment.getExternalStorageDirectory(), fileName);
//            fileUnique = new File(Environment.getExternalStorageDirectory(), fileNameUnique);
//            fileEdge = new File(Environment.getExternalStorageDirectory(), fileNameEdges);
//            fileUniqueHash = new File(Environment.getExternalStorageDirectory(), fileNameHash);
//            file2 = new File(Environment.getExternalStorageDirectory(), fileName2);
//            try {
//                file.createNewFile();
//                fileUnique.createNewFile();
//                fileEdge.createNewFile();
//                fileUniqueHash.createNewFile();
//                file2.createNewFile();
//                openFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return instance;
    }

    /**
     * openFile - Reads in the existing files that were saved in the device's
     * external storage and assigns it to the existing variables.
     */
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

            ois = new ObjectInputStream(new FileInputStream(file2));
            fusionList2 = (HashMap<Pair<String, String>, String>) ois.readObject();
            ois.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * writeFile - Writes over the existing files associated with the object with
     * the current objects.
     */
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

            oos = new ObjectOutputStream(new FileOutputStream(file2));
            oos.writeObject(fusionList2);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * deleteFile - Cleans up all files that were created saved by the Fusion application
     */
    public void deleteFile(){
        file.delete();
        fileUnique.delete();
        fileEdge.delete();
        fileUniqueHash.delete();
        file2.delete();
    }


    /**
     * addToMap - Adds 1st part, 2nd part, and the result of the combination to the
     * hashmap and adds all the unique names to a set. The hashmaps and the set is
     * saved onto the device's external storage.
     * @param pairLeft
     * @param pairRight
     * @param value
     */
    public void addToMap(String pairLeft, String pairRight, String value){
        fusionList.put(new Pair(pairLeft, pairRight), value);
        fusionList2.put(new Pair(pairLeft,value),pairRight);
        uniqueString.add(pairLeft);
        uniqueString.add(pairRight);
        uniqueString.add(value);
//        writeFile();
    }

    /**
     * uniqueStringToVertexSet - Makes a Vertex set out of an existing String set.
     */
    public void uniqueStringToVertexSet(){
        Vertex v;
        for(String name : uniqueString){
//            Log.d("uniqueStringToVertexSet",name);
            v = new Vertex(name,name);
            uniqueSet.add(v);
            uniqueHash.put(name, v);
        }
//        writeFile();
    }

    /**
     * generateEdge - Creates all the edges that has the vertex as it's initial start point.
     * @param source
     */
    public void generateEdge(Vertex source){
        String id;
        String destination;
        Vertex vertexDestination;
        Edge e;
        int weight = 1;
        for(Vertex next : uniqueSet){
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

    /**
     * generateAllEdges - Takes all the unique vertexes and creates all existing edges
     * between all of them and saves it to the device's external storage.
     */
    public void generateAllEdges(){
        for (Vertex vertex : uniqueSet){
            generateEdge(vertex);
        }
        Log.d("generateAllEdges", "Generate all edges complete");
//        writeFile();
    }

    /**
     * getResult - Retrieves the expected result of the combination of the first and second pair.
     * @param pairLeft
     * @param pairRight
     * @return
     */
    public String getResult(String pairLeft, String pairRight){
        return fusionList.get(new Pair(pairLeft, pairRight));
    }

    /**
     * getMaterial - Uses information on the first part of the pair and the expected result to
     * find out the Second part of the pair needed to create the result
     * @param pairLeft - First part of the pair to create the result
     * @param result - Expected result
     * @return
     */
    public String getMaterial(String pairLeft, String result){
        return fusionList2.get(new Pair(pairLeft, result));
    }

    /**
     * destroyInstance - Invalidates the FileManager's current instance
     */
    public void destroyInstance(){
        instance = null;
    }

    /**
     * getMaterialUsage - Gets all combinations using specified material
     * @param material1
     * @return
     */
    public String[][] getMaterialUsage(Vertex material1){
        String[][] combination = null;
        String result = null;
        int i = 0;
        for(String material2 : uniqueString ){
            result = null;
            result = fusionList.get(new Pair(material1.getName(),material2));
            if(!(result.equals("") || result == null)){
                combination[i][0] = material1.getName();
                combination[i][1] = material2;
                combination[i][2] = result;
                i++;
            }
        }
        return combination;
    }

    /**
     * getAllMaterials - Gets all combinations that create the given result.
     * @param result
     * @return
     */
    public String[][] getAllMaterials(Vertex result){
        String[][] combination = null;
        String material2 = null;
        int i = 0;
        for(String material1 : uniqueString){
            material2 = null;
            material2 = fusionList2.get(new Pair(material1,result.getName()));
            if(!(material2.equals("") || material2 == null)){
                combination[i][0] = material1;
                combination[i][1] = material2;
                combination[i][2] = result.getName();
                i++;
            }
        }
        return combination;
    }

    public static HashMap<String, Vertex> getUniqueHash() {
        return uniqueHash;
    }

    public static File getFile() {
        return file;
    }

    public static List<Edge> getEdges() {
        return edges;
    }

    public ArrayList<String> getUnique(){
        ArrayList<String> stringUnique = new ArrayList<String>();
        for(Vertex item : uniqueSet){
            stringUnique.add(item.getName());
        }
        return new ArrayList<String>(stringUnique);
    }

}
