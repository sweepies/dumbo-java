package science.amberfall.dumbo_irc;

// Class to parse the config file into

class Config {

    String host;
    Boolean ssl;
    Integer port;
    String modes;
    String nickname;
    String realname;
    String ident;
    String password;
    Character delimiter;
    String[] channels;
    String[] ops;
    String[] blocked;
    Integer tacos;

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
