package rs.moma.entities;

import rs.moma.managers.TipoviTretmana;

import static rs.moma.DataTools.SP1;

public class TipTretmana {
    public final int    ID;
    public final String Tip;

    public TipTretmana(int id, String tip) {
        ID  = id == -1 ? new TipoviTretmana().getNewID() : id;
        Tip = tip;
    }

    public TipTretmana(String line) {
        String[] data = line.split(SP1);
        ID  = Integer.parseInt(data[0]);
        Tip = data[1];
    }

    @Override
    public String toString() {
        return Integer.toString(ID) + SP1 + Tip;
    }

    @Override
    public boolean equals(Object tipTretmana) {
        if (this == tipTretmana) return true;
        if (!(tipTretmana instanceof TipTretmana)) return false;
        return ID == ((TipTretmana) tipTretmana).ID;
    }
}
