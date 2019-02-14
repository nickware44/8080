package sample;

public class RegisterSystem {
    private short [] Registers = new short[7];

    public void setRegisterA(short Data) {
        Registers[0] = Data;
    }

    public void setRegisterB(short Data) {
        Registers[1] = Data;
    }

    public void setRegisterC(short Data) {
        Registers[2] = Data;
    }

    public void setRegisterD(byte Data) {
        Registers[3] = Data;
    }

    public void setRegisterE(short Data) {
        Registers[4] = Data;
    }

    public void setRegisterH(short Data) {
        Registers[5] = Data;
    }

    public void setRegisterL(short Data) {
        Registers[6] = Data;
    }

    public short getRegisterA() {
        return Registers[0];
    }

    public short getRegisterB() {
        return Registers[1];
    }

    public short getRegisterC() {
        return Registers[2];
    }

    public short getRegisterD() {
        return Registers[3];
    }

    public short getRegisterE() {
        return Registers[4];
    }

    public short getRegisterH() {
        return Registers[5];
    }

    public short getRegisterL() {
        return Registers[6];
    }

    public short [] getRegisterM() {
        short [] M = new short[2];
        M[0] = Registers[5];
        M[1] = Registers[6];
        return M;
    }
}
