package rs.moma.managers;

import rs.moma.entities.Tretman;
import rs.moma.DataTools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static rs.moma.DataTools.fileTretmani;
import static rs.moma.DataTools.toArrayList;

public class Tretmani {
    private final String poruka = "tretman";

    // CRUD
    public ArrayList<Tretman> get() {
        ArrayList<Tretman> tretmani = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileTretmani), StandardCharsets.UTF_8);
            for (String line : lines)
                tretmani.add(new Tretman(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja tretmana!");
        }
        return tretmani;
    }

    public Tretman get(int id) {
        Optional<Tretman> tretman = get().stream().filter(tmp -> tmp.ID == id).findFirst();
        if (tretman.isPresent()) return tretman.get();
        System.err.println("Traženi tretman ne postoji!");
        return null;
    }

    public void add(Tretman tretman) {
        DataTools.add(get(), fileTretmani, poruka, tretman);
    }

    public void remove(Tretman tretman) {
        DataTools.remove(get(), fileTretmani, poruka, tretman);
    }

    public void edit(Tretman oldTretman, Tretman newTretman) {
        DataTools.edit(get(), fileTretmani, poruka, oldTretman, newTretman);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<Tretman> tretmani) {
        for (Tretman tretman : tretmani)
            if (tretman.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        ArrayList<Tretman> tretmani = get();
        int                i        = 0;
        while (isTakenID(i, tretmani)) i++;
        return i;
    }

    // Pretraga
    public ArrayList<Tretman> filter(int tipID, String naziv, int minTrajanje, int maxTrajanje, float minCena, float maxCena) {
        return toArrayList(get().stream().filter(tretman -> (tipID == -1 || tretman.TipID == tipID) &&
                                                            (naziv == null || tretman.Naziv.toLowerCase().contains(naziv.toLowerCase())) &&
                                                            (minTrajanje == -1 || tretman.Trajanje >= minTrajanje) &&
                                                            (maxTrajanje == -1 || tretman.Trajanje <= maxTrajanje) &&
                                                            (minCena == -1 || tretman.Cena >= minCena) &&
                                                            (maxCena == -1 || tretman.Cena <= maxCena)));
    }
}
