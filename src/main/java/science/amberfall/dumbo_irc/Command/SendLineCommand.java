package science.amberfall.dumbo_irc.Command;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputUser;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.Dumbo;

import java.util.Arrays;

public class SendLineCommand implements CommandExecutor {

    private final Logger log = Dumbo.getLog();
    private final String[] ops;

    public SendLineCommand(String[] ops) {
        this.ops = ops;

    }

    @Command(aliases = {".sendline", ".rawline"})
    public String onSendLineCommand(GenericMessageEvent event) {
        final PircBotX bot = event.getBot();
        String[] args = event.getMessage().split("\\s");

        if (Arrays.stream(ops).anyMatch(event.getUser().getHostname()::matches)) {
            OutputRaw raw = new OutputRaw(bot);
            raw.rawLine(event.getMessage().substring(args[0].length() + 1)); // Send a raw line removing the command and space before it
        } else {
            log.warn("Command blocked: " + event.getMessage() + "(Hostname not in ops list)");
            OutputUser user = new OutputUser(bot, event.getUserHostmask());
            user.notice("You are not authorized to use that command.");
        }
        return null;
    }
}
