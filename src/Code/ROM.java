package Code;
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
  
  private String name;
  private boolean isGBC;
  private int type;
  private int romSize;
  private int ramSize;
  
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
    
    loadParameters();
  }
  
  private void loadParameters() {
    char[] data = new char[11];
    for (int i = 0; i < data.length; i++) {
      data[i] = (char) romData[0x134 + i];
    }
    name = String.valueOf(data);
    
    isGBC = (romData[0x143] == (byte) 0x80 || romData[0x143] == (byte) 0xc0);
    
    type = (int) romData[0x147] & 0xff;
    
    int tempRomSize = (int) romData[0x148] & 0xff;
    if (tempRomSize >= 0x0 && tempRomSize <= 0x7) {
      romSize = (1 << 15) << tempRomSize;
    } else {
      throw new UnsupportedOperationException("other rom sizes not supported yet");
    }
    
    int tempRamSize = (int) romData[0x149];
    if (tempRamSize == 0x0) {
      ramSize = 0;
    } else if (tempRamSize == 0x1) {
      ramSize = 1 << 11;
    } else if (tempRamSize == 0x2) {
      ramSize = 1 << 13;
    } else if (tempRamSize == 0x3) {
      ramSize = 1 << 15;
    } else {
      throw new IllegalArgumentException("illegal ram size in the cartridge!");
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
  
  public int getRomSize() {
    return romSize;
  }
  
  public int getRamSize() {
    return ramSize;
  }
}
