/**
 * 
 * @author milanj91
 * 
 * Representing the GBC memory space (supports MBC3 only currently)
 */

public class Memory {
  private ROM rom;
  private ROMClock clock;
  
  private boolean ramRTCEnable;
  private int romBankNum;
  private int ramBankRTCRegNum;
  private int prevRTCWrite;
  
  public Memory(ROM r, ROMClock c) {
    rom = r;
    clock = c;
    
    ramRTCEnable = false;
    romBankNum = 0x1;     // cannot choose bank 0, since it is already bound
    ramBankRTCRegNum = 0x0;
    prevRTCWrite = 0;
  }
  
  public int readMem(int offset) {
    if (offset < 0x4000) {
      // first rom bank R
      return rom.readMem(offset);
    } else if (offset < 0x8000) {
      // switchable rom bank R
      return rom.readMem(romBankNum * 0x4000 + (offset - 0x4000));
    } else if (offset < 0xa000) {
      // video ram (READ?)
    } else if (offset < 0xc000) {
      // external ram 
      if (ramBankRTCRegNum <= 0x3) {
        // Reading ram bank
        throw new UnsupportedOperationException("NYI");
      } else {
        return clock.latchedClock[ramBankRTCRegNum - 0x8];
      }
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
      if (val == 0x0) {
        ramRTCEnable = false;
      } else if (val == 0xa) {
        ramRTCEnable = true;
      } else {
        throw new IllegalArgumentException("Invalid parameter to ram / RTC enable");
      }
    } else if (offset < 0x4000) {
      // ROM bank number
      if (val == 0x0) {
        romBankNum = 0x1;
      } else if (val > 0x0 && val <= 0x7f) {
        romBankNum = val;
      } else {
        throw new IllegalArgumentException("Invalid parameter to rom bank number");
      }
    } else if (offset < 0x6000) {
      // RAM Bank Number
      if ((val >= 0x0 && val <= 0x3) || (val >= 0x8 && val <= 0xc)) {
        ramBankRTCRegNum = val;
      } else {
        throw new IllegalArgumentException("Invalid parameter to ram bank number");
      }
    } else if (offset < 0x8000) {
      // Clock latching, need to write 0 then 1
      if (prevRTCWrite == 0x0 && val == 0x1) {
        clock.latchClock();
      }
      prevRTCWrite = val;
    }
  }
  
  public void writeMem(int[] mem, int srcOffset, int destOffset, int len) {
    
  }
}
