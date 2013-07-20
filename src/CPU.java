/**
 * 
 * @author milanj91
 *
 * The GBC CPU
 */

public class CPU {
  private static final String FLAGS = "----CHNZ";
  
  public int af;
  public int bc;
  public int de;
  public int hl;
  public int sp;
  public int pc;
  
  public CPU() {
    af = 0x11b0;
    bc = 0x0013;
    de = 0x00d8;
    hl = 0x014d;
    sp = 0xfffe;
    pc = 0x0100;
  }
  
  /* Flag methods */
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
  }
  
  /*
  Bit  Name  Set Clr  Expl.
  7    zf    Z   NZ   Zero Flag
  6    n     -   -    Add/Sub-Flag (BCD)
  5    h     -   -    Half Carry Flag (BCD)
  4    cy    C   NC   Carry Flag
  3-0  -     -   -    Not used (always zero)
  */
}
