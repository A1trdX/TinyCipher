package org.a1trdx.tinycipher.command.types;

import org.a1trdx.tinycipher.command.types.root.DecryptCommand;
import org.a1trdx.tinycipher.command.types.root.EncryptCommand;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
        name = "root",
        subcommands = {
                EncryptCommand.class,
                DecryptCommand.class })
public class RootCommand {

    @Option(names = { "-h", "--help" }, usageHelp = true, hidden = true)
    boolean isHelpRequested;
}
