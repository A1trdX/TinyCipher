package org.a1trdx.tinycipher.command;

import org.a1trdx.tinycipher.command.types.RootCommand;
import picocli.CommandLine;

public class CommandManager {

    private final CommandLine commandLine;

    public CommandManager() {
        commandLine = new CommandLine(new RootCommand());

        // Remove command name after "Usage: "
        commandLine.setCommandName(""); // Make correct indent on header wrap
        commandLine.setHelpFactory((commandSpec, colorScheme) -> {
            return new CommandLine.Help(commandSpec, colorScheme) {
                @Override
                public String synopsis(int synopsisHeadingLength) {
                    var synopsis = super.synopsis(synopsisHeadingLength);
                    return synopsis.substring(synopsis.indexOf(' ') + 1); // Remove command name
                }
            };
        });

        // Disable coloring
        commandLine.setColorScheme(new CommandLine.Help.ColorScheme.Builder()
                .ansi(CommandLine.Help.Ansi.OFF)
                .build());
    }

    public void execute(String... args) {
        commandLine.execute(args);
    }
}
