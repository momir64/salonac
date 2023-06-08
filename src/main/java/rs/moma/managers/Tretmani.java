package rs.moma.managers;

import rs.moma.entities.TipTretmana;
import rs.moma.entities.Tretman;
import rs.moma.helper.DataTools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static rs.moma.helper.DataTools.*;

public class Tretmani {
    private       ArrayList<Tretman> tretmani = new ArrayList<>();
    private final String             poruka   = "tretman";

    public Tretmani() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileTretmani), StandardCharsets.UTF_8);
            for (String line : lines)
                tretmani.add(new Tretman(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja tretmana!");
        }
    }

    // Ažuriranje ključeva
    public boolean removeTip(TipTretmana tip) {
        tretmani = toArrayList(tretmani.stream().filter(tretman -> tretman.TipID != tip.ID));
        return DataTools.save(tretmani, fileTretmani, poruka);
    }

    // CRUD
    public ArrayList<Tretman> get() {
        return tretmani;
    }

    public Tretman get(int id) {
        Optional<Tretman> tretman = tretmani.stream().filter(tmp -> tmp.ID == id).findFirst();
        if (tretman.isPresent()) return tretman.get();
        System.err.println("Traženi tretman ne postoji!");
        return null;
    }

    public boolean add(Tretman tretman) {
        return DataTools.add(tretmani, fileTretmani, poruka, tretman);
    }

    public boolean remove(Tretman tretman) {
        return DataTools.remove(tretmani, fileTretmani, poruka, tretman) && new ZakazaniTretmani().removeTretman(tretman);
    }

    public boolean edit(Tretman oldTretman, Tretman newTretman) {
        return DataTools.edit(tretmani, fileTretmani, poruka, oldTretman, newTretman);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id) {
        for (Tretman tretman : tretmani)
            if (tretman.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        int i = 0;
        while (isTakenID(i)) i++;
        return i;
    }

    // Pretraga
    public ArrayList<Tretman> filter(int tipID, String naziv, int minTrajanje, int maxTrajanje, float minCena, float maxCena) {
        return toArrayList(tretmani.stream().filter(tretman -> (tipID == -1 || tretman.TipID == tipID) &&
                                                               (naziv == null || tretman.Naziv.toLowerCase().contains(naziv.toLowerCase())) &&
                                                               (minTrajanje == -1 || tretman.Trajanje >= minTrajanje) &&
                                                               (maxTrajanje == -1 || tretman.Trajanje <= maxTrajanje) &&
                                                               (minCena == -1 || tretman.Cena >= minCena) &&
                                                               (maxCena == -1 || tretman.Cena <= maxCena)));
    }
}
