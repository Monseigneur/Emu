/**
 * 
 * @author milanj91
 *
 * A gameboy color emulator
 */

public class Emu {
  //private ROM rom;
  private CPU cpu;
  private Memory mem;
  //private int[] memSpace;
  
  public Emu(String name) {
    mem = new Memory(name);
    cpu = new CPU();
    
    
    
    //memSpace = rom.readMem(0, 0x4000);
    //Util.printHex(memSpace, 0, 0x4000);
  }
  
  public void start() {
    
  }
  
  
  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("USAGE: Emu ROM_NAME");
    }
    
    Emu emu = new Emu(args[0]);
    emu.start();
  }
}
