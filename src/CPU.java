/**
 * 
 * @author milanj91
 *
 * The GBC CPU
 */

public class CPU {
  private static final String FLAGS = "----CHNZ";
  
  /*public int af;
  public int bc;
  public int de;
  public int hl;*/
  
  public int A;
  public int B;
  public int C;
  public int D;
  public int E;
  public int H;
  public int L;
  
  public int sp;
  private int pc;
  
  public boolean flagZ;
  public boolean flagN;
  public boolean flagH;
  public boolean flagC;
  
  public CPU() {
    A = 0x11;
    B = 0x00;
    C = 0x13;
    D = 0x00;
    E = 0xd8;
    H = 0x01;
    L = 0x4d;
    sp = 0xfffe;
    pc = 0x0100;
    
    flagZ = true;
    flagN = false;
    flagH = true;
    flagC = true;
  }
  
  /* PC methods */
  public void incPC(int val) {
    pc += val;
  }
  
  public int getPC() {
    return pc;
  }
  
  public void changePC(int newPC) {
    pc = newPC;
  }
 
  /*
  // Flag methods
  public void clearFlags(String flags) {
    for (int i = 0; i < flags.length(); i++) {
      af &= ~(1 << FLAGS.indexOf(flags.charAt(i)));
    }
  }
  
  public void setFlags(String flags) {
    for (int i = 0; i < flags.length(); i++) {
      af |= 1 << FLAGS.indexOf(flags.charAt(i));
    }
  }
  
  public boolean testFlag(String flag) {
    if (flag.length() != 1) {
      throw new IllegalArgumentException("Can only test 1 flag at a time!");
    }
    return (1 << FLAGS.indexOf(flag.charAt(0)) & af) != 0;
  }*/
  
  /*
  Bit  Name  Set Clr  Expl.
  7    zf    Z   NZ   Zero Flag
  6    n     -   -    Add/Sub-Flag (BCD)
  5    h     -   -    Half Carry Flag (BCD)
  4    cy    C   NC   Carry Flag
  3-0  -     -   -    Not used (always zero)
  */
}
