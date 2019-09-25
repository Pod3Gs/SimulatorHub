package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class JTextFieldHintListener implements FocusListener {
  private String hintText;
  private JTextField textField;

  JTextFieldHintListener(JTextField jTextField, String hintText) {
    this.textField = jTextField;
    this.hintText = hintText;
    jTextField.setText(hintText); // 默认直接显示
    jTextField.setForeground(new Color(0xa7d9ec));
  }

  @Override
  public void focusGained(FocusEvent e) {
    // 获取焦点时，清空提示内容
    String temp = textField.getText();
    if (temp.equals(hintText)) {
      textField.setText("");
      textField.setForeground(Color.BLUE);
    }
  }

  @Override
  public void focusLost(FocusEvent e) {
    // 失去焦点时，没有输入内容，显示提示内容
    String temp = textField.getText();
    if (temp.equals("")) {
      textField.setForeground(new Color(0xa7d9ec));
      textField.setText(hintText);
    }
  }
}
