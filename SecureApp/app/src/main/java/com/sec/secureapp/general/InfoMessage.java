package com.sec.secureapp.general;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sec.secureapp.activities.LoginActivity;
import com.sec.secureapp.activities.MainActivity;
import com.sec.secureapp.activities.OtpActivity;
import com.sec.secureapp.client.SSLclient;
import com.sec.secureapp.security.Hashing;

public class InfoMessage extends Thread {

    private Context context;
    private String _case;
    private Object messageInfo;
    private SSLclient client;
    private Hashing hash;

    public InfoMessage(Context context, String _case, Object messageInfo) {
        this.context = context;
        this._case = _case;
        this.messageInfo = messageInfo;
        this.hash = new Hashing();
    }

    @Override
    public void run() {
        client = new SSLclient(this.context);
        client.start();
        if (this._case.equals(T.SIGN_UP)) {
            signup((UserInfo) messageInfo);
        } else if (this._case.equals(T.LOG_IN)) {
            login((UserInfo) messageInfo);
        } else if (this._case.equals(T.OTP)) {
            otp((UserInfo) messageInfo);
        } else if (this._case.equals(T.MAIN)) {
            main(null);
        } else if (this._case.equals(T.OPEN_AUCTIONS)) {
            getOpenAuctions();
        } else if (this._case.equals(T.RUNNING_AUCTIONS)) {
            getRunningAuctions((UserInfo) messageInfo);
        } else if (this._case.equals(T.CREATE_AUCTION)) {
            createAuction((AuctionInfo) messageInfo);
        } else if (this._case.equals(T.PARTICIPATE)) {
            participate((ParticipationInfo) messageInfo);
        } else if (this._case.equals(T.BID)) {
            bid((ParticipationInfo) messageInfo);
        } else if (this._case.equals(T.EXCHANGE)) {
            getExchange();
        } else if (this._case.equals(T.ADD_FUNDS)) {
            addFunds((UserInfo) messageInfo);
        } else if (this._case.equals(T.BALANCE)) {
            getBalance((UserInfo) messageInfo);
        }
        client.closeCrap();
    }

