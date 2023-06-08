package rs.moma.managers;

import rs.moma.helper.DataTools;
import rs.moma.entities.Tretman;
import rs.moma.entities.Zaposlen;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static rs.moma.helper.DataTools.ETipZaposlenog.KOZMETICAR;
import static rs.moma.helper.DataTools.fileZaposleni;
import static rs.moma.helper.DataTools.toArrayList;

public class Zaposleni {
    private final ArrayList<Zaposlen> zaposleni = new ArrayList<>();
    private final String              poruka    = "zaposleni";

    public Zaposleni() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileZaposleni), StandardCharsets.UTF_8);
            for (String line : lines)
                zaposleni.add(new Zaposlen(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja zaposlenih!");
        }
    }

    // CRUD
    public ArrayList<Zaposlen> get() {
        return zaposleni;
    }

    public Zaposlen get(int id) {
        Optional<Zaposlen> zaposlen = zaposleni.stream().filter(tmp -> tmp.ID == id).findFirst();
        if (zaposlen.isPresent()) return zaposlen.get();
        System.err.println("Traženi zaposleni ne postoji!");
        return null;
    }

    public boolean add(Zaposlen zaposlen) {
        return DataTools.add(zaposleni, fileZaposleni, poruka, zaposlen);
    }

    public boolean remove(Zaposlen zaposlen) {
        return DataTools.remove(zaposleni, fileZaposleni, poruka, zaposlen) && new ZakazaniTretmani().removeKozmeticar(zaposlen);
    }

    public boolean edit(Zaposlen oldZaposlen, Zaposlen newZaposlen) {
        return DataTools.edit(zaposleni, fileZaposleni, poruka, oldZaposlen, newZaposlen);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id) {
        for (Zaposlen zaposlen : zaposleni)
            if (zaposlen.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        int i = 0;
        while (isTakenID(i)) i++;
        return i;
    }

    // Specijalne get metode
    public ArrayList<Zaposlen> getRadi(Tretman tretman) {
        return toArrayList(zaposleni.stream().filter(kozmeticar -> Arrays.stream(kozmeticar.ZaduzeniTipoviTretmana).anyMatch(tipID -> tipID == tretman.TipID)));
    }

    public ArrayList<Zaposlen> getKozmeticari() {
        return toArrayList(zaposleni.stream().filter(kozmeticar -> kozmeticar.TipZaposlenog == KOZMETICAR));
    }

    // Prijava
    public Zaposlen prijava(String username, String password) {
        return zaposleni.stream().filter(zaposlen -> zaposlen.Username.equalsIgnoreCase(username) && zaposlen.Lozinka.equals(password)).findFirst().orElse(null);
    }
}
