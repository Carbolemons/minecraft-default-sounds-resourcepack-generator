package com.carbolemons.defaultsgenerator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

class DefaultsGenerator {
  public static void main(final String[] args) throws FileNotFoundException, IOException {
    if(args.length != 0){
      switch(args[0]){
        case "-m":
        case "-make":
        // args[1]
        JSONParser parser = new JSONParser();
        try{
           final String asset_path = args[1];
           final Object obj = parser.parse(new FileReader(asset_path));
           final JSONObject jsonObjectFull = (JSONObject) obj;
           final JSONObject jsonObject = (JSONObject) jsonObjectFull.get("objects");
           final ArrayList<String> paths = new ArrayList<String>();
           for(final Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
            Object next_iteration =  iterator.next();            
            final String key = (String) next_iteration;
             try{
              if(key.contains("sounds"))paths.add(key);
               //System.out.println(next_iteration);
            } catch(final java.lang.StringIndexOutOfBoundsException e){
              if(key.contains("sounds"))paths.add(key);
              //System.out.println(next_iteration);
            }
            }
            for(int i = 2; i < args.length; i++){
              final String exclusion = args[i];
              paths.removeIf(s -> s.contains(exclusion));
            }
            paths.remove("minecraft");
            paths.remove("minecraft/sounds.json");
            for (final String path : paths) System.out.println(path);
            final PrintWriter pw = new PrintWriter(new FileOutputStream("make-directories.txt"));
            for (final String path : paths) pw.println(path);
            pw.close();
        }catch(final ParseException pe) {
           System.out.println(pe);
        }
        break;
        case "-g":
        case "-generate":
        final String uuOS = System.getProperty("os.name").contains("Windows") ? "%appdata%/" : "/home/"+System.getProperty("user.name")+"/";
        final ArrayList<String> mdirectories = new ArrayList<String>();
        mdirectories.addAll(Files.readAllLines(new File(args[1]).toPath(), Charset.defaultCharset()));
        //for(String mpath : mdirectories) System.out.println(mpath);
        JSONParser gparser = new JSONParser();
        try{
           final String asset_path = args[2];
           final String resource_path = args[3];
           final Object obj = gparser.parse(new FileReader(asset_path));
           final JSONObject jsonObjectFull = (JSONObject) obj;
           final JSONObject jsonObject = (JSONObject) jsonObjectFull.get("objects");
           final ArrayList<String> paths = new ArrayList<String>();
           HashMap<String,String> khashes = new HashMap<String,String>();
           for(final Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
            final String key = (String) iterator.next();
            if(!mdirectories.contains(key))continue;
            JSONObject current_key_value = (JSONObject) jsonObject.get(key);
            String current_hash = current_key_value.get("hash").toString();
            khashes.put(key, current_key_value.get("hash").toString());
            try{
            File source = new File(uuOS+".minecraft/assets/objects/"+current_hash.substring(0,2)+"/"+current_hash);
            File dest = new File(resource_path+key);
            dest.getParentFile().mkdirs();
            Files.copy(source.toPath() , dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            //System.out.println(key + " has hash: " + current_key_value.get("hash"));
            }catch(final FileNotFoundException err){
              System.out.println("File/Directory not found. Aborting");
              break;
            }
            }
        }catch(final ParseException pe) {
           System.out.println(pe);
        }
        break;
        default:
        final String uOS = System.getProperty("os.name").contains("Windows") ? "%appdata%/" : "~/";
        System.out.println("YOU HAVE MADE AN ARGUMENT ERROR. CHECK YOUR ARGUMENTS TO SEE IF THEY ARE VALID");
        System.out.println("ex: java -jar DefaultsGenerator.jar -make "+uOS+".minecraft/assets 1.15.json");
        System.out.println("the example above will generate the full sound directories for every sound. you can make exclusion based on words like below");
        System.out.println("ex: java -jar DefaultsGenerator.jar -make "+uOS+".minecraft/assets 1.15.json cow chicken pig item");
        System.out.println("the example above will exclude chickens, cows, pigs, and all items. if you are unsure on what to write, check the minecraft wiki, or generate a full make file and look at the paths");
        System.out.println("ex: java -jar DefaultsGenerator.jar -generate ./make-directories.txt "+uOS+".minecraft/assets 1.15.json YOURRESOURCEPACK/assets/");
        System.out.println("The -generate flag lets the JAR know you have already decided on what sounds you want");
        break;
      }
    } else {
      final String uOS = System.getProperty("os.name").contains("Windows") ? "%appdata%/" : "~/";
      System.out.println(" //This is DefaultsGenerator! It will generate the sounds you want for your resourcepack!//");
      System.out.println("-Made by Carbolemons.| carbolemons.com github.com/Carbolemons\n");
      System.out.printf("Usage:\njava -jar DefaultsGenerator.jar [mode.-make|-generate] [path to makefile|path to .json config from assets folder] [(entity exclusions)|] [X|.minecraft installation path] [X|path to resourcepack]\n\nclear");
      System.out.println("ex: java -jar DefaultsGenerator.jar -make "+uOS+".minecraft/assets/1.15.json");
      System.out.println("the example above will generate the full sound directories for every sound. you can make exclusion based on words like below");
      System.out.println("ex: java -jar DefaultsGenerator.jar -make "+uOS+".minecraft/assets/1.15.json cow chicken pig item");
      System.out.println("the example above will exclude chickens, cows, pigs, and all items. if you are unsure on what to write, check the minecraft wiki, or generate a full make file and look at the paths");
      System.out.println("ex: java -jar DefaultsGenerator.jar -generate ./make-directories.txt "+uOS+".minecraft/assets/1.15.json "+uOS+".minecraft YOURRESOURCEPACK/assets/");
      System.out.println("The -generate flag lets the JAR know you have already decided on what sounds you want");
    }
  }
}
