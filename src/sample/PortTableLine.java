package sample;

public class PortTableLine {

    private short portNum;
    private short value;

    PortTableLine(short _portNum, short _value) {
        portNum = _portNum;
        value = _value;
    }

    public int getPortNum() {
        return portNum;
    }

    public String getValue() {
        String ValueStr = "";
        Short I = value;

        do {
            if (I%16 >= 10) ValueStr = (char)(I%16+55) + ValueStr;
            else ValueStr = (char)(I%16+48) + ValueStr;
            I = (short)(I/16);
        } while (I > 15);
        if (I >= 10) ValueStr = (char)(I+55) + ValueStr;
        else ValueStr = (char)(I+48) + ValueStr;
        return ValueStr;
    }
}
