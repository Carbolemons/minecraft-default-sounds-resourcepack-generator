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

class DefaultsGenerator {
  public static void main(final String[] args) throws FileNotFoundException, IOException {
    if(args.length != 0){
      switch(args[0]){
        case "-m":
        case "-make":
        // args[1]
        final JSONParser parser = new JSONParser();
        try{
           final String asset_path = args[1];
           final Object obj = parser.parse(new FileReader(asset_path));
           final JSONObject jsonObjectFull = (JSONObject) obj;
           final JSONObject jsonObject = (JSONObject) jsonObjectFull.get("objects");
           final ArrayList<String> paths = new ArrayList<String>();
           for(final Iterator iterator = jsonObject.keySet().iterator(); iterator.hasNext();) {
             final String key = (String) iterator.next();
             try{
             if(!paths.contains(key.substring(0,key.lastIndexOf('/')))){
               if(key.contains("sounds"))paths.add(key.substring(0,key.lastIndexOf('/')));
             }
            } catch(final java.lang.StringIndexOutOfBoundsException e){
              if(key.contains("sounds"))paths.add(key.substring(0,key.lastIndexOf('/')));
            }
            }
            for(int i = 2; i < args.length; i++){
              final String exclusion = args[i];
              paths.removeIf(s -> s.contains(exclusion));
            }
            paths.remove("minecraft");
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
        final ArrayList<String> mdirectories = new ArrayList<String>();
        mdirectories.addAll(Files.readAllLines(new File(args[1]).toPath(), Charset.defaultCharset()));
        //for(String mpath : mdirectories) System.out.println(mpath);
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
