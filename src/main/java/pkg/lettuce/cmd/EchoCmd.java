package pkg.lettuce.cmd;

import io.lettuce.core.protocol.ProtocolKeyword;

import java.nio.charset.StandardCharsets;

public class EchoCmd implements ProtocolKeyword {
    private static final String command = "rmod.echo";
    private static final EchoCmd cmd = new EchoCmd();

    public static EchoCmd getInstance() {
        return cmd;
    }


    @Override
    public byte[] getBytes() {
        return command.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String name() {
        return command;
    }
}
