package sample;

import org.jetbrains.annotations.Contract;

public class MemoryTableLine {
    private int memoryAddress;
    private short value;

    MemoryTableLine(int _memoryAddress, short _value) {
        memoryAddress = _memoryAddress;
        value = _value;
    }

    @Contract(pure = true)
    public static String toHEX(int I, int Cap) {
        String ValueStr = "";
        do {
            if (I%16 >= 10) ValueStr = (char)(I%16+55) + ValueStr;
            else ValueStr = (char)(I%16+48) + ValueStr;
            I = (short)(I/16);
        } while (I > 15);
        if (I >= 10) ValueStr = (char)(I+55) + ValueStr;
        else ValueStr = (char)(I+48) + ValueStr;
        while (ValueStr.length() < Cap) ValueStr = "0" + ValueStr;
        return ValueStr;
    }

    public String getMemoryAddress() {
        return toHEX(memoryAddress, 4);
    }

    public String getValue() {
        return toHEX(value, 2);
    }
}
