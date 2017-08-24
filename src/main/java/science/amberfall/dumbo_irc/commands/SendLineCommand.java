package science.amberfall.dumbo_irc.commands;

import com.google.common.collect.Sets;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputUser;
import org.slf4j.Logger;

import java.util.HashSet;

public class SendLineCommand {

    private GenericMessageEvent event;
    private Logger logger;
    private String[] message;
    private String[] ops;

    public SendLineCommand(GenericMessageEvent event, Logger logger, String[] message, String[] ops) {
        this.event = event;
        this.logger = logger;
        this.message = message;
        this.ops = ops;
    }

    public void runCommand() {

        HashSet<String> opsSet = Sets.newHashSet(ops);
        PircBotX bot = event.getBot();

        if (opsSet.contains(event.getUser().getHostname())) {
            OutputRaw raw = new OutputRaw(bot);
            raw.rawLine(event.getMessage().substring(message[0].length() + 1)); // Send a raw line removing the command and space before it
        } else {
            logger.warn("Command blocked: " + event.getMessage() + "(Hostname not in ops list)");
            OutputUser user = new OutputUser(bot, event.getUserHostmask());
            user.notice("You are not authorized to use that command.");
        }

    }
}
