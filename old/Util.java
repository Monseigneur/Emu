/*
 * @author Milan Justel
 * 
 * A class containing utility methods
 */

public class Util {
  public static final int BYTES_PER_LINE = 16;
  
  public static void printHex(byte[] buf, int start, int numBytes) {
    for (int i = 0; i < numBytes; i++) {
      if (i % BYTES_PER_LINE == 0) {
        if (i > 0) {
          System.out.println();
        }
        String addr = String.format("0x%6s:", Integer.toHexString(start + i)).replace(' ', '0');
        System.out.print(addr + " ");
      }
      
      String hex = String.format("%2s", Integer.toHexString(buf[start + i] & 0xff)).replace(' ', '0');
      
      System.out.print(hex + " ");
    }
    System.out.println();
  }
  
  public static void printHex(byte b) {
    System.out.println(String.format("%2s", Integer.toHexString(b & 0xff)).replace(' ', '0'));
  }
  
  public static void printHex(short s) {
    System.out.println(String.format("%4s", Integer.toHexString(s & 0xffff)).replace(' ', '0'));
  }
}
