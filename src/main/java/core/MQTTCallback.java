package core;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import ui.MQTTPanel;
import utils.Constants;
import utils.MyDate;
import utils.ParseUtils;

import java.nio.charset.StandardCharsets;

public class MQTTCallback implements MqttCallback {
  private Logger log = Logger.getLogger(MQTTCallback.class);

  private MQTTPanel mqttPanel;
  private MQTT mqtt;

  MQTTCallback(MQTTPanel mqttPanel, MQTT mqtt) {
    this.mqttPanel = mqttPanel;
    this.mqtt = mqtt;
  }

  public void connectionLost(Throwable cause) {
    System.out.println("connectionLost");
    log.warn("lost connection");
    mqttPanel.dataTextArea.append(
        "失去连接" + new MyDate().getTime() + Constants.NEXT_LINE + Constants.SEPARAE);
    mqttPanel.status.setText("状态：已断开");
    mqtt.changeStatus(false);
  }

  public void messageArrived(String topic, MqttMessage message) {
    log.info("message arrived");
    String msg = new String(message.getPayload(), StandardCharsets.UTF_8);

    System.out.println("topic:" + topic);
    System.out.println("Qos:" + message.getQos());
    System.out.println("message content:" + new String(message.getPayload()));
    mqttPanel.dataTextArea.append(
        "topic:"
            + topic
            + Constants.NEXT_LINE
            + "Qos:"
            + message.getQos()
            + Constants.NEXT_LINE
            + "接收数据内容:"
            + new MyDate().getTime()
            + Constants.NEXT_LINE
            + msg
            + Constants.NEXT_LINE
            + Constants.SEPARAE);
  }

  public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("message deliver success");
    String text = ParseUtils.parse2JSONStr(mqttPanel.sendTextArea.getText());
    mqttPanel.dataTextArea.append(
        "数据发送是否成功:"
            + token.isComplete()
            + "-"
            + new MyDate().getTime()
            + Constants.NEXT_LINE
            + "发送数据内容："
            + Constants.NEXT_LINE
            + text
            + Constants.NEXT_LINE
            + Constants.SEPARAE);
  }
}
