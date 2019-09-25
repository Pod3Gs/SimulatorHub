package ui;

import utils.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;

public class MQTTPanel extends JPanel {
  JTextField ipText;
  JTextField portText;
  JTextField clientIDPanel;
  JTextField username;
  JTextField password;
  JTextField subsTopicText;

  JTextField pubTopicText;
  public JTextField path;

  JButton connectBtn;
  JButton disConnectBtn;
  JButton subscribeBtn;
  JButton disSubscribeBtn;
  JButton publishBtn;

  ButtonGroupPanel subscribeBGP;
  ButtonGroupPanel publishBGP;

  public JTextArea dataTextArea;
  public JTextArea sendTextArea;

  public JLabel status;

  JCheckBox isSSLEnable;

  public File file;

  MQTTPanel() {
    super();
    super.setLayout(new BorderLayout());

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(550, 200));
    mainPanel.setBackground(Constants.MAIN_COLOR);

    JPanel basePanel = new JPanel();
    basePanel.setPreferredSize(new Dimension(449, 200));
    basePanel.setBackground(Constants.MAIN_COLOR);

    JTabbedPane tabbedPane = new JTabbedPane();
    tabbedPane.setPreferredSize(new Dimension(449, 120));
    JPanel connectPanel = new JPanel();
    connectPanel.setBackground(Constants.MAIN_COLOR);
    connectPanel.add(initLabel("服务端IP"));
    ipText = createText("IP", 190, 30);
    connectPanel.add(ipText);
    connectPanel.add(initLabel("服务端口"));
    portText = createText("Port", 80, 30);
    connectPanel.add(portText);
    connectPanel.add(initLabel("客户端ID"));
    clientIDPanel = createText("ClientID", 333, 30);
    connectPanel.add(clientIDPanel);
    tabbedPane.add("基础信息", connectPanel);

    JPanel userPanel = new JPanel();
    userPanel.setBackground(Constants.MAIN_COLOR);
    userPanel.add(createLabel(100, 30, "Username"));
    username = createText("username", 270, 30);
    userPanel.add(username);
    userPanel.add(createLabel(100, 30, "Password"));
    password = createText("password", 270, 30);
    userPanel.add(password);
    tabbedPane.add("用户认证", userPanel);

    JPanel sPanel = new JPanel();
    sPanel.setBackground(Constants.MAIN_COLOR);
    isSSLEnable = new JCheckBox("启用SSL/TLS");
    isSSLEnable.setSelected(false);
    isSSLEnable.setBackground(Constants.MAIN_COLOR);
    sPanel.add(isSSLEnable);
    path = createText("", 160, 30);
    sPanel.add(path);
    JButton certBtn = new JButton("获取证书");
    certBtn.addActionListener(
        e -> {
          JFileChooser jfc = new JFileChooser();
          jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
          jfc.showDialog(new JLabel(), "选择");
          File file = jfc.getSelectedFile();
          if (file != null) {
            path.setText(file.getAbsolutePath());
            this.file = file;
          }
        });
    sPanel.add(certBtn);
    tabbedPane.add("SSL/TLS", sPanel);

    basePanel.add(tabbedPane, BorderLayout.NORTH);

    JPanel controlPanel = new JPanel();
    controlPanel.setPreferredSize(new Dimension(449, 80));
    controlPanel.setBackground(null);
    connectBtn = new JButton("连接服务");
    connectBtn.setPreferredSize(new Dimension(90, 40));
    disConnectBtn = new JButton("断开连接");
    disConnectBtn.setBackground(Color.red);
    disConnectBtn.setPreferredSize(new Dimension(90, 40));
    status = createLabel(170, 40, "状态：未连接");
    controlPanel.add(status);
    controlPanel.add(connectBtn);
    controlPanel.add(disConnectBtn);
    basePanel.add(controlPanel, BorderLayout.CENTER);

    mainPanel.add(basePanel, BorderLayout.NORTH);

    JTabbedPane funcTabbedPane = new JTabbedPane();

    JPanel subscribePanel = new JPanel();
    subscribePanel.setLayout(new BorderLayout());

