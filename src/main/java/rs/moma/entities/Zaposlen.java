package rs.moma.entities;

import rs.moma.helper.Korisnik;
import rs.moma.managers.Tretmani;
import rs.moma.managers.ZakazaniTretmani;
import rs.moma.managers.Zaposleni;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.IntStream;

import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.EStanjeTermina.ZAKAZAN;

public class Zaposlen extends Korisnik {
    public final ETipZaposlenog TipZaposlenog;
    public final ENivoSpreme    Sprema;
    public final float          KoeficijentSprema;
    public final int            GodineStaza;
    public final float          KoeficijentStaz;
    public final float          PlataOsnova;
    public final String         Bonus;
    public final int[]          ZaduzeniTipoviTretmana;

    public Zaposlen(int id, String ime, String prezime, EPol pol, String telefon, String adresa, String username,
                    String lozinka, ETipZaposlenog tipZaposlenog, ENivoSpreme sprema, float koeficijentSprema,
                    int godineStaza, float koeficijentStaz, float plataOsnova, String bonus, int[] zaduzeniTipoviTretmana) {
        super(id, ime, prezime, pol, telefon, adresa, username, lozinka);
        TipZaposlenog          = tipZaposlenog;
        Sprema                 = sprema;
        KoeficijentSprema      = koeficijentSprema;
        GodineStaza            = godineStaza;
        KoeficijentStaz        = koeficijentStaz;
        PlataOsnova            = plataOsnova;
        Bonus                  = bonus;
        ZaduzeniTipoviTretmana = zaduzeniTipoviTretmana;
    }

