package core;

public class MQTTInfo {
    private String ip;
    private String prot;
    private String clientID;
    private String username;
    private String password;

    public MQTTInfo() {
    }

    public MQTTInfo(String ip, String prot, String clientID, String username, String password) {
        this.ip = ip;
        this.prot = prot;
        this.clientID = clientID;
        this.username = username;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
