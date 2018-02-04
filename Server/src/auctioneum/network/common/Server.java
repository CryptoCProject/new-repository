package auctioneum.network.common;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;



public class Server <S extends Runnable>implements Runnable{

    /** The node in which the server is running **/
    private Node owner;

    /** Name of the server **/
    private String name;

    /** Socket running on the owner node**/
    private ServerSocket serverSocket;

    /** Client connection **/
    private Socket clientSocket;

    /** The port that the server listens to **/
    private int listeningPort;

    /** Whether to stop the server **/
    private boolean stop;

    /** Service class **/
    private Class<S> serviceCls;


    public Server(Node owner,int listeningPort,String name, Class<S> serviceCls){
        this.owner = owner;
        this.listeningPort = listeningPort;
        this.name = name;
        this.serviceCls = serviceCls;
        this.stop = false;
    }

    @Override
    public void run() {
        try {
            System.out.println(name+" started at port "+listeningPort);
            this.serverSocket = new ServerSocket(listeningPort);
            while (!stop) {
                this.clientSocket = serverSocket.accept();
                System.out.println("\nConnected with : "+this.clientSocket.getInetAddress()+" at "+listeningPort+" "+new Date());
                S service = this.serviceCls.getConstructor(Node.class,Socket.class).newInstance(this.owner,this.clientSocket);
                Thread serviceExecutor = new Thread(service);
                serviceExecutor.start();
            }
        }catch (SocketException e){
            //Closing server socket
            e.printStackTrace();
            System.out.println(this.name+" terminated");
            this.stop();
        }
        catch (Exception e){
            e.printStackTrace();
            this.stop();
            
        }
    }

    /**
     * Terminates the server.
     */
    public void stop(){
        try {
            System.out.println("Attempting to stop "+this.name+" running at port "+listeningPort+" ...");
            this.stop = true;
            this.serverSocket.close();
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
    }


}

