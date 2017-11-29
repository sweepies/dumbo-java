package science.amberfall.dumbo_irc.commands;

import science.amberfall.dumbo_irc.iface.Command;

import java.util.Collections;

public class TacosCommand implements Command {

    private int amount;

    public TacosCommand(int amount) {
        this.amount = amount;
    }

    @Override
    public String getOutput() {
        return String.join("", Collections.nCopies(amount, "\uD83C\uDF2E"));
    }

    @Override
    public void runCommand() {}
}
