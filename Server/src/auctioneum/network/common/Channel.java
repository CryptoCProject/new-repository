package auctioneum.network.common;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Channel<D extends Serializable>{

    public Channel(){}

    public void send(D data, InetAddress to, int port)throws IOException{
        Socket s = new Socket(to,port);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(data);
        oos.flush();
        oos.close();
        s.close();
    }

    public <R> List<D> getDataOnce(R request, InetAddress from,int port)throws IOException,ClassNotFoundException{
        Socket s = new Socket(from,port);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(request);
        oos.flush();
        List<D> allData = new ArrayList<>();
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Integer dataSize = (Integer) ois.readObject();
        for (int i=0; i<dataSize; i++){
            D data = (D) ois.readObject();
            allData.add(data);
        }
        oos.close();
        ois.close();
        s.close();
        return allData;
    }
    
    public <R> List<List<Object>> getData(R request, InetAddress from, int port)throws IOException,ClassNotFoundException{
        Socket s = new Socket(from,port);
        ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
        oos.writeObject(request);
        oos.flush();
        List<List<Object>> allData = new ArrayList<>();
        ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
        Integer timesToReceive = (Integer) ois.readObject();
        for (int j=0; j<timesToReceive; j++) {
            int dataSize = (int)ois.readObject();
            List <Object> data = new ArrayList();
            for (int i=0; i<dataSize; i++){
                Object obj = ois.readObject();
                data.add(obj);
            }
            allData.add(data);
        }
        oos.close();
        ois.close();
        s.close();
        
        return allData;
    }



}
