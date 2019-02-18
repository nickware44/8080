package sample;

import java.util.ArrayList;
import java.util.List;

public class CommandSystem {
    private ArrayList<Command> Commands = new ArrayList<>();
    private RegisterSystem RS;
    private MemorySystem MS;
    private int CommandCounter;

    CommandSystem(RegisterSystem _RS, MemorySystem _MS) {
        Commands.add(new Command("87", "ADD A"));
        Commands.add(new Command("80", "ADD B"));
        Commands.add(new Command("81", "ADD C"));
        Commands.add(new Command("82", "ADD D"));
        Commands.add(new Command("83", "ADD E"));
        Commands.add(new Command("84", "ADD H"));
        Commands.add(new Command("85", "ADD L"));
        Commands.add(new Command("86", "ADD M"));
        Commands.add(new Command("C6", "ADI", 1, 1));

        Commands.add(new Command("2F", "CMA"));

        Commands.add(new Command("A7", "ANA A"));
        Commands.add(new Command("A0", "ANA B"));
        Commands.add(new Command("A1", "ANA C"));
        Commands.add(new Command("A2", "ANA D"));
        Commands.add(new Command("A3", "ANA E"));
        Commands.add(new Command("A4", "ANA H"));
        Commands.add(new Command("A5", "ANA L"));
        Commands.add(new Command("A6", "ANA M"));
        Commands.add(new Command("E6", "ANI", 1, 1));

        Commands.add(new Command("BF", "CMP A"));
        Commands.add(new Command("B8", "CMP B"));
        Commands.add(new Command("B9", "CMP C"));
        Commands.add(new Command("BA", "CMP D"));
        Commands.add(new Command("BB", "CMP E"));
        Commands.add(new Command("BC", "CMP H"));
        Commands.add(new Command("BD", "CMP L"));
        Commands.add(new Command("BE", "CMP M"));
        Commands.add(new Command("FE", "CMI", 1, 1));

        Commands.add(new Command("3C", "INR A"));
        Commands.add(new Command("04", "INR B"));
        Commands.add(new Command("0C", "INR C"));
        Commands.add(new Command("14", "INR D"));
        Commands.add(new Command("1C", "INR E"));
        Commands.add(new Command("24", "INR H"));
        Commands.add(new Command("2C", "INR L"));
        Commands.add(new Command("34", "INR M"));

        Commands.add(new Command("3D", "DCR A"));
        Commands.add(new Command("05", "DCR B"));
        Commands.add(new Command("0D", "DCR C"));
        Commands.add(new Command("15", "DCR D"));
        Commands.add(new Command("1D", "DCR E"));
        Commands.add(new Command("25", "DCR H"));
        Commands.add(new Command("2D", "DCR L"));
        Commands.add(new Command("35", "DCR M"));

        Commands.add(new Command("7F", "MOV A, A"));
        Commands.add(new Command("78", "MOV A, B"));
        Commands.add(new Command("79", "MOV A, C"));
        Commands.add(new Command("7A", "MOV A, D"));
        Commands.add(new Command("7B", "MOV A, E"));
        Commands.add(new Command("7C", "MOV A, H"));
        Commands.add(new Command("7D", "MOV A, L"));
        Commands.add(new Command("7E", "MOV A, M"));

        Commands.add(new Command("3E", "MVI A,", 1, 1));
        Commands.add(new Command("06", "MVI B,", 1, 1));
        Commands.add(new Command("0E", "MVI C,", 1, 1));
        Commands.add(new Command("16", "MVI D,", 1, 1));
        Commands.add(new Command("1E", "MVI E,", 1, 1));
        Commands.add(new Command("26", "MVI H,", 1, 1));
        Commands.add(new Command("2E", "MVI L,", 1, 1));
        Commands.add(new Command("36", "MVI M,", 1, 1));

        Commands.add(new Command("97", "SUB A"));
        Commands.add(new Command("90", "SUB B"));
        Commands.add(new Command("91", "SUB C"));
        Commands.add(new Command("92", "SUB D"));
        Commands.add(new Command("93", "SUB E"));
        Commands.add(new Command("94", "SUB H"));
        Commands.add(new Command("95", "SUB L"));
        Commands.add(new Command("96", "SUB M"));
        Commands.add(new Command("D6", "SUI", 1, 1));

        Commands.add(new Command("DB", "IN", 1, 1));
        Commands.add(new Command("D3", "OUT", 1, 1));

        Commands.add(new Command("76", "HLT"));

        RS = _RS;
        MS = _MS;

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

            case 0x3E:
                RS.setRegisterA(MS.getMemoryValueLast());
                break;

            case 0x76:
                RS.ResetRegisters();
                MS.ResetMemory();
                ResetCommandCounter();
                break;
        }
    }

    public void ResetCommandCounter() {
        CommandCounter = 0;
    }
}
