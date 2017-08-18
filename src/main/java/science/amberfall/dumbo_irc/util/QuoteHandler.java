package science.amberfall.dumbo_irc.util;

import com.google.gson.Gson;
import science.amberfall.dumbo_irc.Quotes;

import java.io.FileReader;
import java.util.Random;

public class QuoteHandler {

    private static Random random = new Random();

    public static String randomQuote() {
        try {
            String[] quotes = new Gson().fromJson(new FileReader("quotes.json"), Quotes.class).getQuotes();
            Integer quote = random.nextInt(quotes.length);
            return quotes[quote];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
