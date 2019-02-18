package sample;

import java.util.ArrayList;
import java.util.List;

public class Command {
    private String Byte;
    private String ByteRegEx;
    private String Mnemonic;
    private String MnemonicRegEx;
    private int OperandCount;
    private int OperandCap;

    Command(String _Byte, String _Mnemonic, int _OperandCount, int _OperandCap) {
        Byte = _Byte;
        Mnemonic = _Mnemonic;
        OperandCount = _OperandCount;
        OperandCap = _OperandCap;

        ByteRegEx = Byte + " ([0-9A-F]{"+2*OperandCap+"})";
        for (int i = 0; i < OperandCount-1; i++) {
            ByteRegEx += " ([0-9A-F]{"+2*OperandCap+"})";
        }

        MnemonicRegEx = Mnemonic + " ([0-9A-F]{"+2*OperandCap+"})";
        for (int i = 0; i < OperandCount-1; i++) {
            MnemonicRegEx += ", ([0-9A-F]{"+2*OperandCap+"})";
        }
    }

    Command(String _Byte, String _Mnemonic) {
        Byte = _Byte;
        Mnemonic = _Mnemonic;
        OperandCount = 0;
        ByteRegEx = Byte;
        MnemonicRegEx = Mnemonic;
    }

    public List<Integer> Compare(String Comm) {
        if (Comm.matches(MnemonicRegEx)) {
            List<Integer> Operands = new ArrayList<>();
            String OperandsStr;
            int Pos = Comm.length();
            for (int i = 0; i < OperandCount; i++) {
                OperandsStr = Comm.substring(Pos-OperandCap*2, Pos);
                Pos = Pos - OperandCap*2 - 2;
                Operands.add(Str2Byte(OperandsStr));
            }
            return Operands;
        } else if (Comm.matches(ByteRegEx)) {
            List<Integer> Operands = new ArrayList<>();
            String OperandsStr;
            int Pos = Comm.length();
            for (int i = 0; i < OperandCount; i++) {
                OperandsStr = Comm.substring(Pos-OperandCap*2, Pos);
                Pos = Pos - OperandCap*2 - 1;
                Operands.add(Str2Byte(OperandsStr));
            }
            return Operands;
        }
        return null;
    }

    private int Str2Byte(String Str) {
        int B = 0;
        for (int i = 0; i < Str.length()/2; i++) {
            for (int j = 0; j < 2; j++) {
                char Current = Str.charAt(i*Str.length()/2+j);
                if (Current >= 'A' && Current <= 'F') {
                    B += ((Current-55)*(int)Math.pow(16, 1-j));
                } else if (Current >= '0' && Current <= '9') {
                    B += ((Current-48)*(int)Math.pow(16, 1-j));
                }
            }
        }
        //System.out.println(B);
        return B;
    }

    public String getCommandByteStr() {
        return Byte;
    }

    public short getCommandByte() {
        return (short)Str2Byte(Byte);
    }
}
