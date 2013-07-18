/*
 * @author Milan Justel
 * 
 * Functions for each instruction
 */

/*
 * Register numbers
 * A    111   7
 * B    0     0
 * C    1     1
 * D    10    2
 * E    11    3
 * H    100   4
 * L    101   5
 * (AF) 1000  8
 * (BC) 1001  9
 * (DE) 1010  10
 * (HL) 1011  11
 */

public class ISA {
  /*public static void checkOp(byte opcode, CPU cpu) {
    if (opcode == 0) {
      ISA.nop(cpu);
    } else if (opcode == (byte) 0xc3) {
      short addr = (short) ((short) ((romData[cpu.pc + 2] & 0xff) << 8) | (romData[cpu.pc + 1] & 0xff));
      ISA.jmp(cpu, addr);
    } else if (opcode == (byte) 0xfe) {
      byte val = (byte) romData[cpu.pc];
      ISA.cp(cpu, val);
    }
  }*/
  
  /**
   * 06 LD B,n  n -> B
   * 0e LD C,n
   * 16 LD D,n
   * 1e LD E,n
   * 26 LD H,n
   * 2e LD L,n
   */
  public static void ldRn(CPU cpu, int regNum, byte val) {
    if (regNum == 0) {
      cpu.bc &= 0xff00;
      cpu.bc |= (val << 8);
    } else if (regNum == 1) {
      cpu.bc &= 0x00ff;
      cpu.bc |= val;
    } else if (regNum == 2) {
      cpu.de &= 0xff00;
      cpu.de |= (val << 8);
    } else if (regNum == 3) {
      cpu.de &= 0x00ff;
      cpu.de |= val;
    } else if (regNum == 4) {
      cpu.hl &= 0xff00;
      cpu.hl |= (val << 8);
    } else if (regNum == 5) {
      cpu.hl &= 0x0ff;
      cpu.hl |= val;
    } else {
      throw new IllegalArgumentException("bad regNum to ldRn");
    }
  }
  
  /**
   * 7f LD A,A
   * 
   */
  public static void ldRR(CPU cpu, int firstReg, int secondReg) {
    
  }
  
  /**
   * 00 NOP
   */
  public static void nop(CPU cpu) {
    cpu.pc++;
  }
  
  /**
   * c3 JMP a16
   */
  public static void jmp(CPU cpu, short addr) {
    cpu.pc = addr & 0xffff;
  }
  
  /**
   * fe CP d8
   */
  public static void cp(CPU cpu, byte val) {
    int res = (cpu.af >>> 8) - val;
    
    cpu.af |= 0x40;
    if (res == 0) {
      cpu.af |= 0x80;
    } else if (res < 0) {
      cpu.af |= 0x10;
    }
    
    if (((cpu.af >>> 8) & 0xf) < (val & 0xf)) {
      cpu.af |= 0x20;
    }
    
    cpu.pc += 2;
  }

  
}
