package rs.moma.gui;

public class KeyValue {
    String key;
    Object value;

    public KeyValue(String key, Object value) {
        this.key   = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return key;
    }
}