    JPanel subSettings = new JPanel();
    subSettings.setPreferredSize(new Dimension(440, 85));
    subSettings.setBackground(Constants.MAIN_COLOR);
    subSettings.add(new JLabel("TOPIC:"));
    subsTopicText = createText("topic", 170, 30);
    subSettings.add(subsTopicText);
    subscribeBtn = new JButton("订阅");
    subscribeBtn.setPreferredSize(new Dimension(90, 37));
    subSettings.add(subscribeBtn);
    disSubscribeBtn = new JButton("取消订阅");
    disSubscribeBtn.setPreferredSize(new Dimension(90, 37));
    subSettings.add(disSubscribeBtn);
    subscribeBGP = new ButtonGroupPanel();
    subSettings.add(subscribeBGP);

    subscribePanel.add(subSettings, BorderLayout.NORTH);

    funcTabbedPane.add("订阅", subscribePanel);

    JPanel publishPanel = new JPanel();
    publishPanel.setLayout(new BorderLayout());

    JPanel pubSettings = new JPanel();
    pubSettings.setPreferredSize(new Dimension(440, 85));
    pubSettings.setBackground(Constants.MAIN_COLOR);
    pubSettings.add(new JLabel("TOPIC:"));
    pubTopicText = createText("topic", 265, 30);
    pubSettings.add(pubTopicText);
    publishBtn = new JButton("发布");
    publishBtn.setPreferredSize(new Dimension(90, 37));
    pubSettings.add(publishBtn);

    publishBGP = new ButtonGroupPanel();
    pubSettings.add(publishBGP);

    sendTextArea = new JTextArea();
    sendTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    sendTextArea.setLineWrap(true);
    JScrollPane sendText = new JScrollPane(sendTextArea);

    publishPanel.add(pubSettings, BorderLayout.NORTH);
    publishPanel.add(sendText, BorderLayout.CENTER);
    funcTabbedPane.add("发布", publishPanel);

    funcTabbedPane.setPreferredSize(new Dimension(449, 370));
    mainPanel.add(funcTabbedPane, BorderLayout.CENTER);

    JPanel dataPanel = new JPanel();
    dataPanel.setLayout(new BorderLayout());
    dataPanel.setBackground(Color.BLACK);

    dataTextArea = new JTextArea();
    dataTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 17));
    dataTextArea.setLineWrap(true);
    JScrollPane text = new JScrollPane(dataTextArea);

    JPanel blank = createBlank(10, 10, Constants.MAIN_COLOR);
    JPanel funcPanel = createBlank(10, 50, Constants.MAIN_COLOR);
    JButton clearBtn = new JButton("清空日志");
    clearBtn.setPreferredSize(new Dimension(90, 40));
    clearBtn.addActionListener(e -> dataTextArea.setText(Constants.SEPARAE));
    funcPanel.add(clearBtn);
    dataPanel.add(text, BorderLayout.CENTER);
    dataPanel.add(blank, BorderLayout.WEST);
    dataPanel.add(blank, BorderLayout.EAST);
    dataPanel.add(funcPanel, BorderLayout.NORTH);
    dataPanel.add(createBlank(0, 10, Constants.MAIN_COLOR), BorderLayout.SOUTH);

    super.add(dataPanel, BorderLayout.CENTER);
    super.add(mainPanel, BorderLayout.WEST);
  }

  private JTextField createText(String msg, int width, int height) {
    JTextField textField = new JTextField();
    Dimension dimension = new Dimension(width, height);
    textField.setPreferredSize(dimension);
    LineBorder lineBorder = new LineBorder(new Color(0x7de9f2), 1, true);
    textField.setBorder(lineBorder);
    textField.addFocusListener(new JTextFieldHintListener(textField, msg));
    return textField;
  }

  private JPanel createBlank(int w, int h, Color color) {
    JPanel panel = new JPanel();
    panel.setPreferredSize(new Dimension(w, h));
    panel.setBackground(color);
    return panel;
  }

  private JLabel initLabel(String text) {
    JLabel jLabel = new JLabel(text);
    jLabel.setFont(new Font("黑体", Font.PLAIN, 13));
    return jLabel;
  }

  private JLabel createLabel(int w, int h, String name) {
    JLabel jLabel = new JLabel(name);
    jLabel.setPreferredSize(new Dimension(w, h));
    return jLabel;
  }
}
