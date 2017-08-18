package science.amberfall.dumbo_irc;

import com.google.common.collect.Sets;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;
import org.pircbotx.output.OutputRaw;
import org.pircbotx.output.OutputUser;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.util.QuoteHandler;

import java.util.Arrays;
import java.util.Collections;
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

                // Random quote command
                if (Arrays.stream(commands.getRandomquote()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {

                    String quote = QuoteHandler.randomQuote();

                    if (quote != null) {
                        Arrays.stream(quote.split("\n")).forEach(l -> ev.respondWith(l.replaceAll("Qball", "Qbal" + "\u200B" + "l")));
                    } else {
                        log.error("Quotes file could not be read! Aborting.");
                    }
                }

                // Sendline command
                if (Arrays.stream(commands.getSendline()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {

                    HashSet<String> opsSet = Sets.newHashSet(config.getOps());
                    PircBotX bot = ev.getBot();

                    if (opsSet.contains(ev.getUser().getHostname())) {
                        OutputRaw raw = new OutputRaw(bot);
                        raw.rawLine(ev.getMessage().substring(msg[0].length() + 1)); // Send a raw line removing the command and space before it
                    } else {
                        log.warn("Command blocked: " + ev.getMessage() + "(Hostname not in ops list)");
                        OutputUser user = new OutputUser(bot, ev.getUserHostmask());
                        user.notice("You are not authorized to use that command.");
                    }
                }

                // Tacos command
                if (Arrays.stream(commands.getTacos()).anyMatch(msg[0].substring(1)::equalsIgnoreCase)) {
                    ev.respondWith(String.join("", Collections.nCopies(config.getTacos(), "\uD83C\uDF2E"))); // Send tacos
                }
            }
        }
    }

}
