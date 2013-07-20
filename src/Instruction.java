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
  
  // 00
  public static void nop() {
    // Do nothing
  }
  
  // 01 LD BC, d16
  public static void ldBCd16() {
    throw new UnsupportedOperationException();
  }
  
  // 02 LD (BC), A
  public static void ldaBCA() {
    throw new UnsupportedOperationException();
  }
  
  // 03 INC BC
  public static void incBC(CPU cpu) {
    int val = ((cpu.B << 8) | cpu.C) + 1;
    cpu.B = (val >> 8) & 0xff;
    cpu.C = val & 0xff;
  }
  
  // 04 INC B
  public static void incB(CPU cpu) {
    cpu.B++;
    cpu.flagZ = (cpu.B == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.B & 0x0f) == 0);
  }
  
  // 05 DEC B
  public static void decB(CPU cpu) {
    cpu.B--;
    cpu.flagZ = (cpu.B == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.B & 0x0f) == 0x0f);
  }
  
  // 06 LD B, d8
  public static void ldBd8() {
    throw new UnsupportedOperationException();
  }
  
  // 07 RLCA
  public static void rclA(CPU cpu) {
    cpu.flagC = cpu.A > 0x7f;
    cpu.A = ((cpu.A << 1) & 0xff) | (cpu.A >> 7);
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
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
  }
  
  // 0A LD A,(BC)
  public static void ldAaBC() {
    throw new UnsupportedOperationException();
  }
  
  // 0B DEC BC
  public static void decBC(CPU cpu) {
    int val = ((cpu.B << 8) | cpu.C) - 1;
    cpu.B = (val >> 8) & 0xff;
    cpu.C = val & 0xff;
  }
  
  // 0C INC C [Z 0 H -]
  public static void incC(CPU cpu) {
    cpu.C++;
    cpu.flagZ = (cpu.C == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.C & 0xff) == 0); 
  }
  
  // 0D DEC C [Z 1 H -]
  public static void decC(CPU cpu) {
    cpu.C--;
    cpu.flagZ = (cpu.C == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.C & 0x0f) == 0x0f);
  }
  
  // 0E LD C,d8 [- - - -]
  public static void ldCd8() {
    throw new UnsupportedOperationException();
  }
  
  // 0F RRCA [0 0 0 C]
  public static void rrcA(CPU cpu) {
    cpu.flagC = ((cpu.A & 0x01) == 0x1);
    cpu.A = (cpu.A >>> 1) | ((cpu.A & 0x01) << 7);
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
  }
  
  // 10 STOP [- - - -]
  public static void stop() {
    throw new UnsupportedOperationException();
  }
  
  // 11 LD DE,d16 [- - - -]
  public static void ldDEd16() {
    throw new UnsupportedOperationException();
  }
  
  // 12 LD (DE),A [- - - -]
  public static void LDaDEA() {
    throw new UnsupportedOperationException();
  }
  
  // 13 INC DE [- - - -]
  public static void incDE(CPU cpu) {
    int val = ((cpu.D << 8) | cpu.E) + 1;
    cpu.D = (val >> 8) & 0x0f;
    cpu.E = val & 0x0f;
  }
  
  // 14 INC D [Z 0 H -]
  public static void incD(CPU cpu) {
    cpu.D++;
    cpu.flagZ = (cpu.D == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.D & 0xff) == 0); 
  }
  
  // 15 DEC D [Z 1 H -]
  public static void decD(CPU cpu) {
    cpu.D--;
    cpu.flagZ = (cpu.D == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.D & 0x0f) == 0x0f);
  }
  
  // 16 LD D,d8 [- - - -]
  public static void ldDd8() {
    throw new UnsupportedOperationException();
  }
  
  // 17 RLA [0 0 0 C]
  public static void rlA(CPU cpu) {
    int bit = cpu.flagC ? 1 : 0;
    cpu.flagC = cpu.A > 0x7f;
    cpu.A = ((cpu.A << 1) | bit) & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
  }
  
  // 18 JR r8 [- - - -]
  public static void jrR8() {
    throw new UnsupportedOperationException();
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
  }
  
  // 1A LD A,(DE) [- - - -]
  public static void ldAaDE() {
    throw new UnsupportedOperationException();
  }
  
  // 1B DEC DE [- - - -]
  public static void decDE(CPU cpu) {
    int val = ((cpu.D << 8) | cpu.E) - 1;
    cpu.D = (val >> 8) & 0xff;
    cpu.E = val & 0xff;
  }
  
  // 1C INC E [Z 0 H -]
  public static void incE(CPU cpu) {
    cpu.E++;
    cpu.flagZ = (cpu.E == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.E & 0xff) == 0);
  }
  
  // 1D DEC E [Z 1 H -]
  public static void decE(CPU cpu) {
    cpu.E--;
    cpu.flagZ = (cpu.E == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.E & 0x0f) == 0x0f);
  }
  
  // 1E LD E,d8 [- - - -]
  public static void ldEd8() {
    throw new UnsupportedOperationException();
  }
  
  // 1F RRA [0 0 0 C]
  public static void rrA(CPU cpu) {
    int bit = cpu.flagC ? 0x80 : 0;
    cpu.flagC = ((cpu.A & 0x01) == 0x1);
    cpu.A = ((cpu.A >>> 1) | bit) & 0xff;
    cpu.flagZ = false;
    cpu.flagN = false;
    cpu.flagH = false;
  }
  
  // 20 JR NZ,r8 [- - - -]
  public static void jrNZr8() {
    throw new UnsupportedOperationException();
  }
  
  // 21 LD HL,d16 [- - - -]
  public static void ldHLd16() {
    throw new UnsupportedOperationException();
  }
  
  // 22 LD (HL+),A [- - - -]
  public static void ldHLIA() {
    throw new UnsupportedOperationException();
  }
  
  // 23 INC HL [- - - -]
  public static void incHL(CPU cpu) {
    int val = ((cpu.H << 8) | cpu.L) + 1;
    cpu.H = (val >> 8) & 0xff;
    cpu.L = val & 0xff;
  }
  
  // 24 INC H [Z 0 H -]
  public static void incH(CPU cpu) {
    cpu.H++;
    cpu.flagZ = (cpu.H == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.H & 0xff) == 0);
  }
  
  // 25 DEC H [Z 1 H -]
  public static void decH(CPU cpu) {
    cpu.H--;
    cpu.flagZ = (cpu.H == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.H & 0x0f) == 0x0f);
  }
  
  // 26 LD H,d8 [- - - -]
  public static void ldHd8() {
    throw new UnsupportedOperationException();
  }
  
  // 27 DAA [Z - 0 C]
  public static void daA() {
    throw new UnsupportedOperationException();
  }
  
  // 28 JR Z,r8 [- - - -]
  public static void jrZr8() {
    throw new UnsupportedOperationException();
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
  }
  
  // 2C INC L [Z 0 H -]
  public static void incL(CPU cpu) {
    cpu.L++;
    cpu.flagZ = (cpu.L == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.L & 0xff) == 0);
  }
  
  // 2D DEC L [Z 1 H -]
  public static void decL(CPU cpu) {
    cpu.L--;
    cpu.flagZ = (cpu.L == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.L & 0x0f) == 0x0f);
  }
  
  // 2E LD L,d8 [- - - -]
  public static void ldLd8() {
    throw new UnsupportedOperationException();
  }
  
  // 2F CPL [- 1 1 -]
  public static void cpl(CPU cpu) {
    cpu.A = ~cpu.A & 0xff;
    cpu.flagN = true;
    cpu.flagH = true;
  }
  
  // 30 JR NC,r8 [- - - -]
  public static void jrNCr8() {
    throw new UnsupportedOperationException();
  }
  
  // 31 LD SP,d16 [- - - -]
  public static void ldSPd16() {
    throw new UnsupportedOperationException();
  }
  
  // 32 LD (HL-),A [- - - -]
  public static void ldaHLDA() {
    throw new UnsupportedOperationException();
  }
  
  // 33 INC SP [- - - -]
  public static void incSP(CPU cpu) {
    cpu.sp = (cpu.sp + 1) & 0xffff;
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
  }
  
  // 38 JR C,r8 [- - - -]
  public static void jrCr8() {
    throw new UnsupportedOperationException();
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
  }
  
  // 3A LD A,(HL-) [- - - -]
  public static void ldAaHLD() {
    throw new UnsupportedOperationException();
  }
  
  // 3B DEC SP [- - - -]
  public static void decSP(CPU cpu) {
    cpu.sp--;
  }
  
  // 3C INC A [Z 0 H -]
  public static void incA(CPU cpu) {
    cpu.A++;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = ((cpu.A & 0xff) == 0);
  }
  
  // 3D DEC A [Z 1 H -]
  public static void decA(CPU cpu) {
    cpu.A--;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = true;
    cpu.flagH = ((cpu.A & 0x0f) == 0x0f);
  }
  
  // 3E LD A,d8 [- - - -]
  public static void ldAd8() {
    throw new UnsupportedOperationException();
  }
  
  // 3F CCF [- 0 0 C]
  public static void ccf(CPU cpu) {
    cpu.flagC = !cpu.flagC;
  }
  
  // 40 LD B,B [- - - -]
  public static void ldBB(CPU cpu) {
    
  }
  
  // 41 LD B,C [- - - -]
  public static void ldBC(CPU cpu) {
    cpu.B = cpu.C;
  }  
  
  // 42 LD B,D [- - - -]
  public static void ldBD(CPU cpu) {
    cpu.B = cpu.D;
  }
  
  // 43 LD B,E [- - - -]
  public static void ldBE(CPU cpu) {
    cpu.B = cpu.E;
  }
  
  // 44 LD B,H [- - - -]
  public static void ldBH(CPU cpu) {
    cpu.B = cpu.H;
  }
  
  // 45 LD B,L [- - - -]
  public static void ldBL(CPU cpu) {
    cpu.B = cpu.L;
  }
  
  // 46 LD B,(HL) [- - - -]
  public static void ldBaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 47 LD B,A [- - - -]
  public static void ldBA(CPU cpu) {
    cpu.B = cpu.A;
  }
  
  // 48 LD C,B [- - - -]
  public static void ldCB(CPU cpu) {
    cpu.C = cpu.B;
  }
  
  // 49 LD C,C [- - - -]
  public static void ldCC(CPU cpu) {
    
  }
  
  // 4A LD C,D [- - - -]
  public static void ldCD(CPU cpu) {
    cpu.C = cpu.D;
  }
  
  // 4B LD C,E [- - - -]
  public static void ldCE(CPU cpu) {
    cpu.C = cpu.E;
  }
  
  // 4C LD C,H [- - - -]
  public static void ldCH(CPU cpu) {
    cpu.C = cpu.H;
  }
  
  // 4D LD C,L [- - - -]
  public static void ldCL(CPU cpu) {
    cpu.C = cpu.L;
  }
  
  // 4E LD C,(HL) [- - - -]
  public static void ldCaHL() {
    throw new UnsupportedOperationException();
  }
  
  // 4F LD C,A [- - - -]
  public static void ldCA(CPU cpu) {
    cpu.C = cpu.A;
  }
  
  // 50 LD D,B [- - - -]
  public static void ldDB(CPU cpu) {
    cpu.D = cpu.B;
  }

  // 51 LD D,C [- - - -]
  public static void ldDC(CPU cpu) {
    cpu.D = cpu.C;
  }
  
  // 52 LD D,D [- - - -]
  public static void ldDD(CPU cpu) {
    
  }
  
  // 53 LD D,E [- - - -]
  public static void ldDE(CPU cpu) {
    cpu.D = cpu.E;
  }
  
  // 54 LD D,H [- - - -]
  public static void ldDH(CPU cpu) {
    cpu.D = cpu.H;
  }
  
  // 55 LD D,L [- - - -]
  public static void ldDL(CPU cpu) {
    cpu.D = cpu.L;
  }
  
  // 56 LD D,(HL) [- - - -]
  public static void ldDaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 57 LD D,A [- - - -]
  public static void ldDA(CPU cpu) {
    cpu.D = cpu.A;
  }
  
  // 58 LD E,B [- - - -]
  public static void ldEB(CPU cpu) {
    cpu.E = cpu.B;
  }
  
  // 59 LD E,C [- - - -]
  public static void ldEC(CPU cpu) {
    cpu.E = cpu.C;
  }
  
  // 5A LD E,D [- - - -]
  public static void ldED(CPU cpu) {
    cpu.E = cpu.D;
  }
  
  // 5B LD E,E [- - - -]
  public static void ldEE(CPU cpu) {
    
  }
  
  // 5C LD E,H [- - - -]
  public static void ldEH(CPU cpu) {
    cpu.E = cpu.H;
  }
  
  // 5D LD E,L [- - - -]
  public static void ldEL(CPU cpu) {
    cpu.E = cpu.L;
  }
  
  // 5E LD E,(HL) [- - - -]
  public static void ldEaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 5F LD E,A [- - - -]
  public static void ldEA(CPU cpu) {
    cpu.E = cpu.A;
  }
  
  // 60 LD H,B [- - - -]
  public static void ldHB(CPU cpu) {
    cpu.H = cpu.B;
  }
  
  // 61 LD H,C [- - - -]
  public static void ldHC(CPU cpu) {
    cpu.H = cpu.C;
  }
  
  // 62 LD H,D [- - - -]
  public static void ldHD(CPU cpu) {
    cpu.H = cpu.D;
  }

  // 63 LD H,E [- - - -]
  public static void ldHE(CPU cpu) {
    cpu.H = cpu.E;
  }
  
  // 64 LD H,H [- - - -]
  public static void ldHH(CPU cpu) {
    
  }

  // 65 LD H,L [- - - -]
  public static void ldHL(CPU cpu) {
    cpu.H = cpu.L;
  }
  
  // 66 LD H,(HL) [- - - -]
  public static void ldHaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 67 LD H,A [- - - -]
  public static void ldHA(CPU cpu) {
    cpu.H = cpu.A;
  }

  // 68 LD L,B [- - - -]
  public static void ldLB(CPU cpu) {
    cpu.L = cpu.B;
  }

  // 69 LD L,C [- - - -]
  public static void ldLC(CPU cpu) {
    cpu.L = cpu.C;
  }
  
  // 6A LD L,D [- - - -]
  public static void ldLD(CPU cpu) {
    cpu.L = cpu.D;
  }
  
  // 6B LD L,E [- - - -]
  public static void ldLE(CPU cpu) {
    cpu.L = cpu.E;
  }
  
  // 6C LD L,H [- - - -]
  public static void ldLH(CPU cpu) {
    cpu.L = cpu.H;
  }
  
  // 6D LD L,L [- - - -]
  public static void ldLL(CPU cpu) {
    
  }
  
  // 6E LD L,(HL) [- - - -]
  public static void ldaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 6F LD L,A [- - - -]
  public static void ldLA(CPU cpu) {
    cpu.L = cpu.A;
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
  }

  // 78 LD A,C [- - - -]
  public static void ldAC(CPU cpu) {
    cpu.A = cpu.C;
  }
  
  // 78 LD A,D [- - - -]
  public static void ldAD(CPU cpu) {
    cpu.A = cpu.D;
  }
  
  // 78 LD A,E [- - - -]
  public static void ldAE(CPU cpu) {
    cpu.A = cpu.E;
  }
  
  // 78 LD A,H [- - - -]
  public static void ldAH(CPU cpu) {
    cpu.A = cpu.H;
  }
  
  // 78 LD A,L [- - - -]
  public static void ldAL(CPU cpu) {
    cpu.A = cpu.L;
  }
  
  // 78 LD A,(HL) [- - - -]
  public static void ldAaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 7F LD A,A [- - - -]
  public static void ldAA(CPU cpu) {
    
  }
  
  // 80 ADD A,B [Z 0 H C]
  public static void addAB(CPU cpu) {
    int res = cpu.A + cpu.B;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.B & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
  }
  
  // 81 ADD A,C [- - - -]
  public static void addAC(CPU cpu) {
    int res = cpu.A + cpu.C;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.C & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;   
  }
  
  // 82 ADD A,D [- - - -]
  public static void addAD(CPU cpu) {
    int res = cpu.A + cpu.D;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.D & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
  }
  
  // 83 ADD A,E [- - - -]
  public static void addAE(CPU cpu) {
    int res = cpu.A + cpu.E;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.E & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
  }
  
  // 84 ADD A,H [- - - -]
  public static void addAH(CPU cpu) {
    int res = cpu.A + cpu.H;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.H & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
  }
  
  // 85 ADD A,L [- - - -]
  public static void addAL(CPU cpu) {
    int res = cpu.A + cpu.L;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.L & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
  }
  
  // 86 ADD A,(HL) [- - - -]
  public static void addAaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 87 ADD A,A [- - - -]
  public static void addAA(CPU cpu) {
    int res = cpu.A + cpu.A;
    cpu.flagH = (cpu.A & 0x0f) + (cpu.A & 0x0f) > 0x0f;
    cpu.A = res & 0xff;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagC = res > 0xff;
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
  }
  
  // 8E ADC A,(HL) [Z 0 H C]
  public static void adcAaHL(CPU cpu) {
    throw new UnsupportedOperationException();
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
  }
  
  // 90 SUB B [Z 1 H C]
  public static void subB(CPU cpu) {
    int res = cpu.A - cpu.B;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.B & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 91 SUB C [Z 1 H C]
  public static void subC(CPU cpu) {
    int res = cpu.A - cpu.C;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.C & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 92 SUB D [Z 1 H C]
  public static void subD(CPU cpu) {
    int res = cpu.A - cpu.D;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.D & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 93 SUB E [Z 1 H C]
  public static void subE(CPU cpu) {
    int res = cpu.A - cpu.E;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.E & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 94 SUB H [Z 1 H C]
  public static void subH(CPU cpu) {
    int res = cpu.A - cpu.H;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.H & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 95 SUB L [Z 1 H C]
  public static void subL(CPU cpu) {
    int res = cpu.A - cpu.L;
    cpu.flagZ = (res == 0);
    cpu.flagH = (cpu.A & 0x0f) < (cpu.L & 0x0f);
    cpu.flagN = true;
    cpu.flagC = (res < 0);
    cpu.A = res;
  }
  
  // 96 SUB (HL) [Z 1 H C]
  public static void subaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // 97 SUB A [Z 1 H C]
  public static void subA(CPU cpu) {
    cpu.flagZ = true;
    cpu.flagH = false;
    cpu.flagN = true;
    cpu.flagC = false;
    cpu.A = 0;
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
  }
  
  // 9E SBC A,(HL) [Z 1 H C]
  public static void sbcAaHL(CPU cpu) {
    throw new UnsupportedOperationException();
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
  }
  
  // A0 AND B [Z 0 1 0]
  public static void andB(CPU cpu) {
    cpu.A &= cpu.B;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A1 AND C [Z 0 1 0]
  public static void andC(CPU cpu) {
    cpu.A &= cpu.C;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A2 AND D [Z 0 1 0]
  public static void andD(CPU cpu) {
    cpu.A &= cpu.D;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A3 AND E [Z 0 1 0]
  public static void andE(CPU cpu) {
    cpu.A &= cpu.E;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A4 AND H [Z 0 1 0]
  public static void andH(CPU cpu) {
    cpu.A &= cpu.H;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A5 AND L [Z 0 1 0]
  public static void andL(CPU cpu) {
    cpu.A &= cpu.L;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A6 AND (HL) [Z 0 1 0]
  public static void andaHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // A7 AND A [Z 0 1 0]
  public static void andA(CPU cpu) {
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = true;
    cpu.flagC = false;
  }
  
  // A8 XOR B [Z 0 0 0]
  public static void xorB(CPU cpu) {
    cpu.A ^= cpu.B;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // A9 XOR C [Z 0 0 0]
  public static void xorC(CPU cpu) {
    cpu.A ^= cpu.C;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // AA XOR D [Z 0 0 0]
  public static void xorD(CPU cpu) {
    cpu.A ^= cpu.D;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // AB XOR E [Z 0 0 0]
  public static void xorE(CPU cpu) {
    cpu.A ^= cpu.E;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // AC XOR H [Z 0 0 0]
  public static void xorH(CPU cpu) {
    cpu.A ^= cpu.H;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // AD XOR L [Z 0 0 0]
  public static void xorL(CPU cpu) {
    cpu.A ^= cpu.L;
    cpu.flagZ = (cpu.A == 0);
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
  
  // A8 XOR (HL) [Z 0 0 0]
  public static void xoraHL(CPU cpu) {
    throw new UnsupportedOperationException();
  }
  
  // AF XOR A [Z 0 0 0]
  public static void xorA(CPU cpu) {
    cpu.A = 0;
    cpu.flagZ = true;
    cpu.flagN = false;
    cpu.flagH = false;
    cpu.flagC = false;
  }
}