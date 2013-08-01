package Tests;

import static org.junit.Assert.*;

import org.junit.Test;
import Code.*;

public class TestInstructions {
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
  
  
  @Test
  public void test_00() {
    int code = 0x00;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();
    
    m.memSpace[c.getPC()] = code;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertTrue(c.toString().equals(oldState));
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }
  
  @Test
  public void test_01() {
    int code = 0x01;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();
    
    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xfe;
    m.memSpace[c.getPC() + 2] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.B);
    assertEquals(0xfe, c.C);
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_02() {
    int code = 0x02;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();
    
    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.B = 0xde;
    c.C = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.A);
    assertEquals(m.memSpace[0xdead], 0xca);
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_03() {
    int code = 0x03;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();
    
    m.memSpace[c.getPC()] = code;
    c.B = 0xde;
    c.C = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.B);
    assertEquals(0xae, c.C);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_04() {
    int code = 0x04;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.B = 0xff;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0x00, c.B);
    
    assertTrue(c.flagZ);
    assertFalse(c.flagN);
    assertTrue(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_05() {
    int code = 0x05;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.B = 0x00;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xff, c.B);
    
    assertFalse(c.flagZ);
    assertTrue(c.flagN);
    assertTrue(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_06() {
    int code = 0x06;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.B);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_07() {
    int code = 0x07;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.flagC = false;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0x95, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertTrue(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_08() {
    int code = 0x08;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0x00;
    m.memSpace[c.getPC() + 2] = 0xc1;
    c.sp = 0xfff8;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xf8, m.memSpace[0xc100]);
    assertEquals(0xff, m.memSpace[0xc101]);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_09() {
    int code = 0x09;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.H = 0xca;
    c.L = 0xfe;
    c.B = 0xde;
    c.C = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.B);
    assertEquals(0xad, c.C);
    assertEquals(0xa9, c.H);
    assertEquals(0xab, c.L);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertTrue(c.flagH);
    assertTrue(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_0a() {
    int code = 0x0a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.B = 0xca;
    c.C = 0xfe;
    m.memSpace[0xcafe] = 0xde;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_0b() {
    int code = 0x0b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.B = 0xca;
    c.C = 0xfe;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.B);
    assertEquals(0xfd, c.C);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_0c() {
    int code = 0x0c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.C = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xcb, c.C);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_0d() {
    int code = 0x0d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.C = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xc9, c.C);
    
    assertFalse(c.flagZ);
    assertTrue(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_0e() {
    int code = 0x0e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.C);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());   
  }

  @Test
  public void test_0f() {
    int code = 0x0f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.flagC = false;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0x65, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_10() {
    int code = 0x10;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_11() {
    int code = 0x11;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xfe;
    m.memSpace[c.getPC() + 2] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.D);
    assertEquals(0xfe, c.E);
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_12() {
    int code = 0x12;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.D = 0xde;
    c.E = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.A);
    assertEquals(0xca, m.memSpace[0xdead]);
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_13() {
    int code = 0x13;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.D = 0xde;
    c.E = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.D);
    assertEquals(0xae, c.E);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_14() {
    int code = 0x14;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.D = 0xff;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0x00, c.D);
    
    assertTrue(c.flagZ);
    assertFalse(c.flagN);
    assertTrue(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_15() {
    int code = 0x15;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.D = 0x00;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xff, c.D);
    
    assertFalse(c.flagZ);
    assertTrue(c.flagN);
    assertTrue(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_16() {
    int code = 0x16;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.D);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_17() {
    int code = 0x17;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.flagC = false;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0x94, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertTrue(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_18() {
    int code = 0x18;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();
    c.changePC(0xff); // Shift PC forward so that the relative jump isn't the same as an abs jump
    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(oldPC + 0xffffffca, c.getPC());
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    
    c.changePC(0xff); // Shift PC forward so that the relative jump isn't the same as an abs jump
    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0x6a;
    
    oldState = c.toString();
    oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(oldPC + 0x6a, c.getPC());
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
  }

  @Test
  public void test_19() {
    int code = 0x19;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.H = 0xca;
    c.L = 0xfe;
    c.D = 0xde;
    c.E = 0xad;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.D);
    assertEquals(0xad, c.E);
    assertEquals(0xa9, c.H);
    assertEquals(0xab, c.L);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertTrue(c.flagH);
    assertTrue(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1a() {
    int code = 0x1a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.D = 0xca;
    c.E = 0xfe;
    m.memSpace[0xcafe] = 0xde;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xde, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1b() {
    int code = 0x1b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.D = 0xca;
    c.E = 0xfe;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.D);
    assertEquals(0xfd, c.E);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1c() {
    int code = 0x1c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.E = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xcb, c.E);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1d() {
    int code = 0x1d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.E = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xc9, c.E);
    
    assertFalse(c.flagZ);
    assertTrue(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1e() {
    int code = 0x1e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    m.memSpace[c.getPC() + 1] = 0xca;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xca, c.E);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_1f() {
    int code = 0x1f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    c.A = 0xca;
    c.flagC = true;
    
    String oldState = c.toString();
    int oldPC = c.getPC();
    ISA.executeInstr(c, m);
    assertEquals(0xe5, c.A);
    
    assertFalse(c.flagZ);
    assertFalse(c.flagN);
    assertFalse(c.flagH);
    assertFalse(c.flagC);
    assertEquals(oldPC + INSTR_LEN[code], c.getPC());
  }

  @Test
  public void test_20() {
    int code = 0x20;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x20");
  }

  @Test
  public void test_21() {
    int code = 0x21;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x21");
  }

  @Test
  public void test_22() {
    int code = 0x22;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x22");
  }

  @Test
  public void test_23() {
    int code = 0x23;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x23");
  }

  @Test
  public void test_24() {
    int code = 0x24;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x24");
  }

  @Test
  public void test_25() {
    int code = 0x25;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x25");
  }

  @Test
  public void test_26() {
    int code = 0x26;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x26");
  }

  @Test
  public void test_27() {
    int code = 0x27;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x27");
  }

  @Test
  public void test_28() {
    int code = 0x28;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x28");
  }

  @Test
  public void test_29() {
    int code = 0x29;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x29");
  }

  @Test
  public void test_2a() {
    int code = 0x2a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2a");
  }

  @Test
  public void test_2b() {
    int code = 0x2b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2b");
  }

  @Test
  public void test_2c() {
    int code = 0x2c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2c");
  }

  @Test
  public void test_2d() {
    int code = 0x2d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2d");
  }

  @Test
  public void test_2e() {
    int code = 0x2e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2e");
  }

  @Test
  public void test_2f() {
    int code = 0x2f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x2f");
  }

  @Test
  public void test_30() {
    int code = 0x30;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x30");
  }

  @Test
  public void test_31() {
    int code = 0x31;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x31");
  }

  @Test
  public void test_32() {
    int code = 0x32;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x32");
  }

  @Test
  public void test_33() {
    int code = 0x33;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x33");
  }

  @Test
  public void test_34() {
    int code = 0x34;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x34");
  }

  @Test
  public void test_35() {
    int code = 0x35;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x35");
  }

  @Test
  public void test_36() {
    int code = 0x36;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x36");
  }

  @Test
  public void test_37() {
    int code = 0x37;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x37");
  }

  @Test
  public void test_38() {
    int code = 0x38;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x38");
  }

  @Test
  public void test_39() {
    int code = 0x39;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x39");
  }

  @Test
  public void test_3a() {
    int code = 0x3a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3a");
  }

  @Test
  public void test_3b() {
    int code = 0x3b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3b");
  }

  @Test
  public void test_3c() {
    int code = 0x3c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3c");
  }

  @Test
  public void test_3d() {
    int code = 0x3d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3d");
  }

  @Test
  public void test_3e() {
    int code = 0x3e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3e");
  }

  @Test
  public void test_3f() {
    int code = 0x3f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x3f");
  }

  @Test
  public void test_40() {
    int code = 0x40;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x40");
  }

  @Test
  public void test_41() {
    int code = 0x41;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x41");
  }

  @Test
  public void test_42() {
    int code = 0x42;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x42");
  }

  @Test
  public void test_43() {
    int code = 0x43;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x43");
  }

  @Test
  public void test_44() {
    int code = 0x44;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x44");
  }

  @Test
  public void test_45() {
    int code = 0x45;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x45");
  }

  @Test
  public void test_46() {
    int code = 0x46;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x46");
  }

  @Test
  public void test_47() {
    int code = 0x47;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x47");
  }

  @Test
  public void test_48() {
    int code = 0x48;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x48");
  }

  @Test
  public void test_49() {
    int code = 0x49;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x49");
  }

  @Test
  public void test_4a() {
    int code = 0x4a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4a");
  }

  @Test
  public void test_4b() {
    int code = 0x4b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4b");
  }

  @Test
  public void test_4c() {
    int code = 0x4c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4c");
  }

  @Test
  public void test_4d() {
    int code = 0x4d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4d");
  }

  @Test
  public void test_4e() {
    int code = 0x4e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4e");
  }

  @Test
  public void test_4f() {
    int code = 0x4f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x4f");
  }

  @Test
  public void test_50() {
    int code = 0x50;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x50");
  }

  @Test
  public void test_51() {
    int code = 0x51;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x51");
  }

  @Test
  public void test_52() {
    int code = 0x52;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x52");
  }

  @Test
  public void test_53() {
    int code = 0x53;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x53");
  }

  @Test
  public void test_54() {
    int code = 0x54;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x54");
  }

  @Test
  public void test_55() {
    int code = 0x55;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x55");
  }

  @Test
  public void test_56() {
    int code = 0x56;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x56");
  }

  @Test
  public void test_57() {
    int code = 0x57;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x57");
  }

  @Test
  public void test_58() {
    int code = 0x58;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x58");
  }

  @Test
  public void test_59() {
    int code = 0x59;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x59");
  }

  @Test
  public void test_5a() {
    int code = 0x5a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5a");
  }

  @Test
  public void test_5b() {
    int code = 0x5b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5b");
  }

  @Test
  public void test_5c() {
    int code = 0x5c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5c");
  }

  @Test
  public void test_5d() {
    int code = 0x5d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5d");
  }

  @Test
  public void test_5e() {
    int code = 0x5e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5e");
  }

  @Test
  public void test_5f() {
    int code = 0x5f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x5f");
  }

  @Test
  public void test_60() {
    int code = 0x60;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x60");
  }

  @Test
  public void test_61() {
    int code = 0x61;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x61");
  }

  @Test
  public void test_62() {
    int code = 0x62;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x62");
  }

  @Test
  public void test_63() {
    int code = 0x63;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x63");
  }

  @Test
  public void test_64() {
    int code = 0x64;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x64");
  }

  @Test
  public void test_65() {
    int code = 0x65;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x65");
  }

  @Test
  public void test_66() {
    int code = 0x66;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x66");
  }

  @Test
  public void test_67() {
    int code = 0x67;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x67");
  }

  @Test
  public void test_68() {
    int code = 0x68;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x68");
  }

  @Test
  public void test_69() {
    int code = 0x69;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x69");
  }

  @Test
  public void test_6a() {
    int code = 0x6a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6a");
  }

  @Test
  public void test_6b() {
    int code = 0x6b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6b");
  }

  @Test
  public void test_6c() {
    int code = 0x6c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6c");
  }

  @Test
  public void test_6d() {
    int code = 0x6d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6d");
  }

  @Test
  public void test_6e() {
    int code = 0x6e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6e");
  }

  @Test
  public void test_6f() {
    int code = 0x6f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x6f");
  }

  @Test
  public void test_70() {
    int code = 0x70;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x70");
  }

  @Test
  public void test_71() {
    int code = 0x71;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x71");
  }

  @Test
  public void test_72() {
    int code = 0x72;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x72");
  }

  @Test
  public void test_73() {
    int code = 0x73;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x73");
  }

  @Test
  public void test_74() {
    int code = 0x74;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x74");
  }

  @Test
  public void test_75() {
    int code = 0x75;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x75");
  }

  @Test
  public void test_76() {
    int code = 0x76;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x76");
  }

  @Test
  public void test_77() {
    int code = 0x77;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x77");
  }

  @Test
  public void test_78() {
    int code = 0x78;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x78");
  }

  @Test
  public void test_79() {
    int code = 0x79;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x79");
  }

  @Test
  public void test_7a() {
    int code = 0x7a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7a");
  }

  @Test
  public void test_7b() {
    int code = 0x7b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7b");
  }

  @Test
  public void test_7c() {
    int code = 0x7c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7c");
  }

  @Test
  public void test_7d() {
    int code = 0x7d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7d");
  }

  @Test
  public void test_7e() {
    int code = 0x7e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7e");
  }

  @Test
  public void test_7f() {
    int code = 0x7f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x7f");
  }

  @Test
  public void test_80() {
    int code = 0x80;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x80");
  }

  @Test
  public void test_81() {
    int code = 0x81;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x81");
  }

  @Test
  public void test_82() {
    int code = 0x82;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x82");
  }

  @Test
  public void test_83() {
    int code = 0x83;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x83");
  }

  @Test
  public void test_84() {
    int code = 0x84;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x84");
  }

  @Test
  public void test_85() {
    int code = 0x85;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x85");
  }

  @Test
  public void test_86() {
    int code = 0x86;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x86");
  }

  @Test
  public void test_87() {
    int code = 0x87;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x87");
  }

  @Test
  public void test_88() {
    int code = 0x88;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x88");
  }

  @Test
  public void test_89() {
    int code = 0x89;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x89");
  }

  @Test
  public void test_8a() {
    int code = 0x8a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8a");
  }

  @Test
  public void test_8b() {
    int code = 0x8b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8b");
  }

  @Test
  public void test_8c() {
    int code = 0x8c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8c");
  }

  @Test
  public void test_8d() {
    int code = 0x8d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8d");
  }

  @Test
  public void test_8e() {
    int code = 0x8e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8e");
  }

  @Test
  public void test_8f() {
    int code = 0x8f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x8f");
  }

  @Test
  public void test_90() {
    int code = 0x90;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x90");
  }

  @Test
  public void test_91() {
    int code = 0x91;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x91");
  }

  @Test
  public void test_92() {
    int code = 0x92;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x92");
  }

  @Test
  public void test_93() {
    int code = 0x93;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x93");
  }

  @Test
  public void test_94() {
    int code = 0x94;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x94");
  }

  @Test
  public void test_95() {
    int code = 0x95;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x95");
  }

  @Test
  public void test_96() {
    int code = 0x96;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x96");
  }

  @Test
  public void test_97() {
    int code = 0x97;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x97");
  }

  @Test
  public void test_98() {
    int code = 0x98;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x98");
  }

  @Test
  public void test_99() {
    int code = 0x99;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x99");
  }

  @Test
  public void test_9a() {
    int code = 0x9a;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9a");
  }

  @Test
  public void test_9b() {
    int code = 0x9b;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9b");
  }

  @Test
  public void test_9c() {
    int code = 0x9c;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9c");
  }

  @Test
  public void test_9d() {
    int code = 0x9d;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9d");
  }

  @Test
  public void test_9e() {
    int code = 0x9e;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9e");
  }

  @Test
  public void test_9f() {
    int code = 0x9f;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0x9f");
  }

  @Test
  public void test_a0() {
    int code = 0xa0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa0");
  }

  @Test
  public void test_a1() {
    int code = 0xa1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa1");
  }

  @Test
  public void test_a2() {
    int code = 0xa2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa2");
  }

  @Test
  public void test_a3() {
    int code = 0xa3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa3");
  }

  @Test
  public void test_a4() {
    int code = 0xa4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa4");
  }

  @Test
  public void test_a5() {
    int code = 0xa5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa5");
  }

  @Test
  public void test_a6() {
    int code = 0xa6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa6");
  }

  @Test
  public void test_a7() {
    int code = 0xa7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa7");
  }

  @Test
  public void test_a8() {
    int code = 0xa8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa8");
  }

  @Test
  public void test_a9() {
    int code = 0xa9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xa9");
  }

  @Test
  public void test_aa() {
    int code = 0xaa;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xaa");
  }

  @Test
  public void test_ab() {
    int code = 0xab;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xab");
  }

  @Test
  public void test_ac() {
    int code = 0xac;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xac");
  }

  @Test
  public void test_ad() {
    int code = 0xad;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xad");
  }

  @Test
  public void test_ae() {
    int code = 0xae;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xae");
  }

  @Test
  public void test_af() {
    int code = 0xaf;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xaf");
  }

  @Test
  public void test_b0() {
    int code = 0xb0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb0");
  }

  @Test
  public void test_b1() {
    int code = 0xb1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb1");
  }

  @Test
  public void test_b2() {
    int code = 0xb2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb2");
  }

  @Test
  public void test_b3() {
    int code = 0xb3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb3");
  }

  @Test
  public void test_b4() {
    int code = 0xb4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb4");
  }

  @Test
  public void test_b5() {
    int code = 0xb5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb5");
  }

  @Test
  public void test_b6() {
    int code = 0xb6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb6");
  }

  @Test
  public void test_b7() {
    int code = 0xb7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb7");
  }

  @Test
  public void test_b8() {
    int code = 0xb8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb8");
  }

  @Test
  public void test_b9() {
    int code = 0xb9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xb9");
  }

  @Test
  public void test_ba() {
    int code = 0xba;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xba");
  }

  @Test
  public void test_bb() {
    int code = 0xbb;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xbb");
  }

  @Test
  public void test_bc() {
    int code = 0xbc;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xbc");
  }

  @Test
  public void test_bd() {
    int code = 0xbd;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xbd");
  }

  @Test
  public void test_be() {
    int code = 0xbe;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xbe");
  }

  @Test
  public void test_bf() {
    int code = 0xbf;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xbf");
  }

  @Test
  public void test_c0() {
    int code = 0xc0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc0");
  }

  @Test
  public void test_c1() {
    int code = 0xc1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc1");
  }

  @Test
  public void test_c2() {
    int code = 0xc2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc2");
  }

  @Test
  public void test_c3() {
    int code = 0xc3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc3");
  }

  @Test
  public void test_c4() {
    int code = 0xc4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc4");
  }

  @Test
  public void test_c5() {
    int code = 0xc5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc5");
  }

  @Test
  public void test_c6() {
    int code = 0xc6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc6");
  }

  @Test
  public void test_c7() {
    int code = 0xc7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc7");
  }

  @Test
  public void test_c8() {
    int code = 0xc8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc8");
  }

  @Test
  public void test_c9() {
    int code = 0xc9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xc9");
  }

  @Test
  public void test_ca() {
    int code = 0xca;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xca");
  }

  @Test
  public void test_cb() {
    int code = 0xcb;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xcb");
  }

  @Test
  public void test_cc() {
    int code = 0xcc;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xcc");
  }

  @Test
  public void test_cd() {
    int code = 0xcd;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xcd");
  }

  @Test
  public void test_ce() {
    int code = 0xce;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xce");
  }

  @Test
  public void test_cf() {
    int code = 0xcf;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xcf");
  }

  @Test
  public void test_d0() {
    int code = 0xd0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd0");
  }

  @Test
  public void test_d1() {
    int code = 0xd1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd1");
  }

  @Test
  public void test_d2() {
    int code = 0xd2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd2");
  }

  @Test
  public void test_d3() {
    int code = 0xd3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd3");
  }

  @Test
  public void test_d4() {
    int code = 0xd4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd4");
  }

  @Test
  public void test_d5() {
    int code = 0xd5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd5");
  }

  @Test
  public void test_d6() {
    int code = 0xd6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd6");
  }

  @Test
  public void test_d7() {
    int code = 0xd7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd7");
  }

  @Test
  public void test_d8() {
    int code = 0xd8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd8");
  }

  @Test
  public void test_d9() {
    int code = 0xd9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xd9");
  }

  @Test
  public void test_da() {
    int code = 0xda;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xda");
  }

  @Test
  public void test_db() {
    int code = 0xdb;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xdb");
  }

  @Test
  public void test_dc() {
    int code = 0xdc;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xdc");
  }

  @Test
  public void test_dd() {
    int code = 0xdd;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xdd");
  }

  @Test
  public void test_de() {
    int code = 0xde;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xde");
  }

  @Test
  public void test_df() {
    int code = 0xdf;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xdf");
  }

  @Test
  public void test_e0() {
    int code = 0xe0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe0");
  }

  @Test
  public void test_e1() {
    int code = 0xe1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe1");
  }

  @Test
  public void test_e2() {
    int code = 0xe2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe2");
  }

  @Test
  public void test_e3() {
    int code = 0xe3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe3");
  }

  @Test
  public void test_e4() {
    int code = 0xe4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe4");
  }

  @Test
  public void test_e5() {
    int code = 0xe5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe5");
  }

  @Test
  public void test_e6() {
    int code = 0xe6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe6");
  }

  @Test
  public void test_e7() {
    int code = 0xe7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe7");
  }

  @Test
  public void test_e8() {
    int code = 0xe8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe8");
  }

  @Test
  public void test_e9() {
    int code = 0xe9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xe9");
  }

  @Test
  public void test_ea() {
    int code = 0xea;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xea");
  }

  @Test
  public void test_eb() {
    int code = 0xeb;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xeb");
  }

  @Test
  public void test_ec() {
    int code = 0xec;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xec");
  }

  @Test
  public void test_ed() {
    int code = 0xed;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xed");
  }

  @Test
  public void test_ee() {
    int code = 0xee;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xee");
  }

  @Test
  public void test_ef() {
    int code = 0xef;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xef");
  }

  @Test
  public void test_f0() {
    int code = 0xf0;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf0");
  }

  @Test
  public void test_f1() {
    int code = 0xf1;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf1");
  }

  @Test
  public void test_f2() {
    int code = 0xf2;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf2");
  }

  @Test
  public void test_f3() {
    int code = 0xf3;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf3");
  }

  @Test
  public void test_f4() {
    int code = 0xf4;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf4");
  }

  @Test
  public void test_f5() {
    int code = 0xf5;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf5");
  }

  @Test
  public void test_f6() {
    int code = 0xf6;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf6");
  }

  @Test
  public void test_f7() {
    int code = 0xf7;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf7");
  }

  @Test
  public void test_f8() {
    int code = 0xf8;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf8");
  }

  @Test
  public void test_f9() {
    int code = 0xf9;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xf9");
  }

  @Test
  public void test_fa() {
    int code = 0xfa;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xfa");
  }

  @Test
  public void test_fb() {
    int code = 0xfb;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xfb");
  }

  @Test
  public void test_fc() {
    int code = 0xfc;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xfc");
  }

  @Test
  public void test_fd() {
    int code = 0xfd;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xfd");
  }

  @Test
  public void test_fe() {
    int code = 0xfe;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xfe");
  }

  @Test
  public void test_ff() {
    int code = 0xff;
    CPU c = new StubCPU();
    StubMemory m = new StubMemory();

    m.memSpace[c.getPC()] = code;
    fail("NYI: 0xff");
  }
}
