package org.a1trdx.tinycipher;

import org.a1trdx.tinycipher.command.CommandManager;

public class Boot {

    private void run(String[] args) throws Exception {
        var commandManager = new CommandManager();
        commandManager.execute(args);
    }

    public static void main(String[] args) throws Exception {
        new Boot().run(args);
    }
}
