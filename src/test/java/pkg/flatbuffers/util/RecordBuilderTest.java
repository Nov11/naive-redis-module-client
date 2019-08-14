package pkg.flatbuffers.util;

import org.junit.Assert;
import org.junit.Test;
import pkg.flatbuffers.rmod.Record;

import java.nio.ByteBuffer;

public class RecordBuilderTest {
    @Test
    public void serialize() {
        RecordBuilder builder = new RecordBuilder();
        String target = "target";
        String url = "url";
        long ts = 1000;
        String request = "request";
        String response = "response";

        builder.target(target)
                .url(url)
                .ts(ts)
                .request(request)
                .response(response);
        ByteBuffer byteBuffer = builder.serialize();

        byteBuffer.compact();
        byteBuffer.flip();

        Record record = RecordBuilder.deserialize(byteBuffer);

        Assert.assertEquals(target, record.target());
        Assert.assertEquals(url, record.uri());
        Assert.assertEquals(ts, record.ts());
        Assert.assertEquals(request, record.request());
        Assert.assertEquals(response, record.response());
    }
}