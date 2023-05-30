package rs.moma.managers;

import rs.moma.DataTools;
import rs.moma.entities.Tretman;
import rs.moma.entities.Zaposlen;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static rs.moma.DataTools.fileZaposleni;
import static rs.moma.DataTools.toArrayList;

public class Zaposleni {
    private final String poruka = "zaposleni";

    // CRUD
    public ArrayList<Zaposlen> get() {
        ArrayList<Zaposlen> zaposleni = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileZaposleni), StandardCharsets.UTF_8);
            for (String line : lines)
                zaposleni.add(new Zaposlen(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja zaposlenih!");
        }
        return zaposleni;
    }

    public Zaposlen get(int id) {
        Optional<Zaposlen> zaposlen = get().stream().filter(tmp -> tmp.ID == id).findFirst();
        if (zaposlen.isPresent()) return zaposlen.get();
        System.err.println("Traženi zaposleni ne postoji!");
        return null;
    }

    public void add(Zaposlen zaposlen) {
        DataTools.add(get(), fileZaposleni, poruka, zaposlen);
    }

    public void remove(Zaposlen zaposlen) {
        DataTools.remove(get(), fileZaposleni, poruka, zaposlen);
    }

    public void edit(Zaposlen oldZaposlen, Zaposlen newZaposlen) {
        DataTools.edit(get(), fileZaposleni, poruka, oldZaposlen, newZaposlen);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<Zaposlen> zaposleni) {
        for (Zaposlen zaposlen : zaposleni)
            if (zaposlen.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        ArrayList<Zaposlen> zaposleni = get();
        int                 i         = 0;
        while (isTakenID(i, zaposleni)) i++;
        return i;
    }

    // Specijalne get metode
    public ArrayList<Zaposlen> getRadi(Tretman tretman) {
        return toArrayList(get().stream().filter(kozmeticar -> Arrays.stream(kozmeticar.ZaduzeniTretmani).anyMatch(tretmanID -> tretmanID == tretman.ID)));
    }

    // Prijava
    public Zaposlen prijava(String username, String password) {
        return get().stream().filter(zaposlen -> zaposlen.Username.equalsIgnoreCase(username) && zaposlen.Lozinka.equals(password)).findFirst().orElse(null);
    }
}
