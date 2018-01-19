package science.amberfall.dumbo_irc.Command;

import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.Collections;

public class TacosCommand implements CommandExecutor {

    private int amount;

    public TacosCommand(int amount) {
        this.amount = amount;

    }

    @Command(aliases = {".tacos"})
    public String onTacosCommand(GenericMessageEvent event) {
        return String.join("", Collections.nCopies(amount, "\uD83C\uDF2E"));
    }
}
