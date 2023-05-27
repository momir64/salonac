package rs.moma.managers;

import rs.moma.DataTools;
import rs.moma.entities.Klijent;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static rs.moma.DataTools.fileKlijenti;
import static rs.moma.DataTools.toArrayList;

public class Klijenti {
    private final String poruka = "klijent";

    // CRUD
    public ArrayList<Klijent> get() {
        ArrayList<Klijent> klijenti = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileKlijenti), StandardCharsets.UTF_8);
            for (String line : lines)
                klijenti.add(new Klijent(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja klijenata!");
        }
        return klijenti;
    }

    public Klijent get(int id) {
        Optional<Klijent> klijent = get().stream().filter(tmp -> tmp.ID == id).findFirst();
        if (klijent.isPresent()) return klijent.get();
        System.err.println("Traženi klijent ne postoji!");
        return null;
    }

    public void add(Klijent klijent) {
        DataTools.add(get(), fileKlijenti, poruka, klijent);
    }

    public void remove(Klijent klijent) {
        DataTools.remove(get(), fileKlijenti, poruka, klijent);
    }

    public void edit(Klijent oldKlijent, Klijent newKlijent) {
        DataTools.edit(get(), fileKlijenti, poruka, oldKlijent, newKlijent);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<Klijent> klijenti) {
        for (Klijent klijent : klijenti)
            if (klijent.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        ArrayList<Klijent> klijenti = get();
        int                i        = 0;
        while (isTakenID(i, klijenti)) i++;
        return i;
    }

    // Jedinstvenost
    public boolean isUsernameFree(String username) {
        return get().stream().noneMatch(klijent -> klijent.Username.equalsIgnoreCase(username)) &&
               new Zaposleni().get().stream().noneMatch(zaposlen -> zaposlen.Username.equalsIgnoreCase(username));
    }

    // Specijalne get metode
    public ArrayList<Klijent> getLojalne(float minIznosLojalnosti) {
        return toArrayList(get().stream().filter(klijent -> klijent.getKarticaLojalnosti(minIznosLojalnosti)));
    }

    // Prijava
    public Klijent prijava(String username, String password) {
        return get().stream().filter(klijent -> klijent.Username.equalsIgnoreCase(username) && klijent.Lozinka.equals(password)).findFirst().orElse(null);
    }
}
