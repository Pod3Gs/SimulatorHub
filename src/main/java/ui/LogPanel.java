package ui;

import utils.TextAreaLogAppender;

import javax.swing.*;
import java.awt.*;

/*此类为日志打印的panel类*/
public class LogPanel extends JPanel {
  private JScrollPane logScrollPane;
  private JTextArea logTextArea;

  LogPanel() {
    super();
    super.setLayout(new BorderLayout());
    JButton clearBtn = new JButton("清空日志");
    clearBtn.setPreferredSize(new Dimension(0, 40));
    clearBtn.addActionListener(
        e -> {
          this.logTextArea.setText("");
        });
    super.add(clearBtn, BorderLayout.NORTH);
    logTextArea = new JTextArea();
    logScrollPane = new JScrollPane(logTextArea);
    super.add(logScrollPane, BorderLayout.CENTER);
  }

  public void initLog() {
    try {
      Thread t2;
      t2 = new TextAreaLogAppender(logTextArea, logScrollPane);
      t2.start();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, e, "绑定日志输出组件错误", JOptionPane.ERROR_MESSAGE);
    }
  }
}