    public Zaposlen(String ime, String prezime, EPol pol, String telefon, String adresa, String username,
                    String lozinka, ETipZaposlenog tipZaposlenog, ENivoSpreme sprema, float koeficijentSprema,
                    int godineStaza, float koeficijentStaz, float plataOsnova, String bonus, int[] zaduzeniTipoviTretmana) {
        this(new Zaposleni().getNewID(), ime, prezime, pol, telefon, adresa, username, lozinka, tipZaposlenog, sprema,
             koeficijentSprema, godineStaza, koeficijentStaz, plataOsnova, bonus, zaduzeniTipoviTretmana);
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
        ZaduzeniTipoviTretmana = Arrays.stream(Arrays.copyOfRange(data, 15, data.length)).mapToInt(Integer::parseInt).toArray();
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
                Bonus + SP1);
        if (ZaduzeniTipoviTretmana == null || ZaduzeniTipoviTretmana.length == 0) str.append(-1);
        else for (int tipID : ZaduzeniTipoviTretmana) str.append(tipID);
        return str.toString();
    }

    @Override
    public boolean equals(Object zaposleni) {
        if (this == zaposleni) return true;
        if (!(zaposleni instanceof Zaposlen)) return false;
        return ID == ((Zaposlen) zaposleni).ID;
    }

    // Specijalne get metode
    public ArrayList<Tretman> getTretmani() {
        ArrayList<Tretman> tretmani = new ArrayList<>();
        for (Tretman tretman : new Tretmani().get())
            if (Arrays.stream(ZaduzeniTipoviTretmana).anyMatch(tipID -> tipID == tretman.TipID))
                tretmani.add(tretman);
        return tretmani;
    }

    public ArrayList<ZakazaniTretman> getZakazaniTretmani() {
        return new ZakazaniTretmani().getForKozmeticar(this, null, null, ZAKAZAN);
    }

    public ArrayList<Integer> getSlobodniTermini(LocalDate date, ZakazaniTretman oldTretman, int potrebnoTrajanjeTermina) {
        if (date.isBefore(LocalDate.now()))
            return new ArrayList<>(oldTretman != null ? Collections.singletonList(oldTretman.Vreme.getHour()) : Collections.emptyList());
        Salon salon = new Salon();
        ArrayList<Integer> termini = toArrayList(IntStream.rangeClosed(date.isEqual(LocalDate.now()) ?
                                                                       Math.max(LocalDateTime.now().getHour() + 1, salon.PocetakRadnogVremena) : salon.PocetakRadnogVremena,
                                                                       salon.KrajRadnogVremena - (int) Math.ceil(potrebnoTrajanjeTermina / 60.0)).boxed());
        ArrayList<ZakazaniTretman> tretmani = new ZakazaniTretmani().getForKozmeticar(this, date.atStartOfDay(), date.atTime(LocalTime.MAX), ZAKAZAN);
        if (oldTretman != null) tretmani.remove(oldTretman);
        for (ZakazaniTretman tretman : tretmani) {
            for (int i = tretman.Vreme.getHour() - 1; i >= salon.PocetakRadnogVremena && i > tretman.Vreme.getHour() - (int) Math.ceil(potrebnoTrajanjeTermina / 60.0); i--)
                 termini.remove(new Integer(i));
            for (int i = 0; i < tretman.Trajanje; i += 60)
                 termini.remove(new Integer(tretman.Vreme.getHour() + i / 60));
        }
        return termini;
    }

    public String getDisplayName() {
        if (new Zaposleni().get().stream().filter(zaposlen -> (zaposlen.Ime + " " + zaposlen.Prezime).equals(Ime + " " + Prezime)).count() > 1)
            return Ime + " " + Prezime + ", " + Username;
        else
            return Ime + " " + Prezime;
    }

    public int getBrojTretmana(LocalDateTime from, LocalDateTime to) {
        return new ZakazaniTretmani().getForKozmeticar(this, from, to).size();
    }

    public float getVrednostTretmana(LocalDateTime from, LocalDateTime to) {
        return (float) new ZakazaniTretmani().getForKozmeticar(this, from, to).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public int getBrojTretmana(int days) {
        return new ZakazaniTretmani().getForKozmeticar(this, LocalDateTime.now().minusDays(days), null).size();
    }

    public float getVrednostTretmana(int days) {
        return (float) new ZakazaniTretmani().getForKozmeticar(this, LocalDateTime.now().minusDays(days), null).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    public int getBrojTretmanaMesec(int months) {
        return new ZakazaniTretmani().getForKozmeticar(this, LocalDateTime.now().minusMonths(months), null).size();
    }

    public float getVrednostTretmanaMesec(int months) {
        return (float) new ZakazaniTretmani().getForKozmeticar(this, LocalDateTime.now().minusMonths(months), null).stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum();
    }

    private float calcBonus() {
        float bonus = 0;
        if (!Bonus.isEmpty()) for (String rule : Bonus.split(SP3)) {
            String[] data       = rule.split(SP4);
            float    bonusParam = 0;
            if (data[0].equalsIgnoreCase("m"))
                bonusParam = data[2].equalsIgnoreCase("c") ? getBrojTretmanaMesec(Integer.parseInt(data[1])) : data[2].equalsIgnoreCase("v") ? getVrednostTretmanaMesec(Integer.parseInt(data[1])) : 0;
            else if (data[0].equalsIgnoreCase("w"))
                bonusParam = data[2].equalsIgnoreCase("c") ? getBrojTretmana(7 * Integer.parseInt(data[1])) : data[2].equalsIgnoreCase("v") ? getVrednostTretmana(7 * Integer.parseInt(data[1])) : 0;
            else if (data[0].equalsIgnoreCase("d"))
                bonusParam = data[2].equalsIgnoreCase("c") ? getBrojTretmana(Integer.parseInt(data[1])) : data[2].equalsIgnoreCase("v") ? getVrednostTretmana(Integer.parseInt(data[1])) : 0;
            bonus += bonusParam * Float.parseFloat(data[3]);
        }
        return bonus;
    }

    public float getPlata() {
        return PlataOsnova + (Sprema.ordinal() + 1) * KoeficijentSprema + GodineStaza * KoeficijentStaz + calcBonus();
    }
}
