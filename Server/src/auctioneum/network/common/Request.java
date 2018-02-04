package auctioneum.network.common;


import java.io.Serializable;
import java.util.List;

public class Request implements Serializable {

    /** Request types **/

    public static final String REGISTER = "REGISTER";
    public static final String CONNECT = "CONNECT";
    public static final String UPDATE_PEERS = "UPDATE: Peers";
    public static final String UPDATE_BLOCKCHAIN = "UPDATE: Blockchain";
    public static final String GET_AUCTIONS = "GET: Auction";



    private final String serviceType;

    private List<String> params;

    public Request(final String serviceType, List<String> params){
        this.serviceType = serviceType;
        this.params = params;

    }

    public String getServiceType() {
        return this.serviceType;
    }

    public List<String> getParams() {
        return this.params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }


}
