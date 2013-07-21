/**
 * 
 * @author milanj91
 *
 */

public class ISA {
  private static class Instruction {
    private static final int[] INSTR_LEN = {
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
    
    // 00 NOP [- - - -]
    private static void nop(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x00]);
    }
    
    // 01 LD BC,d16 [- - - -]
    private static void ldBCd16(CPU cpu, Memory mem) {
      cpu.C = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.B = mem.readMem(cpu.getPC() + 2) & 0xff;
      cpu.incPC(INSTR_LEN[0x01]);
    }
    
    // 02 LD (BC),A [- - - -]
    private static void ldaBCA(CPU cpu, Memory mem) {
      int addr = (cpu.B << 8) | cpu.C;
      mem.writeMem(cpu.A & 0xff, addr);
      cpu.incPC(INSTR_LEN[0x02]);
    }
    
    // 03 INC BC [- - - -]
    private static void incBC(CPU cpu) {
      int val = ((cpu.B << 8) | cpu.C) + 1;
      cpu.B = (val >> 8) & 0xff;
      cpu.C = val & 0xff;
      cpu.incPC(INSTR_LEN[0x03]);
    }
    
    // 04 INC B
    private static void incB(CPU cpu) {
      cpu.B = (cpu.B + 1) & 0xff;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.B & 0x0f) == 0);
      cpu.incPC(INSTR_LEN[0x04]);
    }
    
    // 05 DEC B
    private static void decB(CPU cpu) {
      cpu.B = (cpu.B - 1) & 0xff;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.B & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x05]);
    }
    
    // 06 LD B, d8
    private static void ldBd8(CPU cpu, Memory mem) {
      cpu.B = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x06]);
    }
    
    // 07 RLCA
    private static void rclA(CPU cpu) {
      cpu.flagC = cpu.A > 0x7f;
      cpu.A = ((cpu.A << 1) & 0xff) | (cpu.A >> 7);
      cpu.flagZ = false;
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.incPC(INSTR_LEN[0x07]);
    }
    
    // 08 LD (a16),SP
    private static void lda16SP() {
      throw new UnsupportedOperationException();
    }
    
    // 09 ADD HL,BC
    private static void addHLBC(CPU cpu) {
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
    private static void ldAaBC(CPU cpu, Memory mem) {
      int addr = (cpu.B << 8) | cpu.C;
      cpu.A = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x0a]);
    }
    
    // 0B DEC BC
    private static void decBC(CPU cpu) {
      int val = ((cpu.B << 8) | cpu.C) - 1;
      cpu.B = (val >> 8) & 0xff;
      cpu.C = val & 0xff;
      cpu.incPC(INSTR_LEN[0x0b]);
    }
    
    // 0C INC C [Z 0 H -]
    private static void incC(CPU cpu) {
      cpu.C = (cpu.C + 1) & 0xff;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.C & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x0c]);
    }
    
    // 0D DEC C [Z 1 H -]
    private static void decC(CPU cpu) {
      cpu.C = (cpu.C - 1) & 0xff;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.C & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x0d]);
    }
    
    // 0E LD C,d8 [- - - -]
    private static void ldCd8(CPU cpu, Memory mem) {
      cpu.C = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x0e]);
    }
    
    // 0F RRCA [0 0 0 C]
    private static void rrcA(CPU cpu) {
      cpu.flagC = ((cpu.A & 0x01) == 0x1);
      cpu.A = (cpu.A >>> 1) | ((cpu.A & 0x01) << 7);
      cpu.flagZ = false;
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.incPC(INSTR_LEN[0x0f]);
    }
    
    // 10 STOP [- - - -]
    private static void stop() {
      throw new UnsupportedOperationException();
    }
    
    // 11 LD DE,d16 [- - - -]
    private static void ldDEd16(CPU cpu, Memory mem) {
      cpu.E = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.D = mem.readMem(cpu.getPC() + 2) & 0xff;
      cpu.incPC(INSTR_LEN[0x11]);
    }
    
    // 12 LD (DE),A [- - - -]
    private static void ldaDEA() {
      throw new UnsupportedOperationException();
    }
    
    // 13 INC DE [- - - -]
    private static void incDE(CPU cpu) {
      int val = ((cpu.D << 8) | cpu.E) + 1;
      cpu.D = (val >> 8) & 0x0f;
      cpu.E = val & 0x0f;
      cpu.incPC(INSTR_LEN[0x13]);
    }
    
    // 14 INC D [Z 0 H -]
    private static void incD(CPU cpu) {
      cpu.D = (cpu.D + 1) & 0xff;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.D & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x14]);
    }
    
    // 15 DEC D [Z 1 H -]
    private static void decD(CPU cpu) {
      cpu.D = (cpu.D - 1) & 0xff;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.D & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x15]);
    }
    
    // 16 LD D,d8 [- - - -]
    private static void ldDd8(CPU cpu, Memory mem) {
      cpu.D = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x16]);
    }
    
    // 17 RLA [0 0 0 C]
    private static void rlA(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.A > 0x7f;
      cpu.A = ((cpu.A << 1) | bit) & 0xff;
      cpu.flagZ = false;
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.incPC(INSTR_LEN[0x17]);
    }
    
    // 18 JR r8 [- - - -]
    private static void jrR8(CPU cpu, Memory mem) {
      int offset = mem.readMem(cpu.getPC() + 1) & 0xff;
      if ((offset & 0x80) != 0) {
        offset |= 0xffffff00;   // Should make the value negative so the JR works appropriately TODO
      }
      cpu.incPC(offset);
    }
    
    // 19 ADD HL,DE [- 0 H C]
    private static void addHLDE(CPU cpu) {
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
    private static void ldAaDE(CPU cpu, Memory mem) {
      int addr = (cpu.D << 8) | cpu.E;
      cpu.A = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x1a]);
    }
    
    // 1B DEC DE [- - - -]
    private static void decDE(CPU cpu) {
      int val = ((cpu.D << 8) | cpu.E) - 1;
      cpu.D = (val >> 8) & 0xff;
      cpu.E = val & 0xff;
      cpu.incPC(INSTR_LEN[0x1b]);
    }
    
    // 1C INC E [Z 0 H -]
    private static void incE(CPU cpu) {
      cpu.E = (cpu.E + 1) & 0xff;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.E & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x1c]);
    }
    
    // 1D DEC E [Z 1 H -]
    private static void decE(CPU cpu) {
      cpu.E = (cpu.E - 1) & 0xff;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.E & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x1d]);
    }
    
    // 1E LD E,d8 [- - - -]
    private static void ldEd8(CPU cpu, Memory mem) {
      cpu.E = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x1e]);
    }
    
    // 1F RRA [0 0 0 C]
    private static void rrA(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.A & 0x01) == 0x1);
      cpu.A = ((cpu.A >>> 1) | bit) & 0xff;
      cpu.flagZ = false;
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.incPC(INSTR_LEN[0x1f]);
    }
    
    // 20 JR NZ,r8 [- - - -]
    private static void jrNZr8(CPU cpu, Memory mem) {
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
    private static void ldHLd16(CPU cpu, Memory mem) {
      cpu.L = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.H = mem.readMem(cpu.getPC() + 2) & 0xff;
      cpu.incPC(INSTR_LEN[0x21]);
    }
    
    // 22 LD (HL+),A [- - - -]
    private static void ldaHLIA() {
      throw new UnsupportedOperationException();
    }
    
    // 23 INC HL [- - - -]
    private static void incHL(CPU cpu) {
      int val = ((cpu.H << 8) | cpu.L) + 1;
      cpu.H = (val >> 8) & 0xff;
      cpu.L = val & 0xff;
      cpu.incPC(INSTR_LEN[0x23]);
    }
    
    // 24 INC H [Z 0 H -]
    private static void incH(CPU cpu) {
      cpu.H = (cpu.H + 1) & 0xff;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.H & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x24]);
    }
    
    // 25 DEC H [Z 1 H -]
    private static void decH(CPU cpu) {
      cpu.H = (cpu.H - 1) & 0xff;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.H & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x25]);
    }
    
    // 26 LD H,d8 [- - - -]
    private static void ldHd8(CPU cpu, Memory mem) {
      cpu.H = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x26]);
    }
    
    // 27 DAA [Z - 0 C]
    private static void daA() {
      throw new UnsupportedOperationException();
    }
    
    // 28 JR Z,r8 [- - - -]
    private static void jrZr8(CPU cpu, Memory mem) {
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
    private static void addHLHL(CPU cpu) {
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
    private static void ldAaHLI() {
      throw new UnsupportedOperationException();
    }
    
    // 2B DEC HL [- - - -]
    private static void decHL(CPU cpu) {
      int val = ((cpu.H << 8) | cpu.L) - 1;
      cpu.H = (val >> 8) & 0xff;
      cpu.L = val & 0xff;
      cpu.incPC(INSTR_LEN[0x2b]);
    }
    
    // 2C INC L [Z 0 H -]
    private static void incL(CPU cpu) {
      cpu.L = (cpu.L + 1) & 0xff;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.L & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x2c]);
    }
    
    // 2D DEC L [Z 1 H -]
    private static void decL(CPU cpu) {
      cpu.L = (cpu.L - 1) & 0xff;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.L & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x2d]);
    }
    
    // 2E LD L,d8 [- - - -]
    private static void ldLd8(CPU cpu, Memory mem) {
      cpu.L = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x2e]);
    }
    
    // 2F CPL [- 1 1 -]
    private static void cpl(CPU cpu) {
      cpu.A = ~cpu.A & 0xff;
      cpu.flagN = true;
      cpu.flagH = true;
      cpu.incPC(INSTR_LEN[0x2f]);
    }
    
    // 30 JR NC,r8 [- - - -]
    private static void jrNCr8(CPU cpu, Memory mem) {
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
    private static void ldSPd16(CPU cpu, Memory mem) {
      cpu.sp = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.incPC(INSTR_LEN[0x31]); // TODO
    }
    
    // 32 LD (HL-),A [- - - -]
    private static void ldaHLDA() {
      throw new UnsupportedOperationException();
    }
    
    // 33 INC SP [- - - -]
    private static void incSP(CPU cpu) {
      cpu.sp = (cpu.sp + 1) & 0xffff;
      cpu.incPC(INSTR_LEN[0x33]);
    }
    
    // 34 INC (HL) [Z 0 H -]
    private static void incaHL(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 35 DEC (HL) [Z 1 H -]
    private static void decaHL(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 36 LD (HL),d8 [- - - -]
    private static void ldaHLd8() {
      throw new UnsupportedOperationException();
    }
    
    // 37 SCF [- 0 0 1]
    private static void scf(CPU cpu) {
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = true;
      cpu.incPC(INSTR_LEN[0x37]);
    }
    
    // 38 JR C,r8 [- - - -]
    private static void jrCr8(CPU cpu, Memory mem) {
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
    private static void addHLSP(CPU cpu) {
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
    private static void ldAaHLD() {
      throw new UnsupportedOperationException();
    }
    
    // 3B DEC SP [- - - -]
    private static void decSP(CPU cpu) {
      cpu.sp = (cpu.sp - 1) & 0xffff;
      cpu.incPC(INSTR_LEN[0x3b]);
    }
    
    // 3C INC A [Z 0 H -]
    private static void incA(CPU cpu) {
      cpu.A = (cpu.A + 1) & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = ((cpu.A & 0xff) == 0);
      cpu.incPC(INSTR_LEN[0x3c]);
    }
    
    // 3D DEC A [Z 1 H -]
    private static void decA(CPU cpu) {
      cpu.A = (cpu.A - 1) & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = true;
      cpu.flagH = ((cpu.A & 0x0f) == 0x0f);
      cpu.incPC(INSTR_LEN[0x3d]);
    }
    
    // 3E LD A,d8 [- - - -]
    private static void ldAd8(CPU cpu, Memory mem) {
      cpu.A = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.incPC(INSTR_LEN[0x3e]);
    }
    
    // 3F CCF [- 0 0 C]
    private static void ccf(CPU cpu) {
      cpu.flagC = !cpu.flagC;
      cpu.incPC(INSTR_LEN[0x3f]);
    }
    
    // 40 LD B,B [- - - -]
    private static void ldBB(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x40]);
    }
    
    // 41 LD B,C [- - - -]
    private static void ldBC(CPU cpu) {
      cpu.B = cpu.C;
      cpu.incPC(INSTR_LEN[0x41]);
    }  
    
    // 42 LD B,D [- - - -]
    private static void ldBD(CPU cpu) {
      cpu.B = cpu.D;
      cpu.incPC(INSTR_LEN[0x42]);
    }
    
    // 43 LD B,E [- - - -]
    private static void ldBE(CPU cpu) {
      cpu.B = cpu.E;
      cpu.incPC(INSTR_LEN[0x43]);
    }
    
    // 44 LD B,H [- - - -]
    private static void ldBH(CPU cpu) {
      cpu.B = cpu.H;
      cpu.incPC(INSTR_LEN[0x44]);
    }
    
    // 45 LD B,L [- - - -]
    private static void ldBL(CPU cpu) {
      cpu.B = cpu.L;
      cpu.incPC(INSTR_LEN[0x45]);
    }
    
    // 46 LD B,(HL) [- - - -]
    private static void ldBaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.B = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x46]);
    }
    
    // 47 LD B,A [- - - -]
    private static void ldBA(CPU cpu) {
      cpu.B = cpu.A;
      cpu.incPC(INSTR_LEN[0x47]);
    }
    
    // 48 LD C,B [- - - -]
    private static void ldCB(CPU cpu) {
      cpu.C = cpu.B;
      cpu.incPC(INSTR_LEN[0x48]);
    }
    
    // 49 LD C,C [- - - -]
    private static void ldCC(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x49]);
    }
    
    // 4A LD C,D [- - - -]
    private static void ldCD(CPU cpu) {
      cpu.C = cpu.D;
      cpu.incPC(INSTR_LEN[0x4a]);
    }
    
    // 4B LD C,E [- - - -]
    private static void ldCE(CPU cpu) {
      cpu.C = cpu.E;
      cpu.incPC(INSTR_LEN[0x4b]);
    }
    
    // 4C LD C,H [- - - -]
    private static void ldCH(CPU cpu) {
      cpu.C = cpu.H;
      cpu.incPC(INSTR_LEN[0x4c]);
    }
    
    // 4D LD C,L [- - - -]
    private static void ldCL(CPU cpu) {
      cpu.C = cpu.L;
      cpu.incPC(INSTR_LEN[0x4d]);
    }
    
    // 4E LD C,(HL) [- - - -]
    private static void ldCaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.C = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x4e]);
    }
    
    // 4F LD C,A [- - - -]
    private static void ldCA(CPU cpu) {
      cpu.C = cpu.A;
      cpu.incPC(INSTR_LEN[0x4f]);
    }
    
    // 50 LD D,B [- - - -]
    private static void ldDB(CPU cpu) {
      cpu.D = cpu.B;
      cpu.incPC(INSTR_LEN[0x50]);
    }
  
    // 51 LD D,C [- - - -]
    private static void ldDC(CPU cpu) {
      cpu.D = cpu.C;
      cpu.incPC(INSTR_LEN[0x51]);
    }
    
    // 52 LD D,D [- - - -]
    private static void ldDD(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x52]);
    }
    
    // 53 LD D,E [- - - -]
    private static void ldDE(CPU cpu) {
      cpu.D = cpu.E;
      cpu.incPC(INSTR_LEN[0x53]);
    }
    
    // 54 LD D,H [- - - -]
    private static void ldDH(CPU cpu) {
      cpu.D = cpu.H;
      cpu.incPC(INSTR_LEN[0x54]);
    }
    
    // 55 LD D,L [- - - -]
    private static void ldDL(CPU cpu) {
      cpu.D = cpu.L;
      cpu.incPC(INSTR_LEN[0x55]);
    }
    
    // 56 LD D,(HL) [- - - -]
    private static void ldDaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.D = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x56]);
    }
    
    // 57 LD D,A [- - - -]
    private static void ldDA(CPU cpu) {
      cpu.D = cpu.A;
      cpu.incPC(INSTR_LEN[0x57]);
    }
    
    // 58 LD E,B [- - - -]
    private static void ldEB(CPU cpu) {
      cpu.E = cpu.B;
      cpu.incPC(INSTR_LEN[0x58]);
    }
    
    // 59 LD E,C [- - - -]
    private static void ldEC(CPU cpu) {
      cpu.E = cpu.C;
      cpu.incPC(INSTR_LEN[0x59]);
    }
    
    // 5A LD E,D [- - - -]
    private static void ldED(CPU cpu) {
      cpu.E = cpu.D;
      cpu.incPC(INSTR_LEN[0x5a]);
    }
    
    // 5B LD E,E [- - - -]
    private static void ldEE(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x5b]);
    }
    
    // 5C LD E,H [- - - -]
    private static void ldEH(CPU cpu) {
      cpu.E = cpu.H;
      cpu.incPC(INSTR_LEN[0x5c]);
    }
    
    // 5D LD E,L [- - - -]
    private static void ldEL(CPU cpu) {
      cpu.E = cpu.L;
      cpu.incPC(INSTR_LEN[0x5d]);
    }
    
    // 5E LD E,(HL) [- - - -]
    private static void ldEaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.E = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x5e]);
    }
    
    // 5F LD E,A [- - - -]
    private static void ldEA(CPU cpu) {
      cpu.E = cpu.A;
      cpu.incPC(INSTR_LEN[0x5f]);
    }
    
    // 60 LD H,B [- - - -]
    private static void ldHB(CPU cpu) {
      cpu.H = cpu.B;
      cpu.incPC(INSTR_LEN[0x60]);
    }
    
    // 61 LD H,C [- - - -]
    private static void ldHC(CPU cpu) {
      cpu.H = cpu.C;
      cpu.incPC(INSTR_LEN[0x61]);
    }
    
    // 62 LD H,D [- - - -]
    private static void ldHD(CPU cpu) {
      cpu.H = cpu.D;
      cpu.incPC(INSTR_LEN[0x62]);
    }
  
    // 63 LD H,E [- - - -]
    private static void ldHE(CPU cpu) {
      cpu.H = cpu.E;
      cpu.incPC(INSTR_LEN[0x63]);
    }
    
    // 64 LD H,H [- - - -]
    private static void ldHH(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x64]);
    }
  
    // 65 LD H,L [- - - -]
    private static void ldHL(CPU cpu) {
      cpu.H = cpu.L;
      cpu.incPC(INSTR_LEN[0x65]);
    }
    
    // 66 LD H,(HL) [- - - -]
    private static void ldHaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.H = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x66]);
    }
    
    // 67 LD H,A [- - - -]
    private static void ldHA(CPU cpu) {
      cpu.H = cpu.A;
      cpu.incPC(INSTR_LEN[0x67]);
    }
  
    // 68 LD L,B [- - - -]
    private static void ldLB(CPU cpu) {
      cpu.L = cpu.B;
      cpu.incPC(INSTR_LEN[0x68]);
    }
  
    // 69 LD L,C [- - - -]
    private static void ldLC(CPU cpu) {
      cpu.L = cpu.C;
      cpu.incPC(INSTR_LEN[0x69]);
    }
    
    // 6A LD L,D [- - - -]
    private static void ldLD(CPU cpu) {
      cpu.L = cpu.D;
      cpu.incPC(INSTR_LEN[0x6a]);
    }
    
    // 6B LD L,E [- - - -]
    private static void ldLE(CPU cpu) {
      cpu.L = cpu.E;
      cpu.incPC(INSTR_LEN[0x6b]);
    }
    
    // 6C LD L,H [- - - -]
    private static void ldLH(CPU cpu) {
      cpu.L = cpu.H;
      cpu.incPC(INSTR_LEN[0x6c]);
    }
    
    // 6D LD L,L [- - - -]
    private static void ldLL(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x6d]);
    }
    
    // 6E LD L,(HL) [- - - -]
    private static void ldLaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.L = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x6e]);
    }
    
    // 6F LD L,A [- - - -]
    private static void ldLA(CPU cpu) {
      cpu.L = cpu.A;
      cpu.incPC(INSTR_LEN[0x6f]);
    }
    
    // 70 LD (HL),B [- - - -]
    private static void ldaHLB(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 71 LD (HL),C [- - - -]
    private static void ldaHLC(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 72 LD (HL),D [- - - -]
    private static void ldaHLD(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 73 LD (HL),E [- - - -]
    private static void ldaHLE(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 74 LD (HL),H [- - - -]
    private static void ldaHLH(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 75 LD (HL),L [- - - -]
    private static void ldaHLL(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 76 HALT [- - - -]
    private static void halt() {
      throw new UnsupportedOperationException();
    }
    
    // 77 LD (HL),A [- - - -]
    private static void ldaHLA(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // 78 LD A,B [- - - -]
    private static void ldAB(CPU cpu) {
      cpu.A = cpu.B;
      cpu.incPC(INSTR_LEN[0x78]);
    }
  
    // 79 LD A,C [- - - -]
    private static void ldAC(CPU cpu) {
      cpu.A = cpu.C;
      cpu.incPC(INSTR_LEN[0x79]);
    }
    
    // 7A LD A,D [- - - -]
    private static void ldAD(CPU cpu) {
      cpu.A = cpu.D;
      cpu.incPC(INSTR_LEN[0x7a]);
    }
    
    // 7B LD A,E [- - - -]
    private static void ldAE(CPU cpu) {
      cpu.A = cpu.E;
      cpu.incPC(INSTR_LEN[0x7b]);
    }
    
    // 7C LD A,H [- - - -]
    private static void ldAH(CPU cpu) {
      cpu.A = cpu.H;
      cpu.incPC(INSTR_LEN[0x7c]);
    }
    
    // 7D LD A,L [- - - -]
    private static void ldAL(CPU cpu) {
      cpu.A = cpu.L;
      cpu.incPC(INSTR_LEN[0x7d]);
    }
    
    // 7E LD A,(HL) [- - - -]
    private static void ldAaHL(CPU cpu, Memory mem) {
      int addr = (cpu.H << 8) | cpu.L;
      cpu.A = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0x7e]);
    }
    
    // 7F LD A,A [- - - -]
    private static void ldAA(CPU cpu) {
      // Do nothing
      cpu.incPC(INSTR_LEN[0x7f]);
    }
    
    // 80 ADD A,B [Z 0 H C]
    private static void addAB(CPU cpu) {
      int res = cpu.A + cpu.B;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.B & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x80]);
    }
    
    // 81 ADD A,C [- - - -]
    private static void addAC(CPU cpu) {
      int res = cpu.A + cpu.C;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.C & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x80]);
    }
    
    // 82 ADD A,D [- - - -]
    private static void addAD(CPU cpu) {
      int res = cpu.A + cpu.D;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.D & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x82]);
    }
    
    // 83 ADD A,E [- - - -]
    private static void addAE(CPU cpu) {
      int res = cpu.A + cpu.E;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.E & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x83]);
    }
    
    // 84 ADD A,H [- - - -]
    private static void addAH(CPU cpu) {
      int res = cpu.A + cpu.H;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.H & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x84]);
    }
    
    // 85 ADD A,L [- - - -]
    private static void addAL(CPU cpu) {
      int res = cpu.A + cpu.L;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.L & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x85]);
    }
    
    // 86 ADD A,(HL) [- - - -]
    private static void addAaHL(CPU cpu, Memory mem) {
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
    private static void addAA(CPU cpu) {
      int res = cpu.A + cpu.A;
      cpu.flagH = (cpu.A & 0x0f) + (cpu.A & 0x0f) > 0x0f;
      cpu.A = res & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagC = res > 0xff;
      cpu.incPC(INSTR_LEN[0x87]);
    }
    
    // 88 ADC A,B [Z 0 H C]
    private static void adcAB(CPU cpu) {
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
    private static void adcAC(CPU cpu) {
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
    private static void adcAD(CPU cpu) {
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
    private static void adcAE(CPU cpu) {
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
    private static void adcAH(CPU cpu) {
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
    private static void adcAL(CPU cpu) {
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
    private static void adcAaHL(CPU cpu, Memory mem) {
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
    private static void adcAA(CPU cpu) {
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
    private static void subB(CPU cpu) {
      int res = cpu.A - cpu.B;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.B & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x90]);
    }
    
    // 91 SUB C [Z 1 H C]
    private static void subC(CPU cpu) {
      int res = cpu.A - cpu.C;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.C & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x91]);
    }
    
    // 92 SUB D [Z 1 H C]
    private static void subD(CPU cpu) {
      int res = cpu.A - cpu.D;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.D & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x92]);
    }
    
    // 93 SUB E [Z 1 H C]
    private static void subE(CPU cpu) {
      int res = cpu.A - cpu.E;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.E & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x93]);
    }
    
    // 94 SUB H [Z 1 H C]
    private static void subH(CPU cpu) {
      int res = cpu.A - cpu.H;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.H & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x94]);
    }
    
    // 95 SUB L [Z 1 H C]
    private static void subL(CPU cpu) {
      int res = cpu.A - cpu.L;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.L & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.A = res;
      cpu.incPC(INSTR_LEN[0x95]);
    }
    
    // 96 SUB (HL) [Z 1 H C]
    private static void subaHL(CPU cpu, Memory mem) {
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
    private static void subA(CPU cpu) {
      cpu.flagZ = true;
      cpu.flagH = false;
      cpu.flagN = true;
      cpu.flagC = false;
      cpu.A = 0;
      cpu.incPC(INSTR_LEN[0x97]);
    }
    
    // 98 SBC A,B [Z 1 H C]
    private static void sbcAB(CPU cpu) {
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
    private static void sbcAC(CPU cpu) {
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
    private static void sbcAD(CPU cpu) {
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
    private static void sbcAE(CPU cpu) {
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
    private static void sbcAH(CPU cpu) {
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
    private static void sbcAL(CPU cpu) {
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
    private static void sbcAaHL(CPU cpu, Memory mem) {
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
    private static void sbcAA(CPU cpu) {
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
    private static void andB(CPU cpu) {
      cpu.A &= cpu.B;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa0]);
    }
    
    // A1 AND C [Z 0 1 0]
    private static void andC(CPU cpu) {
      cpu.A &= cpu.C;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa1]);
    }
    
    // A2 AND D [Z 0 1 0]
    private static void andD(CPU cpu) {
      cpu.A &= cpu.D;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa2]);
    }
    
    // A3 AND E [Z 0 1 0]
    private static void andE(CPU cpu) {
      cpu.A &= cpu.E;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa3]);
    }
    
    // A4 AND H [Z 0 1 0]
    private static void andH(CPU cpu) {
      cpu.A &= cpu.H;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa4]);
    }
    
    // A5 AND L [Z 0 1 0]
    private static void andL(CPU cpu) {
      cpu.A &= cpu.L;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa5]);
    }
    
    // A6 AND (HL) [Z 0 1 0]
    private static void andaHL(CPU cpu, Memory mem) {
      int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
      cpu.A &= val;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa6]);
    }
    
    // A7 AND A [Z 0 1 0]
    private static void andA(CPU cpu) {
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa7]);
    }
    
    // A8 XOR B [Z 0 0 0]
    private static void xorB(CPU cpu) {
      cpu.A ^= cpu.B;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa8]);
    }
    
    // A9 XOR C [Z 0 0 0]
    private static void xorC(CPU cpu) {
      cpu.A ^= cpu.C;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xa9]);
    }
    
    // AA XOR D [Z 0 0 0]
    private static void xorD(CPU cpu) {
      cpu.A ^= cpu.D;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xaa]);
    }
    
    // AB XOR E [Z 0 0 0]
    private static void xorE(CPU cpu) {
      cpu.A ^= cpu.E;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xab]);
    }
    
    // AC XOR H [Z 0 0 0]
    private static void xorH(CPU cpu) {
      cpu.A ^= cpu.H;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xac]);
    }
    
    // AD XOR L [Z 0 0 0]
    private static void xorL(CPU cpu) {
      cpu.A ^= cpu.L;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xad]);
    }
    
    // AE XOR (HL) [Z 0 0 0]
    private static void xoraHL(CPU cpu, Memory mem) {
      int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
      cpu.A ^= val;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xae]);
    }
    
    // AF XOR A [Z 0 0 0]
    private static void xorA(CPU cpu) {
      cpu.A = 0;
      cpu.flagZ = true;
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xaf]);
    }
    
    // B0 OR B [Z 0 0 0]
    private static void orB(CPU cpu) {
      cpu.A |= cpu.B;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb0]);
    }
    
    // B1 OR C [Z 0 0 0]
    private static void orC(CPU cpu) {
      cpu.A |= cpu.C;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb1]);
    }
    
    // B2 OR D [Z 0 0 0]
    private static void orD(CPU cpu) {
      cpu.A |= cpu.D;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb2]);
    }
    
    // B3 OR E [Z 0 0 0]
    private static void orE(CPU cpu) {
      cpu.A |= cpu.E;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb3]);
    }
    
    // B4 OR H [Z 0 0 0]
    private static void orH(CPU cpu) {
      cpu.A |= cpu.H;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb4]);
    }
    
    // B5 OR L [Z 0 0 0]
    private static void orL(CPU cpu) {
      cpu.A |= cpu.L;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb5]);
    }
    
    // B6 OR (HL) [Z 0 0 0]
    private static void oraHL(CPU cpu, Memory mem) {
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
    private static void orA(CPU cpu) {
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xb7]);
    }
    
    // B8 CP B [Z 1 H C]
    private static void cpB(CPU cpu) {
      int res = cpu.A - cpu.B;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.B & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xb8]);
    }
    
    // B9 CP C [Z 1 H C]
    private static void cpC(CPU cpu) {
      int res = cpu.A - cpu.C;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.C & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xb9]);
    }
    
    // BA CP D [Z 1 H C]
    private static void cpD(CPU cpu) {
      int res = cpu.A - cpu.D;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.D & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xba]);
    }
    
    // BB CP E [Z 1 H C]
    private static void cpE(CPU cpu) {
      int res = cpu.A - cpu.E;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.E & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xbb]);
    }
    
    // BC CP H [Z 1 H C]
    private static void cpH(CPU cpu) {
      int res = cpu.A - cpu.H;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.H & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xbc]);
    }
    
    // BD CP L [Z 1 H C]
    private static void cpL(CPU cpu) {
      int res = cpu.A - cpu.L;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (cpu.L & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xbd]);
    }
    
    // BE CP (HL) [Z 1 H C]
    private static void cpaHL(CPU cpu, Memory mem) {
      int val = mem.readMem((cpu.H << 8) | cpu.L) & 0xff;
      int res = cpu.A - val;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xbe]);
    }
    
    // BF CP A [Z 1 H C]
    private static void cpA(CPU cpu) {
      cpu.flagZ = true;
      cpu.flagH = false;
      cpu.flagN = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xbf]);
    }
    
    // C0 RET NZ [- - - -]
    private static void retNZ() {
      throw new UnsupportedOperationException();
    }
    
    // C1 POP BC [- - - -]
    private static void popBC(CPU cpu, Memory mem) {
      cpu.C = mem.readMem(cpu.sp) & 0xff;
      cpu.B = mem.readMem(cpu.sp + 1) & 0xff;
      cpu.sp = (cpu.sp + 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xc1]);
    }
    
    // C2 JP NZ,a16 [- - - -]
    private static void jpNZa16(CPU cpu, Memory mem) {
      if (!cpu.flagZ) {
        int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
        cpu.changePC(addr);
      } else {
        cpu.incPC(INSTR_LEN[0xc2]);
      }
    }
    
    // C3 JP a16 [- - - -]
    private static void jpa16(CPU cpu, Memory mem) {
      int newAddr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.changePC(newAddr);
    }
    
    // C4 CALL NZ,a16 [- - - -]
    private static void callNZa16() {
      throw new UnsupportedOperationException();
    }
    
    // C5 PUSH BC [- - - -]
    private static void pushBC(CPU cpu, Memory mem) {
      mem.writeMem(cpu.B & 0xff, (cpu.sp - 1) & 0xffff);
      mem.writeMem(cpu.C & 0xff, (cpu.sp - 1) & 0xffff);
      cpu.sp = (cpu.sp - 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xc5]);
    }
    
    // C6 ADD A,d8 [Z 0 H C]
    private static void addAd8(CPU cpu, Memory mem) {
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
    private static void rst00() {
      throw new UnsupportedOperationException();
    }
    
    // C8 RET Z [- - - -]
    private static void retZ() {
      throw new UnsupportedOperationException();
    }
    
    // C9 RET [- - - -]
    private static void ret() {
      throw new UnsupportedOperationException();
    }
    
    // CA JP Z,a16 [- - - -]
    private static void jpZa16(CPU cpu, Memory mem) {
      if (cpu.flagZ) {
        int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
        cpu.changePC(addr);
      } else {
        cpu.incPC(INSTR_LEN[0xca]);
      }
    }
    
    // CB PREFIX CB [- - - -]
    private static void preCB(CPU cpu, Memory mem) {
      execCBInstr(cpu, mem);
    }
    
    // CC CALL Z,a16 [- - - -]
    private static void callZa16() {
      throw new UnsupportedOperationException();
    }
    
    // CD CALL a16 [- - - -]
    private static void calla16() {
      throw new UnsupportedOperationException();
    }
    
    // CE ADC A,d8 [Z 0 H C]
    private static void adcAd8(CPU cpu, Memory mem) {
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
    private static void rst08() {
      throw new UnsupportedOperationException();
    }
    
    // D0 RET NC [- - - -]
    private static void retNC() {
      throw new UnsupportedOperationException();
    }
    
    // D1 POP DE [- - - -]
    private static void popDE(CPU cpu, Memory mem) {
      cpu.E = mem.readMem(cpu.sp) & 0xff;
      cpu.D = mem.readMem(cpu.sp + 1) & 0xff;
      cpu.sp = (cpu.sp + 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xd1]);
    }
    
    // D2 JP NC,a16 [- - - -]
    private static void jpNCa16(CPU cpu, Memory mem) {
      if (!cpu.flagC) {
        int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
        cpu.changePC(addr);
      } else {
        cpu.incPC(INSTR_LEN[0xd2]);
      }
    }
    
    // D3 DOES NOT EXIST
    
    // D4 CALL NC,a16 [- - - -]
    private static void callNCa16() {
      throw new UnsupportedOperationException();
    }
    
    // D5 PUSH DE [- - - -]
    private static void pushDE(CPU cpu, Memory mem) {
      mem.writeMem(cpu.D & 0xff, (cpu.sp - 1) & 0xffff);
      mem.writeMem(cpu.E & 0xff, (cpu.sp - 1) & 0xffff);
      cpu.sp = (cpu.sp - 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xd5]);
    }
    
    // D6 SUB d8 [Z 1 H C]
    private static void subd8(CPU cpu, Memory mem) {
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
    private static void rst10() {
      throw new UnsupportedOperationException();
    }
    
    // D8 RET C [- - - -]
    private static void retC() {
      throw new UnsupportedOperationException();
    }
    
    // D9 RETI [- - - -]
    private static void reti() {
      throw new UnsupportedOperationException();
    }
    
    // DA JP C,a16 [- - - -]
    private static void jpCa16(CPU cpu, Memory mem) {
      if (cpu.flagC) {
        int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
        cpu.changePC(addr);
      } else {
        cpu.incPC(INSTR_LEN[0xda]);
      }
    }
    
    // DB DOES NOT EXIST
    
    // DC CALL C,a16 [- - - -]
    private static void callCa16() {
      throw new UnsupportedOperationException();
    }
    
    // DD DOES NOT EXIST
    
    // DE SBC A,d8 [Z 1 H C]
    private static void sbcAd8(CPU cpu, Memory mem) {
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
    private static void rst18() {
      throw new UnsupportedOperationException();
    }
    
    // E0 LDH (a8),A [- - - -]
    private static void ldha8A(CPU cpu, Memory mem) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | 0xff00;
      mem.writeMem(cpu.A & 0xff, addr);
      cpu.incPC(INSTR_LEN[0xe0]);
    }
    
    // E1 POP HL [- - - -]
    private static void popHL(CPU cpu, Memory mem) {
      cpu.L = mem.readMem(cpu.sp) & 0xff;
      cpu.H = mem.readMem(cpu.sp + 1) & 0xff;
      cpu.sp = (cpu.sp + 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xe1]);
    }
    
    // E2 LD (C),A [- - - -]
    private static void ldaCA(CPU cpu, Memory mem) {
      int addr = 0xff00 | (cpu.C & 0xff);
      mem.writeMem(cpu.A & 0xff, addr);
      cpu.incPC(INSTR_LEN[0xe2]);
    }
    
    // E3 DOES NOT EXIST
    
    // E4 DOES NOT EXIST
    
    // E5 PUSH HL [- - - -]
    private static void pushHL(CPU cpu, Memory mem) {
      mem.writeMem(cpu.H & 0xff, (cpu.sp - 1) & 0xffff);
      mem.writeMem(cpu.L & 0xff, (cpu.sp - 1) & 0xffff);
      cpu.sp = (cpu.sp - 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xe5]);
    }
    
    // E6 AND d8 [Z 0 1 0]
    private static void andd8(CPU cpu, Memory mem) {
      int val = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.A &= val;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = true;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xe6]);
    }
    
    // E7 RST 20H [- - - -]
    private static void rst20() {
      throw new UnsupportedOperationException();
    }
    
    // E8 ADD SP,r8 [0 0 H C]
    private static void addSPr8(CPU cpu, Memory mem) {
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
    private static void jpaHL(CPU cpu) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      cpu.changePC(addr);
    }
    
    // EA LD (a16),A [- - - -]
    private static void lda16A(CPU cpu, Memory mem) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      mem.writeMem(cpu.A & 0xff, addr);
      cpu.incPC(INSTR_LEN[0xea]);
    }
    
    // EB DOES NOT EXIST
    
    // EC DOES NOT EXIST
    
    // ED DOES NOT EXIST
    
    // EE XOR d8 [Z 0 0 0]
    private static void xord8(CPU cpu, Memory mem) {
      int val = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.A ^= val;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xee]);
    }
    
    // EF RST 28H [- - - -]
    private static void rst28() {
      throw new UnsupportedOperationException();
    }
    
    // F0 LDH A,(a8) [- - - -]
    private static void ldhAa8(CPU cpu, Memory mem) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | 0xff00;
      cpu.A = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0xf0]);
    }
    
    // F1 POP AF [Z N H C]
    private static void popAF(CPU cpu, Memory mem) {
      int flags = mem.readMem(cpu.sp) & 0xff;
      cpu.A = mem.readMem(cpu.sp + 1) & 0xff;
      cpu.sp = (cpu.sp + 2) & 0xffff;
      cpu.flagZ = (flags & 0x80) != 0;
      cpu.flagN = (flags & 0x40) != 0;
      cpu.flagH = (flags & 0x20) != 0;
      cpu.flagC = (flags & 0x10) != 0;
      cpu.incPC(INSTR_LEN[0xc1]);
    }
    
    // F2 LD A,(C) [- - - -]
    private static void ldAaC(CPU cpu, Memory mem) {
      int addr = (cpu.C & 0xff) | 0xff00;
      cpu.A = mem.readMem(addr) & 0xff;
      cpu.incPC(INSTR_LEN[0xf2]);
    }
    
    // F3 DI [- - - -]
    private static void di() {
      throw new UnsupportedOperationException();
    }
    
    // F4 DOES NOT EXIST
    
    // F5 PUSH AF [- - - -]
    private static void pushAF(CPU cpu, Memory mem) {
      mem.writeMem(cpu.A & 0xff, (cpu.sp - 1) & 0xffff);
      int flags = cpu.flagZ ? 0x80 : 0x00;
      flags |= cpu.flagN ? 0x40 : 0x00;
      flags |= cpu.flagH ? 0x20 : 0x00;
      flags |= cpu.flagC ? 0x10 : 0x00;
      mem.writeMem(flags & 0xff, (cpu.sp - 1) & 0xffff);
      cpu.sp = (cpu.sp - 2) & 0xffff;
      cpu.incPC(INSTR_LEN[0xf5]);
    }
    
    // F6 OR d8 [Z 0 0 0]
    private static void ord8(CPU cpu, Memory mem) {
      int val = mem.readMem(cpu.getPC() + 1) & 0xff;
      cpu.A |= val;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      cpu.incPC(INSTR_LEN[0xf6]);
    }
    
    // F7 RST 30H [- - - -]
    private static void rst30() {
      throw new UnsupportedOperationException();
    }
    
    // F8 LD HL,SP+r8 [0 0 H C]
    private static void ldHLSPr8(CPU cpu) {
      throw new UnsupportedOperationException();
    }
    
    // F9 LD SP,HL [- - - -]
    private static void ldSPHL(CPU cpu, Memory mem) {
      cpu.sp = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      cpu.incPC(INSTR_LEN[0xf9]);
    }
    
    // FA LD A,(a16) [- - - -]
    private static void ldAa16(CPU cpu, Memory mem) {
      int addr = (mem.readMem(cpu.getPC() + 1) & 0xff) | ((mem.readMem(cpu.getPC() + 2) & 0xff) << 8);
      cpu.A = mem.readMem(addr) & 0xff;
    }
    
    // FB EI [- - - -]
    private static void ei() {
      throw new UnsupportedOperationException();
    }
    
    // FC DOES NOT EXIST
    
    // FD DOES NOT EXIST
    
    // FE CP d8 [Z 1 H C]
    private static void cpd8(CPU cpu, Memory mem) {
      int val = mem.readMem(cpu.getPC() + 1) & 0xff;
      int res = cpu.A - val;
      cpu.flagZ = (res == 0);
      cpu.flagH = (cpu.A & 0x0f) < (val & 0x0f);
      cpu.flagN = true;
      cpu.flagC = (res < 0);
      cpu.incPC(INSTR_LEN[0xfe]);
    }
    
    // FF RST 38H [- - - -]
    private static void rst38() {
      throw new UnsupportedOperationException();
    }
  }

  private static class CBInstruction {
    private static final int CB_INSTR_LEN = 2;
    // missing 0x0) -> 0x3F
    // 00 RLC B [Z 0 0 C]
    private static void rlcB(CPU cpu) {
      cpu.flagC = cpu.B > 0x7f;
      cpu.B = ((cpu.B << 1) & 0xff) | (cpu.B >> 7);
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 01 RLC C [Z 0 0 C]
    private static void rlcC(CPU cpu) {
      cpu.flagC = cpu.C > 0x7f;
      cpu.C = ((cpu.C << 1) & 0xff) | (cpu.C >> 7);
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 02 RLC D [Z 0 0 C]
    private static void rlcD(CPU cpu) {
      cpu.flagC = cpu.D > 0x7f;
      cpu.D = ((cpu.D << 1) & 0xff) | (cpu.D >> 7);
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 03 RLC E [Z 0 0 C]
    private static void rlcE(CPU cpu) {
      cpu.flagC = cpu.E > 0x7f;
      cpu.E = ((cpu.E << 1) & 0xff) | (cpu.E >> 7);
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 04 RLC H [Z 0 0 C]
    private static void rlcH(CPU cpu) {
      cpu.flagC = cpu.H > 0x7f;
      cpu.H = ((cpu.H << 1) & 0xff) | (cpu.H >> 7);
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 05 RLC L [Z 0 0 C]
    private static void rlcL(CPU cpu) {
      cpu.flagC = cpu.L > 0x7f;
      cpu.L = ((cpu.L << 1) & 0xff) | (cpu.L >> 7);
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 06 RLC (HL) [Z 0 0 C]
    private static void rlcaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      cpu.flagC = regVal > 0x7f;
      regVal = ((regVal << 1) & 0xff) | (regVal >> 7);
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 07 RLC A [Z 0 0 C]
    private static void rlcA(CPU cpu) {
      cpu.flagC = cpu.A > 0x7f;
      cpu.A = ((cpu.A << 1) & 0xff) | (cpu.A >> 7);
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 08 RRC B [Z 0 0 C]
    private static void rrcB(CPU cpu) {
      cpu.flagC = ((cpu.B & 0x01) == 0x1);
      cpu.B = (cpu.B >>> 1) | ((cpu.B & 0x01) << 7);
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }

    // 09 RRC C [Z 0 0 C]
    private static void rrcC(CPU cpu) {
      cpu.flagC = ((cpu.C & 0x01) == 0x1);
      cpu.C = (cpu.C >>> 1) | ((cpu.C & 0x01) << 7);
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0A RRC D [Z 0 0 C]
    private static void rrcD(CPU cpu) {
      cpu.flagC = ((cpu.D & 0x01) == 0x1);
      cpu.D = (cpu.D >>> 1) | ((cpu.D & 0x01) << 7);
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0B RRC E [Z 0 0 C]
    private static void rrcE(CPU cpu) {
      cpu.flagC = ((cpu.E & 0x01) == 0x1);
      cpu.E = (cpu.E >>> 1) | ((cpu.E & 0x01) << 7);
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0C RRC H [Z 0 0 C]
    private static void rrcH(CPU cpu) {
      cpu.flagC = ((cpu.H & 0x01) == 0x1);
      cpu.H = (cpu.H >>> 1) | ((cpu.H & 0x01) << 7);
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0D RRC L [Z 0 0 C]
    private static void rrcL(CPU cpu) {
      cpu.flagC = ((cpu.L & 0x01) == 0x1);
      cpu.L = (cpu.L >>> 1) | ((cpu.L & 0x01) << 7);
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0E RRC (HL) [Z 0 0 C]
    private static void rrcaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      cpu.flagC = ((regVal & 0x01) == 0x1);
      regVal = (regVal >>> 1) | ((regVal & 0x01) << 7);
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 0F RRC A [Z 0 0 C]
    private static void rrcA(CPU cpu) {
      cpu.flagC = ((cpu.A & 0x01) == 0x1);
      cpu.A = (cpu.A >>> 1) | ((cpu.A & 0x01) << 7);
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 10 RL B [Z 0 0 C]
    private static void rlB(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.B > 0x7f;
      cpu.B = ((cpu.B << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 11 RL C [Z 0 0 C]
    private static void rlC(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.C > 0x7f;
      cpu.C = ((cpu.C << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 12 RL D [Z 0 0 C]
    private static void rlD(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.D > 0x7f;
      cpu.D = ((cpu.D << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 13 RL E [Z 0 0 C]
    private static void rlE(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.E > 0x7f;
      cpu.E = ((cpu.E << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 14 RL H [Z 0 0 C]
    private static void rlH(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.H > 0x7f;
      cpu.H = ((cpu.H << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 15 RL L [Z 0 0 C]
    private static void rlL(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.L > 0x7f;
      cpu.L = ((cpu.L << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 16 RL (HL) [Z 0 0 C]
    private static void rlaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = regVal > 0x7f;
      regVal = ((regVal << 1) | bit) & 0xff;
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 17 RL A [Z 0 0 C]
    private static void rlA(CPU cpu) {
      int bit = cpu.flagC ? 1 : 0;
      cpu.flagC = cpu.A > 0x7f;
      cpu.A = ((cpu.A << 1) | bit) & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 18 RR B [Z 0 0 C]
    private static void rrB(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.B & 0x01) == 0x1);
      cpu.B = ((cpu.B >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 19 RR C [Z 0 0 C]
    private static void rrC(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.C & 0x01) == 0x1);
      cpu.C = ((cpu.C >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 1A RR D [Z 0 0 C]
    private static void rrD(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.D & 0x01) == 0x1);
      cpu.D = ((cpu.D >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 1B RR E [Z 0 0 C]
    private static void rrE(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.E & 0x01) == 0x1);
      cpu.E = ((cpu.E >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 1C RR H [Z 0 0 C]
    private static void rrH(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.H & 0x01) == 0x1);
      cpu.H = ((cpu.H >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 1D RR L [Z 0 0 C]
    private static void rrL(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.L & 0x01) == 0x1);
      cpu.L = ((cpu.L >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 1E RR (HL) [Z 0 0 C]
    private static void rraHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((regVal & 0x01) == 0x1);
      regVal = ((regVal >>> 1) | bit) & 0xff;
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 1F RR A [Z 0 0 C]
    private static void rrA(CPU cpu) {
      int bit = cpu.flagC ? 0x80 : 0;
      cpu.flagC = ((cpu.A & 0x01) == 0x1);
      cpu.A = ((cpu.A >>> 1) | bit) & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 20 SLA B [Z 0 0 C]
    private static void slaB(CPU cpu) {
      cpu.flagC = cpu.B > 0x7f;
      cpu.B = (cpu.B << 1) & 0xff;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 21 SLA C [Z 0 0 C]
    private static void slaC(CPU cpu) {
      cpu.flagC = cpu.C > 0x7f;
      cpu.C = (cpu.C << 1) & 0xff;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 22 SLA D [Z 0 0 C]
    private static void slaD(CPU cpu) {
      cpu.flagC = cpu.D > 0x7f;
      cpu.D = (cpu.D << 1) & 0xff;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 23 SLA E [Z 0 0 C]
    private static void slaE(CPU cpu) {
      cpu.flagC = cpu.E > 0x7f;
      cpu.E = (cpu.E << 1) & 0xff;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 24 SLA H [Z 0 0 C]
    private static void slaH(CPU cpu) {
      cpu.flagC = cpu.H > 0x7f;
      cpu.H = (cpu.H << 1) & 0xff;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 25 SLA L [Z 0 0 C]
    private static void slaL(CPU cpu) {
      cpu.flagC = cpu.L > 0x7f;
      cpu.L = (cpu.L << 1) & 0xff;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 26 SLA (HL) [Z 0 0 C]
    private static void slaaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      cpu.flagC = regVal > 0x7f;
      regVal = (regVal << 1) & 0xff;
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 27 SLA A [Z 0 0 C]
    private static void slaA(CPU cpu) {
      cpu.flagC = cpu.A > 0x7f;
      cpu.A = (cpu.A << 1) & 0xff;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 28 SRA B [Z 0 0 0]
    private static void sraB(CPU cpu) {
      cpu.flagC = (cpu.B & 0x1) == 0x1;
      int highBit = cpu.B & 0x80;
      cpu.B = ((cpu.B >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 29 SRA C [Z 0 0 0]
    private static void sraC(CPU cpu) {
      cpu.flagC = (cpu.C & 0x1) == 0x1;
      int highBit = cpu.C & 0x80;
      cpu.C = ((cpu.C >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 2A SRA D [Z 0 0 0]
    private static void sraD(CPU cpu) {
      cpu.flagC = (cpu.D & 0x1) == 0x1;
      int highBit = cpu.D & 0x80;
      cpu.D = ((cpu.D >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 2B SRA E [Z 0 0 0]
    private static void sraE(CPU cpu) {
      cpu.flagC = (cpu.E & 0x1) == 0x1;
      int highBit = cpu.E & 0x80;
      cpu.E = ((cpu.E >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 2C SRA H [Z 0 0 0]
    private static void sraH(CPU cpu) {
      cpu.flagC = (cpu.H & 0x1) == 0x1;
      int highBit = cpu.H & 0x80;
      cpu.H = ((cpu.H >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 2D SRA L [Z 0 0 0]
    private static void sraL(CPU cpu) {
      cpu.flagC = (cpu.L & 0x1) == 0x1;
      int highBit = cpu.L & 0x80;
      cpu.L = ((cpu.L >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 2E SRA (HL) [Z 0 0 0]
    private static void sraaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      cpu.flagC = (regVal & 0x1) == 0x1;
      int highBit = regVal & 0x80;
      regVal = ((regVal >> 1) & 0xff) | highBit;
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 2F SRA A [Z 0 0 0]
    private static void sraA(CPU cpu) {
      cpu.flagC = (cpu.A & 0x1) == 0x1;
      int highBit = cpu.A & 0x80;
      cpu.A = ((cpu.A >> 1) & 0xff) | highBit;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 30 SWAP B [Z 0 0 0]
    private static void swapB(CPU cpu) {
      cpu.B = ((cpu.B << 8) & 0xff00) | ((cpu.B >> 8) & 0xff);
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 31 SWAP C [Z 0 0 0]
    private static void swapC(CPU cpu) {
      cpu.C = ((cpu.C << 8) & 0xff00) | ((cpu.C >> 8) & 0xff);
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 32 SWAP D [Z 0 0 0]
    private static void swapD(CPU cpu) {
      cpu.D = ((cpu.D << 8) & 0xff00) | ((cpu.D >> 8) & 0xff);
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 33 SWAP E [Z 0 0 0]
    private static void swapE(CPU cpu) {
      cpu.E = ((cpu.E << 8) & 0xff00) | ((cpu.E >> 8) & 0xff);
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 34 SWAP H [Z 0 0 0]
    private static void swapH(CPU cpu) {
      cpu.H = ((cpu.H << 8) & 0xff00) | ((cpu.H >> 8) & 0xff);
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 35 SWAP L [Z 0 0 0]
    private static void swapL(CPU cpu) {
      cpu.L = ((cpu.L << 8) & 0xff00) | ((cpu.L >> 8) & 0xff);
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 36 SWAP (HL) [Z 0 0 0]
    private static void swapaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      regVal = ((regVal << 8) & 0xff00) | ((regVal >> 8) & 0xff);
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 37 SWAP A [Z 0 0 0]
    private static void swapA(CPU cpu) {
      cpu.A = ((cpu.A << 8) & 0xff00) | ((cpu.A >> 8) & 0xff);
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      cpu.flagC = false;
    }
    
    // 38 SRL B [Z 0 0 C]
    private static void srlB(CPU cpu) {
      cpu.flagC = (cpu.B & 0x1) == 0x1;
      cpu.B = (cpu.B & 0xff) >> 1;
      cpu.flagZ = (cpu.B == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 39 SRL C [Z 0 0 C]
    private static void srlC(CPU cpu) {
      cpu.flagC = (cpu.C & 0x1) == 0x1;
      cpu.C = (cpu.C & 0xff) >> 1;
      cpu.flagZ = (cpu.C == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 3A SRL D [Z 0 0 C]
    private static void srlD(CPU cpu) {
      cpu.flagC = (cpu.D & 0x1) == 0x1;
      cpu.D = (cpu.D & 0xff) >> 1;
      cpu.flagZ = (cpu.D == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 3B SRL E [Z 0 0 C]
    private static void srlE(CPU cpu) {
      cpu.flagC = (cpu.E & 0x1) == 0x1;
      cpu.E = (cpu.E & 0xff) >> 1;
      cpu.flagZ = (cpu.E == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 3C SRL H [Z 0 0 C]
    private static void srlH(CPU cpu) {
      cpu.flagC = (cpu.H & 0x1) == 0x1;
      cpu.H = (cpu.H & 0xff) >> 1;
      cpu.flagZ = (cpu.H == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 3D SRL L [Z 0 0 C]
    private static void srlL(CPU cpu) {
      cpu.flagC = (cpu.L & 0x1) == 0x1;
      cpu.L = (cpu.L & 0xff) >> 1;
      cpu.flagZ = (cpu.L == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 3E SRL (HL) [Z 0 0 C]
    private static void srlaHL(CPU cpu, Memory mem) {
      int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
      int regVal = mem.readMem(addr) & 0xff;
      cpu.flagC = (regVal & 0x1) == 0x1;
      regVal = (regVal & 0xff) >> 1;
      cpu.flagZ = (regVal == 0);
      cpu.flagN = false;
      cpu.flagH = false;
      mem.writeMem(regVal & 0xff, addr);
    }
    
    // 3F SRL A [Z 0 0 C]
    private static void srlA(CPU cpu) {
      cpu.flagC = (cpu.A & 0x1) == 0x1;
      cpu.A = (cpu.A & 0xff) >> 1;
      cpu.flagZ = (cpu.A == 0);
      cpu.flagN = false;
      cpu.flagH = false;
    }
    
    // 40 - 7F BIT b, r [Z 0 1 -]
    private static void bit(CPU cpu, Memory mem, int bit, String reg) {
      int regVal = 0;
      if (reg.equals("B")) {
        regVal = cpu.B;
      } else if (reg.equals("C")) {
        regVal = cpu.C;
      } else if (reg.equals("D")) {
        regVal = cpu.D;
      } else if (reg.equals("E")) {
        regVal = cpu.E;
      } else if (reg.equals("H")) {
        regVal = cpu.H;
      } else if (reg.equals("L")) {
        regVal = cpu.L;
      } else if (reg.equals("(HL)")) {
        int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
        regVal = mem.readMem(addr) & 0xff;
      } else if (reg.equals("A")) {
        regVal = cpu.A;
      } else {
        throw new IllegalArgumentException("Invalid register");
      }
      
      cpu.flagZ = ((regVal & (1 << bit)) == 0);
      cpu.flagN = false;
      cpu.flagH = true;
    }
    
    // 80 - BF RES b, r [- - - -]
    private static void res(CPU cpu, Memory mem, int bit, String reg) {
      int mask = ~(1 << bit);
      if (reg.equals("B")) {
        cpu.B &= mask;
      } else if (reg.equals("C")) {
        cpu.C &= mask;
      } else if (reg.equals("D")) {
        cpu.D &= mask;
      } else if (reg.equals("E")) {
        cpu.E &= mask;
      } else if (reg.equals("H")) {
        cpu.H &= mask;
      } else if (reg.equals("L")) {
        cpu.L &= mask;
      } else if (reg.equals("(HL)")) {
        int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
        int regVal = mem.readMem(addr) & 0xff;
        mem.writeMem(regVal & mask, addr);
      } else if (reg.equals("A")) {
        cpu.A &= mask;
      } else {
        throw new IllegalArgumentException("Invalid register");
      }
    }
    
    // C0 - FF SET b, r [- - - -]
    private static void set(CPU cpu, Memory mem, int bit, String reg) {
      int mask = 1 << bit;
      if (reg.equals("B")) {
        cpu.B |= mask;
      } else if (reg.equals("C")) {
        cpu.C |= mask;
      } else if (reg.equals("D")) {
        cpu.D |= mask;
      } else if (reg.equals("E")) {
        cpu.E |= mask;
      } else if (reg.equals("H")) {
        cpu.H |= mask;
      } else if (reg.equals("L")) {
        cpu.L |= mask;
      } else if (reg.equals("(HL)")) {
        int addr = ((cpu.H & 0xff) << 8) | (cpu.L & 0xff);
        int regVal = mem.readMem(addr) & 0xff;
        mem.writeMem(regVal | mask, addr);
      } else if (reg.equals("A")) {
        cpu.A |= mask;
      } else {
        throw new IllegalArgumentException("Invalid register");
      }
    }
  }
  
  /**
   * Execute an instruction
   * @param cpu the CPU object
   * @param mem the Memory object
   * @throws IllegalArgumentException if invalid opcode
   */
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
    else if (opcode == 0xc1) { Instruction.popBC(cpu, mem); }
    else if (opcode == 0xc2) { Instruction.jpNZa16(cpu, mem); }
    else if (opcode == 0xc3) { Instruction.jpa16(cpu, mem); }
    else if (opcode == 0xc4) { Instruction.callNZa16(); }
    else if (opcode == 0xc5) { Instruction.pushBC(cpu, mem); }
    else if (opcode == 0xc6) { Instruction.addAd8(cpu, mem); }
    else if (opcode == 0xc7) { Instruction.rst00(); }
    else if (opcode == 0xc8) { Instruction.retZ(); }
    else if (opcode == 0xc9) { Instruction.ret(); }
    else if (opcode == 0xca) { Instruction.jpZa16(cpu, mem); }
    else if (opcode == 0xcb) { Instruction.preCB(cpu, mem); }
    else if (opcode == 0xcc) { Instruction.callZa16(); }
    else if (opcode == 0xcd) { Instruction.calla16(); }
    else if (opcode == 0xce) { Instruction.adcAd8(cpu, mem); }
    else if (opcode == 0xcf) { Instruction.rst08(); }
    
    else if (opcode == 0xd0) { Instruction.retNC(); }
    else if (opcode == 0xd1) { Instruction.popDE(cpu, mem); }
    else if (opcode == 0xd2) { Instruction.jpNCa16(cpu, mem); }
    else if (opcode == 0xd3) { throw new IllegalArgumentException("Invalid opcode 0xd3"); }
    else if (opcode == 0xd4) { Instruction.callNCa16(); }
    else if (opcode == 0xd5) { Instruction.pushDE(cpu, mem); }
    else if (opcode == 0xd6) { Instruction.subd8(cpu, mem); }
    else if (opcode == 0xd7) { Instruction.rst10(); }
    else if (opcode == 0xd8) { Instruction.retC(); }
    else if (opcode == 0xd9) { Instruction.reti(); }
    else if (opcode == 0xda) { Instruction.jpCa16(cpu, mem); }
    else if (opcode == 0xdb) { throw new IllegalArgumentException("Invalid opcode 0xdb"); }
    else if (opcode == 0xdc) { Instruction.callCa16(); }
    else if (opcode == 0xdd) { throw new IllegalArgumentException("Invalid opcode 0xdd"); }
    else if (opcode == 0xde) { Instruction.sbcAd8(cpu, mem); }
    else if (opcode == 0xdf) { Instruction.rst18(); }
    
    else if (opcode == 0xe0) { Instruction.ldha8A(cpu, mem); }
    else if (opcode == 0xe1) { Instruction.popHL(cpu, mem); }
    else if (opcode == 0xe2) { Instruction.ldaCA(cpu, mem); }
    else if (opcode == 0xe3) { throw new IllegalArgumentException("Invalid opcode 0xe3"); }
    else if (opcode == 0xe4) { throw new IllegalArgumentException("Invalid opcode 0xe4"); }
    else if (opcode == 0xe5) { Instruction.pushHL(cpu, mem); }
    else if (opcode == 0xe6) { Instruction.andd8(cpu, mem); }
    else if (opcode == 0xe7) { Instruction.rst20(); }
    else if (opcode == 0xe8) { Instruction.addSPr8(cpu, mem); }
    else if (opcode == 0xe9) { Instruction.jpaHL(cpu); }
    else if (opcode == 0xea) { Instruction.lda16A(cpu, mem); }
    else if (opcode == 0xeb) { throw new IllegalArgumentException("Invalid opcode 0xeb"); }
    else if (opcode == 0xec) { throw new IllegalArgumentException("Invalid opcode 0xec"); }
    else if (opcode == 0xed) { throw new IllegalArgumentException("Invalid opcode 0xed"); }
    else if (opcode == 0xee) { Instruction.xord8(cpu, mem); }
    else if (opcode == 0xef) { Instruction.rst28(); }
    
    else if (opcode == 0xf0) { Instruction.ldhAa8(cpu, mem); }
    else if (opcode == 0xf1) { Instruction.popAF(cpu, mem); }
    else if (opcode == 0xf2) { Instruction.ldAaC(cpu, mem); }
    else if (opcode == 0xf3) { Instruction.di(); }
    else if (opcode == 0xf4) { throw new IllegalArgumentException("Invalid opcode 0xf4"); }
    else if (opcode == 0xf5) { Instruction.pushAF(cpu, mem); }
    else if (opcode == 0xf6) { Instruction.ord8(cpu, mem); }
    else if (opcode == 0xf7) { Instruction.rst30(); }
    else if (opcode == 0xf8) { Instruction.ldHLSPr8(cpu); }
    else if (opcode == 0xf9) { Instruction.ldSPHL(cpu, mem); }
    else if (opcode == 0xfa) { Instruction.ldAa16(cpu, mem); }
    else if (opcode == 0xfb) { Instruction.ei(); }
    else if (opcode == 0xfc) { throw new IllegalArgumentException("Invalid opcode 0xfc"); }
    else if (opcode == 0xfd) { throw new IllegalArgumentException("Invalid opcode 0xfd"); }
    else if (opcode == 0xfe) { Instruction.cpd8(cpu, mem); }
    else if (opcode == 0xff) { Instruction.rst38(); }
    else { throw new IllegalArgumentException("Invalid opcode"); }
  }
  
  
  /**
   * Executes a CB instruction
   * @param cpu the CPU object
   * @param mem the Memory object
   * @throws IllegalArgumentException if invalid opcode
   */
  private static void execCBInstr(CPU cpu, Memory mem) {
    int cbOpcode = mem.readMem(cpu.getPC() + 1) & 0xff;
    System.out.print("PC: ");
    Util.printHex(cpu.getPC());
    System.out.print("OP: ");
    Util.printHex(cbOpcode);
    System.out.println();
    
    
    if (cbOpcode == 0x00) { CBInstruction.rlcB(cpu); }
    else if (cbOpcode == 0x01) { CBInstruction.rlcC(cpu); }
    else if (cbOpcode == 0x02) { CBInstruction.rlcD(cpu); }
    else if (cbOpcode == 0x03) { CBInstruction.rlcE(cpu); }
    else if (cbOpcode == 0x04) { CBInstruction.rlcH(cpu); }
    else if (cbOpcode == 0x05) { CBInstruction.rlcL(cpu); }
    else if (cbOpcode == 0x06) { CBInstruction.rlcaHL(cpu, mem); }
    else if (cbOpcode == 0x07) { CBInstruction.rlcA(cpu); }
    else if (cbOpcode == 0x08) { CBInstruction.rrcB(cpu); }
    else if (cbOpcode == 0x09) { CBInstruction.rrcC(cpu); }
    else if (cbOpcode == 0x0a) { CBInstruction.rrcD(cpu); }
    else if (cbOpcode == 0x0b) { CBInstruction.rrcE(cpu); }
    else if (cbOpcode == 0x0c) { CBInstruction.rrcH(cpu); }
    else if (cbOpcode == 0x0d) { CBInstruction.rrcL(cpu); }
    else if (cbOpcode == 0x0e) { CBInstruction.rrcaHL(cpu, mem); }
    else if (cbOpcode == 0x0f) { CBInstruction.rrcA(cpu); }
    
    else if (cbOpcode == 0x10) { CBInstruction.rlB(cpu); }
    else if (cbOpcode == 0x11) { CBInstruction.rlC(cpu); }
    else if (cbOpcode == 0x12) { CBInstruction.rlD(cpu); }
    else if (cbOpcode == 0x13) { CBInstruction.rlE(cpu); }
    else if (cbOpcode == 0x14) { CBInstruction.rlH(cpu); }
    else if (cbOpcode == 0x15) { CBInstruction.rlL(cpu); }
    else if (cbOpcode == 0x16) { CBInstruction.rlaHL(cpu, mem); }
    else if (cbOpcode == 0x17) { CBInstruction.rlA(cpu); }
    else if (cbOpcode == 0x18) { CBInstruction.rrB(cpu); }
    else if (cbOpcode == 0x19) { CBInstruction.rrC(cpu); }
    else if (cbOpcode == 0x1a) { CBInstruction.rrD(cpu); }
    else if (cbOpcode == 0x1b) { CBInstruction.rrE(cpu); }
    else if (cbOpcode == 0x1c) { CBInstruction.rrH(cpu); }
    else if (cbOpcode == 0x1d) { CBInstruction.rrL(cpu); }
    else if (cbOpcode == 0x1e) { CBInstruction.rraHL(cpu, mem); }
    else if (cbOpcode == 0x1f) { CBInstruction.rrA(cpu); }
    
    else if (cbOpcode == 0x20) { CBInstruction.slaB(cpu); }
    else if (cbOpcode == 0x21) { CBInstruction.slaC(cpu); }
    else if (cbOpcode == 0x22) { CBInstruction.slaD(cpu); }
    else if (cbOpcode == 0x23) { CBInstruction.slaE(cpu); }
    else if (cbOpcode == 0x24) { CBInstruction.slaH(cpu); }
    else if (cbOpcode == 0x25) { CBInstruction.slaL(cpu); }
    else if (cbOpcode == 0x26) { CBInstruction.slaaHL(cpu, mem); }
    else if (cbOpcode == 0x27) { CBInstruction.slaA(cpu); }
    else if (cbOpcode == 0x28) { CBInstruction.sraB(cpu); }
    else if (cbOpcode == 0x29) { CBInstruction.sraC(cpu); }
    else if (cbOpcode == 0x2a) { CBInstruction.sraD(cpu); }
    else if (cbOpcode == 0x2b) { CBInstruction.sraE(cpu); }
    else if (cbOpcode == 0x2c) { CBInstruction.sraH(cpu); }
    else if (cbOpcode == 0x2d) { CBInstruction.sraL(cpu); }
    else if (cbOpcode == 0x2e) { CBInstruction.sraaHL(cpu, mem); }
    else if (cbOpcode == 0x2f) { CBInstruction.sraA(cpu); }
    
    else if (cbOpcode == 0x30) { CBInstruction.swapB(cpu); }
    else if (cbOpcode == 0x31) { CBInstruction.swapC(cpu); }
    else if (cbOpcode == 0x32) { CBInstruction.swapD(cpu); }
    else if (cbOpcode == 0x33) { CBInstruction.swapE(cpu); }
    else if (cbOpcode == 0x34) { CBInstruction.swapH(cpu); }
    else if (cbOpcode == 0x35) { CBInstruction.swapL(cpu); }
    else if (cbOpcode == 0x36) { CBInstruction.swapaHL(cpu, mem); }
    else if (cbOpcode == 0x37) { CBInstruction.swapA(cpu); }
    else if (cbOpcode == 0x38) { CBInstruction.srlB(cpu); }
    else if (cbOpcode == 0x39) { CBInstruction.srlC(cpu); }
    else if (cbOpcode == 0x3a) { CBInstruction.srlD(cpu); }
    else if (cbOpcode == 0x3b) { CBInstruction.srlE(cpu); }
    else if (cbOpcode == 0x3c) { CBInstruction.srlH(cpu); }
    else if (cbOpcode == 0x3d) { CBInstruction.srlL(cpu); }
    else if (cbOpcode == 0x3e) { CBInstruction.srlaHL(cpu, mem); }
    else if (cbOpcode == 0x3f) { CBInstruction.srlA(cpu); }
    
    else if (cbOpcode == 0x40) { CBInstruction.bit(cpu, mem, 0, "B"); }
    else if (cbOpcode == 0x41) { CBInstruction.bit(cpu, mem, 0, "C"); }
    else if (cbOpcode == 0x42) { CBInstruction.bit(cpu, mem, 0, "D"); }
    else if (cbOpcode == 0x43) { CBInstruction.bit(cpu, mem, 0, "E"); }
    else if (cbOpcode == 0x44) { CBInstruction.bit(cpu, mem, 0, "H"); }
    else if (cbOpcode == 0x45) { CBInstruction.bit(cpu, mem, 0, "L"); }
    else if (cbOpcode == 0x46) { CBInstruction.bit(cpu, mem, 0, "(HL)"); }
    else if (cbOpcode == 0x47) { CBInstruction.bit(cpu, mem, 0, "A"); }
    else if (cbOpcode == 0x48) { CBInstruction.bit(cpu, mem, 1, "B"); }
    else if (cbOpcode == 0x49) { CBInstruction.bit(cpu, mem, 1, "C"); }
    else if (cbOpcode == 0x4a) { CBInstruction.bit(cpu, mem, 1, "D"); }
    else if (cbOpcode == 0x4b) { CBInstruction.bit(cpu, mem, 1, "E"); }
    else if (cbOpcode == 0x4c) { CBInstruction.bit(cpu, mem, 1, "H"); }
    else if (cbOpcode == 0x4d) { CBInstruction.bit(cpu, mem, 1, "L"); }
    else if (cbOpcode == 0x4e) { CBInstruction.bit(cpu, mem, 1, "(HL)"); }
    else if (cbOpcode == 0x4f) { CBInstruction.bit(cpu, mem, 1, "A"); }

    else if (cbOpcode == 0x50) { CBInstruction.bit(cpu, mem, 2, "B"); }
    else if (cbOpcode == 0x51) { CBInstruction.bit(cpu, mem, 2, "C"); }
    else if (cbOpcode == 0x52) { CBInstruction.bit(cpu, mem, 2, "D"); }
    else if (cbOpcode == 0x53) { CBInstruction.bit(cpu, mem, 2, "E"); }
    else if (cbOpcode == 0x54) { CBInstruction.bit(cpu, mem, 2, "H"); }
    else if (cbOpcode == 0x55) { CBInstruction.bit(cpu, mem, 2, "L"); }
    else if (cbOpcode == 0x56) { CBInstruction.bit(cpu, mem, 2, "(HL)"); }
    else if (cbOpcode == 0x57) { CBInstruction.bit(cpu, mem, 2, "A"); }
    else if (cbOpcode == 0x58) { CBInstruction.bit(cpu, mem, 3, "B"); }
    else if (cbOpcode == 0x59) { CBInstruction.bit(cpu, mem, 3, "C"); }
    else if (cbOpcode == 0x5a) { CBInstruction.bit(cpu, mem, 3, "D"); }
    else if (cbOpcode == 0x5b) { CBInstruction.bit(cpu, mem, 3, "E"); }
    else if (cbOpcode == 0x5c) { CBInstruction.bit(cpu, mem, 3, "H"); }
    else if (cbOpcode == 0x5d) { CBInstruction.bit(cpu, mem, 3, "L"); }
    else if (cbOpcode == 0x5e) { CBInstruction.bit(cpu, mem, 3, "(HL)"); }
    else if (cbOpcode == 0x5f) { CBInstruction.bit(cpu, mem, 3, "A"); }
    
    else if (cbOpcode == 0x60) { CBInstruction.bit(cpu, mem, 4, "B"); }
    else if (cbOpcode == 0x61) { CBInstruction.bit(cpu, mem, 4, "C"); }
    else if (cbOpcode == 0x62) { CBInstruction.bit(cpu, mem, 4, "D"); }
    else if (cbOpcode == 0x63) { CBInstruction.bit(cpu, mem, 4, "E"); }
    else if (cbOpcode == 0x64) { CBInstruction.bit(cpu, mem, 4, "H"); }
    else if (cbOpcode == 0x65) { CBInstruction.bit(cpu, mem, 4, "L"); }
    else if (cbOpcode == 0x66) { CBInstruction.bit(cpu, mem, 4, "(HL)"); }
    else if (cbOpcode == 0x67) { CBInstruction.bit(cpu, mem, 4, "A"); }
    else if (cbOpcode == 0x68) { CBInstruction.bit(cpu, mem, 5, "B"); }
    else if (cbOpcode == 0x69) { CBInstruction.bit(cpu, mem, 5, "C"); }
    else if (cbOpcode == 0x6a) { CBInstruction.bit(cpu, mem, 5, "D"); }
    else if (cbOpcode == 0x6b) { CBInstruction.bit(cpu, mem, 5, "E"); }
    else if (cbOpcode == 0x6c) { CBInstruction.bit(cpu, mem, 5, "H"); }
    else if (cbOpcode == 0x6d) { CBInstruction.bit(cpu, mem, 5, "L"); }
    else if (cbOpcode == 0x6e) { CBInstruction.bit(cpu, mem, 5, "(HL)"); }
    else if (cbOpcode == 0x6f) { CBInstruction.bit(cpu, mem, 5, "A"); }
    
    else if (cbOpcode == 0x70) { CBInstruction.bit(cpu, mem, 6, "B"); }
    else if (cbOpcode == 0x71) { CBInstruction.bit(cpu, mem, 6, "C"); }
    else if (cbOpcode == 0x72) { CBInstruction.bit(cpu, mem, 6, "D"); }
    else if (cbOpcode == 0x73) { CBInstruction.bit(cpu, mem, 6, "E"); }
    else if (cbOpcode == 0x74) { CBInstruction.bit(cpu, mem, 6, "H"); }
    else if (cbOpcode == 0x75) { CBInstruction.bit(cpu, mem, 6, "L"); }
    else if (cbOpcode == 0x76) { CBInstruction.bit(cpu, mem, 6, "(HL)"); }
    else if (cbOpcode == 0x77) { CBInstruction.bit(cpu, mem, 6, "A"); }
    else if (cbOpcode == 0x78) { CBInstruction.bit(cpu, mem, 7, "B"); }
    else if (cbOpcode == 0x79) { CBInstruction.bit(cpu, mem, 7, "C"); }
    else if (cbOpcode == 0x7a) { CBInstruction.bit(cpu, mem, 7, "D"); }
    else if (cbOpcode == 0x7b) { CBInstruction.bit(cpu, mem, 7, "E"); }
    else if (cbOpcode == 0x7c) { CBInstruction.bit(cpu, mem, 7, "H"); }
    else if (cbOpcode == 0x7d) { CBInstruction.bit(cpu, mem, 7, "L"); }
    else if (cbOpcode == 0x7e) { CBInstruction.bit(cpu, mem, 7, "(HL)"); }
    else if (cbOpcode == 0x7f) { CBInstruction.bit(cpu, mem, 7, "A"); }
    
    else if (cbOpcode == 0x80) { CBInstruction.res(cpu, mem, 0, "B"); }
    else if (cbOpcode == 0x81) { CBInstruction.res(cpu, mem, 0, "C"); }
    else if (cbOpcode == 0x82) { CBInstruction.res(cpu, mem, 0, "D"); }
    else if (cbOpcode == 0x83) { CBInstruction.res(cpu, mem, 0, "E"); }
    else if (cbOpcode == 0x84) { CBInstruction.res(cpu, mem, 0, "H"); }
    else if (cbOpcode == 0x85) { CBInstruction.res(cpu, mem, 0, "L"); }
    else if (cbOpcode == 0x86) { CBInstruction.res(cpu, mem, 0, "(HL)"); }
    else if (cbOpcode == 0x87) { CBInstruction.res(cpu, mem, 0, "A"); }
    else if (cbOpcode == 0x88) { CBInstruction.res(cpu, mem, 1, "B"); }
    else if (cbOpcode == 0x89) { CBInstruction.res(cpu, mem, 1, "C"); }
    else if (cbOpcode == 0x8a) { CBInstruction.res(cpu, mem, 1, "D"); }
    else if (cbOpcode == 0x8b) { CBInstruction.res(cpu, mem, 1, "E"); }
    else if (cbOpcode == 0x8c) { CBInstruction.res(cpu, mem, 1, "H"); }
    else if (cbOpcode == 0x8d) { CBInstruction.res(cpu, mem, 1, "L"); }
    else if (cbOpcode == 0x8e) { CBInstruction.res(cpu, mem, 1, "(HL)"); }
    else if (cbOpcode == 0x8f) { CBInstruction.res(cpu, mem, 1, "A"); }

    else if (cbOpcode == 0x90) { CBInstruction.res(cpu, mem, 2, "B"); }
    else if (cbOpcode == 0x91) { CBInstruction.res(cpu, mem, 2, "C"); }
    else if (cbOpcode == 0x92) { CBInstruction.res(cpu, mem, 2, "D"); }
    else if (cbOpcode == 0x93) { CBInstruction.res(cpu, mem, 2, "E"); }
    else if (cbOpcode == 0x94) { CBInstruction.res(cpu, mem, 2, "H"); }
    else if (cbOpcode == 0x95) { CBInstruction.res(cpu, mem, 2, "L"); }
    else if (cbOpcode == 0x96) { CBInstruction.res(cpu, mem, 2, "(HL)"); }
    else if (cbOpcode == 0x97) { CBInstruction.res(cpu, mem, 2, "A"); }
    else if (cbOpcode == 0x98) { CBInstruction.res(cpu, mem, 3, "B"); }
    else if (cbOpcode == 0x99) { CBInstruction.res(cpu, mem, 3, "C"); }
    else if (cbOpcode == 0x9a) { CBInstruction.res(cpu, mem, 3, "D"); }
    else if (cbOpcode == 0x9b) { CBInstruction.res(cpu, mem, 3, "E"); }
    else if (cbOpcode == 0x9c) { CBInstruction.res(cpu, mem, 3, "H"); }
    else if (cbOpcode == 0x9d) { CBInstruction.res(cpu, mem, 3, "L"); }
    else if (cbOpcode == 0x9e) { CBInstruction.res(cpu, mem, 3, "(HL)"); }
    else if (cbOpcode == 0x9f) { CBInstruction.res(cpu, mem, 3, "A"); }
    
    else if (cbOpcode == 0xa0) { CBInstruction.res(cpu, mem, 4, "B"); }
    else if (cbOpcode == 0xa1) { CBInstruction.res(cpu, mem, 4, "C"); }
    else if (cbOpcode == 0xa2) { CBInstruction.res(cpu, mem, 4, "D"); }
    else if (cbOpcode == 0xa3) { CBInstruction.res(cpu, mem, 4, "E"); }
    else if (cbOpcode == 0xa4) { CBInstruction.res(cpu, mem, 4, "H"); }
    else if (cbOpcode == 0xa5) { CBInstruction.res(cpu, mem, 4, "L"); }
    else if (cbOpcode == 0xa6) { CBInstruction.res(cpu, mem, 4, "(HL)"); }
    else if (cbOpcode == 0xa7) { CBInstruction.res(cpu, mem, 4, "A"); }
    else if (cbOpcode == 0xa8) { CBInstruction.res(cpu, mem, 5, "B"); }
    else if (cbOpcode == 0xa9) { CBInstruction.res(cpu, mem, 5, "C"); }
    else if (cbOpcode == 0xaa) { CBInstruction.res(cpu, mem, 5, "D"); }
    else if (cbOpcode == 0xab) { CBInstruction.res(cpu, mem, 5, "E"); }
    else if (cbOpcode == 0xac) { CBInstruction.res(cpu, mem, 5, "H"); }
    else if (cbOpcode == 0xad) { CBInstruction.res(cpu, mem, 5, "L"); }
    else if (cbOpcode == 0xae) { CBInstruction.res(cpu, mem, 5, "(HL)"); }
    else if (cbOpcode == 0xaf) { CBInstruction.res(cpu, mem, 5, "A"); }
    
    else if (cbOpcode == 0xb0) { CBInstruction.res(cpu, mem, 6, "B"); }
    else if (cbOpcode == 0xb1) { CBInstruction.res(cpu, mem, 6, "C"); }
    else if (cbOpcode == 0xb2) { CBInstruction.res(cpu, mem, 6, "D"); }
    else if (cbOpcode == 0xb3) { CBInstruction.res(cpu, mem, 6, "E"); }
    else if (cbOpcode == 0xb4) { CBInstruction.res(cpu, mem, 6, "H"); }
    else if (cbOpcode == 0xb5) { CBInstruction.res(cpu, mem, 6, "L"); }
    else if (cbOpcode == 0xb6) { CBInstruction.res(cpu, mem, 6, "(HL)"); }
    else if (cbOpcode == 0xb7) { CBInstruction.res(cpu, mem, 6, "A"); }
    else if (cbOpcode == 0xb8) { CBInstruction.res(cpu, mem, 7, "B"); }
    else if (cbOpcode == 0xb9) { CBInstruction.res(cpu, mem, 7, "C"); }
    else if (cbOpcode == 0xba) { CBInstruction.res(cpu, mem, 7, "D"); }
    else if (cbOpcode == 0xbb) { CBInstruction.res(cpu, mem, 7, "E"); }
    else if (cbOpcode == 0xbc) { CBInstruction.res(cpu, mem, 7, "H"); }
    else if (cbOpcode == 0xbd) { CBInstruction.res(cpu, mem, 7, "L"); }
    else if (cbOpcode == 0xbe) { CBInstruction.res(cpu, mem, 7, "(HL)"); }
    else if (cbOpcode == 0xbf) { CBInstruction.res(cpu, mem, 7, "A"); }
    
    else if (cbOpcode == 0xc0) { CBInstruction.set(cpu, mem, 0, "B"); }
    else if (cbOpcode == 0xc1) { CBInstruction.set(cpu, mem, 0, "C"); }
    else if (cbOpcode == 0xc2) { CBInstruction.set(cpu, mem, 0, "D"); }
    else if (cbOpcode == 0xc3) { CBInstruction.set(cpu, mem, 0, "E"); }
    else if (cbOpcode == 0xc4) { CBInstruction.set(cpu, mem, 0, "H"); }
    else if (cbOpcode == 0xc5) { CBInstruction.set(cpu, mem, 0, "L"); }
    else if (cbOpcode == 0xc6) { CBInstruction.set(cpu, mem, 0, "(HL)"); }
    else if (cbOpcode == 0xc7) { CBInstruction.set(cpu, mem, 0, "A"); }
    else if (cbOpcode == 0xc8) { CBInstruction.set(cpu, mem, 1, "B"); }
    else if (cbOpcode == 0xc9) { CBInstruction.set(cpu, mem, 1, "C"); }
    else if (cbOpcode == 0xca) { CBInstruction.set(cpu, mem, 1, "D"); }
    else if (cbOpcode == 0xcb) { CBInstruction.set(cpu, mem, 1, "E"); }
    else if (cbOpcode == 0xcc) { CBInstruction.set(cpu, mem, 1, "H"); }
    else if (cbOpcode == 0xcd) { CBInstruction.set(cpu, mem, 1, "L"); }
    else if (cbOpcode == 0xce) { CBInstruction.set(cpu, mem, 1, "(HL)"); }
    else if (cbOpcode == 0xcf) { CBInstruction.set(cpu, mem, 1, "A"); }

    else if (cbOpcode == 0xd0) { CBInstruction.set(cpu, mem, 2, "B"); }
    else if (cbOpcode == 0xd1) { CBInstruction.set(cpu, mem, 2, "C"); }
    else if (cbOpcode == 0xd2) { CBInstruction.set(cpu, mem, 2, "D"); }
    else if (cbOpcode == 0xd3) { CBInstruction.set(cpu, mem, 2, "E"); }
    else if (cbOpcode == 0xd4) { CBInstruction.set(cpu, mem, 2, "H"); }
    else if (cbOpcode == 0xd5) { CBInstruction.set(cpu, mem, 2, "L"); }
    else if (cbOpcode == 0xd6) { CBInstruction.set(cpu, mem, 2, "(HL)"); }
    else if (cbOpcode == 0xd7) { CBInstruction.set(cpu, mem, 2, "A"); }
    else if (cbOpcode == 0xd8) { CBInstruction.set(cpu, mem, 3, "B"); }
    else if (cbOpcode == 0xd9) { CBInstruction.set(cpu, mem, 3, "C"); }
    else if (cbOpcode == 0xda) { CBInstruction.set(cpu, mem, 3, "D"); }
    else if (cbOpcode == 0xdb) { CBInstruction.set(cpu, mem, 3, "E"); }
    else if (cbOpcode == 0xdc) { CBInstruction.set(cpu, mem, 3, "H"); }
    else if (cbOpcode == 0xdd) { CBInstruction.set(cpu, mem, 3, "L"); }
    else if (cbOpcode == 0xde) { CBInstruction.set(cpu, mem, 3, "(HL)"); }
    else if (cbOpcode == 0xdf) { CBInstruction.set(cpu, mem, 3, "A"); }
    
    else if (cbOpcode == 0xe0) { CBInstruction.set(cpu, mem, 4, "B"); }
    else if (cbOpcode == 0xe1) { CBInstruction.set(cpu, mem, 4, "C"); }
    else if (cbOpcode == 0xe2) { CBInstruction.set(cpu, mem, 4, "D"); }
    else if (cbOpcode == 0xe3) { CBInstruction.set(cpu, mem, 4, "E"); }
    else if (cbOpcode == 0xe4) { CBInstruction.set(cpu, mem, 4, "H"); }
    else if (cbOpcode == 0xe5) { CBInstruction.set(cpu, mem, 4, "L"); }
    else if (cbOpcode == 0xe6) { CBInstruction.set(cpu, mem, 4, "(HL)"); }
    else if (cbOpcode == 0xe7) { CBInstruction.set(cpu, mem, 4, "A"); }
    else if (cbOpcode == 0xe8) { CBInstruction.set(cpu, mem, 5, "B"); }
    else if (cbOpcode == 0xe9) { CBInstruction.set(cpu, mem, 5, "C"); }
    else if (cbOpcode == 0xea) { CBInstruction.set(cpu, mem, 5, "D"); }
    else if (cbOpcode == 0xeb) { CBInstruction.set(cpu, mem, 5, "E"); }
    else if (cbOpcode == 0xec) { CBInstruction.set(cpu, mem, 5, "H"); }
    else if (cbOpcode == 0xed) { CBInstruction.set(cpu, mem, 5, "L"); }
    else if (cbOpcode == 0xee) { CBInstruction.set(cpu, mem, 5, "(HL)"); }
    else if (cbOpcode == 0xef) { CBInstruction.set(cpu, mem, 5, "A"); }
    
    else if (cbOpcode == 0xf0) { CBInstruction.set(cpu, mem, 6, "B"); }
    else if (cbOpcode == 0xf1) { CBInstruction.set(cpu, mem, 6, "C"); }
    else if (cbOpcode == 0xf2) { CBInstruction.set(cpu, mem, 6, "D"); }
    else if (cbOpcode == 0xf3) { CBInstruction.set(cpu, mem, 6, "E"); }
    else if (cbOpcode == 0xf4) { CBInstruction.set(cpu, mem, 6, "H"); }
    else if (cbOpcode == 0xf5) { CBInstruction.set(cpu, mem, 6, "L"); }
    else if (cbOpcode == 0xf6) { CBInstruction.set(cpu, mem, 6, "(HL)"); }
    else if (cbOpcode == 0xf7) { CBInstruction.set(cpu, mem, 6, "A"); }
    else if (cbOpcode == 0xf8) { CBInstruction.set(cpu, mem, 7, "B"); }
    else if (cbOpcode == 0xf9) { CBInstruction.set(cpu, mem, 7, "C"); }
    else if (cbOpcode == 0xfa) { CBInstruction.set(cpu, mem, 7, "D"); }
    else if (cbOpcode == 0xfb) { CBInstruction.set(cpu, mem, 7, "E"); }
    else if (cbOpcode == 0xfc) { CBInstruction.set(cpu, mem, 7, "H"); }
    else if (cbOpcode == 0xfd) { CBInstruction.set(cpu, mem, 7, "L"); }
    else if (cbOpcode == 0xfe) { CBInstruction.set(cpu, mem, 7, "(HL)"); }
    else if (cbOpcode == 0xff) { CBInstruction.set(cpu, mem, 7, "A"); }
    else { throw new IllegalArgumentException("Invalid CB opcode"); }
    
    cpu.incPC(CBInstruction.CB_INSTR_LEN);
  }
}
