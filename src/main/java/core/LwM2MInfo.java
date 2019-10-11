package core;

public class LwM2MInfo {
  private String ip;
  private String port;
  private String IMEI;
  private String IMSI;

  public LwM2MInfo(String ip, String port, String imsi, String imei) {
    this.ip = ip;
    this.port = port;
    this.IMSI = imsi;
    this.IMEI = imei;
  }

  public LwM2MInfo() {}

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getIMEI() {
    return IMEI;
  }

  public void setIMEI(String IMEI) {
    this.IMEI = IMEI;
  }

  public String getIMSI() {
    return IMSI;
  }

  public void setIMSI(String IMSI) {
    this.IMSI = IMSI;
  }
}
