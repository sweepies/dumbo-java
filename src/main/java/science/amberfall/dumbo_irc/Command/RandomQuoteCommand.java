package science.amberfall.dumbo_irc.Command;

import org.pircbotx.hooks.types.GenericMessageEvent;
import org.slf4j.Logger;
import science.amberfall.dumbo_irc.Dumbo;
import science.amberfall.dumbo_irc.util.QuoteHandler;

public class RandomQuoteCommand implements CommandExecutor {

    private final Logger log = Dumbo.getLog();

    @Command(aliases = {".quote", ".randomquote", ".dumbo", ".dumball", ".dum"})
    public String onRandomQuoteCommand(GenericMessageEvent event) {

        final String quote = QuoteHandler.randomQuote();

        if (quote != null) {
            return quote.replaceAll("Qball", "Qbal" + "\u200B" + "l");
        } else {
            log.error("Quotes file could not be read! Aborting.");
        }
        return null;
    }
}
