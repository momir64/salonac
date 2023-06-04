package rs.moma.managers;

import rs.moma.entities.Klijent;
import rs.moma.entities.Tretman;
import rs.moma.entities.ZakazaniTretman;
import rs.moma.entities.Zaposlen;
import rs.moma.helper.BrojVrednost;
import rs.moma.helper.DataTools.*;
import rs.moma.helper.DataTools;
import rs.moma.helper.KeyValue;
import rs.moma.helper.NazivVrednostVreme;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static rs.moma.helper.DataTools.EStanjeTermina.*;
import static rs.moma.helper.DataTools.ETipZaposlenog.KOZMETICAR;
import static rs.moma.helper.DataTools.fileZakazaniTretmani;
import static rs.moma.helper.DataTools.toArrayList;

public class ZakazaniTretmani {
    private final String poruka = "zakazani tretman";

    // CRUD
    public ArrayList<ZakazaniTretman> get() {
        ArrayList<ZakazaniTretman> zakazaniTretmani = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileZakazaniTretmani), StandardCharsets.UTF_8);
            for (String line : lines)
                zakazaniTretmani.add(new ZakazaniTretman(line));
            for (ZakazaniTretman tretman : updateStanjeTretmana(zakazaniTretmani))
                DataTools.edit(zakazaniTretmani, fileZakazaniTretmani, poruka, tretman, tretman);
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja zakazanih tretmana!");
        }
        return zakazaniTretmani;
    }

    public ZakazaniTretman get(int id) {
        Optional<ZakazaniTretman> zakazaniTretman = get().stream().filter(tmp -> tmp.ID == id).findFirst();
        if (zakazaniTretman.isPresent()) return zakazaniTretman.get();
        System.err.println("Traženi zakazani tretman ne postoji!");
        return null;
    }

    public boolean add(ZakazaniTretman zakazaniTretman) {
        return DataTools.add(get(), fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public boolean remove(ZakazaniTretman zakazaniTretman) {
        return DataTools.remove(get(), fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public boolean edit(ZakazaniTretman oldTretman, ZakazaniTretman newTretman) {
        return DataTools.edit(get(), fileZakazaniTretmani, poruka, oldTretman, newTretman);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<ZakazaniTretman> zakazaniTretmani) {
        for (ZakazaniTretman zakazaniTretman : zakazaniTretmani)
            if (zakazaniTretman.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        ArrayList<ZakazaniTretman> zakazaniTretmani = get();
        int                        i                = 0;
        while (isTakenID(i, zakazaniTretmani)) i++;
        return i;
    }

    // Pretraga
    public ArrayList<ZakazaniTretman> filter(int tipID, int tretmanID, float minPlaceno, float maxPlaceno) {
        return toArrayList(get().stream().filter(t -> {
                                    Tretman tr = new Tretmani().get(t.TretmanID);
                                    return (minPlaceno == -1 || t.getPlaceniIznos() >= minPlaceno) &&
                                           (maxPlaceno == -1 || t.getPlaceniIznos() <= maxPlaceno) &&
                                           (tretmanID == -1 || tr.ID == tretmanID) &&
                                           (tipID == -1 || tr.TipID == tipID);
                                }).sorted(Comparator.comparing(t -> t.KozmeticarID))
                                .sorted(Comparator.comparing(t -> t.Vreme)));
    }

    // Specijalne get metode
    public ArrayList<NazivVrednostVreme> getPrihodi(LocalDateTime from, LocalDateTime to) {
        return toArrayList(get().stream().filter(tretman -> (from == null || tretman.Vreme.isAfter(from) || tretman.Vreme.isEqual(from))
                                                            && (to == null || tretman.Vreme.isBefore(to) || tretman.Vreme.isEqual(to)))
                                .map(tretman -> new NazivVrednostVreme(new Tretmani().get(tretman.TretmanID).Naziv, tretman.getPlaceniIznos(), tretman.Vreme)));
    }

    public double getPrihodiVrednost(LocalDateTime from, LocalDateTime to) {
        return getPrihodi(from, to).stream().mapToDouble(nvv -> nvv.Vrednost).sum();
    }

    public LocalDate getOldestDate() {
        ArrayList<ZakazaniTretman> tretmani = get();
        if (tretmani.isEmpty()) return LocalDate.now();
        return tretmani.stream().min(Comparator.comparing(tretman -> tretman.Vreme)).get().Vreme.toLocalDate();
    }

    public ArrayList<KeyValue> getKozmeticariIzvestaj(LocalDateTime from, LocalDateTime to) {
        ArrayList<Zaposlen>        kozmeticari = toArrayList(new Zaposleni().get().stream().filter(zaposlen -> zaposlen.TipZaposlenog == KOZMETICAR));
        ArrayList<KeyValue>        izvestaji   = new ArrayList<>();
        ArrayList<ZakazaniTretman> tretmani;
        for (Zaposlen kozmeticar : kozmeticari) {
            tretmani = getKozmeticar(kozmeticar, from, to, false);
            izvestaji.add(new KeyValue(kozmeticar, new BrojVrednost(tretmani.size(), (float) tretmani.stream().mapToDouble(ZakazaniTretman::getPlaceniIznos).sum())));
        }
        return toArrayList(izvestaji.stream().sorted(Comparator.comparing(i -> ((Zaposlen) i.Key).Ime)));
    }

    public ArrayList<KeyValue> getTretmaniIzvestaj(LocalDateTime from, LocalDateTime to) {
        ArrayList<Tretman>         tretmani         = new Tretmani().get();
        ArrayList<KeyValue>        izvestaji        = new ArrayList<>();
        ArrayList<ZakazaniTretman> zakazaniTretmani = get();
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
        return (int) get().stream().filter(tr -> tr.Stanje == status && (from == null || tr.Vreme.isAfter(from) || tr.Vreme.isEqual(from))
                                                 && (to == null || tr.Vreme.isBefore(to) || tr.Vreme.isEqual(to))).count();
    }

    public ArrayList<ZakazaniTretman> getKlijent(Klijent klijent, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return getKlijentKozmeticar(klijent, null, from, to, samoZakazani);
    }

    public ArrayList<ZakazaniTretman> getKozmeticar(Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return getKlijentKozmeticar(null, zaposlen, from, to, samoZakazani);
    }

    public ArrayList<ZakazaniTretman> getKlijentKozmeticar(Klijent klijent, Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return toArrayList(get().stream().filter(tretman -> (klijent == null || tretman.KlijentID == klijent.ID) &&
                                                            (zaposlen == null || tretman.KozmeticarID == zaposlen.ID) &&
                                                            (from == null || tretman.Vreme.isAfter(from) || tretman.Vreme.isEqual(from)) &&
                                                            (to == null || tretman.Vreme.isBefore(to) || tretman.Vreme.isEqual(to)) &&
                                                            (!samoZakazani || tretman.Stanje == ZAKAZAN))
                                .sorted(Comparator.comparing(t -> t.KozmeticarID))
                                .sorted(Comparator.comparing(t -> t.Vreme)));
    }

    // Izmena
    public void otkaziTretman(ZakazaniTretman tretman, boolean otkazaoSalon) {
        tretman.Stanje = otkazaoSalon ? OTKAZAO_SALON : OTKAZAO_KLIJENT;
        edit(tretman, tretman);
    }

    public ArrayList<ZakazaniTretman> updateStanjeTretmana(ArrayList<ZakazaniTretman> tretmani) {
        return toArrayList(tretmani.stream().filter(tretman -> tretman.Vreme.isBefore(LocalDateTime.now()) && tretman.Stanje == ZAKAZAN).peek(tretman -> tretman.Stanje = IZVRSEN));
    }
}
