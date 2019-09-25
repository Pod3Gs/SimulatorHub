package core;

import org.eclipse.californium.core.*;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.core.network.config.NetworkConfig;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.cipher.CipherSuite;
import ui.MyFrame;
import utils.ParseUtils;

import java.net.InetSocketAddress;
import java.util.Arrays;

public class Client {
  private CoapServer server;
  private DeviceResource resource;
  private MyFrame serverFrame;
  private boolean isStart = false;
  private boolean isConnect = false;

  public Client(MyFrame myFrame) {
    this.serverFrame = myFrame;
  }

  public void setConnect(boolean connect) {
    isConnect = connect;
  }

  public boolean getIsStart() {
    return this.isStart;
  }

  public boolean getIsConnect() {
    return this.isConnect;
  }

  private CoapEndpoint getServerEndpoint() {
    return new CoapEndpoint(5683, NetworkConfig.getStandard());
  }

  public void start() {
    this.server = new CoapServer();
    if (serverFrame.isSecure()) {
      DtlsConnectorConfig.Builder builder = new DtlsConnectorConfig.Builder(new InetSocketAddress(0));
      builder.setPskStore(new MyPskStore(serverFrame));
      builder.setSupportedCipherSuites(new CipherSuite[]{CipherSuite.TLS_PSK_WITH_AES_128_CCM_8, CipherSuite.TLS_PSK_WITH_AES_128_CBC_SHA256});
      DTLSConnector connector = new DTLSConnector(builder.build());
      this.server.addEndpoint(new CoapEndpoint(connector, NetworkConfig.getStandard()));
    } else {
      this.server.addEndpoint(getServerEndpoint());
    }
    CoapResource rootResource = new CoapResource("t");
    this.resource = new DeviceResource("d", this.serverFrame);
    DeviceResource ubResource = new DeviceResource("up", this.serverFrame);
    DeviceResource downResource = new DeviceResource("down", this.serverFrame);
    rootResource.add(this.resource);
    this.server.add(rootResource);
    this.server.add(ubResource);
    this.server.add(downResource);
    this.server.start();
    this.isStart = true;

    InetSocketAddress inetSocketAddress = this.resource.getEndpoints().get(0).getAddress();
    System.out.println(
        "TupClient CoAP server start. {"
            + inetSocketAddress.getAddress().getHostAddress()
            + "}:{"
            + inetSocketAddress.getPort()
            + "}");
  }

  public void stop() {
    this.server.destroy();
    this.isStart = false;
    this.isConnect = false;
  }

  public CoapResponse register() {
    String ip = this.serverFrame.ipText.getText();
    String ep = this.serverFrame.verifyCodeText.getText();
    String port = this.serverFrame.port.getText();
    CoapClient.Builder builder = new CoapClient.Builder(ip, Integer.parseInt(port));
    CoapClient coapClient = builder.query("ep=" + ep+"&simulator=true").path(new String[] {"t", "r"}).create();
    coapClient.setEndpoint(this.server.getEndpoints().get(0));
    coapClient.setTimeout(3000);
    return coapClient.post((byte[]) null, 42);
  }

  private void uploadMoData(byte[] data) {
    System.out.println("notify changed :{" + Arrays.toString(data) + "}");
    this.resource.stateChanged(data);
  }

  public void uploadHEX(String data) {
    this.uploadMoData(ParseUtils.parseHexStr2Byte(data));
  }

  public void uploadJSON(String data) {
    this.uploadMoData(ParseUtils.convertMsgToByte(data));
  }
}
