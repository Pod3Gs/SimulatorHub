package ui;

import utils.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class LwM2MPanel extends JPanel {
  public JTextField ipText;
  public JTextField verifyCodeText;
  public JTextField port;
  public JTextField pskText;
  public JTextField resourceIdText;

  JButton sendBtn;
  JButton registerBtn;

  public JTextArea getTextArea;
  JTextArea sendTextArea;

  JLabel psk;

  public JRadioButton json;
  JRadioButton hex;

  LwM2MPanel() {
    super.setLayout(new BorderLayout());

    JPanel connectPanel = new JPanel();
    connectPanel.setBackground(Constants.MAIN_COLOR);
    registerBtn = new JButton("设备连接");
    connectPanel.add(createLabel("连接地址"));
    ipText = createText("IP");
    ipText.setPreferredSize(new Dimension(90, 30));
    connectPanel.add(ipText);
    connectPanel.add(createLabel("端口"));
    port = createText("Port");
    port.setPreferredSize(new Dimension(50, 30));
    connectPanel.add(port);
    connectPanel.add(createLabel("IMEI"));
    verifyCodeText = createText("IMEI");
    connectPanel.add(verifyCodeText);
    psk = createLabel("IMSI");
    pskText = createText("IMSI");
    connectPanel.add(psk);
    connectPanel.add(pskText);
    connectPanel.add(registerBtn);
    super.add(connectPanel, BorderLayout.NORTH);

    JPanel sendPanel = new JPanel();
    sendPanel.setBackground(Constants.MAIN_COLOR);
    sendPanel.setLayout(new BorderLayout());
    sendPanel.setPreferredSize(new Dimension(400, 520));
    sendTextArea = new JTextArea();
    // sendTextArea.setPreferredSize(new Dimension(370, 450));
    sendTextArea.setBackground(Color.WHITE);
    sendTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
    sendTextArea.setLineWrap(true);
    JPanel sendCmd = new JPanel();
    sendCmd.setPreferredSize(new Dimension(300, 40));
    sendCmd.setBackground(Constants.MAIN_COLOR);
    sendCmd.add(createLabel("资源ID"));
    resourceIdText = createText("id");
    sendCmd.add(resourceIdText);
    sendPanel.add(sendCmd, BorderLayout.NORTH);
    JScrollPane sendText = new JScrollPane(sendTextArea);
    sendPanel.add(sendText, BorderLayout.CENTER);
    JPanel control = new JPanel();
    control.setBackground(Constants.MAIN_COLOR);
    sendBtn = new JButton("数据上报");
    json = new JRadioButton("JSON");
    json.setBackground(Constants.MAIN_COLOR);
    hex = new JRadioButton("HEX");
    hex.setBackground(Constants.MAIN_COLOR);
    ButtonGroup buttonGroup = new ButtonGroup();
    hex.setSelected(true);
    buttonGroup.add(hex);
    buttonGroup.add(json);
//    control.add(hex);
//    control.add(json);
    control.add(sendBtn);
    sendPanel.add(control, BorderLayout.SOUTH);
    sendPanel.add(createBlank(20, 0, Constants.MAIN_COLOR), BorderLayout.WEST);

    JPanel tabPanel = new JPanel();
    tabPanel.setBackground(Constants.MAIN_COLOR);
    tabPanel.setLayout(new BorderLayout());
    tabPanel.setPreferredSize(new Dimension(530, 520));
    getTextArea = new JTextArea();
    // getTextArea.setPreferredSize(new Dimension(490, 400));
    getTextArea.setBackground(Color.WHITE);
    getTextArea.setFont(new Font("微软雅黑", Font.PLAIN, 17));
    getTextArea.setLineWrap(true);
    JPanel command = new JPanel();
    command.setBackground(Constants.MAIN_COLOR);
    command.setPreferredSize(new Dimension(300, 40));
    command.add(createLabel("数据显示区域"), BorderLayout.NORTH);
    JScrollPane getText = new JScrollPane(getTextArea);
    tabPanel.add(getText, BorderLayout.CENTER);
    JButton clearBtn = new JButton("清空内容");
    clearBtn.addActionListener(e -> getTextArea.setText(Constants.SEPARAE));
    command.add(clearBtn);
    tabPanel.add(command, BorderLayout.NORTH);
    tabPanel.add(createBlank(20, 0, Constants.MAIN_COLOR), BorderLayout.WEST);
    tabPanel.add(createBlank(0, 39, Constants.MAIN_COLOR), BorderLayout.SOUTH);
    tabPanel.add(createBlank(20, 0, Constants.MAIN_COLOR), BorderLayout.EAST);

    super.add(sendPanel, BorderLayout.WEST);

    super.add(tabPanel, BorderLayout.CENTER);
  }

  private JLabel createLabel(String name) {
    return new JLabel(name);
  }

  private JTextField createText(String msg) {
    JTextField textField = new JTextField();
    Dimension dimension = new Dimension(150, 30);
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
}
