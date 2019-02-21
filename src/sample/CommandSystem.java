package sample;

import java.util.ArrayList;
import java.util.List;

public class CommandSystem {
    private ArrayList<Command> Commands = new ArrayList<>();
    private RegisterSystem RS;
    private MemorySystem MS;
    private PortSystem PS;
    private int CommandCounter;

    CommandSystem(RegisterSystem _RS, MemorySystem _MS, PortSystem _PS) {
        Commands.add(new Command("87", "ADD A"));
        Commands.add(new Command("80", "ADD B"));
        Commands.add(new Command("81", "ADD C"));
        Commands.add(new Command("82", "ADD D"));
        Commands.add(new Command("83", "ADD E"));
        Commands.add(new Command("84", "ADD H"));
        Commands.add(new Command("85", "ADD L"));
        Commands.add(new Command("C6", "ADI", 1, 1));

        Commands.add(new Command("2F", "CMA"));

        Commands.add(new Command("3C", "INR A"));
        Commands.add(new Command("04", "INR B"));
        Commands.add(new Command("0C", "INR C"));
        Commands.add(new Command("14", "INR D"));
        Commands.add(new Command("1C", "INR E"));
        Commands.add(new Command("24", "INR H"));
        Commands.add(new Command("2C", "INR L"));

        Commands.add(new Command("3D", "DCR A"));
        Commands.add(new Command("05", "DCR B"));
        Commands.add(new Command("0D", "DCR C"));
        Commands.add(new Command("15", "DCR D"));
        Commands.add(new Command("1D", "DCR E"));
        Commands.add(new Command("25", "DCR H"));
        Commands.add(new Command("2D", "DCR L"));

        Commands.add(new Command("3E", "MVI A,", 1, 1));
        Commands.add(new Command("06", "MVI B,", 1, 1));
        Commands.add(new Command("0E", "MVI C,", 1, 1));
        Commands.add(new Command("16", "MVI D,", 1, 1));
        Commands.add(new Command("1E", "MVI E,", 1, 1));
        Commands.add(new Command("26", "MVI H,", 1, 1));
        Commands.add(new Command("2E", "MVI L,", 1, 1));

        Commands.add(new Command("97", "SUB A"));
        Commands.add(new Command("90", "SUB B"));
        Commands.add(new Command("91", "SUB C"));
        Commands.add(new Command("92", "SUB D"));
        Commands.add(new Command("93", "SUB E"));
        Commands.add(new Command("94", "SUB H"));
        Commands.add(new Command("95", "SUB L"));
        Commands.add(new Command("D6", "SUI", 1, 1));

        Commands.add(new Command("DB", "IN", 1, 1));
        Commands.add(new Command("D3", "OUT", 1, 1));

        Commands.add(new Command("76", "HLT"));

        RS = _RS;
        MS = _MS;
        PS = _PS;

        CommandCounter = 0;
    }

    public int InputCommand(String Comm) {
        List<Integer> Operands;
        for (int i = 0; i < Commands.size(); i++) {
            Command C = Commands.get(i);
            if ((Operands = C.Compare(Comm)) != null) {
                MS.setMemoryValueNext(C.getCommandByte());
                //System.out.println(C.getCommandByteStr()+"~"+C.getCommandByte());
                for (int j = 0; j < Operands.size(); j++) MS.setMemoryValueNext(Operands.get(j).shortValue());
                //DoAction(C.getCommandByteStr(), Operands);
                CommandCounter++;
                return -1;
            }
        }
        return MS.getIterator();
    }

    public void ExecuteCommands() {
        MS.ResetIterator();
        for (int i = 0; i < CommandCounter; i++) DoAction(MS.getMemoryValueLast());
    }

    public void ExecuteNextCommand() {
        DoAction(MS.getMemoryValueLast());
    }

