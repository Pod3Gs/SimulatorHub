package core;

import org.eclipse.californium.scandium.dtls.pskstore.PskStore;
import ui.MyFrame;
import utils.ParseUtils;

import java.net.InetSocketAddress;

public class MyPskStore implements PskStore {
  private MyFrame frame;

  public MyPskStore(MyFrame myFrame) {
    this.frame = myFrame;
  }


  @Override
  public byte[] getKey(String identity) {
    return ParseUtils.parseHexStr2Byte(frame.pskText.getText());
  }

  @Override
  public String getIdentity(InetSocketAddress inetAddress) {
    return frame.verifyCodeText.getText();
  }
}
