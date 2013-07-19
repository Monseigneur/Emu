/**
 * 
 * @author milanj91
 * 
 * MBC3 only
 */

public class Memory {
  private ROM rom;
  
  private boolean ramRTCEnable;
  private int romBankNum;
  private int romBankHighBits;
  private int ramBankNum;
  private boolean ramBankMode;
  
  public Memory(String romName) {
    rom = new ROM(romName);
    
    ramRTCEnable = false; // TODO
    romBankNum = 1;
    romBankHighBits = 0;
    ramBankMode = false;
  }
  
  public int readMem(int offset) {
    if (offset < 0x4000) {
      // first rom bank R
      return rom.readMem(offset);
    } else if (offset < 0x8000) {
      // switchable rom bank R
      int highBits = 0x0;
      if (!ramBankMode) {
        highBits = ramBankNum << 5;
      }
      int newOffset = (romBankNum | highBits) * 0x4000 + offset;
      return rom.readMem(newOffset);
    } else if (offset < 0xa000) {
      // video ram (READ?)
    } else if (offset < 0xc000) {
      // external ram RW
      
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
  
  public void writeMem(int val, int offset) {
    if (offset < 0x2000) {
      // Ram enable
      if (val == 0) {
        ramRTCEnable = false;
      } else if (val == 0xa) {
        ramRTCEnable = true;
      } else {
        throw new IllegalArgumentException("Invalid value to ram enable"); // TODO
      }
    } else if (offset < 0x4000) {
      // ROM bank number
      if (val == 0x0 && ramBankMode) {
        romBankNum = 1;
      } else if (val >= 0x0 && val <= 0x1f){
        romBankNum = val;
      } else {
        throw new IllegalArgumentException("Invalid value for rom bank");
      }
    } else if (offset < 0x6000) {
      // RAM Bank Number - or - Upper Bits of ROM Bank Number
      if (val >= 0 && val < 4) {
        ramBankNum = val;
      } else {
        throw new IllegalArgumentException("Illegal value for ram bank num / rom bank num high bits");
      }
    } else if (offset < 0x8000) {
      // ROM / RAM mode select
      if (val == 0) {
        ramBankMode = false;
      } else if (val == 1) {
        ramBankMode = true;
      } else {
        throw new IllegalArgumentException("Invalid valud for rom / ram mode");
      }
    }
  }
  
  public void writeMem(int[] mem, int srcOffset, int destOffset, int len) {
    
  }
}
