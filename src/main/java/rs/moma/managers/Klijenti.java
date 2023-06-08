package rs.moma.managers;

import rs.moma.helper.DataTools;
import rs.moma.entities.Klijent;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static rs.moma.helper.DataTools.fileKlijenti;
import static rs.moma.helper.DataTools.toArrayList;

public class Klijenti {
    private final ArrayList<Klijent> klijenti = new ArrayList<>();
    private final String             poruka   = "klijent";

    public Klijenti() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileKlijenti), StandardCharsets.UTF_8);
            for (String line : lines)
                klijenti.add(new Klijent(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja klijenata!");
        }
    }

    // CRUD
    public ArrayList<Klijent> get() {
        return klijenti;
    }

    public Klijent get(int id) {
        Optional<Klijent> klijent = klijenti.stream().filter(tmp -> tmp.ID == id).findFirst();
        if (klijent.isPresent()) return klijent.get();
        System.err.println("Traženi klijent ne postoji!");
        return null;
    }

    public boolean add(Klijent klijent) {
        return DataTools.add(klijenti, fileKlijenti, poruka, klijent);
    }

    public boolean remove(Klijent klijent) {
        return DataTools.remove(klijenti, fileKlijenti, poruka, klijent) && new ZakazaniTretmani().removeKlijent(klijent);
    }

    public boolean edit(Klijent oldKlijent, Klijent newKlijent) {
        return DataTools.edit(klijenti, fileKlijenti, poruka, oldKlijent, newKlijent);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id) {
        for (Klijent klijent : klijenti)
            if (klijent.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        int i = 0;
        while (isTakenID(i)) i++;
        return i;
    }

    // Jedinstvenost
    public boolean isUsernameTaken(String username) {
        return klijenti.stream().anyMatch(klijent -> klijent.Username.equalsIgnoreCase(username)) ||
               new Zaposleni().get().stream().anyMatch(zaposlen -> zaposlen.Username.equalsIgnoreCase(username));
    }

    // Specijalne get metode
    public ArrayList<Klijent> getLojalne() {
        return toArrayList(klijenti.stream().filter(Klijent::getKarticaLojalnosti));
    }

    // Prijava
    public Klijent prijava(String username, String password) {
        return klijenti.stream().filter(klijent -> klijent.Username.equalsIgnoreCase(username) && klijent.Lozinka.equals(password)).findFirst().orElse(null);
    }
}
