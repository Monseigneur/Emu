package Tests;

import Code.Memory;

public class StubMemory extends Memory {
  public int[] memSpace;
  
  public StubMemory() {
    super(null, null);
    
    memSpace = new int[1 << 16];
  }
  
  public int readMem(int offset) {
    return memSpace[offset];
  }
  
  public void writeMem(int val, int offset) {
    memSpace[offset] = val;
  }
}
