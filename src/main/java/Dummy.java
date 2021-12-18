import org.apache.kafka.common.header.Header;

public class Dummy implements Header {
    // https://kafka.apache.org/23/javadoc/org/apache/kafka/common/header/Header.html
    int rs;

    public Dummy(int recordSize) {
        this.rs = recordSize;
    }

    @Override
    public String key() {
        return "";
    }

    @Override
    public byte[] value() {
        return new byte[rs - 30];
    }
}
