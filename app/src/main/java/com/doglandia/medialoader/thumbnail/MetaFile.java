package com.doglandia.medialoader.thumbnail;


import java.io.File;
import java.io.IOException;

/**
 * meta file describing the contents of the thumbnail local storage
 */

public class MetaFile {



    MetaFile(File file){

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
