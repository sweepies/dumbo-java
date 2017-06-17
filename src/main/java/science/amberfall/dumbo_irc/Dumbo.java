package science.amberfall.dumbo_irc;

import com.google.gson.Gson;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;
import org.pircbotx.output.OutputRaw;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import javax.net.ssl.SSLSocketFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Dumbo extends ListenerAdapter {

    private static Config config;

    private static Commands commands;

    private static OptionParser parser;

    private static Logger log = LoggerFactory.getLogger("science.amberfall.dumbo_irc.Dumbo");

    public static void main(String[] args) throws Exception {

        Dumbo.parser = Options.getParser();
        OptionSet options = parseOpts(args);

        if (options.has("clean")) {
            try {
                File configFile = new File("config.yml");
                System.out.println("Deleting config.yml..");
                configFile.delete();
                File commandsFile = new File("commands.yml");
                System.out.println("Deleting commands.yml..");
                commandsFile.delete();
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

        getQuotes(args);

        List<String> autoJoinChannels = new ArrayList<>();
        for (int i = 0; i < config.getChannels().length; i++) {
            autoJoinChannels.add(config.getChannels()[i]);
        }

        if (config.getSSL()) {
            new PircBotX(makeConfig(true, autoJoinChannels)).startBot();
        } else {
            new PircBotX(makeConfig(false, autoJoinChannels)).startBot();
        }

    }

    public static OptionSet parseOpts(String[] args) {
        try {
            OptionSet options = parser.parse(args);
            return options;
        } catch (joptsimple.OptionException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static Configuration makeConfig(Boolean useSSL, List<String> autoJoinChannels) {
        if (useSSL) {
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
                    .setSocketFactory(SSLSocketFactory.getDefault())
                    .addListener(new Dumbo())
                    .buildConfiguration();
        } else {
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
                    .addListener(new Dumbo())
                    .buildConfiguration();
        }
    }

    public static void getQuotes(String[] args) {

        OptionSet options = parseOpts(args);
        File file = new File("quotes.json");

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

    public String randomQuote() {
        try {
            FileReader reader = new FileReader("quotes.json");
            Gson gson = new Gson();
            Quotes quotes = gson.fromJson(reader, Quotes.class);
            String[] quotesList = quotes.getQuotes();
            Random random = new Random();
            Integer quote = random.nextInt(quotesList.length);
            return quotesList[quote];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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

        List<String> blockedList = new ArrayList<>();
        Collections.addAll(blockedList, config.getBlocked());

        if (blockedList.contains(ev.getUser().getHostname())) {
            log.warn("Command blocked: " + ev.getMessage() + "(Hostname in blocked list)");
        } else {

            // Random quote command
            for (int i = 0; i < commands.getRandomquote().length; i++) {
                if (msg[0].substring(1).equalsIgnoreCase(commands.getRandomquote()[i]) && msg[0].charAt(0) == config.getDelimiter()) {
                    String quote = randomQuote();
                    if (quote != null) {
                        ev.respondWith(quote.replaceAll("Qball", "Qbal" + "\u200B" + "l")); // Zero width space to prevent pinging
                    } else {
                        log.error("Quotes file could not be read! Aborting.");
                    }
                    break;
                }
            }

            // Sendline command
            for (int i = 0; i < commands.getSendline().length; i++) {
                if (msg[0].substring(1).equalsIgnoreCase(commands.getSendline()[i]) && msg[0].charAt(0) == config.getDelimiter()) {
                    List<String> opsList = new ArrayList<>();
                    Collections.addAll(opsList, config.getOps());
                    if (opsList.contains(ev.getUser().getHostname())) {
                        PircBotX bot = ev.getBot();
                        OutputRaw raw = new OutputRaw(bot);
                        raw.rawLine(ev.getMessage().substring(msg[0].length() + 1)); // Send a raw line removing the command and space before it
                        break;
                    } else {
                        log.warn("Command blocked: " + ev.getMessage() + "(Hostname not in ops list)");
                    }
                    break;
                }
            }

            // Tacos command
            for (int i = 0; i < commands.getTacos().length; i++) {
                if (msg[0].substring(1).equalsIgnoreCase(commands.getTacos()[i]) && msg[0].charAt(0) == config.getDelimiter()) {
                    ev.respondWith(String.join("", Collections.nCopies(config.getTacos(), "\uD83C\uDF2E"))); // Send tacos
                    break;
                }
            }

        }
    }
}
