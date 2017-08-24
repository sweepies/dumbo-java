package science.amberfall.dumbo_irc.commands;

import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.iface.Command;
import science.amberfall.dumbo_irc.util.QuoteHandler;

import java.util.Arrays;

public class RandomQuoteCommand implements Command {

    private GenericMessageEvent event;
    private Logger logger;

    public RandomQuoteCommand(GenericMessageEvent event, Logger logger) {
        this.event = event;
        this.logger = logger;
    }

    @Override
    public void runCommand() {

        String quote = QuoteHandler.randomQuote();

        if (quote != null) {
            Arrays.stream(quote.split("\n")).forEach(l -> event.respondWith(l.replaceAll("Qball", "Qbal" + "\u200B" + "l")));
        } else {
            logger.error("Quotes file could not be read! Aborting.");
        }
    }
}
