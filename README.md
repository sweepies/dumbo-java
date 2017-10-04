![Dumbo](https://static.sweepy.pw/Dumbo-Bukkit/logo.png)

![Build status](https://api.travis-ci.org/sweepyoface/dumbo-java.svg?branch=master)
![Current release](https://img.shields.io/github/release/sweepyoface/dumbo-java.svg)
![License](https://img.shields.io/github/license/sweepyoface/dumbo-java.svg)

This is the Java version of the [Dumbo](https://github.com/sweepyoface/dumbo) IRC bot. This version is more polished and is generally nicer.

# Downloading
You can download the latest build from [Jenkins](https://ci.sweepy.pw/job/Dumbo-Java/).

# Building
1. Install [Apache Maven](https://maven.apache.org/).
2. Clone this repository.
3. Run `mvn clean install`.
4. The compiled jar will be in the `target` directory.

# Commands
Command triggers can be changed in the configuration file.

| Command | Description |
| --- | --- |
| Random quote | Prints a random Qball quote in the channel. |
| Send line | Sends a raw line of text to the IRC server. |
| Tacos | Prints a configurable amount of tacos in the channel. |

# Usage
`java -jar dumbo_irc.jar`

| Option | Description |
| --- | --- |
| `--makeconf` | Generates configuration files without running the bot. Useful when running for the first time. |
| `--clean` | Resets the current configuration files to the default values. |
| `--update` | Fetch the most recent quotes.json file from the [central repository](https://github.com/sweepyoface/dumbo-quotes). |
