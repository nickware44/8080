package sample;

public class MemorySystem {
    private short [] Memory;
    private int Iterator;

    MemorySystem() {
        ResetMemory();
    }

    public void ResetMemory() {
        Memory = new short[65536];
        Iterator = 0;
    }

    public void ResetIterator() {
        Iterator = 0;
    }

    public void setMemoryValue(int Address, short Value) {
        Memory[Address] = (short)Math.abs(Value&0xFF);
    }

    public void setMemoryValueNext(short Value) {
        Memory[Iterator] = (short)Math.abs(Value&0xFF);
        Iterator++;
    }

    public short getMemoryValue(int Address) {
        return Memory[Address];
    }

    public short getMemoryValueLast() {
        if (Iterator == 0) Iterator++;
        Iterator++;
        return Memory[Iterator-2];
    }

    public void setIterator(int _Iterator) {
        Iterator = _Iterator;
    }

    public int getIterator() {
        return Iterator;
    }

    public void setMemory(short[] _Memory) {
        Memory = _Memory;
    }

    public short [] getMemory() {
        return Memory;
    }
}
