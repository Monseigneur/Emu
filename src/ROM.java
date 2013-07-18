/**
 * 
 * @author milanj91
 *
 * A ROM 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ROM {
  private byte[] romData;
  
  public ROM(String name) {
    File f = new File(name);
    FileInputStream fin = null;
    
    try {
      fin = new FileInputStream(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    romData = new byte[(int) f.length()];
    
    try {
      fin.read(romData);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  public int readMem(int offset) {
    if (offset > romData.length) {
      throw new IllegalArgumentException("Out of bounds rom read");
    }
    
    return romData[offset];
  }
  
  public int[] readMem(int offset, int len) {
    if (offset + len > romData.length) {
       throw new IllegalArgumentException("Cannot fit the data!");
    }
    
    int[] memSpace = new int[len];
    for (int i = 0; i < len; i++) {
      memSpace[i] = romData[offset + i];
    }
    
    return memSpace;
  }
}
