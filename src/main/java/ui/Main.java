package ui;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Client;
import core.MQTT;
import core.MQTTInfo;
import core.NBInfo;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.Utils;
import org.eclipse.paho.client.mqttv3.MqttException;
import utils.Constants;
import utils.MyDate;
import utils.ParseUtils;

import javax.swing.*;
import java.io.*;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    Logger log = Logger.getLogger(Main.class);
    //此处不使用注释处是因为打包JAR包后无法读取配置信息
    PropertyConfigurator.configure(
        Main.class.getClassLoader().getResourceAsStream("log4j.properties"));
    // PropertyConfigurator.configure("log4j.properties");
    final MyFrame frame = new MyFrame();
    frame.logPanel.initLog();
    log.info("application started");
    int optionResponse = JOptionPane.showConfirmDialog(frame, "是否开启DTLS加密传输？", "提示", 0);
    if (optionResponse == 0) {
      frame.setSecure(true);
      frame.getTextArea.setText("已选择加密，请填写PSK"+Constants.NEXT_LINE+Constants.SEPARAE);
    } else {
      frame.setSecure(false);
      frame.getTextArea.setText("未选择加密"+Constants.NEXT_LINE+Constants.SEPARAE);
      frame.psk.setVisible(false);
      frame.pskText.setVisible(false);
//      this.logTextArea.printLog("未选择DTLS加密传输通道，无需填写PSK！", new Object[0]);
//      this.isUseSecure = false;
//      this.pskLabel.setVisible(false);
//      this.pskTextField.setVisible(false);
    }
    //由于打成jar包后无法直接对信息进行写入，所以这里采用在jar包路径中读取信息，若没有则在连接成功后创建文件
    String path = System.getProperty("java.class.path");
    int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
    int lastIndex = path.lastIndexOf(File.separator) + 1;
    String mainPath = path.substring(firstIndex, lastIndex);
    ObjectMapper mapper = new ObjectMapper();
    if (new File(mainPath, "mqttHistory").exists()) {
      InputStream fileInputStream = new FileInputStream(mainPath + "/mqttHistory");
      String result =
          new BufferedReader(new InputStreamReader(fileInputStream))
              .lines()
              .collect(Collectors.joining(System.lineSeparator()));
      System.out.println("mqttHistory:" + result);

      try {
        MQTTInfo mqttInfo = mapper.readValue(result, MQTTInfo.class);
        frame.mqttPanel.ipText.setText(mqttInfo.getIp());
        frame.mqttPanel.portText.setText(mqttInfo.getProt());
        frame.mqttPanel.clientIDPanel.setText(mqttInfo.getClientID());
        frame.mqttPanel.username.setText(mqttInfo.getUsername());
        frame.mqttPanel.password.setText(mqttInfo.getPassword());
      } catch (IOException e) {
        e.printStackTrace();
      }
      log.info("data loaded successfully");
      frame.mqttPanel.dataTextArea.append(
          "已为您恢复基础信息，若要加密传输，请重新选择证书文件" + Constants.NEXT_LINE + Constants.SEPARAE);
    }

    if (new File(mainPath, "nbHistory").exists()) {
      InputStream NBDataStream = new FileInputStream(mainPath + "/nbHistory");
      String NBResult =
          new BufferedReader(new InputStreamReader(NBDataStream))
              .lines()
              .collect(Collectors.joining(System.lineSeparator()));
      System.out.println("NBHISTORY:" + NBResult);
      try {
        NBInfo nbInfo = mapper.readValue(NBResult, NBInfo.class);
        frame.ipText.setText(nbInfo.getIp());
        frame.port.setText(nbInfo.getPort());
        frame.verifyCodeText.setText(nbInfo.getVerifyCode());
        frame.pskText.setText(nbInfo.getPsk());
      } catch (IOException e) {
        e.printStackTrace();
      }
      frame.getTextArea.append("已为您恢复基础信息,请注册以开始" + Constants.NEXT_LINE + Constants.SEPARAE);
    }
    final Client client = new Client(frame);
    frame.registerBtn.addActionListener(
        e -> {
          if (client.getIsStart()) {
            client.stop();
          }
          client.start();
          CoapResponse response = client.register();
          if (response == null) {
            frame.getTextArea.append("连接服务器失败" + Constants.NEXT_LINE + Constants.SEPARAE);
          } else {
            System.out.println(Utils.prettyPrint(response)); // 打印格式良好的输出
            System.out.println(response.getCode().value);
            if (response.getCode().value <= 95 && response.getCode().value >= 64) {
              NBInfo nbInfo =
                  new NBInfo(frame.ipText.getText(),frame.port.getText(), frame.verifyCodeText.getText(),frame.pskText.getText());
              System.out.println(mainPath);
              File file = new File(mainPath, "nbHistory");
              if (file.exists()) {
                // 文件已经存在，输出文件的相关信息
                System.out.println(file.getAbsolutePath());
                System.out.println(file.getName());
                System.out.println(file.length());
              } else {
                try {
                  // 创建新文件
                  file.createNewFile();
                } catch (IOException ex) {
                  System.out.println("创建新文件时出现了错误。。。");
                  ex.printStackTrace();
                }
              }
              try {
                FileWriter fw = new FileWriter(file);
                mapper.writeValue(fw, nbInfo);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
              frame.getTextArea.append(
                  "注册设备成功-" + new MyDate().getTime() + Constants.NEXT_LINE + Constants.SEPARAE);
              client.setConnect(true);
            } else {
              frame.getTextArea.append(
                  "注册设备失败-" + new MyDate().getTime() + Constants.NEXT_LINE + Constants.SEPARAE);
            }
          }
        });

    frame.sendBtn.addActionListener(
        e -> {
          if (!client.getIsConnect()) {
            frame.getTextArea.append(
                "未成功注册，不能进行数据上报" + Constants.NEXT_LINE + Constants.SEPARAE);
          } else if (frame.sendTextArea.getText().isEmpty()) {
            frame.getTextArea.append("上报数据不能为空" + Constants.NEXT_LINE + Constants.SEPARAE);
          } else {
            if (frame.hex.isSelected()) {
              client.uploadHEX(ParseUtils.parse2JSONStr(frame.sendTextArea.getText()));
            } else {
              client.uploadJSON(ParseUtils.parse2JSONStr(frame.sendTextArea.getText()));
            }
          }
        });
    MQTTPanel mqttPanel = frame.mqttPanel;
    MQTT mqtt = new MQTT(mqttPanel, frame.logPanel);
    mqttPanel.connectBtn.addActionListener(
        e -> {
          if (mqtt.isConnected) {
            mqttPanel.dataTextArea.append("建立新连接前，请先断开原有连接" + Constants.NEXT_LINE + Constants.SEPARAE);
          } else {
            try {
              mqtt.connect(
                  mqttPanel.ipText.getText(),
                  mqttPanel.portText.getText(),
                  mqttPanel.username.getText(),
                  mqttPanel.password.getText(),
                  mqttPanel.clientIDPanel.getText(),
                  mqttPanel.isSSLEnable.isSelected());
            } catch (MqttException ex) {
              mqttPanel.dataTextArea.append(printException(ex));
              ex.printStackTrace();
            } catch (Exception ex) {
              ex.printStackTrace();
              mqttPanel.dataTextArea.append("加密设置出错" + Constants.NEXT_LINE + Constants.SEPARAE);
            }
            if (mqtt.client.isConnected()) {
              log.info("connect success");
              MQTTInfo mqttInfo =
                  new MQTTInfo(
                      mqttPanel.ipText.getText(),
                      mqttPanel.portText.getText(),
                      mqttPanel.clientIDPanel.getText(),
                      mqttPanel.username.getText(),
                      mqttPanel.password.getText());
              String s = JSONObject.toJSONString(mqttInfo);
              System.out.println("jason" + s);

              File file = new File(mainPath, "mqttHistory");
              if (file.exists()) {
                // 文件已经存在，输出文件的相关信息
                System.out.println(file.getAbsolutePath());
                System.out.println(file.getName());
                System.out.println(file.length());
              } else {
                try {
                  // 创建新文件
                  file.createNewFile();
                } catch (IOException ex) {
                  System.out.println("创建新文件时出现了错误。。。");
                  log.info("write data failed");
                  ex.printStackTrace();
                }
              }
              try {
                FileWriter fw = new FileWriter(file);
                mapper.writeValue(fw, mqttInfo);
              } catch (IOException ex) {
                ex.printStackTrace();
              }
              mqttPanel.status.setText("状态：已连接");
              mqtt.changeStatus(true);
              mqttPanel.dataTextArea.append(
                  "连接成功-" + new MyDate().getTime() + Constants.NEXT_LINE + Constants.SEPARAE);
            }
          }
        });

    mqttPanel.disConnectBtn.addActionListener(
        e -> {
          if (mqtt.isConnected) {
            try {
              mqtt.disconnect();
            } catch (MqttException ex) {
              mqttPanel.dataTextArea.append(printException(ex));
              ex.printStackTrace();
              log.warn("disconnect failed");
            }
            if (!mqtt.client.isConnected()) {
              log.info("disconnect success");
              mqttPanel.status.setText("状态：已断开");
              mqtt.changeStatus(false);
              mqttPanel.dataTextArea.append(
                  "连接已断开-" + new MyDate().getTime() + Constants.NEXT_LINE + Constants.SEPARAE);
            }
          }
        });

    mqttPanel.subscribeBtn.addActionListener(
        e -> {
          if (mqtt.isConnected) {
            try {
              mqtt.subscribe(mqttPanel.subsTopicText.getText(), mqttPanel.subscribeBGP.getQos());
            } catch (MqttException ex) {
              mqttPanel.dataTextArea.append(printException(ex));
              ex.printStackTrace();
              log.warn("subscribe failed");
            }
            mqttPanel.dataTextArea.append(
                "订阅成功"
                    + "-"
                    + new MyDate().getTime()
                    + Constants.NEXT_LINE
                    + "TOPIC："
                    + mqttPanel.subsTopicText.getText()
                    + Constants.NEXT_LINE
                    + Constants.SEPARAE);
            log.info("subscribe success");
          } else {
            mqttPanel.dataTextArea.append("请先连接" + Constants.NEXT_LINE + Constants.SEPARAE);
          }
        });

    mqttPanel.disSubscribeBtn.addActionListener(
        e -> {
          if (mqtt.isConnected) {
            try {
              mqtt.disSubscribe(mqttPanel.subsTopicText.getText());
            } catch (MqttException ex) {
              log.warn("unsubscribe failed");
              mqttPanel.dataTextArea.append(printException(ex));
              ex.printStackTrace();
            }
            mqttPanel.dataTextArea.append(
                "订阅取消"
                    + "-"
                    + new MyDate().getTime()
                    + Constants.NEXT_LINE
                    + "TOPIC："
                    + mqttPanel.subsTopicText.getText()
                    + Constants.NEXT_LINE
                    + Constants.SEPARAE);
            log.info("unsubscribe success");
          } else {
            mqttPanel.dataTextArea.append("请先连接" + Constants.NEXT_LINE + Constants.SEPARAE);
          }
        });

    mqttPanel.publishBtn.addActionListener(
        e -> {
          if (mqtt.isConnected) {
            try {
              mqtt.publish(
                  mqttPanel.pubTopicText.getText(),
                  mqttPanel.publishBGP.getQos(),
                  ParseUtils.parse2JSONStr(mqttPanel.sendTextArea.getText()));
            } catch (MqttException ex) {
              log.warn("publish failed");
              mqttPanel.dataTextArea.append(printException(ex));
              ex.printStackTrace();
            }
            log.info("publish success");
          } else {
            mqttPanel.dataTextArea.append("请先连接" + Constants.NEXT_LINE + Constants.SEPARAE);
          }
        });
  }

  private static String printException(MqttException me) {
    return "reason: "
        + me.getReasonCode()
        + Constants.NEXT_LINE
        + "msg: "
        + me.getMessage()
        + Constants.NEXT_LINE
        + "loc: "
        + me.getLocalizedMessage()
        + Constants.NEXT_LINE
        + "cause: "
        + me.getCause()
        + Constants.NEXT_LINE
        + Constants.SEPARAE;
  }
}
