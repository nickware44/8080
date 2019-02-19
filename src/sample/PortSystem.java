package sample;

public class PortSystem {
    private short [] Port;

    PortSystem() {
        ResetPorts();
    }

    public void ResetPorts() {
        Port = new short[3];
    }

    public short ReadPort(short PortNum) {
        return Port[PortNum];
    }

    public void WritePort(short PortNum, short Value) {
        Port[PortNum] = (short)(Value&0xFF);
    }
}
