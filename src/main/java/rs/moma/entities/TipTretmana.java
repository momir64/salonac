package rs.moma.entities;

import rs.moma.managers.TipoviTretmana;

import static rs.moma.DataTools.SP1;

public class TipTretmana {
    public final int    ID;
    public final String Tip;

    public TipTretmana(int id, String tip, boolean newID) {
        this(newID ? new TipoviTretmana().getNewID() : id, tip);
    }

    public TipTretmana(int id, String tip) {
        Tip = tip;
        ID  = id;
    }

    public TipTretmana(String line) {
        String[] data = line.split(SP1);
        ID  = Integer.parseInt(data[0]);
        Tip = data[1];
    }

    @Override
    public String toString() {
        return ID + SP1 + Tip;
    }

    @Override
    public boolean equals(Object tipTretmana) {
        if (this == tipTretmana) return true;
        if (!(tipTretmana instanceof TipTretmana)) return false;
        return ID == ((TipTretmana) tipTretmana).ID;
    }
}
