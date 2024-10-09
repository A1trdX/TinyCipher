package org.a1trdx.tinycipher.command.types.root;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.Callable;

import org.a1trdx.tinycipher.cipher.CipherTool;
import org.a1trdx.tinycipher.misc.util.ConsoleUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "encrypt", description = "Encrypt file.")
public class EncryptCommand implements Callable<Void> {

    @Parameters(index = "0", paramLabel = "<input>", description = "Path to input file")
    private Path inputFile;

    @Parameters(index = "1", paramLabel = "<output>", description = "Path to output file")
    private Path outputFile;

    @Option(names = { "-h", "--help" }, usageHelp = true, hidden = true)
    boolean isHelpRequested;

    @Option(names = { "--base64" }, description = "Encode output to Base64")
    private boolean isEncodeToBase64;

    @Override
    public Void call() throws Exception {
        ConsoleUtils.print("Password: ");
        var password = ConsoleUtils.readPassword();
        var passwordBytes = password.getBytes(StandardCharsets.UTF_8);

        ConsoleUtils.printLine("Encrypting...");

        var inputBytes = Files.readAllBytes(inputFile);

        var outputBytes = CipherTool.encrypt(inputBytes, passwordBytes);
        if (isEncodeToBase64) {
            outputBytes = Base64.getEncoder().encode(outputBytes);
        }

        Files.write(outputFile, outputBytes);
        ConsoleUtils.printLine("Done!");
        return null;
    }
}
