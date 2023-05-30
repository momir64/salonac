package rs.moma.managers;

import rs.moma.DataTools;
import rs.moma.entities.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static rs.moma.DataTools.EStanjeTermina.*;
import static rs.moma.DataTools.fileZakazaniTretmani;
import static rs.moma.DataTools.toArrayList;

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

    public void add(ZakazaniTretman zakazaniTretman) {
        DataTools.add(get(), fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public void remove(ZakazaniTretman zakazaniTretman) {
        DataTools.remove(get(), fileZakazaniTretmani, poruka, zakazaniTretman);
    }

    public void edit(ZakazaniTretman oldTretman, ZakazaniTretman newTretman) {
        DataTools.edit(get(), fileZakazaniTretmani, poruka, oldTretman, newTretman);
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
    public ArrayList<ZakazaniTretman> getKlijent(Klijent klijent, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return getKlijentKozmeticar(klijent, null, from, to, samoZakazani);
    }

    public ArrayList<ZakazaniTretman> getKozmeticar(Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return getKlijentKozmeticar(null, zaposlen, from, to, samoZakazani);
    }

    public ArrayList<ZakazaniTretman> getKlijentKozmeticar(Klijent klijent, Zaposlen zaposlen, LocalDateTime from, LocalDateTime to, boolean samoZakazani) {
        return toArrayList(get().stream().filter(tretman -> (klijent == null || tretman.KlijentID == klijent.ID) &&
                                                            (zaposlen == null || tretman.KozmeticarID == zaposlen.ID) &&
                                                            (from == null || tretman.Vreme.isAfter(from)) &&
                                                            (to == null || tretman.Vreme.isBefore(to)) &&
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
