package com.doglandia.medialoader.thumbnail;


import com.doglandia.medialoader.model.Resource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * meta file describing the contents of the thumbnail local storage
 */

public class MetaFile {

    private JsonObject data;

    private File file;

    MetaFile(File file){
        this.file = file;

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            data = new JsonObject();
        }else{
            JsonParser parser = new JsonParser();
            try {
                data = parser.parse(readFile(file.getPath())).getAsJsonObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean noThumbnailFor(Resource resource) {
        return !data.has(resource.getLocation());
    }

    public void addMetaForThumbnail(Resource resource, String filePath){
        data.addProperty(resource.getLocation(), filePath);


    }

    private String readFile(String path) throws IOException{
        BufferedReader reader = new BufferedReader( new FileReader(path));;
        String         line = null;
        StringBuilder  stringBuilder = new StringBuilder();
        String         ls = System.getProperty("line.separator");

        try {
            while( ( line = reader.readLine() ) != null ) {
                stringBuilder.append( line );
                stringBuilder.append( ls );
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

    public String getThumbnailFor(Resource resource) {
        return data.get(resource.getLocation()).getAsString();
    }

    public void saveFile() throws FileNotFoundException {
        JsonParser jsonParser = new JsonParser();
        PrintWriter out = new PrintWriter(file);
        out.print(data.toString());
        out.close();
    }
}
