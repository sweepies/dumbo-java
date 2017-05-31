package science.amberfall.dumbo_irc;

import joptsimple.OptionParser;

public class Options {

    public static OptionParser getParser() {
        OptionParser parser = new OptionParser();
        parser.accepts("makeconf", "Generates configuration files without running the bot");
        parser.accepts("clean", "Reset the current configuration files to the default values");
        parser.accepts("update", "Update the quotes.json file");
        return parser;
    }

}
