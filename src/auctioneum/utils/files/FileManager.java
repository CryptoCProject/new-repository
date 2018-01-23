package auctioneum.utils.files;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class FileManager {

    public static final String KEY_STORAGE_DIR = "";


    public static boolean storeToDisk(String name, String content){
        try {
            File file = new File(KEY_STORAGE_DIR + name + ".ks");
            if (!file.exists()) {
                file.createNewFile();
                FileWriter fw = new FileWriter(file);
                fw.write(content);
                fw.flush();
                fw.close();
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public static String readFile(String name){
        File file = new File(KEY_STORAGE_DIR + name + ".ks");
        try {
            if (file.exists()){
                StringBuilder sb = new StringBuilder();
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line=br.readLine())!= null){
                    sb.append(line);
                }
                br.close();
                fr.close();
                return sb.toString();
            }
            else {
                throw new FileNotFoundException();
            }

        }
        catch (Exception e){
            return null;
        }
    }


}