    private void signup(UserInfo ui) {
        client.sendMessage(T.SIGN_UP + T.getJson(new String[]{"u", hash.getHashedCode(ui.getName()), "p", hash.getHashedCode(ui.getPwd()), "e", ui.getEmail()}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.SIGN_UP_MESSAGE != null) {
                if (T.SIGN_UP_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Unsuccessful registration. Something went wrong with the server. Try again please.", Toast.LENGTH_LONG);
                } else if (T.SIGN_UP_MESSAGE.equals(T.NAME_EXIST)) {
                    T.VIEW_TOAST(this.context, "Unsuccessful registration. Try an other user name.", Toast.LENGTH_LONG);
                } else {
                    String prkey = T.SIGN_UP_MESSAGE;
                    T.USER_ID = hash.getHashedCode(ui.getName());
                    T.DB.createTables();
                    T.DB.initializeValues(prkey.getBytes(), T.USER_ID);
                    T.SIGN_UP_MESSAGE = null;
                    client.sendMessage(T.PRIVATE_KEY + T.PRIVATE_KEY_ACK);
                    counter = 0;
                    for (; ; ) {
                        T.SLEEP(100);
                        counter++;
                        if (T.SIGN_UP_MESSAGE != null) {
                            if (T.SIGN_UP_MESSAGE.equals(T.SUCCESS)) {
                                T.VIEW_TOAST(this.context, "Successful registration. Log in please.", Toast.LENGTH_LONG);
                                Intent intent = new Intent(this.context, LoginActivity.class);
                                this.context.startActivity(intent);
                            } else if (T.SIGN_UP_MESSAGE.equals(T.NOT_SUCCESS)) {
                                T.VIEW_TOAST(this.context, "Unsuccessful registration. Something went wrong with the server. Try again please.", Toast.LENGTH_LONG);
                            }
                            break;
                        } else if (counter == 50) {
                            T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                            break;
                        }
                    }
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.SIGN_UP_MESSAGE = null;
    }

    private void login(UserInfo ui) {
        client.sendMessage(T.LOG_IN + T.getJson(new String[]{"u", hash.getHashedCode(ui.getName()), "p", hash.getHashedCode(ui.getPwd()), "o", ui.getSalt()}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.LOG_IN_MESSAGE != null) {
                if (T.LOG_IN_MESSAGE.equals(T.SUCCESS)) {
                    T.USER_ID = hash.getHashedCode(ui.getName());
                    Intent intent = new Intent(this.context, OtpActivity.class);
                    intent.putExtra("name", ui.getName());
                    this.context.startActivity(intent);
                } else if (T.LOG_IN_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Something went wrong with the server. Try again please.", Toast.LENGTH_LONG);
                } else if (T.LOG_IN_MESSAGE.equals(T.WRONG_CREDENTIALS)) {
                    T.VIEW_TOAST(this.context, "Wrong user name or password. Try again please.", Toast.LENGTH_LONG);
                }
                break;
            } else if (counter == 80) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.LOG_IN_MESSAGE = null;
    }

    private void otp(UserInfo ui) {
        client.sendMessage(T.OTP + T.getJson(new String[]{"u", hash.getHashedCode(ui.getName()), "o", ui.getOtp()}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.OTP_MESSAGE != null) {
                if (T.OTP_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Something went wrong with the server. Try again please.", Toast.LENGTH_LONG);
                } else if (T.OTP_MESSAGE.equals(T.WRONG_OTP)) {
                    T.VIEW_TOAST(this.context, "Wrong OTP. Try again please.", Toast.LENGTH_LONG);
                } else if (T.OTP_MESSAGE.equals(T.OTP_ERROR)) {
                    T.VIEW_TOAST(this.context, "You tried 3 times or time elapsed. Log in again.", Toast.LENGTH_LONG);
                    Intent intent = new Intent(this.context, LoginActivity.class);
                    this.context.startActivity(intent);
                } else if (T.OTP_MESSAGE.equals(T.SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Successful otp", Toast.LENGTH_LONG);
                    Intent intent = new Intent(this.context, MainActivity.class);
                    this.context.startActivity(intent);
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.OTP_MESSAGE = null;
    }

    private void main(Object obj) {
        client.sendMessage(T.MAIN);
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.MAIN_MESSAGE != null) {
                Intent i = new Intent("com.sec.secureapp.MAIN_TEXTVIEW");
                i.putExtra("message", T.MAIN_MESSAGE);
                context.sendBroadcast(i);
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.MAIN_MESSAGE = null;
    }

    private void getOpenAuctions() {
        client.sendMessage(T.OPEN_AUCTIONS);
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.OPEN_AUCTIONS_MESSAGE != null) {
                if (T.OPEN_AUCTIONS_MESSAGE.equals(T.AUCTION_ERROR)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                } else {
                    String data = T.OPEN_AUCTIONS_MESSAGE;
                    Intent i = new Intent("com.sec.secureapp.OPEN_AUCTIONS");
                    i.putExtra("getAuctions", data);
                    i.putExtra("running", false);
                    context.sendBroadcast(i);
                    break;
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.OPEN_AUCTIONS_MESSAGE = null;
    }

    private void getRunningAuctions(UserInfo ui) {
        client.sendMessage(T.RUNNING_AUCTIONS + T.getJson(new String[]{"u", ui.getName()}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.RUNNING_AUCTIONS_MESSAGE != null) {
                if (T.RUNNING_AUCTIONS_MESSAGE.equals(T.AUCTION_ERROR)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                } else {
                    String data = T.RUNNING_AUCTIONS_MESSAGE;
                    Intent i = new Intent("com.sec.secureapp.RUNNING_AUCTIONS");
                    i.putExtra("getAuctions", data);
                    i.putExtra("running", true);
                    context.sendBroadcast(i);
                    break;
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.RUNNING_AUCTIONS_MESSAGE = null;
    }

    private void createAuction(AuctionInfo ui) {
        client.sendMessage(T.CREATE_AUCTION + T.getJson(new String[]{"t", ui.getAuction_type(), "u", ui.getAuctioneer_id(), "o", ui.getObject_name(), "p", String.valueOf(ui.getInitial_price())}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.CREATE_AUCTION_MESSAGE != null) {
                String mes = T.CREATE_AUCTION_MESSAGE.substring(0, 1);
                System.out.println(mes);
                if (mes.equals(T.AUCTION_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Auction created.", Toast.LENGTH_LONG);
                    String auction_id = T.CREATE_AUCTION_MESSAGE.substring(1);
                    T.AC = new AuctionConnection(this.context, auction_id, "0");
                    T.AC.start();
                    Intent intent = new Intent(this.context, MainActivity.class);
                    this.context.startActivity(intent);
                } else if (mes.equals(T.AUCTION_ERROR)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.CREATE_AUCTION_MESSAGE = null;
    }

    private void participate(ParticipationInfo ui) {
        client.sendMessage(T.PARTICIPATE + T.getJson(new String[]{"u", ui.getParticipant_id(), "i", String.valueOf(ui.getAuction_id())}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.PARTICIPATE_MESSAGE != null) {
                if (T.PARTICIPATE_MESSAGE.equals(T.SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Participation done.", Toast.LENGTH_LONG);
                    ParticipatedAuctions.auctionsParticipated.add(ui.getAuction_id()); // store participated auction
                    T.AC = new AuctionConnection(this.context, String.valueOf(ui.getAuction_id()), "1");
                    T.AC.start();
                } else if (T.PARTICIPATE_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.PARTICIPATE_MESSAGE = null;
    }

    public void bid(ParticipationInfo ui) {
        client.sendMessage(T.BID + T.getJson(new String[]{"u", ui.getParticipant_id(), "i", String.valueOf(ui.getAuction_id()), "p", String.valueOf(ui.getPrice())}).toString());
        int counter = 0;
        for (;;){
            T.SLEEP(100);
            counter++;
            if (T.BID_MESSAGE != null) {
                String mes = T.BID_MESSAGE.substring(0,1);
                if (mes.equals(T.SUCCESS)) {
//                    T.VIEW_TOAST(this.context, "New bid: " +  T.BID_MESSAGE.substring(1), Toast.LENGTH_LONG);
                }
                else if (mes.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                }
                break;
            }
            else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.BID_MESSAGE = null;
    }

    public void getExchange() {
        client.sendMessage(T.EXCHANGE);
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.EXCHANGE_MESSAGE != null) {
                //T.VIEW_TOAST(this.context, "Exchange done.", Toast.LENGTH_LONG);
                System.out.println("Exchange done.");
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
    }

    public void addFunds(UserInfo ui) {
        client.sendMessage(T.ADD_FUNDS + T.getJson(new String[]{"u", ui.getName(), "m", ui.getPwd()}).toString()); // pwd variable is used for funds for this call only
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.ADD_FUNDS_MESSAGE != null) {
                if (T.ADD_FUNDS_MESSAGE.equals(T.SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Zafeirium added to account "+(Double.parseDouble(ui.getPwd())*Double.parseDouble(T.EXCHANGE_MESSAGE)), Toast.LENGTH_LONG);
                } else if (T.ADD_FUNDS_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Server not responding . Try again please.", Toast.LENGTH_LONG);
                }
                break;
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
        T.ADD_FUNDS_MESSAGE = null;
    }

    public void getBalance(UserInfo ui) {
        client.sendMessage(T.BALANCE + T.getJson(new String[]{"u", ui.getName()}).toString());
        int counter = 0;
        for (; ; ) {
            T.SLEEP(100);
            counter++;
            if (T.BALANCE_MESSAGE != null) {
                if (T.BALANCE_MESSAGE.equals(T.NOT_SUCCESS)) {
                    T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                    break;
                } else {
                    //T.VIEW_TOAST(this.context, "Get Balance Successful.", Toast.LENGTH_LONG);
                    System.out.println("Get balance successful.");

                    Intent i = new Intent("com.sec.secureapp.FUNDS_CHANGED");
                    context.sendBroadcast(i);
                    break;
                }
            } else if (counter == 50) {
                T.VIEW_TOAST(this.context, "Server not responding. Try again please.", Toast.LENGTH_LONG);
                break;
            }
        }
    }


}
