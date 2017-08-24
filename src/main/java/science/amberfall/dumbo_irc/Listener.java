package science.amberfall.dumbo_irc;

import com.google.common.collect.Sets;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.commands.RandomQuoteCommand;
import science.amberfall.dumbo_irc.commands.SendLineCommand;
import science.amberfall.dumbo_irc.commands.TacosCommand;

import java.util.Arrays;
import java.util.HashSet;

public class Listener extends ListenerAdapter {

    private Config config;
    private Commands commands;
    private Logger log;

    Listener(Config config, Commands commands, Logger log) {
        this.config = config;
        this.commands = commands;
        this.log = log;

    }

    @Override
    public void onConnect(ConnectEvent ev) {
        PircBotX bot = ev.getBot();
        OutputIRC irc = new OutputIRC(bot);
        irc.mode(bot.getNick(), "+" + config.getModes());
    }

    @Override
    public void onGenericMessage(GenericMessageEvent ev) {

        String[] msg = ev.getMessage().trim().split("\\s+");
        Character delimiter = config.getDelimiter();

        // If the first character is the command operator
        if (msg[0].charAt(0) == delimiter) {

            HashSet<String> blockedSet = Sets.newHashSet(config.getBlocked());

            // If the user is blocked, do nothing
            if (!blockedSet.contains(ev.getUser().getHostname())) {

                if (Arrays.stream(commands.getRandomquote()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {

                    new RandomQuoteCommand(ev, log).runCommand();

                } else if (Arrays.stream(commands.getSendline()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {

                    new SendLineCommand(ev, log, msg, config.getOps()).runCommand();

                } else if (Arrays.stream(commands.getTacos()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {

                    new TacosCommand(ev, config.getTacos()).runCommand();
                }
            }
        }
    }

}
