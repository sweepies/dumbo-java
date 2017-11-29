package science.amberfall.dumbo_irc.commands;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputUser;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.iface.Command;

import java.util.Arrays;

public class SendLineCommand implements Command {

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

    @Override
    public void runCommand() {
        PircBotX bot = event.getBot();

        if (Arrays.stream(ops).anyMatch(event.getUser().getHostname()::matches)) {
            OutputRaw raw = new OutputRaw(bot);
            raw.rawLine(event.getMessage().substring(message[0].length() + 1)); // Send a raw line removing the command and space before it
        } else {
            logger.warn("Command blocked: " + event.getMessage() + "(Hostname not in ops list)");
            OutputUser user = new OutputUser(bot, event.getUserHostmask());
            user.notice("You are not authorized to use that command.");
        }

    }

    @Override
    public String getOutput() {
        return event.getMessage().substring(message[0].length() + 1);
    }
}
