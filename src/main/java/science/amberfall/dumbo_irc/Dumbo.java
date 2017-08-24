package science.amberfall.dumbo_irc;

import com.google.common.collect.Sets;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Dumbo {

    private static Config config;

    private static Commands commands;

    private static OptionParser parser;

    private static OptionSet options;

    private static Logger log = LoggerFactory.getLogger("science.amberfall.dumbo_irc.Dumbo");

    public static void main(String[] args) throws Exception {

        Dumbo.parser = Options.getParser();

        try {
            options = parser.parse(args);
        } catch (joptsimple.OptionException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }

        assert options != null;
        if (options.has("clean")) {
            try {
                File configFile = new File("config.yml");
                System.out.println("Deleting config.yml..");
                if (!configFile.delete()) {
                    System.out.println("Unable to clean.");
                    System.exit(1);
                }
                File commandsFile = new File("commands.yml");
                System.out.println("Deleting commands.yml..");
                if (!commandsFile.delete()) {
                    System.out.println("Unable to clean.");
                    System.exit(1);
                }
            } catch (Exception e) {
                System.out.println("Unable to clean: ");
                e.printStackTrace();
                System.exit(1);
            }
        }

        // Read config file {
        try {

            File configFile = new File("config.yml");
            if (configFile.exists()) {
                System.out.println("Config file already exists, continuing.");
            } else {
                System.out.println("Creating default config file..");

                InputStream in = Dumbo.class.getResourceAsStream("/config.yml");
                FileOutputStream out = new FileOutputStream("config.yml");
                IOUtils.copy(in, out);

                System.out.println("Done!");
            }

            FileReader reader = new FileReader("config.yml");
            Yaml yaml = new Yaml();
            Dumbo.config = yaml.loadAs(reader, Config.class);

        } catch (Exception e) {
            System.out.println("Failed to read config file:");
            e.printStackTrace();
            System.exit(1);
        }
        // }

        // Read commands file {
        try {

            File commandsFile = new File("commands.yml");
            if (commandsFile.exists()) {
                System.out.println("Commands file already exists, continuing.");
            } else {
                System.out.println("Creating default commands file..");

                InputStream in = Dumbo.class.getResourceAsStream("/commands.yml");
                FileOutputStream out = new FileOutputStream("commands.yml");
                IOUtils.copy(in, out);

                System.out.println("Done!");
            }

            FileReader reader = new FileReader("commands.yml");
            Yaml yaml = new Yaml();
            Dumbo.commands = yaml.loadAs(reader, Commands.class);

        } catch (Exception e) {
            System.out.println("Failed to read commands file:");
            e.printStackTrace();
            System.exit(1);
        }
        // }

        if (options.has("makeconf")) {
            System.exit(0);
        }

        getQuotes();

        new PircBotX(makeConfig()).startBot();
    }

    private static Configuration makeConfig() {

        HashSet<String> autoJoinChannels = Sets.newHashSet(config.getChannels());

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
                .addAutoJoinChannels(autoJoinChannels)
                .setSocketFactory(config.getSsl() ? SSLSocketFactory.getDefault() : SocketFactory.getDefault())
                .addListener(new Listener(config, commands, log))
                .buildConfiguration();
    }

    private static void getQuotes() {

        File file = new File("quotes.json");

        assert options != null;
        if (file.exists() && !options.has("update")) {
            System.out.println("Quotes file already exists, continuing.");
        } else {
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
    }
}
