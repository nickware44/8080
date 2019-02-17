package sample;

public class MemorySystem {
    private short Memory[];

    MemorySystem() {
        Memory = new short[65536];
    }

    public void setMemoryValue(int Address, short Value) {
        Memory[Address] = Value;
    }

    public short getMemoryValue(int Address) {
        return Memory[Address];
    }
}
