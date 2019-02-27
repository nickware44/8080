package sample;

public class FlagTableLine {
    private String flagName;
    private boolean state;

    FlagTableLine(String _flagName, boolean _state) {
        flagName = _flagName;
        state = _state;
    }

    public String getFlagName() {
        return flagName;
    }

    public String getState() {
        String ValueStr;

        if (state) {
            ValueStr = "true";
        } else {
            ValueStr = "false";
        }

        return ValueStr;
    }
}
