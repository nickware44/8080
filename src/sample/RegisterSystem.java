package sample;

public class RegisterSystem {
    private short [] CommonRegisters = new short[7];
    private boolean [] FlagsRegister = new boolean[8];

    RegisterSystem() {
        ResetRegisters();
    }

    public void setRegisterA(short Data) {
        CommonRegisters[0] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterB(short Data) {
        CommonRegisters[1] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterC(short Data) {
        CommonRegisters[2] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterD(short Data) {
        CommonRegisters[3] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterE(short Data) {
        CommonRegisters[4] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterH(short Data) {
        CommonRegisters[5] = (short)Math.abs(Data&0xFF);
    }

    public void setRegisterL(short Data) {
        CommonRegisters[6] = (short)Math.abs(Data&0xFF);
    }

    public short getRegisterA() {
        return CommonRegisters[0];
    }

    public short getRegisterB() {
        return CommonRegisters[1];
    }

    public short getRegisterC() {
        return CommonRegisters[2];
    }

    public short getRegisterD() {
        return CommonRegisters[3];
    }

    public short getRegisterE() {
        return CommonRegisters[4];
    }

    public short getRegisterH() {
        return CommonRegisters[5];
    }

    public short getRegisterL() {
        return CommonRegisters[6];
    }

    public void setCYFlag(boolean Value) {
        FlagsRegister[0]= Value;
    }

    public void setPFlag(boolean Value) {
        FlagsRegister[2]= Value;
    }

    public void setACFlag(boolean Value) {
        FlagsRegister[4]= Value;
    }

    public void setZFlag(boolean Value) {
        FlagsRegister[6]= Value;
    }


    public void setSFlag(boolean Value) {
        FlagsRegister[7]= Value;
    }

    public boolean getCYFlag() {
        return FlagsRegister[0];
    }

    public boolean getPFlag() {
        return FlagsRegister[2];
    }

    public boolean getACFlag() {
        return FlagsRegister[4];
    }

    public boolean getZFlag() {
        return FlagsRegister[6];
    }

    public boolean getSFlag() {
        return FlagsRegister[7];
    }

    public void ResetRegisters() {
        CommonRegisters = new short[7];
        FlagsRegister = new boolean[8];
    }
}
