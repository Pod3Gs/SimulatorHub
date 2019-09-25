package ui;

import utils.Constants;

import javax.swing.*;
import java.awt.*;

class ButtonGroupPanel extends JPanel {
  private JRadioButton qos0;
  private JRadioButton qos1;
  private JRadioButton qos2;

  ButtonGroupPanel() {
    super();
    super.setBackground(Constants.MAIN_COLOR);
    super.setPreferredSize(new Dimension(440, 30));
    qos0 = new JRadioButton("qos0");
    qos1 = new JRadioButton("qos1");
    qos2 = new JRadioButton("qos2");
    qos0.setBackground(Constants.MAIN_COLOR);
    qos1.setBackground(Constants.MAIN_COLOR);
    qos2.setBackground(Constants.MAIN_COLOR);
    qos0.setSelected(true);
    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(qos0);
    buttonGroup.add(qos1);
    buttonGroup.add(qos2);
    super.add(qos0);
    super.add(qos1);
    super.add(qos2);
  }

  int getQos() {
    if (qos0.isSelected()) {
      return 0;
    } else if (qos1.isSelected()) {
      return 1;
    } else if (qos2.isSelected()) {
      return 2;
    } else return 0;
  }
}
