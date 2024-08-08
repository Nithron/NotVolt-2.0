package dev.nottekk.notvolt.utils.data;

import dev.nottekk.notvolt.commands.interfaces.Command;
import dev.nottekk.notvolt.commands.interfaces.ICommand;

public class CommandUtil {

    public static String getCommandName(ICommand command) {
        Class<?> commandClass = command.getClass();
        if (commandClass.isAnnotationPresent(Command.class)) {
            return commandClass.getAnnotation(Command.class).name();
        } else {
            return null;
        }
    }

    public static String getCommandDesString(ICommand command) {
        Class<?> commandClass = command.getClass();
        if (commandClass.isAnnotationPresent(Command.class)) {
            return commandClass.getAnnotation(Command.class).description();
        } else {
            return null;
        }
    }

}
