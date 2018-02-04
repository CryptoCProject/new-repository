package auctioneum.network.common;

import auctioneum.blockchain.Account;
import auctioneum.blockchain.Block;
import auctioneum.blockchain.BlockChain;
import auctioneum.blockchain.Transaction;
import auctioneum.utils.files.FileManager;
import auctioneum.utils.keys.RSA;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class Node implements Serializable{

    /** Node's Account **/
    private Account account;

    /** Blockchain copy **/
    private BlockChain blockChain;

    /** Transaction Pool **/
    private transient List<Transaction> txPool;

    /** Node's peers **/
    private transient List<Node> peers;

    /** The ip address of the logged node **/
    private InetAddress ip;

    /** Port for incoming transactions **/
    private int transactionsPort;

    /** Port for incoming blocks to be validated **/
    private int validationsPort;

    /** Server that handles the incoming blocks for validation **/
    private transient Server validationServer;

    /** Server for receiving transactions **/
    private transient Server transactionServer;

    /** Stores/Retrieves files stored in the node's disk **/
    private transient FileManager fileManager;


    public Node(){
        this.ip = Settings.IP;
        this.validationsPort = Settings.VALIDATIONS_PORT;
        this.transactionsPort = Settings.TRANSACTIONS_PORT;
        this.fileManager = new FileManager();
        this.peers = Settings.PEERS;
        this.validationServer = new Server<ValidationService>(this,this.validationsPort,"VdsServer",ValidationService.class);
        this.transactionServer = new Server<TransactionService>(this, this.transactionsPort,"TxsServer",TransactionService.class);
        this.blockChain = this.updateCopy();
        this.txPool = new ArrayList<>();
    }

    /**----------------------------------- Network ----------------------------------**/

    public void register(){
        try{
            Channel<String> channel = new Channel<>();
            Request request = new Request(Request.REGISTER,null);
            int port = Settings.REGISTRATION_PORT;
            List<String> keys = channel.getDataOnce(request,InetAddress.getLocalHost(),port);
            FileManager.storeToDisk("PublicKey",keys.get(0));
            FileManager.storeToDisk("PrivateKey",keys.get(1));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void connect(){
        try {
            Channel<Account> channel = new Channel<>();
            String signedServiceType = RSA.sign(Request.CONNECT,RSA.getPrivateKeyFromString(FileManager.readFile("PrivateKey")));
            List<String> params = new ArrayList<>();
            params.add(FileManager.readFile("PublicKey"));
            params.add(String.valueOf(Settings.TRANSACTIONS_PORT));
            params.add(String.valueOf(Settings.VALIDATIONS_PORT));
            Request request = new Request(signedServiceType,params);
            List<List<Object>> connectionData = channel.getData(request,Settings.REGULATOR_IP,Settings.CONNECTION_PORT);
            Account account = (Account) connectionData.get(0).get(0);
            this.setAccount(account);
            List<Node> peers = new ArrayList<>();
            for(Object obj: connectionData.get(1)){
                Node peer = (Node) obj;
                peers.add(peer);
            }
            this.setPeers(peers);
            System.out.println(account);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updatePeers(){}

    /**-------------------------------- Transactions --------------------------------**/

    public void addTransaction(Transaction transaction){ synchronized (this.txPool){this.txPool.add(transaction);} }

    public boolean hasTransaction(Transaction transaction){ synchronized (this.txPool){return this.txPool.contains(transaction);} }

    public void updateTxPool(final String action, List<Transaction> txs){
        synchronized (this.txPool) {
            switch (action){
                case "add":{
                    for (Transaction validatedTx : txs) {
                        this.txPool.add(validatedTx);
                    }
                }
                case "remove":{
                    for (Transaction validatedTx : txs) {
                        this.txPool.remove(validatedTx);
                    }
                }
            }
        }
    }

    public void sendTransaction(Transaction transaction, Node target){
        try{
            Channel<Transaction> channel = new Channel();
            channel.send(transaction,target.getIp(),target.getTransactionsPort());
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    public void startTxServer(){
        Thread txService = new Thread(this.transactionServer);
        txService.start();
    }

    /**----------------------------------- Blocks -----------------------------------**/

    public void addBlock(Block block){
        synchronized (this.blockChain){
            this.blockChain.add(block);
        }
    }

    public void sendForValidation(Block block, Node target){
        try {
            Channel<Block> channel = new Channel();
            channel.send(block,target.getIp(),target.getValidationsPort());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startVdnServer(){
        Thread vdnService = new Thread(this.validationServer);
        vdnService.start();
    }

    /**--------------------------------- Blockchain ---------------------------------**/

    public BlockChain updateCopy(){return null;}


    /**
     * Leave auctioneum network
     */
    public void leave(){
        this.transactionServer.stop();
        this.validationServer.stop();
    }


    /**----------------------------- Accessors-Mutators -----------------------------**/

    public InetAddress getIp() {
        return this.ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public int getTransactionsPort() {
        return this.transactionsPort;
    }

    public void setTransactionsPort(int transactionsPort) {
        this.transactionsPort = transactionsPort;
    }

    public int getValidationsPort() {
        return this.validationsPort;
    }

    public void setValidationsPort(int validationsPort) {
        this.validationsPort = validationsPort;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public void setFileManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    public Account getAccount() {
        return this.account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Server getValidationServer() {
        return this.validationServer;
    }

    public void setValidationServer(Server validationServer) {
        this.validationServer = validationServer;
    }

    public Server getTransactionServer() {
        return this.transactionServer;
    }

    public void setTransactionServer(Server transactionServer) {
        this.transactionServer = transactionServer;
    }

    public BlockChain getBlockChain() {
        return this.blockChain;
    }

    public void setBlockChain(BlockChain blockChain) {
        this.blockChain = blockChain;
    }

    public List<Node> getPeers() {
        return this.peers;
    }

    public void setPeers(List<Node> peers) {
        this.peers = peers;
    }

    public List<Transaction> getTxPool() {
        return this.txPool;
    }

    public void setTxPool(List<Transaction> txPool) {
        this.txPool = txPool;
    }


    @Override
    public boolean equals(Object o) {
        if(o instanceof Node) {
            boolean sameIP = this.getIp().equals(((Node)o).getIp());
            boolean samePublicKey = this.getAccount().getAddress().equals(((Node)o).getAccount().getAddress());
            if(sameIP && samePublicKey){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = this.account.getAddress().hashCode()+ (37*hashCode)  ;
        hashCode = (hashCode*23)+ this.ip.hashCode();
        return hashCode;
    }


    @Override
    public String toString() {
        try {
            String res = "\nNode: "+this.ip;
            res+= "\nAddress:"+this.account.getAddress();
            return res;
        }catch (Exception e){
            return e.getMessage();
        }
    }

}
