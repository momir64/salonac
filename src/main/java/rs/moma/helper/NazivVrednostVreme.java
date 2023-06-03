package rs.moma.helper;

import java.time.LocalDateTime;

public class NazivVrednostVreme {
    public final String        Naziv;
    public final float         Vrednost;
    public final LocalDateTime Vreme;

    public NazivVrednostVreme(String naziv, float vrednost, LocalDateTime vreme) {
        Naziv    = naziv;
        Vrednost = vrednost;
        Vreme    = vreme;
    }
}
