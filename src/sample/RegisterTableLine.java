package sample;

public class RegisterTableLine {
    private String registerName;
    private short value;

    RegisterTableLine(String _registerName, short _value) {
        registerName = _registerName;
        value = _value;
    }

    public String getRegisterName() {
        return registerName;
    }

    public String getValue() {
        String ValueStr = "";
        Short I = value;

        do {
            if (I%16 >= 10) ValueStr = (char)(I%16+55) + ValueStr;
            else ValueStr = (char)(I%16+48) + ValueStr;
            I = (short)(I/16);
        } while (I > 15);
        if (I > 0 && I >= 10) ValueStr = (char)(I+55) + ValueStr;
        else if (I > 0) ValueStr = (char)(I+48) + ValueStr;
        return ValueStr;
    }
}
