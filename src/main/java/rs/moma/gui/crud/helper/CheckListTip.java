package rs.moma.gui.crud.helper;

import rs.moma.entities.TipTretmana;

public class CheckListTip {
    public final TipTretmana Tip;
    public       boolean     Selected;

    public CheckListTip(TipTretmana tip) {
        Tip = tip;
    }

    @Override
    public String toString() {
        return Tip.Tip;
    }
}
