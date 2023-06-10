package rs.moma.managers;

import rs.moma.entities.*;
import rs.moma.gui.helper.NameValue;
import rs.moma.helper.BrojVrednost;
import rs.moma.helper.DataTools;
import rs.moma.helper.KeyValue;
import rs.moma.helper.NazivVrednostVreme;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static rs.moma.helper.DataTools.*;
import static rs.moma.helper.DataTools.EStanjeTermina.*;
import static rs.moma.helper.DataTools.ETipZaposlenog.KOZMETICAR;

public class ZakazaniTretmani {
    private       ArrayList<ZakazaniTretman> zakazaniTretmani = new ArrayList<>();
    private final String                     poruka           = "zakazani tretman";

    public ZakazaniTretmani() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileZakazaniTretmani), StandardCharsets.UTF_8);
            for (String line : lines)
                zakazaniTretmani.add(new ZakazaniTretman(line));
            for (ZakazaniTretman tretman : updateStanjeTretmana())
                DataTools.edit(zakazaniTretmani, fileZakazaniTretmani, poruka, tretman, tretman);
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja zakazanih tretmana!");
        }
    }

    // Ažuriranje ključeva
    public boolean removeTretman(Tretman tretman) {
        zakazaniTretmani = toArrayList(zakazaniTretmani.stream().filter(zakazaniTretman -> zakazaniTretman.TretmanID != tretman.ID));
        return DataTools.save(zakazaniTretmani, fileZakazaniTretmani, poruka);
    }

    public boolean removeKlijent(Klijent klijent) {
        zakazaniTretmani = toArrayList(zakazaniTretmani.stream().filter(zakazaniTretman -> zakazaniTretman.KlijentID != klijent.ID));
        return DataTools.save(zakazaniTretmani, fileZakazaniTretmani, poruka);
    }

    public boolean removeKozmeticar(Zaposlen zaposlen) {
        zakazaniTretmani = toArrayList(zakazaniTretmani.stream().filter(zakazaniTretman -> zakazaniTretman.KozmeticarID != zaposlen.ID));
        return DataTools.save(zakazaniTretmani, fileZakazaniTretmani, poruka);
    }

    // CRUD
    public ArrayList<ZakazaniTretman> get() {
        return zakazaniTretmani;
    }

    public ZakazaniTretman get(int id) {
        Optional<ZakazaniTretman> zakazaniTretman = zakazaniTretmani.stream().filter(tmp -> tmp.ID == id).findFirst();
        if (zakazaniTretman.isPresent()) return zakazaniTretman.get();
        System.err.println("Traženi zakazani tretman ne postoji!");
        return null;
    }

    public boolean add(ZakazaniTretman zakazaniTretman) {
        return DataTools.add(zakazaniTretmani, fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public boolean remove(ZakazaniTretman zakazaniTretman) {
        return DataTools.remove(zakazaniTretmani, fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public boolean edit(ZakazaniTretman oldTretman, ZakazaniTretman newTretman) {
        return DataTools.edit(zakazaniTretmani, fileZakazaniTretmani, poruka, oldTretman, newTretman);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id) {
        for (ZakazaniTretman zakazaniTretman : zakazaniTretmani)
            if (zakazaniTretman.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        int i = 0;
        while (isTakenID(i)) i++;
        return i;
    }

    // Pretraga
    public ArrayList<ZakazaniTretman> filter(int tipID, int tretmanID, float minPlaceno, float maxPlaceno) {
        Tretmani tretmani = new Tretmani();
        return toArrayList(zakazaniTretmani.stream().filter(t -> (minPlaceno == -1 || t.getPlaceniIznos() >= minPlaceno) &&
                                                                 (maxPlaceno == -1 || t.getPlaceniIznos() <= maxPlaceno) &&
                                                                 (tretmanID == -1 || tretmani.get(t.TretmanID).ID == tretmanID) &&
                                                                 (tipID == -1 || tretmani.get(t.TretmanID).TipID == tipID))
                                           .sorted(Comparator.comparing(t -> t.KozmeticarID))
                                           .sorted(Comparator.comparing(t -> t.Vreme)));
    }

    public ArrayList<ZakazaniTretman> filter(Klijent klijent, Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, EStanjeTermina stanje) {
        return toArrayList(zakazaniTretmani.stream().filter(tretman -> (klijent == null || tretman.KlijentID == klijent.ID) &&
                                                                       (zaposlen == null || tretman.KozmeticarID == zaposlen.ID) &&
                                                                       (from == null || tretman.Vreme.isAfter(from) || tretman.Vreme.isEqual(from)) &&
                                                                       (to == null || tretman.Vreme.isBefore(to) || tretman.Vreme.isEqual(to)) &&
                                                                       (stanje == null || tretman.Stanje == stanje))
                                           .sorted(Comparator.comparing(t -> t.KozmeticarID))
                                           .sorted(Comparator.comparing(t -> t.Vreme)));
    }

    public ArrayList<ZakazaniTretman> filter(int tipID, LocalDateTime from, LocalDateTime to) {
        Tretmani tretmani = new Tretmani();
        return toArrayList(zakazaniTretmani.stream().filter(tretman -> (tipID == -1 || tretmani.get(tretman.TretmanID).TipID == tipID) &&
                                                                       (from == null || tretman.Vreme.isAfter(from) || tretman.Vreme.isEqual(from)) &&
                                                                       (to == null || tretman.Vreme.isBefore(to)))
                                           .sorted(Comparator.comparing(t -> t.Vreme)));
    }

    public ArrayList<ZakazaniTretman> getForKozmeticar(Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, EStanjeTermina stanje) {
        return filter(null, zaposlen, from, to, stanje);
    }

    public ArrayList<ZakazaniTretman> getForKlijent(Klijent klijent, LocalDateTime from, LocalDateTime to) {
        return filter(klijent, null, from, to, null);
    }

    public ArrayList<ZakazaniTretman> getForKozmeticar(Zaposlen zaposlen, LocalDateTime from, LocalDateTime to) {
        return filter(null, zaposlen, from, to, null);
    }

    // Specijalne get metode
    public ArrayList<NazivVrednostVreme> getPrihodi(LocalDateTime from, LocalDateTime to) {
        Tretmani tretmani = new Tretmani();
        return toArrayList(zakazaniTretmani.stream().filter(tretman -> (from == null || tretman.Vreme.isAfter(from) || tretman.Vreme.isEqual(from))
                                                                       && (to == null || tretman.Vreme.isBefore(to) || tretman.Vreme.isEqual(to)))
                                           .map(tretman -> new NazivVrednostVreme(tretmani.get(tretman.TretmanID).Naziv, tretman.getPlaceniIznos(), tretman.Vreme)));
    }

    public double getPrihodiVrednost(LocalDateTime from, LocalDateTime to) {
        return getPrihodi(from, to).stream().mapToDouble(nvv -> nvv.Vrednost).sum();
    }

    public LocalDate getNewestDate() {
        if (zakazaniTretmani.isEmpty()) return LocalDate.now();
        LocalDate newest = zakazaniTretmani.stream().max(Comparator.comparing(tretman -> tretman.Vreme)).get().Vreme.toLocalDate();
        if (newest.isBefore(LocalDate.now())) return LocalDate.now();
        return newest;
    }

    public LocalDate getOldestDate() {
        if (zakazaniTretmani.isEmpty()) return LocalDate.now();
        return zakazaniTretmani.stream().min(Comparator.comparing(tretman -> tretman.Vreme)).get().Vreme.toLocalDate();
    }

    public ArrayList<NameValue> getKozmeticariDijagramData() {
        ArrayList<Zaposlen>        kozmeticari = toArrayList(new Zaposleni().get().stream().filter(zaposlen -> zaposlen.TipZaposlenog == KOZMETICAR));
        ArrayList<NameValue>       izvestaji   = new ArrayList<>();
        ArrayList<ZakazaniTretman> tretmani;
        for (Zaposlen kozmeticar : kozmeticari) {
            tretmani = getForKozmeticar(kozmeticar, LocalDateTime.now().minusDays(30), null, IZVRSEN);
            izvestaji.add(new NameValue(kozmeticar.getDisplayName(), tretmani.size()));
        }
        return izvestaji;
    }

    public ArrayList<NameValue> getStatusiDijagramData() {
        return toArrayList(Arrays.stream(values()).map(status -> new NameValue(getStanjeName(status), countOfStatus(status, LocalDateTime.now().minusDays(30), LocalDateTime.now()))));
    }

    public ArrayList<NameValue> getPrihodiTipDijagramData() {
        ArrayList<NameValue> izvestaji = new ArrayList<>();
        for (TipTretmana tip : new TipoviTretmana().get()) {
            ArrayList<KeyValue> meseci = new ArrayList<>();
            for (int i = 0; i < 12; i++)
                 meseci.add(new KeyValue(LocalDateTime.now().getMonthValue() - i - 1, filter(tip.ID, LocalDate.now().withDayOfMonth(1).atTime(0, 0).minusMonths(i),
                                                                                             LocalDate.now().withDayOfMonth(1).atTime(0, 0).minusMonths(i - 1))
                         .stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum()));
            izvestaji.add(new NameValue(tip.Tip, meseci));
        }
        ArrayList<KeyValue> meseci = new ArrayList<>();
        for (int i = 0; i < 12; i++)
             meseci.add(new KeyValue(LocalDateTime.now().getMonthValue() - i - 1, getPrihodiVrednost(LocalDate.now().withDayOfMonth(1).atTime(0, 0).minusMonths(i),
                                                                                                     LocalDate.now().withDayOfMonth(1).atTime(0, 0).minusMonths(i - 1))));
        izvestaji.add(new NameValue("Ukupno", meseci));
        return izvestaji;
    }

    public ArrayList<KeyValue> getKozmeticariIzvestaj(LocalDateTime from, LocalDateTime to) {
        ArrayList<Zaposlen>        kozmeticari = toArrayList(new Zaposleni().get().stream().filter(zaposlen -> zaposlen.TipZaposlenog == KOZMETICAR));
        ArrayList<KeyValue>        izvestaji   = new ArrayList<>();
        ArrayList<ZakazaniTretman> tretmani;
        for (Zaposlen kozmeticar : kozmeticari) {
            tretmani = getForKozmeticar(kozmeticar, from, to);
            izvestaji.add(new KeyValue(kozmeticar, new BrojVrednost(tretmani.size(), (float) tretmani.stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum())));
        }
        return toArrayList(izvestaji.stream().sorted(Comparator.comparing(i -> ((Zaposlen) i.Key).Ime)));
    }

    public ArrayList<KeyValue> getTretmaniIzvestaj(LocalDateTime from, LocalDateTime to) {
        ArrayList<Tretman>         tretmani  = new Tretmani().get();
        ArrayList<KeyValue>        izvestaji = new ArrayList<>();
        ArrayList<ZakazaniTretman> tmp;
        for (Tretman tretman : tretmani) {
            tmp = toArrayList(zakazaniTretmani.stream().filter(tr -> tr.TretmanID == tretman.ID
                                                                     && (from == null || tr.Vreme.isAfter(from) || tr.Vreme.isEqual(from))
                                                                     && (to == null || tr.Vreme.isBefore(to) || tr.Vreme.isEqual(to))));
            izvestaji.add(new KeyValue(tretman, new BrojVrednost(tmp.size(), (float) tmp.stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum())));
        }
        return toArrayList(izvestaji.stream().sorted(Comparator.comparing(tr -> ((Tretman) tr.Key).Naziv)));
    }

    public int countOfStatus(EStanjeTermina status, LocalDateTime from, LocalDateTime to) {
        return (int) zakazaniTretmani.stream().filter(tr -> tr.Stanje == status
                                                            && (from == null || tr.Vreme.isAfter(from) || tr.Vreme.isEqual(from))
                                                            && (to == null || tr.Vreme.isBefore(to) || tr.Vreme.isEqual(to))).count();
    }

    // Izmena
    public void otkaziTretman(ZakazaniTretman tretman, boolean otkazaoSalon) {
        tretman.Stanje = otkazaoSalon ? OTKAZAO_SALON : OTKAZAO_KLIJENT;
        edit(tretman, tretman);
    }

    public ArrayList<ZakazaniTretman> updateStanjeTretmana() {
        return toArrayList(zakazaniTretmani.stream().filter(tretman -> tretman.Vreme.isBefore(LocalDateTime.now()) && tretman.Stanje == ZAKAZAN).peek(tretman -> tretman.Stanje = IZVRSEN));
    }
}
