package pkg.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.protocol.AsyncCommand;
import io.lettuce.core.protocol.Command;
import io.lettuce.core.protocol.ProtocolKeyword;
import io.lettuce.core.protocol.RedisCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

public class LettuceClient {
    private static final Logger logger = LoggerFactory.getLogger(LettuceClient.class);
    private static final RedisCodec<String, String> codec = StringCodec.UTF8;
    private static final RedisClient redisClient = RedisClient.create();
    private StatefulRedisConnection<String, String> connection;

    private RAND randCommand = new RAND();

    public LettuceClient(String host, int port) {
        connection = redisClient.connect(RedisURI.create(host, port));
    }

    public RedisFuture<Long> rand() {
        return connection.async().dispatch(randCommand, new IntegerOutput<String, String>(codec));
    }

    public RedisFuture<Long> randCmd() {
        RedisCommand<String, String, Long> command = new Command<>(randCommand, new IntegerOutput<String, String>(codec));
        AsyncCommand<String, String, Long> async = new AsyncCommand<>(command);
        connection.dispatch(async);
        return async;
    }

    public void shutdown() {
        connection.close();
    }

    static class RAND implements ProtocolKeyword {
        private static final RAND command_rand = new RAND();

        public static RAND getInstance() {
            return command_rand;
        }

        private static final String command = "helloworld.rand";

        private RAND() {

        }

        @Override
        public byte[] getBytes() {
            return command.getBytes(StandardCharsets.UTF_8);
        }

        @Override
        public String name() {
            return "helloworld.rand";
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LettuceClient lettuceClient = new LettuceClient("localhost", 6379);
        Long response = lettuceClient.rand().get();
        logger.info("response : {}", response);
        logger.info("response : {}", lettuceClient.randCmd().get());
    }
}
