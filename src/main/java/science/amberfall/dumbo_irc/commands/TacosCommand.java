package science.amberfall.dumbo_irc.commands;

import org.pircbotx.hooks.types.GenericMessageEvent;
import science.amberfall.dumbo_irc.iface.Command;

import java.util.Collections;

public class TacosCommand implements Command {

    private GenericMessageEvent event;
    private int amount;

    public TacosCommand(GenericMessageEvent event, int amount) {
        this.event = event;
        this.amount = amount;
    }

    @Override
    public void runCommand() {
        event.respondWith(String.join("", Collections.nCopies(amount, "\uD83C\uDF2E"))); // Send tacos
    }
}
