package core;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.Utils;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import ui.MyFrame;
import utils.Constants;
import utils.MyDate;
import utils.ParseUtils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DeviceResource extends CoapResource {
  private byte[] data = null;
  private MyFrame sourseFrame;

  DeviceResource(String name, MyFrame myFrame) {
    super(name);
    setObservable(true);
    getAttributes().setTitle("Observable resource");
    getAttributes().addResourceType("observe");
    getAttributes().setObservable();
    setObserveType(CoAP.Type.CON);
    this.sourseFrame = myFrame;
  }

  public void handleGET(CoapExchange exchange) {
    try {
      System.out.println(Constants.START_LINE + "DeviceResource.handleGET" + Constants.START_LINE);
      System.out.println(
          "handle GET from {"
              + exchange.getSourceAddress()
              + "}:{"
              + exchange.getSourcePort()
              + "} ");

      System.out.println(exchange.advanced().getResponse());
      System.out.println(ParseUtils.parseByte2HexStr(exchange.getRequestPayload()));

      Response response = new Response(CoAP.ResponseCode.CONTENT);

      if (null != this.data) {
        System.out.println("@first time@");
        System.out.println("response GET :{" + new String(this.data) + "}");
        if (!sourseFrame.json.isSelected()) {
          sourseFrame.getTextArea.append(
              new MyDate().getTime()
                  + Constants.NEXT_LINE
                  + "数据上报内容为："
                  + Constants.NEXT_LINE
                  + ParseUtils.parseByte2HexStr(this.data)
                  + Constants.NEXT_LINE
                  + Constants.SEPARAE);
        } else {
          sourseFrame.getTextArea.append(
              new MyDate().getTime()
                  + Constants.NEXT_LINE
                  + "数据上报内容为："
                  + Constants.NEXT_LINE
                  + new String(this.data, StandardCharsets.UTF_8)
                  + Constants.NEXT_LINE
                  + Constants.SEPARAE);
        }
        response.setPayload(this.data);
      }
      this.data = null;
      exchange.respond(response);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
    System.out.println(Constants.END_LINE + "DeviceResource.handleGET" + Constants.END_LINE);
  }

  public void handlePOST(CoapExchange exchange) {
    try {
      System.out.println(Constants.START_LINE + "DeviceResource.handlePOST" + Constants.START_LINE);
      System.out.println(
          "receive POST from {"
              + exchange.getSourceAddress()
              + "}:{"
              + exchange.getSourcePort()
              + "}:\n{"
              + exchange.advanced().getRequest()
              + "} ");

      byte[] rawPayload = exchange.getRequestPayload();

      String payload = new String(exchange.getRequestPayload(), StandardCharsets.UTF_8);
      System.out.println(Utils.prettyPrint(exchange.advanced().getRequest())); // 打印格式良好的输出
      String s = ParseUtils.parseByte2HexStr(rawPayload);
      System.out.println("POST rawpayload is {" + Arrays.toString(rawPayload) + "}");
      System.out.println("POST rawpayload-coded is {" + s + "}");
      System.out.println("POST payload is {" + payload + "}");
      if (!sourseFrame.json.isSelected()) {
        sourseFrame.getTextArea.append(
            new MyDate().getTime()
                + Constants.NEXT_LINE
                + "服务器下发原始命令为："
                + Constants.NEXT_LINE
                + s
                + Constants.NEXT_LINE
                + Constants.SEPARAE);
      } else {
        sourseFrame.getTextArea.append(
            new MyDate().getTime()
                + Constants.NEXT_LINE
                + "服务器下发命令为："
                + Constants.NEXT_LINE
                + payload
                + Constants.NEXT_LINE
                + Constants.SEPARAE);
      }

      exchange.respond(CoAP.ResponseCode.CONTENT);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
    System.out.println(Constants.END_LINE + "DeviceResource.handlePOST" + Constants.END_LINE);
  }

  private void setPayload(String payload) {
    if (null == payload) {
      this.data = null;
    } else {
      this.data = payload.getBytes();
    }
  }

  public void stateChanged(String payload) {
    setPayload(payload);
    changed();
  }

  void stateChanged(byte[] payload) {
    this.data = payload;
    changed();
  }
}
