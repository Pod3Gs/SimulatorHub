package core;

import org.apache.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import ui.LogPanel;
import ui.MQTTPanel;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

public class MQTT {
  private Logger log = Logger.getLogger(MQTT.class);

  public boolean isConnected = false;
  public MqttClient client;
  private MQTTPanel mqttPanel;

  public MQTT(MQTTPanel mqttPanel,LogPanel logPanel) {
    this.mqttPanel = mqttPanel;
    logPanel.initLog();
  }

  public void changeStatus(boolean f) {
    this.isConnected = f;
  }

  public IMqttToken connect(
      String ip, String port, String username, String password, String clientId, boolean isEnable)
          throws Exception {
    log.info("begin to cennect");
    String broker = null;
    if (isEnable) {
      broker = "ssl://" + ip + ":" + port;
    } else {
      broker = "tcp://" + ip + ":" + port;
    }
    // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
    this.client = new MqttClient(broker, clientId, new MemoryPersistence());
    // MQTT的连接设置
    MqttConnectOptions options = new MqttConnectOptions();
    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
    options.setCleanSession(true);
    // 设置连接的用户名
    options.setUserName(username);
    // 设置连接的密码
    options.setPassword(password.toCharArray());
    // 设置超时时间 单位为秒
    options.setConnectionTimeout(10);
    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
    options.setKeepAliveInterval(20);
    if (isEnable){
      System.out.println(mqttPanel.path.getText());
      SSLSocketFactory socketFactory = getSocketFactoryByChoose(mqttPanel.file);
      options.setSocketFactory(socketFactory);
    }
    // 设置回调函数
    this.client.setCallback(new MQTTCallback(this.mqttPanel,this));
    return this.client.connectWithResult(options);
  }

  public IMqttToken subscribe(String topic, int qos) throws MqttException {
    log.info("begin to subscribe");
    return this.client.subscribeWithResponse(topic, qos);
  }

  public void disSubscribe(String topic) throws MqttException {
    log.info("begin to unsubscribe");
    this.client.unsubscribe(topic);
  }

  public void publish(String topic, int qos, String content) throws MqttException {
    log.info("publish");
    MqttMessage message = new MqttMessage(content.getBytes(StandardCharsets.UTF_8));
    // 设置消息的服务质量
    message.setQos(qos);
    // 发布消息
    this.client.publish(topic, message);
  }

  public void disconnect() throws MqttException {
    log.info("disconnect");
    // 断开连接
    this.client.disconnect();
    // 关闭客户端
    this.client.close();
  }



  private static SSLSocketFactory getSocketFactoryByChoose(File file) throws Exception {
    InputStream certStream = new FileInputStream(file);
    CertificateFactory certFactory = CertificateFactory.getInstance("X509");
    Certificate certificate =  certFactory.generateCertificate(certStream);
    SSLContext sslContext = SSLContext.getInstance("TLS");
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null);
    keyStore.setCertificateEntry("alias", certificate);
    trustManagerFactory.init(keyStore);
    sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
    return sslContext.getSocketFactory();
  }
}
