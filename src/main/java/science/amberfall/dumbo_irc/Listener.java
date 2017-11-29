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
        // Set user modes
        irc.mode(bot.getNick(), "+" + config.getAddModes());
        irc.mode(bot.getNick(), "-" + config.getRemoveModes());
    }

    @Override
    public void onGenericMessage(GenericMessageEvent ev) {

        String[] msg = ev.getMessage().trim().split("\\s+");
        Character delimiter = config.getDelimiter();

        // If the first character is the command operator
        if (msg[0].charAt(0) == delimiter) {

            // The command without the operator (the first character)
            final String command = msg[0].substring(1);

            // Convert blocked hosts array to iterable HashSet
            final HashSet<String> blockedSet = Sets.newHashSet(config.getBlocked());

            // Make sure the user isn't blocked
            if (blockedSet.stream().noneMatch(ev.getUser().getHostname()::matches)) {

                if (Arrays.stream(commands.getRandomquote()).anyMatch(command::equalsIgnoreCase)) {
                    ev.respondWith(new RandomQuoteCommand(log).getOutput());
                    return;
                }

                if (Arrays.stream(commands.getSendline()).anyMatch(command::equalsIgnoreCase)) {
                    new SendLineCommand(ev, log, msg, config.getOps()).runCommand();
                    return;
                }

                if (Arrays.stream(commands.getTacos()).anyMatch(command::equalsIgnoreCase)) {
                    ev.respondWith(new TacosCommand(config.getTacos()).getOutput());
                }
            }
        }
    }

}
