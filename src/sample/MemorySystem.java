package sample;

public class MemorySystem {
    private short Memory[];
    private int Iterator;

    MemorySystem() {
        Memory = new short[65536];
        Iterator = 0;
    }

    public void ResetMemory() {
        Memory = new short[65536];
        Iterator = 0;
    }

    public void ResetIterator() {
        Iterator = 0;
    }

    public void setMemoryValue(int Address, short Value) {
        Memory[Address] = Value;
    }

    public void setMemoryValueNext(short Value) {
        Memory[Iterator] = Value;
        Iterator++;
    }

    public short getMemoryValue(int Address) {
        return Memory[Address];
    }
}
