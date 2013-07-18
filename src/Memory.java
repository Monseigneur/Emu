/**
 * 
 * @author milanj91
 *
 */

public class Memory {
  private ROM rom;
  
  public Memory(String romName) {
    rom = new ROM(romName);
  }
  
  public int readMem(int offset) {
    if (offset < 0x4000) {
      // first rom bank
      return rom.readMem(offset);
    } else if (offset < 0x8000) {
      // switchable rom bank
    } else if (offset < 0xa000) {
      // video ram (READ?)
    } else if (offset < 0xc000) {
      // external ram
    } else if (offset < 0xd000) {
      // work ram bank 0
    } else if (offset < 0xe000) {
      // work ram switchable
    } else if (offset < 0xfe00) {
      // mirror area, not used?
    } else if (offset < 0xfea0) {
      // sprite attribute table
    } else if (offset < 0xff00) {
      // NOT USABLE
    } else if (offset < 0xff80) {
      // IO registers
    } else if (offset < 0xffff) {
      // High ram
    } else {
      // Interrupt enable register
    }
    return 0;
  }
  
  public void writeMem(int[] mem, int srcOffset, int destOffset, int len) {
    
  }
}
