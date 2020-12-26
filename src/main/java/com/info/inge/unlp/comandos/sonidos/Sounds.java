package com.info.inge.unlp.comandos.sonidos;

import java.io.File;

import java.util.HashMap;

public class Sounds {
    private Sounds() {
    }
    public static HashMap<String, File> getSonidos() {
        return sonidos;
    }

    private  static HashMap<String, File> sonidos=new HashMap<String, File>();
    public static void encontrarSonidos(){
        File path;
        File [] archs;
        String resource = "./sonidos";

        try {
            path= new File(resource);
            archs=path.listFiles();
            for (File arch: archs) {
                if(arch.getName().contains(".mp3")){
                    if(!sonidos.containsKey(arch.getName().toLowerCase()))
                        sonidos.put(arch.getName().toLowerCase(),arch);
                }
            }

        } catch (Exception e) {
            System.err.println("No se encontro el archivo o no existe la carpeta sonidos");
        }
    }
}
