package Tests;

import Code.CPU;

public class StubCPU extends CPU {
  public StubCPU() {
    super();

    A = 0x00;
    B = 0x00;
    C = 0x00;
    D = 0x00;
    E = 0x00;
    H = 0x00;
    L = 0x00;
    sp = 0xfffe;
    this.changePC(0x0000);
    
    flagZ = false;
    flagN = false;
    flagH = false;
    flagC = false;
  }
  
  public String toString() {
    String s = "" + this.flagZ + this.flagN + this.flagH + this.flagC + " ";
    s += A + "," + B + "," + C + "," + D + "," + E + "," + H + "," + L + "," + sp;
    return s;
  }
}