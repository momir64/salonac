package rs.moma.entities;

import rs.moma.DataTools.ENivoSpreme;
import rs.moma.DataTools.EPol;
import rs.moma.DataTools.ETipZaposlenog;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static rs.moma.DataTools.SP1;

public class Zaposlen extends Korisnik {
    public final ETipZaposlenog TipZaposlenog;
    public final ENivoSpreme    Sprema;
    public final float          KoeficijentSprema;
    public final int            GodineStaza;
    public final float          KoeficijentStaz;
    public final float          PlataOsnova;
    public final String         Bonus;
    public final int            PocetakRadnoVreme;
    public final int            KrajRadnoVreme;
    public final int[]          ZaduzeniTipoviTretmana;

    public Zaposlen(int id, String ime, String prezime, EPol pol, String telefon, String adresa, String username, String lozinka,
                    ETipZaposlenog tipZaposlenog, ENivoSpreme sprema, float koeficijentSprema, int godineStaza, float koeficijentStaz,
                    float plataOsnova, String bonus, int pocetakRadnoVreme, int krajRadnoVreme, int[] zaduzeniTipoviTretmana) {
        super(id, ime, prezime, pol, telefon, adresa, username, lozinka);
        TipZaposlenog          = tipZaposlenog;
        Sprema                 = sprema;
        KoeficijentSprema      = koeficijentSprema;
        GodineStaza            = godineStaza;
        KoeficijentStaz        = koeficijentStaz;
        PlataOsnova            = plataOsnova;
        Bonus                  = bonus;
        PocetakRadnoVreme      = pocetakRadnoVreme;
        KrajRadnoVreme         = krajRadnoVreme;
        ZaduzeniTipoviTretmana = zaduzeniTipoviTretmana;
    }

    public Zaposlen(String line) {
        super(String.join(SP1, Arrays.copyOfRange(line.split(SP1), 0, 8)));
        String[] data = line.split(SP1);
        TipZaposlenog          = ETipZaposlenog.valueOf(data[8]);
        Sprema                 = ENivoSpreme.valueOf(data[9]);
        KoeficijentSprema      = Float.parseFloat(data[10]);
        GodineStaza            = Integer.parseInt(data[11]);
        KoeficijentStaz        = Float.parseFloat(data[12]);
        PlataOsnova            = Float.parseFloat(data[13]);
        Bonus                  = data[14];
        PocetakRadnoVreme      = Integer.parseInt(data[15]);
        KrajRadnoVreme         = Integer.parseInt(data[16]);
        ZaduzeniTipoviTretmana = Arrays.stream(Arrays.copyOfRange(data, 16, data.length)).mapToInt(Integer::parseInt).toArray();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(
                super.toString() + SP1 +
                TipZaposlenog + SP1 +
                Sprema + SP1 +
                KoeficijentSprema + SP1 +
                GodineStaza + SP1 +
                KoeficijentStaz + SP1 +
                PlataOsnova + SP1 +
                Bonus + SP1 +
                PocetakRadnoVreme + SP1 +
                KrajRadnoVreme);
        if (ZaduzeniTipoviTretmana == null) str.append(",null");
        else for (int tipID : ZaduzeniTipoviTretmana) str.append(SP1).append(tipID);
        return str.toString();
    }

    @Override
    public boolean equals(Object zaposleni) {
        if (this == zaposleni) return true;
        if (!(zaposleni instanceof Zaposlen)) return false;
        return ID == ((Zaposlen) zaposleni).ID;
    }

    // Specijalne get metode
    public ArrayList<String> getTretmani() {
        ArrayList<String> tretmani = new ArrayList<>();
        for (int id : ZaduzeniTipoviTretmana)
            for (Tretman tretman : new Tretmani().filter(id, null, -1, -1, -1, -1))
                tretmani.add(tretman.Naziv);
        return tretmani;
    }

    public ArrayList<ZakazaniTretman> getZakazaniTretmani() {
        return new ZakazaniTretmani().getKozmeticar(this, null, null, false);
    }

    public int getBrojTretmana(LocalDateTime from, LocalDateTime to) {
        return new ZakazaniTretmani().getKozmeticar(this, from, to, false).size();
    }

    public float getVrednostTretmana(LocalDateTime from, LocalDateTime to) {
        return (float) new ZakazaniTretmani().getKozmeticar(this, from, to, false).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public int getBrojTretmana(int days) {
        return new ZakazaniTretmani().getKozmeticar(this, LocalDateTime.now().minusDays(days), null, false).size();
    }

    public float getVrednostTretmana(int days) {
        return (float) new ZakazaniTretmani().getKozmeticar(this, LocalDateTime.now().minusDays(days), null, false).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public int getBrojTretmanaMesec() {
        return new ZakazaniTretmani().getKozmeticar(this, LocalDateTime.now().minusMonths(1), null, false).size();
    }

    public float getVrednostTretmanaMesec() {
        return (float) new ZakazaniTretmani().getKozmeticar(this, LocalDateTime.now().minusMonths(1), null, false).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    private float calcBonus() {
        float bonus = 0;
        if (Bonus != null) for (String rule : Bonus.split("\\+")) {
            String[] data = rule.split("-");
            if (data[0].equalsIgnoreCase("c"))
                bonus += getBrojTretmanaMesec() * Float.parseFloat(data[1]);
            else if (data[0].equalsIgnoreCase("v"))
                bonus += getVrednostTretmanaMesec() * Float.parseFloat(data[1]);
        }
        return bonus;
    }

    public float getPlata() {
        return PlataOsnova + (Sprema.ordinal() + 1) * KoeficijentSprema + GodineStaza * KoeficijentStaz + calcBonus();
    }
}
