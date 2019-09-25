package core;

public class NBInfo {
  private String ip;
  private String port;
  private String verifyCode;
  private String psk;

  public NBInfo(String ip, String port, String verifyCode, String psk) {
    this.ip = ip;
    this.port = port;
    this.verifyCode = verifyCode;
    this.psk = psk;
  }

  public NBInfo() {}

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

  public String getVerifyCode() {
    return verifyCode;
  }

  public void setVerifyCode(String verifyCode) {
    this.verifyCode = verifyCode;
  }

  public String getPsk() {
    return psk;
  }

  public void setPsk(String psk) {
    this.psk = psk;
  }
}
