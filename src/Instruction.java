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
  
  
}
