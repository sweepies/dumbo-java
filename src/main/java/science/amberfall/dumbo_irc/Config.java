package science.amberfall.dumbo_irc;

// Class to parse the config file into

class Config {

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

    String getHost() {
        return host;
    }

    Boolean getSSL() {
        return ssl;
    }

    Integer getPort() {
        return port;
    }

    String getModes() {
        return modes;
    }

    String getNickname() {
        return nickname;
    }

    String getRealname() {
        return realname;
    }

    String getIdent() {
        return ident;
    }

    String getPassword() {
        return password;
    }

    Character getDelimiter() {
        return delimiter;
    }

    String[] getChannels() {
        return channels;
    }

    String[] getOps() {
        return ops;
    }

    String[] getBlocked() {
        return blocked;
    }

    Integer getTacos() {
        return tacos;
    }

}
