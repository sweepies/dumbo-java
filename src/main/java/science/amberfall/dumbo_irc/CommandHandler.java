package science.amberfall.dumbo_irc;


import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import science.amberfall.dumbo_irc.Command.Command;
import science.amberfall.dumbo_irc.Command.CommandExecutor;

import java.lang.reflect.Method;
import java.util.HashMap;

public class CommandHandler extends ListenerAdapter {
    private final HashMap<String, SimpleCommand> commands = new HashMap<>();

    public void registerCommand(CommandExecutor executor) {
        final Method[] methods = executor.getClass().getMethods();

        for (Method method : methods) {
            if (method.getAnnotation(Command.class) != null) {
                Command annotation = method.getAnnotation(Command.class);
                String[] aliases = annotation.aliases();
                SimpleCommand command = new SimpleCommand(annotation, method, executor);

                for (String alias : aliases) {
                    commands.put(alias.toLowerCase().replace(" ", ""), command);
                }
            }
        }
    }

    @Override
    public void onGenericMessage(GenericMessageEvent genericMessageEvent) {
        final String commandString = genericMessageEvent.getMessage().split("\\s")[0];
        final SimpleCommand command = this.commands.get(commandString.toLowerCase());

        this.invokeMethod(command, genericMessageEvent);
    }

    private void invokeMethod(SimpleCommand command, GenericMessageEvent genericMessageEvent) {
        final Method method = command.getMethod();
        Object reply = null;

        try {
            method.setAccessible(true);
            reply = method.invoke(command.getExecutor(), genericMessageEvent);
        } catch (Exception e) {
            Dumbo.getLog().error("An error occurred while invoking method " + method.getName() + "!");
            e.printStackTrace();
        }

        if (reply != null) {
            genericMessageEvent.respondWith(String.valueOf(reply));
        }

    }

}

