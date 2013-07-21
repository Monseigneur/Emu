/**
 * 
 * @author milanj91
 *
 */

public class Instruction {
  public static final int[] INSTR_LEN = {
    1, 3, 1, 1, 1, 1, 2, 1, 3, 1, 1, 1, 1, 1, 2, 1,
    2, 3, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1,
    2, 3, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1,
    2, 3, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
    1, 1, 3, 3, 3, 1, 2, 1, 1, 1, 3, 1, 3, 3, 2, 1,
    1, 1, 2, 0, 3, 1, 2, 1, 1, 1, 3, 0, 3, 0, 2, 1,
    2, 1, 2, 0, 0, 1, 2, 1, 2, 1, 3, 0, 0, 0, 2, 1,
    2, 1, 2, 1, 0, 1, 2, 1, 2, 1, 3, 1, 0, 0, 2, 1
  };
  
  public static final int CB_INSTR_LEN = 2;
  
  // 00 NOP [- - - -]
  public static void nop(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x00]);
  }
  
  // 01 LD BC,d16 [- - - -]
  public static void ldBCd16(CPU cpu, Memory mem) {
    cpu.C = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.B = mem.readMem(cpu.getPC() + 2) & 0xff;
    cpu.incPC(INSTR_LEN[0x01]);
  }
  
  // 02 LD (BC),A [- - - -]
  public static void ldaBCA(CPU cpu, Memory mem) {
    int addr = (cpu.B << 8) | cpu.C;
    mem.writeMem(cpu.A & 0xff, addr);
    cpu.incPC(INSTR_LEN[0x02]);
  }
  
  // 03 INC BC [- - - -]
  public static void incBC(CPU cpu) {
    int val = ((cpu.B << 8) | cpu.C) + 1;
    cpu.B = (val >> 8) & 0xff;
    cpu.C = val & 0xff;
    cpu.incPC(INSTR_LEN[0x03]);
  }
  
  // 04 INC B
  public static void incB(CPU cpu) {
    cpu.B = (cpu.B + 1) & 0xff;
    cpu.flagZ = (cpu.B == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.B & 0x0f) == 0);
    cpu.incPC(INSTR_LEN[0x04]);
  }
  
  // 05 DEC B
  public static void decB(CPU cpu) {
    cpu.B = (cpu.B - 1) & 0xff;
    cpu.flagZ = (cpu.B == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.B & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x05]);
  }
  
  // 06 LD B, d8
  public static void ldBd8(CPU cpu, Memory mem) {
    cpu.B = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x06]);
  }
  
  // 07 RLCA
  public static void rclA(CPU cpu) {
    cpu.flagC = cpu.A > 0x7f;
    cpu.A = ((cpu.A << 1) & 0xff) | (cpu.A >> 7);
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.incPC(INSTR_LEN[0x07]);
  }
  
  // 08 LD (a16),SP
  public static void lda16SP() {
    throw new UnsupportedOperationException();
  }
  
  // 09 ADD HL,BC
  public static void addHLBC(CPU cpu) {
    int hl = (cpu.H << 8) | cpu.L;
    int bc = (cpu.B << 8) | cpu.C;
    int res = hl + bc;
    cpu.H = (res >> 8) & 0xff;
    cpu.L = res & 0xff;
    cpu.flagN = false;
    cpu.flagH = (hl & 0x0fff) + (bc & 0x0fff) > 0x0fff; 
    cpu.flagC = res > 0xffff;
    cpu.incPC(INSTR_LEN[0x09]);
  }
  
  // 0A LD A,(BC)
  public static void ldAaBC(CPU cpu, Memory mem) {
    int addr = (cpu.B << 8) | cpu.C;
    cpu.A = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x0a]);
  }
  
  // 0B DEC BC
  public static void decBC(CPU cpu) {
    int val = ((cpu.B << 8) | cpu.C) - 1;
    cpu.B = (val >> 8) & 0xff;
    cpu.C = val & 0xff;
    cpu.incPC(INSTR_LEN[0x0b]);
  }
  
  // 0C INC C [Z 0 H -]
  public static void incC(CPU cpu) {
    cpu.C = (cpu.C + 1) & 0xff;
    cpu.flagZ = (cpu.C == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.C & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x0c]);
  }
  
  // 0D DEC C [Z 1 H -]
  public static void decC(CPU cpu) {
    cpu.C = (cpu.C - 1) & 0xff;
    cpu.flagZ = (cpu.C == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.C & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x0d]);
  }
  
  // 0E LD C,d8 [- - - -]
  public static void ldCd8(CPU cpu, Memory mem) {
    cpu.C = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x0e]);
  }
  
  // 0F RRCA [0 0 0 C]
  public static void rrcA(CPU cpu) {
    cpu.flagC = ((cpu.A & 0x01) == 0x1);
    cpu.A = (cpu.A >>> 1) | ((cpu.A & 0x01) << 7);
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.incPC(INSTR_LEN[0x0f]);
  }
  
  // 10 STOP [- - - -]
  public static void stop() {
    throw new UnsupportedOperationException();
  }
  
  // 11 LD DE,d16 [- - - -]
  public static void ldDEd16(CPU cpu, Memory mem) {
    cpu.E = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.D = mem.readMem(cpu.getPC() + 2) & 0xff;
    cpu.incPC(INSTR_LEN[0x11]);
  }
  
  // 12 LD (DE),A [- - - -]
  public static void ldaDEA() {
    throw new UnsupportedOperationException();
  }
  
  // 13 INC DE [- - - -]
  public static void incDE(CPU cpu) {
    int val = ((cpu.D << 8) | cpu.E) + 1;
    cpu.D = (val >> 8) & 0x0f;
    cpu.E = val & 0x0f;
    cpu.incPC(INSTR_LEN[0x13]);
  }
  
  // 14 INC D [Z 0 H -]
  public static void incD(CPU cpu) {
    cpu.D = (cpu.D + 1) & 0xff;
    cpu.flagZ = (cpu.D == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.D & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x14]);
  }
  
  // 15 DEC D [Z 1 H -]
  public static void decD(CPU cpu) {
    cpu.D = (cpu.D - 1) & 0xff;
    cpu.flagZ = (cpu.D == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.D & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x15]);
  }
  
  // 16 LD D,d8 [- - - -]
  public static void ldDd8(CPU cpu, Memory mem) {
    cpu.D = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x16]);
  }
  
  // 17 RLA [0 0 0 C]
  public static void rlA(CPU cpu) {
    int bit = cpu.flagC ? 1 : 0;
    cpu.flagC = cpu.A > 0x7f;
    cpu.A = ((cpu.A << 1) | bit) & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.incPC(INSTR_LEN[0x17]);
  }
  
  // 18 JR r8 [- - - -]
  public static void jrR8(CPU cpu, Memory mem) {
    int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
    if ((offset & 0x80) != 0) {
      offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
    }
    cpu.incPC(offset);
  }
  
  // 19 ADD HL,DE [- 0 H C]
  public static void addHLDE(CPU cpu) {
    int hl = (cpu.H << 8) | cpu.L;
    int de = (cpu.D << 8) | cpu.E;
    int res = hl + de;
    cpu.H = (res >> 8) & 0xff;
    cpu.L = res & 0xff;
    cpu.flagN = false;
    cpu.flagH = (hl & 0x0fff) + (de & 0x0fff) > 0x0fff; 
    cpu.flagC = res > 0xffff;
    cpu.incPC(INSTR_LEN[0x19]);
  }
  
  // 1A LD A,(DE) [- - - -]
  public static void ldAaDE(CPU cpu, Memory mem) {
    int addr = (cpu.D << 8) | cpu.E;
    cpu.A = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x1a]);
  }
  
  // 1B DEC DE [- - - -]
  public static void decDE(CPU cpu) {
    int val = ((cpu.D << 8) | cpu.E) - 1;
    cpu.D = (val >> 8) & 0xff;
    cpu.E = val & 0xff;
    cpu.incPC(INSTR_LEN[0x1b]);
  }
  
  // 1C INC E [Z 0 H -]
  public static void incE(CPU cpu) {
    cpu.E = (cpu.E + 1) & 0xff;
    cpu.flagZ = (cpu.E == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.E & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x1c]);
  }
  
  // 1D DEC E [Z 1 H -]
  public static void decE(CPU cpu) {
    cpu.E = (cpu.E - 1) & 0xff;
    cpu.flagZ = (cpu.E == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.E & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x1d]);
  }
  
  // 1E LD E,d8 [- - - -]
  public static void ldEd8(CPU cpu, Memory mem) {
    cpu.E = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x1e]);
  }
  
  // 1F RRA [0 0 0 C]
  public static void rrA(CPU cpu) {
    int bit = cpu.flagC ? 0x80 : 0;
    cpu.flagC = ((cpu.A & 0x01) == 0x1);
    cpu.A = ((cpu.A >>> 1) | bit) & 0xff;
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.incPC(INSTR_LEN[0x1f]);
  }
  
  // 20 JR NZ,r8 [- - - -]
  public static void jrNZr8(CPU cpu, Memory mem) {
    if (!cpu.flagZ) {
      int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
      if ((offset & 0x80) != 0) {
        offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
      }
      cpu.incPC(offset);
    } else {
      cpu.incPC(INSTR_LEN[0x20]);
    }
  }
  
  // 21 LD HL,d16 [- - - -]
  public static void ldHLd16(CPU cpu, Memory mem) {
    cpu.L = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.H = mem.readMem(cpu.getPC() + 2) & 0xff;
    cpu.incPC(INSTR_LEN[0x21]);
  }
  
  // 22 LD (HL+),A [- - - -]
  public static void ldaHLIA() {
    throw new UnsupportedOperationException();
  }
  
  // 23 INC HL [- - - -]
  public static void incHL(CPU cpu) {
    int val = ((cpu.H << 8) | cpu.L) + 1;
    cpu.H = (val >> 8) & 0xff;
    cpu.L = val & 0xff;
    cpu.incPC(INSTR_LEN[0x23]);
  }
  
  // 24 INC H [Z 0 H -]
  public static void incH(CPU cpu) {
    cpu.H = (cpu.H + 1) & 0xff;
    cpu.flagZ = (cpu.H == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.H & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x24]);
  }
  
  // 25 DEC H [Z 1 H -]
  public static void decH(CPU cpu) {
    cpu.H = (cpu.H - 1) & 0xff;
    cpu.flagZ = (cpu.H == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.H & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x25]);
  }
  
  // 26 LD H,d8 [- - - -]
  public static void ldHd8(CPU cpu, Memory mem) {
    cpu.H = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x26]);
  }
  
  // 27 DAA [Z - 0 C]
  public static void daA() {
    throw new UnsupportedOperationException();
  }
  
  // 28 JR Z,r8 [- - - -]
  public static void jrZr8(CPU cpu, Memory mem) {
    if (cpu.flagZ) {
      int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
      if ((offset & 0x80) != 0) {
        offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
      }
      cpu.incPC(offset);
    } else {
      cpu.incPC(INSTR_LEN[0x28]);
    }
  }
  
  // 29 ADD HL,HL [- 0 H C]
  public static void addHLHL(CPU cpu) {
    int hl = (cpu.H << 8) | cpu.L;
    int res = hl + hl;
    cpu.H = (res >> 8) & 0xff;
    cpu.L = res & 0xff;
    cpu.flagN = false;
    cpu.flagH = (hl & 0x0fff) + (hl & 0x0fff) > 0x0fff; 
    cpu.flagC = res > 0xffff;
    cpu.incPC(INSTR_LEN[0x29]);
  }
  
  // 2A LD A,(HL+) [- - - -]
  public static void ldAaHLI() {
    throw new UnsupportedOperationException();
  }
  
  // 2B DEC HL [- - - -]
  public static void decHL(CPU cpu) {
    int val = ((cpu.H << 8) | cpu.L) - 1;
    cpu.H = (val >> 8) & 0xff;
    cpu.L = val & 0xff;
    cpu.incPC(INSTR_LEN[0x2b]);
  }
  
  // 2C INC L [Z 0 H -]
  public static void incL(CPU cpu) {
    cpu.L = (cpu.L + 1) & 0xff;
    cpu.flagZ = (cpu.L == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.L & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x2c]);
  }
  
  // 2D DEC L [Z 1 H -]
  public static void decL(CPU cpu) {
    cpu.L = (cpu.L - 1) & 0xff;
    cpu.flagZ = (cpu.L == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.L & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x2d]);
  }
  
  // 2E LD L,d8 [- - - -]
  public static void ldLd8(CPU cpu, Memory mem) {
    cpu.L = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x2e]);
  }
  
  // 2F CPL [- 1 1 -]
  public static void cpl(CPU cpu) {
    cpu.A = ~cpu.A & 0xff;
    cpu.flagN = true;
    cpu.flagH = true;
    cpu.incPC(INSTR_LEN[0x2f]);
  }
  
  // 30 JR NC,r8 [- - - -]
  public static void jrNCr8(CPU cpu, Memory mem) {
    if (!cpu.flagC) {
      int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
      if ((offset & 0x80) != 0) {
        offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
      }
      cpu.incPC(offset);
    } else {
      cpu.incPC(INSTR_LEN[0x30]);
    }
  }
  
  // 31 LD SP,d16 [- - - -]
  public static void ldSPd16(CPU cpu, Memory mem) {
    cpu.sp = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
    cpu.incPC(INSTR_LEN[0x31]); // TODO
  }
  
  // 32 LD (HL-),A [- - - -]
  public static void ldaHLDA() {
    throw new UnsupportedOperationException();
  }
  
  // 33 INC SP [- - - -]
  public static void incSP(CPU cpu) {
    cpu.sp = (cpu.sp + 1) & 0xffff;
    cpu.incPC(INSTR_LEN[0x33]);
  }
  
  // 34 INC (HL) [Z 0 H -]
  public static void incaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 35 DEC (HL) [Z 1 H -]
  public static void decaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 36 LD (HL),d8 [- - - -]
  public static void ldaHLd8() {
    throw new UnsupportedOperationException();
  }
  
  // 37 SCF [- 0 0 1]
  public static void scf(CPU cpu) {
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = true;
    cpu.incPC(INSTR_LEN[0x37]);
  }
  
  // 38 JR C,r8 [- - - -]
  public static void jrCr8(CPU cpu, Memory mem) {
    if (cpu.flagC) {
      int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
      if ((offset & 0x80) != 0) {
        offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
      }
      cpu.incPC(offset);
    } else {
      cpu.incPC(INSTR_LEN[0x30]);
    }
  }
  
  // 39 ADD HL,SP [- 0 H C]
  public static void addHLSP(CPU cpu) {
    int hl = (cpu.H << 8) | cpu.L;
    int res = hl + cpu.sp;
    cpu.H = (res >> 8) & 0xff;
    cpu.L = res & 0xff;
    cpu.flagN = false;
    cpu.flagH = (hl & 0x0fff) + (cpu.sp & 0x0fff) > 0x0fff; 
    cpu.flagC = res > 0xffff;
    cpu.incPC(INSTR_LEN[0x39]);
  }
  
  // 3A LD A,(HL-) [- - - -]
  public static void ldAaHLD() {
    throw new UnsupportedOperationException();
  }
  
  // 3B DEC SP [- - - -]
  public static void decSP(CPU cpu) {
    cpu.sp = (cpu.sp - 1) & 0xffff;
    cpu.incPC(INSTR_LEN[0x3b]);
  }
  
  // 3C INC A [Z 0 H -]
  public static void incA(CPU cpu) {
    cpu.A = (cpu.A + 1) & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.A & 0xff) == 0);
    cpu.incPC(INSTR_LEN[0x3c]);
  }
  
  // 3D DEC A [Z 1 H -]
  public static void decA(CPU cpu) {
    cpu.A = (cpu.A - 1) & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.A & 0x0f) == 0x0f);
    cpu.incPC(INSTR_LEN[0x3d]);
  }
  
  // 3E LD A,d8 [- - - -]
  public static void ldAd8(CPU cpu, Memory mem) {
    cpu.A = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.incPC(INSTR_LEN[0x3e]);
  }
  
  // 3F CCF [- 0 0 C]
  public static void ccf(CPU cpu) {
    cpu.flagC = !cpu.flagC;
    cpu.incPC(INSTR_LEN[0x3f]);
  }
  
  // 40 LD B,B [- - - -]
  public static void ldBB(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x40]);
  }
  
  // 41 LD B,C [- - - -]
  public static void ldBC(CPU cpu) {
    cpu.B = cpu.C;
    cpu.incPC(INSTR_LEN[0x41]);
  }  
  
  // 42 LD B,D [- - - -]
  public static void ldBD(CPU cpu) {
    cpu.B = cpu.D;
    cpu.incPC(INSTR_LEN[0x42]);
  }
  
  // 43 LD B,E [- - - -]
  public static void ldBE(CPU cpu) {
    cpu.B = cpu.E;
    cpu.incPC(INSTR_LEN[0x43]);
  }
  
  // 44 LD B,H [- - - -]
  public static void ldBH(CPU cpu) {
    cpu.B = cpu.H;
    cpu.incPC(INSTR_LEN[0x44]);
  }
  
  // 45 LD B,L [- - - -]
  public static void ldBL(CPU cpu) {
    cpu.B = cpu.L;
    cpu.incPC(INSTR_LEN[0x45]);
  }
  
  // 46 LD B,(HL) [- - - -]
  public static void ldBaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.B = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x46]);
  }
  
  // 47 LD B,A [- - - -]
  public static void ldBA(CPU cpu) {
    cpu.B = cpu.A;
    cpu.incPC(INSTR_LEN[0x47]);
  }
  
  // 48 LD C,B [- - - -]
  public static void ldCB(CPU cpu) {
    cpu.C = cpu.B;
    cpu.incPC(INSTR_LEN[0x48]);
  }
  
  // 49 LD C,C [- - - -]
  public static void ldCC(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x49]);
  }
  
  // 4A LD C,D [- - - -]
  public static void ldCD(CPU cpu) {
    cpu.C = cpu.D;
    cpu.incPC(INSTR_LEN[0x4a]);
  }
  
  // 4B LD C,E [- - - -]
  public static void ldCE(CPU cpu) {
    cpu.C = cpu.E;
    cpu.incPC(INSTR_LEN[0x4b]);
  }
  
  // 4C LD C,H [- - - -]
  public static void ldCH(CPU cpu) {
    cpu.C = cpu.H;
    cpu.incPC(INSTR_LEN[0x4c]);
  }
  
  // 4D LD C,L [- - - -]
  public static void ldCL(CPU cpu) {
    cpu.C = cpu.L;
    cpu.incPC(INSTR_LEN[0x4d]);
  }
  
  // 4E LD C,(HL) [- - - -]
  public static void ldCaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.C = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x4e]);
  }
  
  // 4F LD C,A [- - - -]
  public static void ldCA(CPU cpu) {
    cpu.C = cpu.A;
    cpu.incPC(INSTR_LEN[0x4f]);
  }
  
  // 50 LD D,B [- - - -]
  public static void ldDB(CPU cpu) {
    cpu.D = cpu.B;
    cpu.incPC(INSTR_LEN[0x50]);
  }

  // 51 LD D,C [- - - -]
  public static void ldDC(CPU cpu) {
    cpu.D = cpu.C;
    cpu.incPC(INSTR_LEN[0x51]);
  }
  
  // 52 LD D,D [- - - -]
  public static void ldDD(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x52]);
  }
  
  // 53 LD D,E [- - - -]
  public static void ldDE(CPU cpu) {
    cpu.D = cpu.E;
    cpu.incPC(INSTR_LEN[0x53]);
  }
  
  // 54 LD D,H [- - - -]
  public static void ldDH(CPU cpu) {
    cpu.D = cpu.H;
    cpu.incPC(INSTR_LEN[0x54]);
  }
  
  // 55 LD D,L [- - - -]
  public static void ldDL(CPU cpu) {
    cpu.D = cpu.L;
    cpu.incPC(INSTR_LEN[0x55]);
  }
  
  // 56 LD D,(HL) [- - - -]
  public static void ldDaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.D = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x56]);
  }
  
  // 57 LD D,A [- - - -]
  public static void ldDA(CPU cpu) {
    cpu.D = cpu.A;
    cpu.incPC(INSTR_LEN[0x57]);
  }
  
  // 58 LD E,B [- - - -]
  public static void ldEB(CPU cpu) {
    cpu.E = cpu.B;
    cpu.incPC(INSTR_LEN[0x58]);
  }
  
  // 59 LD E,C [- - - -]
  public static void ldEC(CPU cpu) {
    cpu.E = cpu.C;
    cpu.incPC(INSTR_LEN[0x59]);
  }
  
  // 5A LD E,D [- - - -]
  public static void ldED(CPU cpu) {
    cpu.E = cpu.D;
    cpu.incPC(INSTR_LEN[0x5a]);
  }
  
  // 5B LD E,E [- - - -]
  public static void ldEE(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x5b]);
  }
  
  // 5C LD E,H [- - - -]
  public static void ldEH(CPU cpu) {
    cpu.E = cpu.H;
    cpu.incPC(INSTR_LEN[0x5c]);
  }
  
  // 5D LD E,L [- - - -]
  public static void ldEL(CPU cpu) {
    cpu.E = cpu.L;
    cpu.incPC(INSTR_LEN[0x5d]);
  }
  
  // 5E LD E,(HL) [- - - -]
  public static void ldEaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.E = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x5e]);
  }
  
  // 5F LD E,A [- - - -]
  public static void ldEA(CPU cpu) {
    cpu.E = cpu.A;
    cpu.incPC(INSTR_LEN[0x5f]);
  }
  
  // 60 LD H,B [- - - -]
  public static void ldHB(CPU cpu) {
    cpu.H = cpu.B;
    cpu.incPC(INSTR_LEN[0x60]);
  }
  
  // 61 LD H,C [- - - -]
  public static void ldHC(CPU cpu) {
    cpu.H = cpu.C;
    cpu.incPC(INSTR_LEN[0x61]);
  }
  
  // 62 LD H,D [- - - -]
  public static void ldHD(CPU cpu) {
    cpu.H = cpu.D;
    cpu.incPC(INSTR_LEN[0x62]);
  }

  // 63 LD H,E [- - - -]
  public static void ldHE(CPU cpu) {
    cpu.H = cpu.E;
    cpu.incPC(INSTR_LEN[0x63]);
  }
  
  // 64 LD H,H [- - - -]
  public static void ldHH(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x64]);
  }

  // 65 LD H,L [- - - -]
  public static void ldHL(CPU cpu) {
    cpu.H = cpu.L;
    cpu.incPC(INSTR_LEN[0x65]);
  }
  
  // 66 LD H,(HL) [- - - -]
  public static void ldHaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.H = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x66]);
  }
  
  // 67 LD H,A [- - - -]
  public static void ldHA(CPU cpu) {
    cpu.H = cpu.A;
    cpu.incPC(INSTR_LEN[0x67]);
  }

  // 68 LD L,B [- - - -]
  public static void ldLB(CPU cpu) {
    cpu.L = cpu.B;
    cpu.incPC(INSTR_LEN[0x68]);
  }

  // 69 LD L,C [- - - -]
  public static void ldLC(CPU cpu) {
    cpu.L = cpu.C;
    cpu.incPC(INSTR_LEN[0x69]);
  }
  
  // 6A LD L,D [- - - -]
  public static void ldLD(CPU cpu) {
    cpu.L = cpu.D;
    cpu.incPC(INSTR_LEN[0x6a]);
  }
  
  // 6B LD L,E [- - - -]
  public static void ldLE(CPU cpu) {
    cpu.L = cpu.E;
    cpu.incPC(INSTR_LEN[0x6b]);
  }
  
  // 6C LD L,H [- - - -]
  public static void ldLH(CPU cpu) {
    cpu.L = cpu.H;
    cpu.incPC(INSTR_LEN[0x6c]);
  }
  
  // 6D LD L,L [- - - -]
  public static void ldLL(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x6d]);
  }
  
  // 6E LD L,(HL) [- - - -]
  public static void ldLaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.L = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x6e]);
  }
  
  // 6F LD L,A [- - - -]
  public static void ldLA(CPU cpu) {
    cpu.L = cpu.A;
    cpu.incPC(INSTR_LEN[0x6f]);
  }
  
  // 70 LD (HL),B [- - - -]
  public static void ldaHLB(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 71 LD (HL),C [- - - -]
  public static void ldaHLC(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 72 LD (HL),D [- - - -]
  public static void ldaHLD(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 73 LD (HL),E [- - - -]
  public static void ldaHLE(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 74 LD (HL),H [- - - -]
  public static void ldaHLH(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 75 LD (HL),L [- - - -]
  public static void ldaHLL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 76 HALT [- - - -]
  public static void halt() {
    throw new UnsupportedOperationException();
  }
  
  // 77 LD (HL),A [- - - -]
  public static void ldaHLA(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 78 LD A,B [- - - -]
  public static void ldAB(CPU cpu) {
    cpu.A = cpu.B;
    cpu.incPC(INSTR_LEN[0x78]);
  }

  // 79 LD A,C [- - - -]
  public static void ldAC(CPU cpu) {
    cpu.A = cpu.C;
    cpu.incPC(INSTR_LEN[0x79]);
  }
  
  // 7A LD A,D [- - - -]
  public static void ldAD(CPU cpu) {
    cpu.A = cpu.D;
    cpu.incPC(INSTR_LEN[0x7a]);
  }
  
  // 7B LD A,E [- - - -]
  public static void ldAE(CPU cpu) {
    cpu.A = cpu.E;
    cpu.incPC(INSTR_LEN[0x7b]);
  }
  
  // 7C LD A,H [- - - -]
  public static void ldAH(CPU cpu) {
    cpu.A = cpu.H;
    cpu.incPC(INSTR_LEN[0x7c]);
  }
  
  // 7D LD A,L [- - - -]
  public static void ldAL(CPU cpu) {
    cpu.A = cpu.L;
    cpu.incPC(INSTR_LEN[0x7d]);
  }
  
  // 7E LD A,(HL) [- - - -]
  public static void ldAaHL(CPU cpu, Memory mem) {
    int addr = (cpu.H << 8) | cpu.L;
    cpu.A = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0x7e]);
  }
  
  // 7F LD A,A [- - - -]
  public static void ldAA(CPU cpu) {
    // Do nothing
    cpu.incPC(INSTR_LEN[0x7f]);
  }
  
  // 80 ADD A,B [Z 0 H C]
  public static void addAB(CPU cpu) {
    int res = cpu.A + cpu.B;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.B & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x80]);
  }
  
  // 81 ADD A,C [- - - -]
  public static void addAC(CPU cpu) {
    int res = cpu.A + cpu.C;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.C & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x80]);
  }
  
  // 82 ADD A,D [- - - -]
  public static void addAD(CPU cpu) {
    int res = cpu.A + cpu.D;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.D & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x82]);
  }
  
  // 83 ADD A,E [- - - -]
  public static void addAE(CPU cpu) {
    int res = cpu.A + cpu.E;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.E & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x83]);
  }
  
  // 84 ADD A,H [- - - -]
  public static void addAH(CPU cpu) {
    int res = cpu.A + cpu.H;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.H & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x84]);
  }
  
  // 85 ADD A,L [- - - -]
  public static void addAL(CPU cpu) {
    int res = cpu.A + cpu.L;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.L & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x85]);
  }
  
  // 86 ADD A,(HL) [- - - -]
  public static void addAaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    int res = cpu.A + val;
    cpu.flagH = (cpu.A & 0x0f) + (val & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x86]);
  }
  
  // 87 ADD A,A [- - - -]
  public static void addAA(CPU cpu) {
    int res = cpu.A + cpu.A;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.A & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x87]);
  }
  
  // 88 ADC A,B [Z 0 H C]
  public static void adcAB(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.B + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.B & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x88]);
  }
  
  // 89 ADC A,C [Z 0 H C]
  public static void adcAC(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.C + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.C & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x89]);
  }
  
  // 8A ADC A,D [Z 0 H C]
  public static void adcAD(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.D + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.D & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8a]);
  }
  
  // 8B ADC A,E [Z 0 H C]
  public static void adcAE(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.E + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.E & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8b]);
  }
  
  // 8C ADC A,H [Z 0 H C]
  public static void adcAH(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.H + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.H & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8c]);
  }
  
  // 8D ADC A,L [Z 0 H C]
  public static void adcAL(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.L + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.L & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8d]);
  }
  
  // 8E ADC A,(HL) [Z 0 H C]
  public static void adcAaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + val + carry;
    cpu.flagH = (cpu.A & 0x0f) + (val & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8e]);
  }
  
  // 8F ADC A,A [Z 0 H C]
  public static void adcAA(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + cpu.A + carry;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.A & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0x8f]);
  }
  
  // 90 SUB B [Z 1 H C]
  public static void subB(CPU cpu) {
    int res = cpu.A - cpu.B;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.B & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x90]);
  }
  
  // 91 SUB C [Z 1 H C]
  public static void subC(CPU cpu) {
    int res = cpu.A - cpu.C;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.C & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x91]);
  }
  
  // 92 SUB D [Z 1 H C]
  public static void subD(CPU cpu) {
    int res = cpu.A - cpu.D;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.D & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x92]);
  }
  
  // 93 SUB E [Z 1 H C]
  public static void subE(CPU cpu) {
    int res = cpu.A - cpu.E;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.E & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x93]);
  }
  
  // 94 SUB H [Z 1 H C]
  public static void subH(CPU cpu) {
    int res = cpu.A - cpu.H;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.H & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x94]);
  }
  
  // 95 SUB L [Z 1 H C]
  public static void subL(CPU cpu) {
    int res = cpu.A - cpu.L;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.L & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x95]);
  }
  
  // 96 SUB (HL) [Z 1 H C]
  public static void subaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    int res = cpu.A - val;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x96]);
  }
  
  // 97 SUB A [Z 1 H C]
  public static void subA(CPU cpu) {
    cpu.flagZ = true;
    cpu.flagH = false;
    cpu.flagN = true;
    cpu.flagC = false;
    cpu.A = 0;
    cpu.incPC(INSTR_LEN[0x97]);
  }
  
  // 98 SBC A,B [Z 1 H C]
  public static void sbcAB(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.B - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.B & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x98]);
  }
  
  // 99 SBC A,C [Z 1 H C]
  public static void sbcAC(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.C - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.C & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x99]);
  }
  
  // 9A SBC A,D [Z 1 H C]
  public static void sbcAD(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.D - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.D & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x9a]);
  }
  
  // 9B SBC A,E [Z 1 H C]
  public static void sbcAE(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.E - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.E & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x9b]);
  }
  
  // 9C SBC A,H [Z 1 H C]
  public static void sbcAH(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.H - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.H & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x9c]);
  }
  
  // 9D SBC A,L [Z 1 H C]
  public static void sbcAL(CPU cpu) {
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - cpu.L - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (cpu.L & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x9d]);
  }
  
  // 9E SBC A,(HL) [Z 1 H C]
  public static void sbcAaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - val - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (val & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0x9e]);
  }
  
  // 9F SBC A,A [Z 1 H C]
  public static void sbcAA(CPU cpu) {
    if (cpu.flagC) {
      cpu.A = 0xff;
      cpu.flagZ = false;
      cpu.flagC = true;
      cpu.flagH = true;
    } else {
      cpu.A = 0;
      cpu.flagZ = true;
      cpu.flagC = false;
      cpu.flagH = false;
    }
    cpu.flagN = true;
    cpu.incPC(INSTR_LEN[0x9f]);
  }
  
  // A0 AND B [Z 0 1 0]
  public static void andB(CPU cpu) {
    cpu.A &= cpu.B;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa0]);
  }
  
  // A1 AND C [Z 0 1 0]
  public static void andC(CPU cpu) {
    cpu.A &= cpu.C;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa1]);
  }
  
  // A2 AND D [Z 0 1 0]
  public static void andD(CPU cpu) {
    cpu.A &= cpu.D;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa2]);
  }
  
  // A3 AND E [Z 0 1 0]
  public static void andE(CPU cpu) {
    cpu.A &= cpu.E;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa3]);
  }
  
  // A4 AND H [Z 0 1 0]
  public static void andH(CPU cpu) {
    cpu.A &= cpu.H;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa4]);
  }
  
  // A5 AND L [Z 0 1 0]
  public static void andL(CPU cpu) {
    cpu.A &= cpu.L;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa5]);
  }
  
  // A6 AND (HL) [Z 0 1 0]
  public static void andaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    cpu.A &= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa6]);
  }
  
  // A7 AND A [Z 0 1 0]
  public static void andA(CPU cpu) {
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa7]);
  }
  
  // A8 XOR B [Z 0 0 0]
  public static void xorB(CPU cpu) {
    cpu.A ^= cpu.B;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa8]);
  }
  
  // A9 XOR C [Z 0 0 0]
  public static void xorC(CPU cpu) {
    cpu.A ^= cpu.C;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xa9]);
  }
  
  // AA XOR D [Z 0 0 0]
  public static void xorD(CPU cpu) {
    cpu.A ^= cpu.D;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xaa]);
  }
  
  // AB XOR E [Z 0 0 0]
  public static void xorE(CPU cpu) {
    cpu.A ^= cpu.E;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xab]);
  }
  
  // AC XOR H [Z 0 0 0]
  public static void xorH(CPU cpu) {
    cpu.A ^= cpu.H;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xac]);
  }
  
  // AD XOR L [Z 0 0 0]
  public static void xorL(CPU cpu) {
    cpu.A ^= cpu.L;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xad]);
  }
  
  // AE XOR (HL) [Z 0 0 0]
  public static void xoraHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    cpu.A ^= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xae]);
  }
  
  // AF XOR A [Z 0 0 0]
  public static void xorA(CPU cpu) {
    cpu.A = 0;
    cpu.flagZ = true;
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xaf]);
  }
  
  // B0 OR B [Z 0 0 0]
  public static void orB(CPU cpu) {
    cpu.A |= cpu.B;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb0]);
  }
  
  // B1 OR C [Z 0 0 0]
  public static void orC(CPU cpu) {
    cpu.A |= cpu.C;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb1]);
  }
  
  // B2 OR D [Z 0 0 0]
  public static void orD(CPU cpu) {
    cpu.A |= cpu.D;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb2]);
  }
  
  // B3 OR E [Z 0 0 0]
  public static void orE(CPU cpu) {
    cpu.A |= cpu.E;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb3]);
  }
  
  // B4 OR H [Z 0 0 0]
  public static void orH(CPU cpu) {
    cpu.A |= cpu.H;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb4]);
  }
  
  // B5 OR L [Z 0 0 0]
  public static void orL(CPU cpu) {
    cpu.A |= cpu.L;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb5]);
  }
  
  // B6 OR (HL) [Z 0 0 0]
  public static void oraHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    cpu.A |= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb5]);
    throw new UnsupportedOperationException();
  }
  
  // B7 OR A [Z 0 0 0]
  public static void orA(CPU cpu) {
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xb7]);
  }
  
  // B8 CP B [Z 1 H C]
  public static void cpB(CPU cpu) {
    int res = cpu.A - cpu.B;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.B & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xb8]);
  }
  
  // B9 CP C [Z 1 H C]
  public static void cpC(CPU cpu) {
    int res = cpu.A - cpu.C;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.C & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xb9]);
  }
  
  // BA CP D [Z 1 H C]
  public static void cpD(CPU cpu) {
    int res = cpu.A - cpu.D;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.D & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xba]);
  }
  
  // BB CP E [Z 1 H C]
  public static void cpE(CPU cpu) {
    int res = cpu.A - cpu.E;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.E & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xbb]);
  }
  
  // BC CP H [Z 1 H C]
  public static void cpH(CPU cpu) {
    int res = cpu.A - cpu.H;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.H & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xbc]);
  }
  
  // BD CP L [Z 1 H C]
  public static void cpL(CPU cpu) {
    int res = cpu.A - cpu.L;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.L & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xbd]);
  }
  
  // BE CP (HL) [Z 1 H C]
  public static void cpaHL(CPU cpu, Memory mem) {
    int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
    int res = cpu.A - val;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xbe]);
  }
  
  // BF CP A [Z 1 H C]
  public static void cpA(CPU cpu) {
    cpu.flagZ = true;
    cpu.flagH = false;
    cpu.flagN = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xbf]);
  }
  
  // C0 RET NZ [- - - -]
  public static void retNZ() {
    throw new UnsupportedOperationException();
  }
  
  // C1 POP BC [- - - -]
  public static void popBC(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // C2 JP NZ,a16 [- - - -]
  public static void jpNZa16(CPU cpu, Memory mem) {
    if (!cpu.flagZ) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.changePC(addr);
    } else {
      cpu.incPC(INSTR_LEN[0xc2]);
    }
  }
  
  // C3 JP a16 [- - - -]
  public static void jpa16(CPU cpu, Memory mem) {
    int newAddr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
    cpu.changePC(newAddr);
  }
  
  // C4 CALL NZ,a16 [- - - -]
  public static void callNZa16() {
    throw new UnsupportedOperationException();
  }
  
  // C5 PUSH BC [- - - -]
  public static void pushBC(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // C6 ADD A,d8 [Z 0 H C]
  public static void addAd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int res = cpu.A + val;
    cpu.flagH = (cpu.A & 0x0f) + (val & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0xc6]);
  }
  
  // C7 RST 00H [- - - -]
  public static void rst00() {
    throw new UnsupportedOperationException();
  }
  
  // C8 RET Z [- - - -]
  public static void retZ() {
    throw new UnsupportedOperationException();
  }
  
  // C9 RET [- - - -]
  public static void ret() {
    throw new UnsupportedOperationException();
  }
  
  // CA JP Z,a16 [- - - -]
  public static void jpZa16(CPU cpu, Memory mem) {
    if (cpu.flagZ) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.changePC(addr);
    } else {
      cpu.incPC(INSTR_LEN[0xca]);
    }
  }
  
  // CB PREFIX CB [- - - -]
  public static void preCB() {
    // Need to call the other block of commands :(
    throw new UnsupportedOperationException();
  }
  
  // CC CALL Z,a16 [- - - -]
  public static void callZa16() {
    throw new UnsupportedOperationException();
  }
  
  // CD CALL a16 [- - - -]
  public static void calla16() {
    throw new UnsupportedOperationException();
  }
  
  // CE ADC A,d8 [Z 0 H C]
  public static void adcAd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A + val + carry;
    cpu.flagH = (cpu.A & 0x0f) + (val & 0x0f) + carry > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0xce]);
  }
  
  // CF RST 08H [- - - -]
  public static void rst08() {
    throw new UnsupportedOperationException();
  }
  
  // D0 RET NC [- - - -]
  public static void retNC() {
    throw new UnsupportedOperationException();
  }
  
  // D1 POP DE [- - - -]
  public static void popDE(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // D2 JP NC,a16 [- - - -]
  public static void jpNCa16(CPU cpu, Memory mem) {
    if (!cpu.flagC) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.changePC(addr);
    } else {
      cpu.incPC(INSTR_LEN[0xd2]);
    }
  }
  
  // D3 DOES NOT EXIST
  
  // D4 CALL NC,a16 [- - - -]
  public static void callNCa16() {
    throw new UnsupportedOperationException();
  }
  
  // D5 PUSH DE [- - - -]
  public static void pushDE(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // D6 SUB d8 [Z 1 H C]
  public static void subd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int res = cpu.A - val;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0xd6]);
  }
  
  // D7 RST 10H [- - - -]
  public static void rst10() {
    throw new UnsupportedOperationException();
  }
  
  // D8 RET C [- - - -]
  public static void retC() {
    throw new UnsupportedOperationException();
  }
  
  // D9 RETI [- - - -]
  public static void reti() {
    throw new UnsupportedOperationException();
  }
  
  // DA JP C,a16 [- - - -]
  public static void jpCa16(CPU cpu, Memory mem) {
    if (cpu.flagC) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.changePC(addr);
    } else {
      cpu.incPC(INSTR_LEN[0xda]);
    }
  }
  
  // DB DOES NOT EXIST
  
  // DC CALL C,a16 [- - - -]
  public static void callCa16() {
    throw new UnsupportedOperationException();
  }
  
  // DD DOES NOT EXIST
  
  // DE SBC A,d8 [Z 1 H C]
  public static void sbcAd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int carry = cpu.flagC ? 1 : 0;
    int res = cpu.A - val - carry;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) - (val & 0x0f) - carry < 0;
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
    cpu.incPC(INSTR_LEN[0xde]);
  }
  
  // DF RST 18H [- - - -]
  public static void rst18() {
    throw new UnsupportedOperationException();
  }
  
  // E0 LDH (a8),A [- - - -]
  public static void ldha8A(CPU cpu, Memory mem) {
    int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | 0xff00;
    mem.writeMem(cpu.A & 0xff, addr);
    cpu.incPC(INSTR_LEN[0xe0]);
  }
  
  // E1 POP HL [- - - -]
  public static void popHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // E2 LD (C),A [- - - -]
  public static void ldaCA(CPU cpu, Memory mem) {
    int addr = 0xff00 | (cpu.C & 0xff);
    mem.writeMem(cpu.A & 0xff, addr);
    cpu.incPC(INSTR_LEN[0xe2]);
  }
  
  // E3 DOES NOT EXIST
  
  // E4 DOES NOT EXIST
  
  // E5 PUSH HL [- - - -]
  public static void pushHL() {
    throw new UnsupportedOperationException();
  }
  
  // E6 AND d8 [Z 0 1 0]
  public static void andd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.A &= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xe6]);
  }
  
  // E7 RST 20H [- - - -]
  public static void rst20() {
    throw new UnsupportedOperationException();
  }
  
  // E8 ADD SP,r8 [0 0 H C]
  public static void addSPr8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int res = cpu.sp + val;
    cpu.flagH = (cpu.sp & 0x0f) + (val & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
    cpu.incPC(INSTR_LEN[0xe8]);
  }
  
  // E9 JP (HL) [- - - -]
  public static void jpaHL(CPU cpu) {
    int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
    cpu.changePC(addr);
  }
  
  // EA LD (a16),A [- - - -]
  public static void lda16A(CPU cpu, Memory mem) {
    int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
    mem.writeMem(cpu.A & 0xff, addr);
    cpu.incPC(INSTR_LEN[0xea]);
  }
  
  // EB DOES NOT EXIST
  
  // EC DOES NOT EXIST
  
  // ED DOES NOT EXIST
  
  // EE XOR d8 [Z 0 0 0]
  public static void xord8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.A ^= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xee]);
  }
  
  // EF RST 28H [- - - -]
  public static void rst28() {
    throw new UnsupportedOperationException();
  }
  
  // F0 LDH A,(a8) [- - - -]
  public static void ldhAa8(CPU cpu, Memory mem) {
    int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | 0xff00;
    cpu.A = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0xf0]);
  }
  
  // F1 POP AF [Z N H C]
  public static void popAF() {
    throw new UnsupportedOperationException();
  }
  
  // F2 LD A,(C) [- - - -]
  public static void ldAaC(CPU cpu, Memory mem) {
    int addr = (cpu.C & 0xff) | 0xff00;
    cpu.A = mem.readMem(addr) & 0xff;
    cpu.incPC(INSTR_LEN[0xf2]);
  }
  
  // F3 DI [- - - -]
  public static void di() {
    throw new UnsupportedOperationException();
  }
  
  // F4 DOES NOT EXIST
  
  // F5 PUSH AF [- - - -]
  public static void pushAF() {
    throw new UnsupportedOperationException();
  }
  
  // F6 OR d8 [Z 0 0 0]
  public static void ord8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    cpu.A |= val;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
    cpu.incPC(INSTR_LEN[0xf6]);
  }
  
  // F7 RST 30H [- - - -]
  public static void rst30() {
    throw new UnsupportedOperationException();
  }
  
  // F8 LD HL,SP+r8 [0 0 H C]
  public static void ldHLSPr8(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // F9 LD SP,HL [- - - -]
  public static void ldSPHL(CPU cpu, Memory mem) {
    cpu.sp = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
    cpu.incPC(INSTR_LEN[0xf9]);
  }
  
  // FA LD A,(a16) [- - - -]
  public static void ldAa16(CPU cpu, Memory mem) {
    int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
    cpu.A = mem.readMem(addr) & 0xff;
  }
  
  // FB EI [- - - -]
  public static void ei() {
    throw new UnsupportedOperationException();
  }
  
  // FC DOES NOT EXIST
  
  // FD DOES NOT EXIST
  
  // FE CP d8 [Z 1 H C]
  public static void cpd8(CPU cpu, Memory mem) {
    int val = mem.readMem(cpu.getPC() + 1) & 0xff;
    int res = cpu.A - val;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.incPC(INSTR_LEN[0xfe]);
  }
  
  // FF RST 38H [- - - -]
  public static void rst38() {
    throw new UnsupportedOperationException();
  }
  
  
  
  
  
  
  public static void executeInstr(CPU cpu, Memory mem) {
    int opcode = mem.readMem(cpu.getPC()) & 0xff;
    System.out.print("PC: ");
    Util.printHex(cpu.getPC());
    System.out.print("OP: ");
    Util.printHex(opcode);
    System.out.println();
    
    
    if (opcode == 0x00) { Instruction.nop(cpu); }
    else if (opcode == 0x01) { Instruction.ldBCd16(cpu, mem); }
    else if (opcode == 0x02) { Instruction.ldaBCA(cpu, mem); }
    else if (opcode == 0x03) { Instruction.incBC(cpu); }
    else if (opcode == 0x04) { Instruction.incB(cpu); }
    else if (opcode == 0x05) { Instruction.decB(cpu); }
    else if (opcode == 0x06) { Instruction.ldBd8(cpu, mem); }
    else if (opcode == 0x07) { Instruction.rclA(cpu); }
    else if (opcode == 0x08) { Instruction.lda16SP(); }
    else if (opcode == 0x09) { Instruction.addHLBC(cpu); }
    else if (opcode == 0x0a) { Instruction.ldAaBC(cpu, mem); }
    else if (opcode == 0x0b) { Instruction.decBC(cpu); }
    else if (opcode == 0x0c) { Instruction.incC(cpu); }
    else if (opcode == 0x0d) { Instruction.decC(cpu); }
    else if (opcode == 0x0e) { Instruction.ldCd8(cpu, mem); }
    else if (opcode == 0x0f) { Instruction.rrcA(cpu); }
    
    else if (opcode == 0x10) { Instruction.stop(); }
    else if (opcode == 0x11) { Instruction.ldDEd16(cpu, mem); }
    else if (opcode == 0x12) { Instruction.ldaDEA(); }
    else if (opcode == 0x13) { Instruction.incDE(cpu); }
    else if (opcode == 0x14) { Instruction.incD(cpu); }
    else if (opcode == 0x15) { Instruction.decD(cpu); }
    else if (opcode == 0x16) { Instruction.ldDd8(cpu, mem); }
    else if (opcode == 0x17) { Instruction.rlA(cpu); }
    else if (opcode == 0x18) { Instruction.jrR8(cpu, mem); }
    else if (opcode == 0x19) { Instruction.addHLDE(cpu); }
    else if (opcode == 0x1a) { Instruction.ldAaDE(cpu, mem); }
    else if (opcode == 0x1b) { Instruction.decDE(cpu); }
    else if (opcode == 0x1c) { Instruction.incE(cpu); }
    else if (opcode == 0x1d) { Instruction.decE(cpu); }
    else if (opcode == 0x1e) { Instruction.ldEd8(cpu, mem); }
    else if (opcode == 0x1f) { Instruction.rrA(cpu); }
    
    else if (opcode == 0x20) { Instruction.jrNZr8(cpu, mem); }
    else if (opcode == 0x21) { Instruction.ldHLd16(cpu, mem); }
    else if (opcode == 0x22) { Instruction.ldaHLIA(); }
    else if (opcode == 0x23) { Instruction.incHL(cpu); }
    else if (opcode == 0x24) { Instruction.incH(cpu); }
    else if (opcode == 0x25) { Instruction.decH(cpu); }
    else if (opcode == 0x26) { Instruction.ldHd8(cpu, mem); }
    else if (opcode == 0x27) { Instruction.daA(); }
    else if (opcode == 0x28) { Instruction.jrZr8(cpu, mem); }
    else if (opcode == 0x29) { Instruction.addHLHL(cpu); }
    else if (opcode == 0x2a) { Instruction.ldAaHLI(); }
    else if (opcode == 0x2b) { Instruction.decHL(cpu); }
    else if (opcode == 0x2c) { Instruction.incL(cpu); }
    else if (opcode == 0x2d) { Instruction.decL(cpu); }
    else if (opcode == 0x2e) { Instruction.ldLd8(cpu, mem); }
    else if (opcode == 0x2f) { Instruction.cpl(cpu); }
    
    else if (opcode == 0x30) { Instruction.jrNCr8(cpu, mem); }
    else if (opcode == 0x31) { Instruction.ldSPd16(cpu, mem); }
    else if (opcode == 0x32) { Instruction.ldaHLDA(); }
    else if (opcode == 0x33) { Instruction.incSP(cpu); }
    else if (opcode == 0x34) { Instruction.incaHL(cpu); }
    else if (opcode == 0x35) { Instruction.decaHL(cpu); }
    else if (opcode == 0x36) { Instruction.ldaHLd8(); }
    else if (opcode == 0x37) { Instruction.scf(cpu); }
    else if (opcode == 0x38) { Instruction.jrCr8(cpu, mem); }
    else if (opcode == 0x39) { Instruction.addHLSP(cpu); }
    else if (opcode == 0x3a) { Instruction.ldAaHLD(); }
    else if (opcode == 0x3b) { Instruction.decSP(cpu); }
    else if (opcode == 0x3c) { Instruction.incA(cpu); }
    else if (opcode == 0x3d) { Instruction.decA(cpu); }
    else if (opcode == 0x3e) { Instruction.ldAd8(cpu, mem); }
    else if (opcode == 0x3f) { Instruction.ccf(cpu); }
    
    else if (opcode == 0x40) { Instruction.ldBB(cpu); }
    else if (opcode == 0x41) { Instruction.ldBC(cpu); }
    else if (opcode == 0x42) { Instruction.ldBD(cpu); }
    else if (opcode == 0x43) { Instruction.ldBE(cpu); }
    else if (opcode == 0x44) { Instruction.ldBH(cpu); }
    else if (opcode == 0x45) { Instruction.ldBL(cpu); }
    else if (opcode == 0x46) { Instruction.ldBaHL(cpu, mem); }
    else if (opcode == 0x47) { Instruction.ldBA(cpu); }
    else if (opcode == 0x48) { Instruction.ldCB(cpu); }
    else if (opcode == 0x49) { Instruction.ldCC(cpu); }
    else if (opcode == 0x4a) { Instruction.ldCD(cpu); }
    else if (opcode == 0x4b) { Instruction.ldCE(cpu); }
    else if (opcode == 0x4c) { Instruction.ldCH(cpu); }
    else if (opcode == 0x4d) { Instruction.ldCL(cpu); }
    else if (opcode == 0x4e) { Instruction.ldCaHL(cpu, mem); }
    else if (opcode == 0x4f) { Instruction.ldCA(cpu); }
    
    else if (opcode == 0x50) { Instruction.ldDB(cpu); }
    else if (opcode == 0x51) { Instruction.ldDC(cpu); }
    else if (opcode == 0x52) { Instruction.ldDD(cpu); }
    else if (opcode == 0x53) { Instruction.ldDE(cpu); }
    else if (opcode == 0x54) { Instruction.ldDH(cpu); }
    else if (opcode == 0x55) { Instruction.ldDL(cpu); }
    else if (opcode == 0x56) { Instruction.ldDaHL(cpu, mem); }
    else if (opcode == 0x57) { Instruction.ldDA(cpu); }
    else if (opcode == 0x58) { Instruction.ldEB(cpu); }
    else if (opcode == 0x59) { Instruction.ldEC(cpu); }
    else if (opcode == 0x5a) { Instruction.ldED(cpu); }
    else if (opcode == 0x5b) { Instruction.ldEE(cpu); }
    else if (opcode == 0x5c) { Instruction.ldEH(cpu); }
    else if (opcode == 0x5d) { Instruction.ldEL(cpu); }
    else if (opcode == 0x5e) { Instruction.ldEaHL(cpu, mem); }
    else if (opcode == 0x5f) { Instruction.ldEA(cpu); }
    
    else if (opcode == 0x60) { Instruction.ldHB(cpu); }
    else if (opcode == 0x61) { Instruction.ldHC(cpu); }
    else if (opcode == 0x62) { Instruction.ldHD(cpu); }
    else if (opcode == 0x63) { Instruction.ldHE(cpu); }
    else if (opcode == 0x64) { Instruction.ldHH(cpu); }
    else if (opcode == 0x65) { Instruction.ldHL(cpu); }
    else if (opcode == 0x66) { Instruction.ldHaHL(cpu, mem); }
    else if (opcode == 0x67) { Instruction.ldHA(cpu); }
    else if (opcode == 0x68) { Instruction.ldLB(cpu); }
    else if (opcode == 0x69) { Instruction.ldLC(cpu); }
    else if (opcode == 0x6a) { Instruction.ldLD(cpu); }
    else if (opcode == 0x6b) { Instruction.ldLE(cpu); }
    else if (opcode == 0x6c) { Instruction.ldLH(cpu); }
    else if (opcode == 0x6d) { Instruction.ldLL(cpu); }
    else if (opcode == 0x6e) { Instruction.ldLaHL(cpu, mem); }
    else if (opcode == 0x6f) { Instruction.ldLA(cpu); }
    
    else if (opcode == 0x70) { Instruction.ldaHLB(cpu); }
    else if (opcode == 0x71) { Instruction.ldaHLC(cpu); }
    else if (opcode == 0x72) { Instruction.ldaHLD(cpu); }
    else if (opcode == 0x73) { Instruction.ldaHLE(cpu); }
    else if (opcode == 0x74) { Instruction.ldaHLH(cpu); }
    else if (opcode == 0x75) { Instruction.ldaHLL(cpu); }
    else if (opcode == 0x76) { Instruction.halt(); }
    else if (opcode == 0x77) { Instruction.ldaHLA(cpu); }
    else if (opcode == 0x78) { Instruction.ldAB(cpu); }
    else if (opcode == 0x79) { Instruction.ldAC(cpu); }
    else if (opcode == 0x7a) { Instruction.ldAD(cpu); }
    else if (opcode == 0x7b) { Instruction.ldAE(cpu); }
    else if (opcode == 0x7c) { Instruction.ldAH(cpu); }
    else if (opcode == 0x7d) { Instruction.ldAL(cpu); }
    else if (opcode == 0x7e) { Instruction.ldAaHL(cpu, mem); }
    else if (opcode == 0x7f) { Instruction.ldAA(cpu); }
    
    else if (opcode == 0x80) { Instruction.addAB(cpu); }
    else if (opcode == 0x81) { Instruction.addAC(cpu); }
    else if (opcode == 0x82) { Instruction.addAD(cpu); }
    else if (opcode == 0x83) { Instruction.addAE(cpu); }
    else if (opcode == 0x84) { Instruction.addAH(cpu); }
    else if (opcode == 0x85) { Instruction.addAL(cpu); }
    else if (opcode == 0x86) { Instruction.addAaHL(cpu, mem); }
    else if (opcode == 0x87) { Instruction.addAA(cpu); }
    else if (opcode == 0x88) { Instruction.adcAB(cpu); }
    else if (opcode == 0x89) { Instruction.adcAC(cpu); }
    else if (opcode == 0x8a) { Instruction.adcAD(cpu); }
    else if (opcode == 0x8b) { Instruction.adcAE(cpu); }
    else if (opcode == 0x8c) { Instruction.adcAH(cpu); }
    else if (opcode == 0x8d) { Instruction.adcAL(cpu); }
    else if (opcode == 0x8e) { Instruction.adcAaHL(cpu, mem); }
    else if (opcode == 0x8f) { Instruction.adcAA(cpu); }
    
    else if (opcode == 0x90) { Instruction.subB(cpu); }
    else if (opcode == 0x91) { Instruction.subC(cpu); }
    else if (opcode == 0x92) { Instruction.subD(cpu); }
    else if (opcode == 0x93) { Instruction.subE(cpu); }
    else if (opcode == 0x94) { Instruction.subH(cpu); }
    else if (opcode == 0x95) { Instruction.subL(cpu); }
    else if (opcode == 0x96) { Instruction.subaHL(cpu, mem); }
    else if (opcode == 0x97) { Instruction.subA(cpu); }
    else if (opcode == 0x98) { Instruction.sbcAB(cpu); }
    else if (opcode == 0x99) { Instruction.sbcAC(cpu); }
    else if (opcode == 0x9a) { Instruction.sbcAD(cpu); }
    else if (opcode == 0x9b) { Instruction.sbcAE(cpu); }
    else if (opcode == 0x9c) { Instruction.sbcAH(cpu); }
    else if (opcode == 0x9d) { Instruction.sbcAL(cpu); }
    else if (opcode == 0x9e) { Instruction.sbcAaHL(cpu, mem); }
    else if (opcode == 0x9f) { Instruction.sbcAA(cpu); }
    
    else if (opcode == 0xa0) { Instruction.andB(cpu); }
    else if (opcode == 0xa1) { Instruction.andC(cpu); }
    else if (opcode == 0xa2) { Instruction.andD(cpu); }
    else if (opcode == 0xa3) { Instruction.andE(cpu); }
    else if (opcode == 0xa4) { Instruction.andH(cpu); }
    else if (opcode == 0xa5) { Instruction.andL(cpu); }
    else if (opcode == 0xa6) { Instruction.andaHL(cpu, mem); }
    else if (opcode == 0xa7) { Instruction.andA(cpu); }
    else if (opcode == 0xa8) { Instruction.xorB(cpu); }
    else if (opcode == 0xa9) { Instruction.xorC(cpu); }
    else if (opcode == 0xaa) { Instruction.xorD(cpu); }
    else if (opcode == 0xab) { Instruction.xorE(cpu); }
    else if (opcode == 0xac) { Instruction.xorH(cpu); }
    else if (opcode == 0xad) { Instruction.xorL(cpu); }
    else if (opcode == 0xae) { Instruction.xoraHL(cpu, mem); }
    else if (opcode == 0xaf) { Instruction.xorA(cpu); }
    
    else if (opcode == 0xb0) { Instruction.orB(cpu); }
    else if (opcode == 0xb1) { Instruction.orC(cpu); }
    else if (opcode == 0xb2) { Instruction.orD(cpu); }
    else if (opcode == 0xb3) { Instruction.orE(cpu); }
    else if (opcode == 0xb4) { Instruction.orH(cpu); }
    else if (opcode == 0xb5) { Instruction.orL(cpu); }
    else if (opcode == 0xb6) { Instruction.oraHL(cpu, mem); }
    else if (opcode == 0xb7) { Instruction.orA(cpu); }
    else if (opcode == 0xb8) { Instruction.cpB(cpu); }
    else if (opcode == 0xb9) { Instruction.cpC(cpu); }
    else if (opcode == 0xba) { Instruction.cpD(cpu); }
    else if (opcode == 0xbb) { Instruction.cpE(cpu); }
    else if (opcode == 0xbc) { Instruction.cpH(cpu); }
    else if (opcode == 0xbd) { Instruction.cpL(cpu); }
    else if (opcode == 0xbe) { Instruction.cpaHL(cpu, mem); }
    else if (opcode == 0xbf) { Instruction.cpA(cpu); }
    
    else if (opcode == 0xc0) { Instruction.retNZ(); }
    else if (opcode == 0xc1) { Instruction.popBC(cpu); }
    else if (opcode == 0xc2) { Instruction.jpNZa16(cpu, mem); }
    else if (opcode == 0xc3) { Instruction.jpa16(cpu, mem); }
    else if (opcode == 0xc4) { Instruction.callNZa16(); }
    else if (opcode == 0xc5) { Instruction.pushBC(cpu); }
    else if (opcode == 0xc6) { Instruction.addAd8(cpu, mem); }
    else if (opcode == 0xc7) { Instruction.rst00(); }
    else if (opcode == 0xc8) { Instruction.retZ(); }
    else if (opcode == 0xc9) { Instruction.ret(); }
    else if (opcode == 0xca) { Instruction.jpZa16(cpu, mem); }
    else if (opcode == 0xcb) { Instruction.preCB(); } // TODO
    else if (opcode == 0xcc) { Instruction.callZa16(); }
    else if (opcode == 0xcd) { Instruction.calla16(); }
    else if (opcode == 0xce) { Instruction.adcAd8(cpu, mem); }
    else if (opcode == 0xcf) { Instruction.rst08(); }
    
    else if (opcode == 0xd0) { Instruction.retNC(); }
    else if (opcode == 0xd1) { Instruction.popDE(cpu); }
    else if (opcode == 0xd2) { Instruction.jpNCa16(cpu, mem); }
    else if (opcode == 0xd3) { throw new UnsupportedOperationException("Invalid opcode 0xd3"); }
    else if (opcode == 0xd4) { Instruction.callNCa16(); }
    else if (opcode == 0xd5) { Instruction.pushDE(cpu); }
    else if (opcode == 0xd6) { Instruction.subd8(cpu, mem); }
    else if (opcode == 0xd7) { Instruction.rst10(); }
    else if (opcode == 0xd8) { Instruction.retC(); }
    else if (opcode == 0xd9) { Instruction.reti(); }
    else if (opcode == 0xda) { Instruction.jpCa16(cpu, mem); }
    else if (opcode == 0xdb) { throw new UnsupportedOperationException("Invalid opcode 0xdb"); }
    else if (opcode == 0xdc) { Instruction.callCa16(); }
    else if (opcode == 0xdd) { throw new UnsupportedOperationException("Invalid opcode 0xdd"); }
    else if (opcode == 0xde) { Instruction.sbcAd8(cpu, mem); }
    else if (opcode == 0xdf) { Instruction.rst18(); }
    
    else if (opcode == 0xe0) { Instruction.ldha8A(cpu, mem); }
    else if (opcode == 0xe1) { Instruction.popHL(cpu); }
    else if (opcode == 0xe2) { Instruction.ldaCA(cpu, mem); }
    else if (opcode == 0xe3) { throw new UnsupportedOperationException("Invalid opcode 0xe3"); }
    else if (opcode == 0xe4) { throw new UnsupportedOperationException("Invalid opcode 0xe4"); }
    else if (opcode == 0xe5) { Instruction.pushHL(); }
    else if (opcode == 0xe6) { Instruction.andd8(cpu, mem); }
    else if (opcode == 0xe7) { Instruction.rst20(); }
    else if (opcode == 0xe8) { Instruction.addSPr8(cpu, mem); }
    else if (opcode == 0xe9) { Instruction.jpaHL(cpu); }
    else if (opcode == 0xea) { Instruction.lda16A(cpu, mem); }
    else if (opcode == 0xeb) { throw new UnsupportedOperationException("Invalid opcode 0xeb"); }
    else if (opcode == 0xec) { throw new UnsupportedOperationException("Invalid opcode 0xec"); }
    else if (opcode == 0xed) { throw new UnsupportedOperationException("Invalid opcode 0xed"); }
    else if (opcode == 0xee) { Instruction.xord8(cpu, mem); }
    else if (opcode == 0xef) { Instruction.rst28(); }
    
    else if (opcode == 0xf0) { Instruction.ldhAa8(cpu, mem); }
    else if (opcode == 0xf1) { Instruction.popAF(); }
    else if (opcode == 0xf2) { Instruction.ldAaC(cpu, mem); }
    else if (opcode == 0xf3) { Instruction.di(); }
    else if (opcode == 0xf4) { throw new UnsupportedOperationException("Invalid opcode 0xf4"); }
    else if (opcode == 0xf5) { Instruction.pushAF(); }
    else if (opcode == 0xf6) { Instruction.ord8(cpu, mem); }
    else if (opcode == 0xf7) { Instruction.rst30(); }
    else if (opcode == 0xf8) { Instruction.ldHLSPr8(cpu); }
    else if (opcode == 0xf9) { Instruction.ldSPHL(cpu, mem); }
    else if (opcode == 0xfa) { Instruction.ldAa16(cpu, mem); }
    else if (opcode == 0xfb) { Instruction.ei(); }
    else if (opcode == 0xfc) { throw new UnsupportedOperationException("Invalid opcode 0xfc"); }
    else if (opcode == 0xfd) { throw new UnsupportedOperationException("Invalid opcode 0xfd"); }
    else if (opcode == 0xfe) { Instruction.cpd8(cpu, mem); }
    else if (opcode == 0xff) { Instruction.rst38(); }
    else { throw new UnsupportedOperationException("Invalid opcode"); }
  }
}
