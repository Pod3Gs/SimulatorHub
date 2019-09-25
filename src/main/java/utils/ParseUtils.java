package utils;

import io.netty.buffer.ByteBuf;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ParseUtils {

  public static byte[] convertMsgToByte(String payload) {
    return payload.getBytes(StandardCharsets.UTF_8);
  }

  public static String parseByte2HexStr(byte[] buf) {
    if (null == buf) {
      return null;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

  public static byte[] parseHexStr2Byte(String hexStr) {
    hexStr = hexStr.replaceAll(" ", "");
    if (hexStr.length() < 1) {
      return null;
    }
    if (hexStr.length() % 2 != 0) {
      hexStr = "0" + hexStr;
    }
    byte[] result = new byte[hexStr.length() / 2];
    for (int i = 0; i < hexStr.length() / 2; i++) {
      int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
      int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
      result[i] = ((byte) (high * 16 + low));
    }
    return result;
  }

  public static String parseByteBuf2HexStr(ByteBuf in) {
    byte[] ins = new byte[in.readableBytes()];

    in.readBytes(ins);

    in.resetReaderIndex();

    return parseByte2HexStr(ins);
  }

  public static byte[] parseHexStr2Bcd(String asc) {
    int len = asc.length();
    int mod = len % 2;
    if (mod != 0) {
      asc = "0" + asc;
      len = asc.length();
    }
    if (len >= 2) {
      len /= 2;
    }
    byte[] bbt = new byte[len];
    byte[] abt = asc.getBytes();

    for (int p = 0; p < asc.length() / 2; p++) {
      int j;
      if ((abt[(2 * p)] >= 48) && (abt[(2 * p)] <= 57)) {
        j = abt[(2 * p)] - 48;
      } else {

        if ((abt[(2 * p)] >= 97) && (abt[(2 * p)] <= 122)) {
          j = abt[(2 * p)] - 97 + 10;
        } else j = abt[(2 * p)] - 65 + 10;
      }
      int k;

      if ((abt[(2 * p + 1)] >= 48) && (abt[(2 * p + 1)] <= 57)) {
        k = abt[(2 * p + 1)] - 48;
      } else {

        if ((abt[(2 * p + 1)] >= 97) && (abt[(2 * p + 1)] <= 122)) {
          k = abt[(2 * p + 1)] - 97 + 10;
        } else k = abt[(2 * p + 1)] - 65 + 10;
      }
      int a = (j << 4) + k;
      byte b = (byte) a;
      bbt[p] = b;
    }

    return bbt;
  }

  public static String parseBcd2HexStr(byte[] bytes) {
    char[] temp = new char[bytes.length * 2];

    for (int i = 0; i < bytes.length; i++) {
      char val = (char) ((bytes[i] & 0xF0) >> 4 & 0xF);
      temp[(i * 2)] = ((char) (val > '\t' ? val + 'A' - 10 : val + '0'));

      val = (char) (bytes[i] & 0xF);
      temp[(i * 2 + 1)] = ((char) (val > '\t' ? val + 'A' - 10 : val + '0'));
    }
    return new String(temp);
  }

  public static String parseStringToHexStr(String plainText) {
    return String.format("%x", new BigInteger(1, plainText.getBytes(Charset.forName("UTF-8"))));
  }

  public static String parse2JSONStr(String str){
    String text = str.replaceAll(Constants.NEXT_LINE, "");
    text = text.replaceAll(" ", "");
    return text;
  }
}
