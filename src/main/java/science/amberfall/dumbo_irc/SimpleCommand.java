package science.amberfall.dumbo_irc;

import science.amberfall.dumbo_irc.Command.Command;
import science.amberfall.dumbo_irc.Command.CommandExecutor;

import java.lang.reflect.Method;

public class SimpleCommand {
    private final Command annotation;
    private final Method method;
    private final CommandExecutor executor;

    SimpleCommand(Command annotation, Method method, CommandExecutor executor) {
        this.annotation = annotation;
        this.method = method;
        this.executor = executor;
    }

    public Command getCommandAnnotation() {
        return this.annotation;
    }

    public Method getMethod() {
        return this.method;
    }

    public CommandExecutor getExecutor() {
        return this.executor;
    }
}
