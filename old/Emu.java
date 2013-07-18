/*
 * @author Milan Justel
 * 
 * Gameboy Color emulator
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Emu {
  public static final String ROM = "silver.gbc";
  
  public static byte[] memSpace = new byte[1 << 16];
  
  public static DrawingPanel lcd;
  
  public static void main(String[] args) {
    File f = new File(ROM);
    FileInputStream fin = null;
    
    /*
    DrawingPanel lcd = new DrawingPanel(160, 144);
    Graphics g = lcd.getGraphics();
    g.setColor(Color.RED);
    g.fillRect(0, 0, 5, 5);
    */
    
    try {
      fin = new FileInputStream(f);
    } catch (IOException e) {
      System.out.println("Failed to open the ROM");
      e.printStackTrace();
    }
    
    byte[] romData = new byte[(int) f.length()];
    
    populateMemSpace(fin);
    
    CPU cpu = new CPU();
    //cpu.printRegs();
    
    for (int i = 0; i < 5; i++) {
      //cpu.printRegs();
      byte opcode = (byte) romData[cpu.pc];
      Util.printHex(romData, cpu.pc, 1);
      //Util.printHex(opcode);
      
      if (opcode == 0) {
        ISA.nop(cpu);
      } else if (opcode == (byte) 0xc3) {
        short addr = (short) ((short) ((romData[cpu.pc + 2] & 0xff) << 8) | (romData[cpu.pc + 1] & 0xff));
        ISA.jmp(cpu, addr);
      } else if (opcode == (byte) 0xfe) {
        byte val = (byte) romData[cpu.pc];
        ISA.cp(cpu, val);
      }
      
    }
    
    try {
      fin.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  
  private static void populateMemSpace(FileInputStream fin) {
    try {
      fin.read(memSpace, 0, 0x4000);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
