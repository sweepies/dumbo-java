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

        HashSet<String> blockedSet = Sets.newHashSet(config.getBlocked());

        Character delimiter = config.getDelimiter();

        if (blockedSet.contains(ev.getUser().getHostname())) {

            PircBotX bot = ev.getBot();
            OutputUser user = new OutputUser(bot, ev.getUserHostmask());

            log.warn("Command blocked: " + ev.getMessage() + "(Hostname in blocked list)");
            user.message("You are not authorized to use that command.");
        } else {

            // Random quote command
            for (String command : commands.getRandomquote()) {
                if (msg[0].substring(1).equalsIgnoreCase(command) && msg[0].charAt(0) == delimiter) {
                    String quote = Dumbo.randomQuote();
                    if (quote != null) {
                        if (quote.contains("\n")) {
                            for (String q : quote.split("\n")) {
                                ev.respondWith(q.replaceAll("Qball", "Qbal" + "\u200B" + "l")); // Zero width space to prevent pinging
                            }
                        } else {
                            ev.respondWith(quote.replaceAll("Qball", "Qbal" + "\u200B" + "l"));
                        }
                    } else {
                        log.error("Quotes file could not be read! Aborting.");
                    }
                    break;
                }
            }

            // Sendline command
            for (String command : commands.getSendline()) {
                if (msg[0].substring(1).equalsIgnoreCase(command) && msg[0].charAt(0) == delimiter) {

                    HashSet<String> opsSet = Sets.newHashSet(config.getOps());
                    PircBotX bot = ev.getBot();

                    if (opsSet.contains(ev.getUser().getHostname())) {
                        OutputRaw raw = new OutputRaw(bot);
                        raw.rawLine(ev.getMessage().substring(msg[0].length() + 1)); // Send a raw line removing the command and space before it
                        break;
                    } else {
                        log.warn("Command blocked: " + ev.getMessage() + "(Hostname not in ops list)");
                        OutputUser user = new OutputUser(bot, ev.getUserHostmask());
                        user.message("You are not authorized to use that command.");
                    }
                    break;
                }
            }

            // Tacos command
            for (String command : commands.getTacos()) {
                if (msg[0].substring(1).equalsIgnoreCase(command) && msg[0].charAt(0) == delimiter) {
                    ev.respondWith(String.join("", Collections.nCopies(config.getTacos(), "\uD83C\uDF2E"))); // Send tacos
                    break;
                }
            }

        }
    }

}
