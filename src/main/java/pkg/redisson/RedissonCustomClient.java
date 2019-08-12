package pkg.redisson;

import org.redisson.api.RFuture;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.client.protocol.RedisCommand;
import org.redisson.client.protocol.convertor.LongReplayConvertor;
import org.redisson.client.protocol.decoder.StringListReplayDecoder;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.slf4j.LoggerFactory.getLogger;

public class RedissonCustomClient {
    private static final Logger logger = getLogger(RedissonCustomClient.class);
    private RedisClient client;
    private RedisConnection connection;

    private RedisCommand<Long> rand = new RedisCommand<Long>("rmod.rand", new LongReplayConvertor());

    public RedissonCustomClient(String url) {
        RedisClientConfig config = new RedisClientConfig();
        config.setAddress(url);
        client = RedisClient.create(config);
        connection = client.connect();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RedissonCustomClient client = new RedissonCustomClient("redis://localhost:6379");
        long randValue = client.rand().get();
        logger.info("rand : {}", randValue);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(String.valueOf(i));
        }
        List<String> echo = client.echo(list).get();
        echo.forEach(c -> {
            logger.info("{}", c);
        });

        client.shutdown();
    }

    public RFuture<Long> rand() {
        return connection.async(rand);
    }

    public RFuture<List<String>> echo(List<String> list) {
        RedisCommand<List<String>> echo = new RedisCommand<List<String>>("rmod.echo", new StringListReplayDecoder());

        return connection.async(echo, (Object[]) list.toArray(new String[]{}));
    }

    public void shutdown() {
        connection.closeAsync();
        client.shutdown();
    }
}
