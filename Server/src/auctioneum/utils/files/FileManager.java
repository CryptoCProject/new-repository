package auctioneum.utils.files;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class FileManager {

    public static final File KEY_STORAGE_DIR = new File(System.getProperty("user.dir")+"/keystore");


    public static boolean storeToDisk(String name, String content){
        try {
            System.out.println(KEY_STORAGE_DIR.getPath());
            File file = new File(KEY_STORAGE_DIR.getPath()+"/" + name + ".ks");
            if (!KEY_STORAGE_DIR.exists()){
                KEY_STORAGE_DIR.mkdir();
            }
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
            e.printStackTrace();
            return false;
        }
    }

    public static String readFile(String name){
        File file = new File(KEY_STORAGE_DIR.getPath()+"/" + name + ".ks");
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
