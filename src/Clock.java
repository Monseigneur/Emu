/**
 * 
 * @author milanj91
 * 
 * The GBC cartridge internal clock
 */

/*
 *   08h  RTC S   Seconds   0-59 (0-3Bh)
     09h  RTC M   Minutes   0-59 (0-3Bh)
     0Ah  RTC H   Hours     0-23 (0-17h)
     0Bh  RTC DL  Lower 8 bits of Day Counter (0-FFh)
     0Ch  RTC DH  Upper 1 bit of Day Counter, Carry Bit, Halt Flag
         Bit 0  Most significant bit of Day Counter (Bit 8)
         Bit 6  Halt (0=Active, 1=Stop Timer)
         Bit 7  Day Counter Carry Bit (1=Counter Overflow)
 */

public class Clock {
  private static final int CLOCK_REG_NUM = 4;
  
  public int[] latchedClock;
  
  private int[] clockData;
  private boolean overflow;
  private long lastTime;
  
  public Clock(long startTime) {
    clockData = new int[CLOCK_REG_NUM];
    latchedClock = new int[CLOCK_REG_NUM + 1];
    
    overflow = false;
    lastTime = startTime;
  }
  
  public void updateClock(long time) {
    long diff = time - lastTime;
    lastTime = time;
    
    diff /= 1000;
    
    clockData[0] += diff;
    rolloverClock();
  }
  
  public void latchClock() {
    for (int i = 0; i < CLOCK_REG_NUM; i++) {
      latchedClock[i] = clockData[i] & 0xff;
    }
    
    latchedClock[4] = clockData[3] & 0x100;
    if (overflow) {
      latchedClock[4] |= 0x80;
    }
  }
  
  private void rolloverClock() {
    if (clockData[0] >= 60) {
      int old = clockData[0];
      clockData[0] %= 60;
      clockData[1] += old / 60;
    }
    
    if (clockData[1] >= 60) {
      int old = clockData[1];
      clockData[1] %= 60;
      clockData[2] += old / 60;
    }
    
    if (clockData[2] >= 24) {
      int old = clockData[2];
      clockData[2] %= 24;
      clockData[3] += old / 24;
    }
    
    overflow = (clockData[3] >= 0x1ff);
  }
}
