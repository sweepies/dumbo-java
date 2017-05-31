package science.amberfall.dumbo_irc;

// Class to parse the config file into

public class Config {

    public String host;
    public Boolean ssl;
    public Integer port;
    public String modes;
    public String nickname;
    public String realname;
    public String ident;
    public String password;
    public Character delimiter;
    public String[] channels;
    public String[] ops;
    public String[] blocked;
    public Integer tacos;

    public String getHost() {
        return host;
    }

    public Boolean getSSL() {
        return ssl;
    }

    public Integer getPort() {
        return port;
    }

    public String getModes() {
        return modes;
    }

    public String getNickname() {
        return nickname;
    }

    public String getRealname() {
        return realname;
    }

    public String getIdent() {
        return ident;
    }

    public String getPassword() {
        return password;
    }

    public Character getDelimiter() {
        return delimiter;
    }

    public String[] getChannels() {
        return channels;
    }

    public String[] getOps() {
        return ops;
    }

    public String[] getBlocked() {
        return blocked;
    }

    public Integer getTacos() {
        return tacos;
    }

}
