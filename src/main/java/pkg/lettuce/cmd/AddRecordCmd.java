package pkg.lettuce.cmd;

import io.lettuce.core.protocol.ProtocolKeyword;

import java.nio.charset.StandardCharsets;

public class AddRecordCmd implements ProtocolKeyword {
    private static final String cmd = "rmod.addRecord";

    @Override
    public byte[] getBytes() {
        return cmd.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String name() {
        return cmd;
    }
}