    private void DoAction(short CommandByte) {
        switch (CommandByte) {
            case 0x87:
                RS.setRegisterA((short)(2*RS.getRegisterA()));
                break;
            case 0x80:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterB()));
                break;
            case 0x81:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterC()));
                break;
            case 0x82:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterD()));
                break;
            case 0x83:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterE()));
                break;
            case 0x84:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterH()));
                break;
            case 0x85:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterL()));
                break;
            case 0x86:
                RS.setRegisterA((short)(RS.getRegisterA()+RS.getRegisterL()));
                break;
            case 0xC6:
                RS.setRegisterA((short)(RS.getRegisterA()+MS.getMemoryValueLast()));
                break;

            case 0x97:
                RS.setRegisterA((short)(0));
                break;
            case 0x90:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterB()));
                break;
            case 0x91:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterC()));
                break;
            case 0x92:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterD()));
                break;
            case 0x93:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterE()));
                break;
            case 0x94:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterH()));
                break;
            case 0x95:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterL()));
                break;
            case 0x96:
                RS.setRegisterA((short)(RS.getRegisterA()-RS.getRegisterL()));
                break;
            case 0xD6:
                RS.setRegisterA((short)(RS.getRegisterA()-MS.getMemoryValueLast()));
                break;

            case 0x3C:
                RS.setRegisterA((short)(RS.getRegisterA()+1));
                break;
            case 0x04:
                RS.setRegisterB((short)(RS.getRegisterB()+1));
                break;
            case 0x0C:
                RS.setRegisterC((short)(RS.getRegisterC()+1));
                break;
            case 0x14:
                RS.setRegisterD((short)(RS.getRegisterD()+1));
                break;
            case 0x1C:
                RS.setRegisterE((short)(RS.getRegisterE()+1));
                break;
            case 0x24:
                RS.setRegisterH((short)(RS.getRegisterH()+1));
                break;
            case 0x2C:
                RS.setRegisterL((short)(RS.getRegisterL()+1));
                break;

            case 0x3D:
                RS.setRegisterA((short)(RS.getRegisterA()-1));
                break;
            case 0x05:
                RS.setRegisterB((short)(RS.getRegisterB()-1));
                break;
            case 0x0D:
                RS.setRegisterC((short)(RS.getRegisterC()-1));
                break;
            case 0x15:
                RS.setRegisterD((short)(RS.getRegisterD()-1));
                break;
            case 0x1D:
                RS.setRegisterE((short)(RS.getRegisterE()-1));
                break;
            case 0x25:
                RS.setRegisterH((short)(RS.getRegisterH()-1));
                break;
            case 0x2D:
                RS.setRegisterL((short)(RS.getRegisterL()+1));
                break;

            case 0x3E:
                RS.setRegisterA(MS.getMemoryValueLast());
                break;
            case 0x06:
                RS.setRegisterB(MS.getMemoryValueLast());
                break;
            case 0x0E:
                RS.setRegisterC(MS.getMemoryValueLast());
                break;
            case 0x16:
                RS.setRegisterD(MS.getMemoryValueLast());
                break;
            case 0x1E:
                RS.setRegisterE(MS.getMemoryValueLast());
                break;
            case 0x26:
                RS.setRegisterH(MS.getMemoryValueLast());
                break;
            case 0x2E:
                RS.setRegisterL(MS.getMemoryValueLast());
                break;

            case 0xDB:
                RS.setRegisterA(PS.ReadPort(MS.getMemoryValueLast()));
                break;

            case 0xD3:
                PS.WritePort(MS.getMemoryValueLast(), RS.getRegisterA());
                break;

            case 0x76:
                RS.ResetRegisters();
                MS.ResetMemory();
                PS.ResetPorts();
                ResetCommandCounter();
                break;
        }
    }

    public void ResetCommandCounter() {
        CommandCounter = 0;
    }

    public void setCommandCounter(int _CommandCounter) {
        CommandCounter = _CommandCounter;
    }

    public int getCommandCounter() {
        return CommandCounter;
    }
}
