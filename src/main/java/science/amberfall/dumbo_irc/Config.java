package science.amberfall.dumbo_irc;

import lombok.Getter;

// Class to parse the config file into

@Getter
public class Config {

    @Getter public String host;
    @Getter public Boolean ssl;
    @Getter public Integer port;
    @Getter public String addModes;
    @Getter public String removeModes;
    @Getter public String nickname;
    @Getter public String realname;
    @Getter public String ident;
    @Getter public String password;
    @Getter public Character delimiter;
    @Getter public String[] channels;
    @Getter public String[] ops;
    @Getter public String[] blocked;
    @Getter public Integer tacos;

}
