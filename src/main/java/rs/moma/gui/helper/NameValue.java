package rs.moma.gui.helper;

import java.util.Objects;

public class NameValue {
    public String Name;
    public Object Value;

    public NameValue(String name, Object value) {
        Name  = name;
        Value = value;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public Object getValue() {
        return Value;
    }

    public void setValue(Object value) {
        this.Value = value;
    }

    @Override
    public String toString() {
        return Name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return Objects.equals(Value, ((NameValue) obj).Value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Value);
    }
}
