package rs.moma.gui;

public class KeyValue {
    private final Object key;
    private final Object value;

    public KeyValue(Object key, Object value) {
        this.key   = key;
        this.value = value;
    }

    public Object getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key.toString();
    }
}
