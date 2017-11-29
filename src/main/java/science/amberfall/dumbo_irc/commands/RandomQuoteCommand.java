package science.amberfall.dumbo_irc.commands;

import org.slf4j.Logger;
import science.amberfall.dumbo_irc.iface.Command;
import science.amberfall.dumbo_irc.util.QuoteHandler;

public class RandomQuoteCommand implements Command {

    private Logger logger;

    public RandomQuoteCommand(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void runCommand() {}

    @Override
    public String getOutput() {

        String quote = QuoteHandler.randomQuote();

        if (quote != null) {
            return quote.replaceAll("Qball", "Qbal" + "\u200B" + "l");
        } else {
            logger.error("Quotes file could not be read! Aborting.");
            return null;
        }
    }
}
