/*
 * @author Milan Justel
 */

public class CPU {
  public short af;
  public short bc;
  public short de;
  public short hl;
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
  
  public void printRegs() {
    System.out.println("PC: " + String.format("%4s", Integer.toHexString(pc & 0xFFFF)).replace(' ', '0'));
    System.out.println("SP: " + String.format("%4s", Integer.toHexString(sp & 0xFFFF)).replace(' ', '0'));
    System.out.println("AF: " + String.format("%4s", Integer.toHexString(af & 0xFFFF)).replace(' ', '0'));
    System.out.println("BC: " + String.format("%4s", Integer.toHexString(bc & 0xFFFF)).replace(' ', '0'));
    System.out.println("DE: " + String.format("%4s", Integer.toHexString(de & 0xFFFF)).replace(' ', '0'));
    System.out.println("HL: " + String.format("%4s", Integer.toHexString(hl & 0xFFFF)).replace(' ', '0'));
  }
}

/*
 16bit Hi   Lo   Name/Function
 AF    A    -    Accumulator & Flags
 BC    B    C    BC
 DE    D    E    DE
 HL    H    L    HL
 SP    -    -    Stack Pointer
 PC    -    -    Program Counter/Pointer
*/

/*
 * A 111
 * B 000
 * C 001
 * D 010
 * E 011
 * H 100
 * L 101
 */

/*
 Bit  Name  Set Clr  Expl.
 7    zf    Z   NZ   Zero Flag
 6    n     -   -    Add/Sub-Flag (BCD)
 5    h     -   -    Half Carry Flag (BCD)
 4    cy    C   NC   Carry Flag
 3-0  -     -   -    Not used (always zero)
 */
