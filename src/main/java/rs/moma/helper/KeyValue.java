package rs.moma.helper;

public class KeyValue {
    public final Object Key;
    public final Object Value;

    public KeyValue(Object key, Object value) {
        Key   = key;
        Value = value;
    }

    @Override
    public String toString() {
        return Key.toString();
    }
}
