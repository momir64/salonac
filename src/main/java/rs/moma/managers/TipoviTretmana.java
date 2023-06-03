package rs.moma.managers;

import rs.moma.entities.TipTretmana;
import rs.moma.helper.DataTools;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

import static rs.moma.helper.DataTools.fileTipoviTretmana;

public class TipoviTretmana {
    private final String poruka = "tip tretmana";

    // CRUD
    public ArrayList<TipTretmana> get() {
        ArrayList<TipTretmana> tipoviTretmana = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileTipoviTretmana), StandardCharsets.UTF_8);
            for (String line : lines)
                tipoviTretmana.add(new TipTretmana(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja tipova tretmana!");
        }
        return tipoviTretmana;
    }

    public TipTretmana get(int id) {
        Optional<TipTretmana> tipTretmana = get().stream().filter(tmp -> tmp.ID == id).findFirst();
        if (tipTretmana.isPresent()) return tipTretmana.get();
        System.err.println("Traženi tip tretmana ne postoji!");
        return null;
    }

    public void add(TipTretmana tipTretmana) {
        DataTools.add(get(), fileTipoviTretmana, poruka, tipTretmana);
    }

    public void remove(TipTretmana tipTretmana) {
        DataTools.remove(get(), fileTipoviTretmana, poruka, tipTretmana);
    }

    public void edit(TipTretmana oldtipTretmana, TipTretmana newtipTretmana) {
        DataTools.edit(get(), fileTipoviTretmana, poruka, oldtipTretmana, newtipTretmana);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<TipTretmana> tipoviTretmana) {
        for (TipTretmana tipTretmana : tipoviTretmana)
            if (tipTretmana.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        ArrayList<TipTretmana> tipoviTretmana = get();
        int                i        = 0;
        while (isTakenID(i, tipoviTretmana)) i++;
        return i;
    }
}
