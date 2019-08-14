package pkg.flatbuffers.util;

import com.google.flatbuffers.FlatBufferBuilder;
import pkg.flatbuffers.rmod.Record;

import java.nio.ByteBuffer;

public class RecordBuilder {
    private int targetIdx = -1;
    private int urlIdx = -1;
    private long ts = -1;
    private int requestIdx = -1;
    private int responseIdx = -1;
    private FlatBufferBuilder flatBufferBuilder = new FlatBufferBuilder(1024);

    static RecordBuilder builder() {
        return new RecordBuilder();
    }

    public static Record deserialize(ByteBuffer byteBuffer) {
        return Record.getRootAsRecord(byteBuffer);
    }

    private static void check(int idx) {
        if (idx != -1) {
            throw new IllegalStateException("given Idx has been updated");
        }
    }

    public RecordBuilder target(String targetString) {
        check(targetIdx);

        targetIdx = flatBufferBuilder.createString(targetString);
        return this;
    }

    public RecordBuilder url(String urlString) {
        check(urlIdx);

        urlIdx = flatBufferBuilder.createString(urlString);
        return this;
    }

    public RecordBuilder request(String requestString) {
        check(requestIdx);

        requestIdx = flatBufferBuilder.createString(requestString);
        return this;
    }

    public RecordBuilder response(String responseString) {
        check(responseIdx);

        responseIdx = flatBufferBuilder.createString(responseString);
        return this;
    }

    public RecordBuilder ts(long ts) {
        this.ts = ts;
        return this;
    }

    public ByteBuffer serialize() {
        Record.startRecord(flatBufferBuilder);
        Record.addTarget(flatBufferBuilder, targetIdx);
        Record.addUri(flatBufferBuilder, urlIdx);
        Record.addTs(flatBufferBuilder, ts);
        Record.addRequest(flatBufferBuilder, requestIdx);
        Record.addResponse(flatBufferBuilder, responseIdx);
        int end = Record.endRecord(flatBufferBuilder);
        flatBufferBuilder.finish(end);
        return flatBufferBuilder.dataBuffer();
    }
}