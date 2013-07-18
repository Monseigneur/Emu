
public class Test {
  public static void main(String[] args) {
    byte[] buf = new byte[20];
    buf[0] = 'a';
    buf[1] = ' ';
    buf[2] = 'c';
    buf[3] = 'o';
    buf[4] = 'o';
    buf[5] = 'l';
    
    Util.printHex(buf, 0, buf.length);
  }
}
