/**
 * 
 * @author milanj91
 *
 * A GBC emulator
 */

public class Emu {
  private ROM rom;
  private CPU cpu;
  private Memory mem;
  private ROMClock clock;
  
  public Emu(String name) {
    rom = new ROM(name);
    clock = new ROMClock(System.currentTimeMillis());
    mem = new Memory(rom, clock);
    cpu = new CPU();
    
    
    
    //memSpace = rom.readMem(0, 0x4000);
    //Util.printHex(memSpace, 0, 0x4000);
  }
  
  public void start() {
    for (int i = 0; i < 15; i++) {
      long s = System.currentTimeMillis();
      ISA.executeInstr(cpu, mem);
      long e = System.currentTimeMillis();
      System.out.println("TIME: " + (e - s));
    }
  }
  
  
  public static void main(String[] args) {
    if (args.length != 1) {
      throw new IllegalArgumentException("USAGE: Emu ROM_NAME");
    }
    
    Emu emu = new Emu(args[0]);
    emu.start();
  }
}
