package org.a1trdx.tinycipher.command.types.root;

import javax.crypto.AEADBadTagException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.concurrent.Callable;

import org.a1trdx.tinycipher.cipher.CipherTool;
import org.a1trdx.tinycipher.cipher.CipherToolException;
import org.a1trdx.tinycipher.misc.util.ConsoleUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "decrypt", description = "Decrypt file.")
public class DecryptCommand implements Callable<Void> {

    @Parameters(index = "0", paramLabel = "<input>", description = "Path to input file")
    private Path inputFile;

    @Parameters(index = "1", paramLabel = "<output>", description = "Path to output file")
    private Path outputFile;

    @Option(names = { "-h", "--help" }, usageHelp = true, hidden = true)
    boolean isHelpRequested;

    @Option(names = { "--base64" }, description = "Decode input from Base64")
    private boolean isDecodeFromBase64;

    @Override
    public Void call() throws Exception {
        ConsoleUtils.print("Password: ");
        var password = ConsoleUtils.readPassword();
        var passwordBytes = password.getBytes(StandardCharsets.UTF_8);

        ConsoleUtils.printLine("Decrypting...");

        var inputBytes = Files.readAllBytes(inputFile);
        if (isDecodeFromBase64) {
            inputBytes = Base64.getDecoder().decode(inputBytes);
        }

        var outputBytes = (byte[]) null;
        try {
            outputBytes = CipherTool.decrypt(inputBytes, passwordBytes);
        } catch (CipherToolException ex) {
            if (ex.getCause() instanceof AEADBadTagException) {
                ConsoleUtils.printLine("Incorrect password!");
                return null;
            } else {
                throw ex;
            }
        }

        Files.write(outputFile, outputBytes);
        ConsoleUtils.printLine("Done!");
        return null;
    }
}