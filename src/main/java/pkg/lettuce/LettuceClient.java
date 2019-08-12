package pkg.lettuce;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.output.IntegerOutput;
import io.lettuce.core.protocol.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pkg.lettuce.cmd.EchoCmd;
import pkg.lettuce.cmd.RandCmd;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LettuceClient {
    private static final Logger logger = LoggerFactory.getLogger(LettuceClient.class);
    private static final RedisCodec<String, String> codec = StringCodec.UTF8;
    private static final RedisClient redisClient = RedisClient.create();
    private StatefulRedisConnection<String, String> connection;

    private RandCmd randCmd = RandCmd.getInstance();

    public LettuceClient(String host, int port) {
        connection = redisClient.connect(RedisURI.create(host, port));
    }

    public RedisFuture<Long> rand() {
        return connection.async().dispatch(randCmd, new IntegerOutput<String, String>(codec));
    }

    public RedisFuture<Long> randCmd() {
        RedisCommand<String, String, Long> command = new Command<>(randCmd, new IntegerOutput<String, String>(codec));
        AsyncCommand<String, String, Long> async = new AsyncCommand<>(command);
        connection.dispatch(async);
        return async;
    }

    public void shutdown() {
        connection.close();
        redisClient.shutdown();
    }

    public RedisFuture<List<Object>> echo(List<String> args) {
        CommandArgs<String, String> commandArgs = new CommandArgs<>(codec);
        args.forEach(commandArgs::add);
        return connection.async().dispatch(EchoCmd.getInstance(), new ArrayOutput<String, String>(codec), commandArgs);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        LettuceClient lettuceClient = new LettuceClient("localhost", 6379);
        Long response = lettuceClient.rand().get();
        logger.info("response : {}", response);
        logger.info("response : {}", lettuceClient.randCmd().get());

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        List<Object> objects = lettuceClient.echo(list).get();
        objects.forEach(o -> {
            String s = (String) o;
            logger.info("{}", s);
        });
    }
}
