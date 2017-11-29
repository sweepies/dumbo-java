package science.amberfall.dumbo_irc;

import lombok.Getter;

// Class to parse the config file into

@Getter
class Config {

    public String host;
    public Boolean ssl;
    public Integer port;
    public String addModes;
    public String removeModes;
    public String nickname;
    public String realname;
    public String ident;
    public String password;
    public Character delimiter;
    public String[] channels;
    public String[] ops;
    public String[] blocked;
    public Integer tacos;

}
