package rs.moma.entities;

import java.time.LocalDateTime;

public class KozmeticarVreme implements Comparable<KozmeticarVreme> {
    public final Zaposlen      Kozmeticar;
    public final LocalDateTime Vreme;

    public KozmeticarVreme(Zaposlen kozmeticar, LocalDateTime vreme) {
        Kozmeticar = kozmeticar;
        Vreme      = vreme;
    }

    @Override
    public int compareTo(KozmeticarVreme termin) {
        return Vreme.compareTo(termin.Vreme);
    }
}
