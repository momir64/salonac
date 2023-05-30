package rs.moma.gui;

import java.util.Objects;

public class ComboKeyValue {
    String key;
    Object value;

    public ComboKeyValue(String key, Object value) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(value, ((ComboKeyValue) obj).value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
