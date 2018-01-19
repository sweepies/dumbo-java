package science.amberfall.dumbo_irc;

import com.google.common.collect.Sets;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import science.amberfall.dumbo_irc.Command.RandomQuoteCommand;
import science.amberfall.dumbo_irc.Command.SendLineCommand;
import science.amberfall.dumbo_irc.Command.TacosCommand;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Dumbo {

    @Getter private static final Logger log = LoggerFactory.getLogger("science.amberfall.dumbo_irc.Dumbo");
    @Getter private static Config config;
    private static OptionSet options;
    private final CommandHandler commandHandler = new CommandHandler();

    private Dumbo() {
        commandHandler.registerCommand(new RandomQuoteCommand());
        commandHandler.registerCommand(new SendLineCommand(config.getOps()));
        commandHandler.registerCommand(new TacosCommand(config.getTacos()));
    }

    public static void main(String[] args) throws Exception {

        final OptionParser parser = Options.getParser();

        try {
            options = parser.parse(args);
            // If there's an error with parsing the options, the program exits. Thus, we can assert that options will not be null.
            assert options != null;
        } catch (joptsimple.OptionException e) {
            System.out.println("Error parsing options: " + e.getMessage());
            System.exit(1);
        }

        // Set config and commands file variables for use by the clean, config file, and commands file logic
        final File configFile = new File("config.yml");
        final File commandsFile = new File("commands.yml");

        // Clean logic {
        if (options.has("clean")) {
            System.out.println("Deleting config.yml..");
            if (!configFile.delete()) {
                System.out.println("Unable to delete config.yml.");
                System.exit(1);
            }
            System.out.println("Deleting commands.yml..");
            if (!commandsFile.delete()) {
                System.out.println("Unable to delete commands.yml.");
                System.exit(1);
            }
        }
        // }

        // Create Yaml instance to read config and commands files
        final Yaml yaml = new Yaml();

        // Config file logic {
        try {
            if (configFile.exists()) {
                System.out.println("Config file already exists, continuing.");
            } else {
                System.out.println("Creating default config file..");

                FileUtils.copyInputStreamToFile(Dumbo.class.getResourceAsStream("/config.yml"), configFile);

                System.out.println("Done!");
            }

            Dumbo.config = yaml.loadAs(new FileReader("config.yml"), Config.class);

        } catch (Exception e) {
            System.out.println("Failed to get config file:");
            e.printStackTrace();
            System.exit(1);
        }
        // }

        // If the makeconf option is present, we're done here
        if (options.has("makeconf")) {
            return;
        }

        // Fetch the quotes from GitHub
        getQuotes();

        // Make the bot config and start it
        new PircBotX(new Dumbo().makeConfig()).startBot();
    }

    private static void getQuotes() {

        File file = new File("quotes.json");

        if (file.exists() && !options.has("update")) {
            System.out.println("Quotes file already exists, continuing.");
            return;
        }

        try {
            System.out.println("Fetching quotes..");
            FileUtils.copyURLToFile(new URL("https://raw.githubusercontent.com/sweepyoface/dumbo-quotes/master/quotes.json"), file);
            System.out.println("Done!");
        } catch (Exception e) {
            System.out.println("Failed to fetch quotes:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Configuration makeConfig() {
        return new Configuration.Builder()
                .setName(config.getNickname())
                .setRealName(config.getRealname())
                .setNickservNick(config.getIdent())
                .setNickservPassword(config.getPassword())
                .setLogin(config.getIdent())
                .setAutoSplitMessage(true)
                .setEncoding(StandardCharsets.UTF_8)
                .setAutoReconnect(true)
                .addServer(config.getHost(), config.getPort())
                // Convert the array of channels to an iterable HashSet
                .addAutoJoinChannels(Sets.newHashSet(config.getChannels()))
                // If SSL is enabled in the config, use the SSLSocketFactory instead of the normal one
                .setSocketFactory(config.getSsl() ? SSLSocketFactory.getDefault() : SocketFactory.getDefault())
                .addListener(commandHandler)
                .buildConfiguration();
    }
}
