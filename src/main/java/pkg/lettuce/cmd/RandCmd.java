package pkg.lettuce.cmd;

import io.lettuce.core.protocol.ProtocolKeyword;

import java.nio.charset.StandardCharsets;

public class RandCmd implements ProtocolKeyword {
    private static final RandCmd command_rand = new RandCmd();

    public static RandCmd getInstance() {
        return command_rand;
    }

    private static final String command = "rmod.rand";

    private RandCmd() {

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
