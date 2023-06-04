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
    private final ArrayList<TipTretmana> tipoviTretmana = new ArrayList<>();
    private final String                 poruka         = "tip tretmana";

    public TipoviTretmana() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileTipoviTretmana), StandardCharsets.UTF_8);
            for (String line : lines)
                tipoviTretmana.add(new TipTretmana(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja tipova tretmana!");
        }
    }

    // CRUD
    public ArrayList<TipTretmana> get() {
        return tipoviTretmana;
    }

    public TipTretmana get(int id) {
        Optional<TipTretmana> tipTretmana = tipoviTretmana.stream().filter(tmp -> tmp.ID == id).findFirst();
        if (tipTretmana.isPresent()) return tipTretmana.get();
        System.err.println("Traženi tip tretmana ne postoji!");
        return null;
    }

    public boolean add(TipTretmana tipTretmana) {
        return DataTools.add(tipoviTretmana, fileTipoviTretmana, poruka, tipTretmana);
    }

    public boolean remove(TipTretmana tipTretmana) {
        return DataTools.remove(tipoviTretmana, fileTipoviTretmana, poruka, tipTretmana) && new Tretmani().removeTip(tipTretmana);
    }

    public boolean edit(TipTretmana oldtipTretmana, TipTretmana newtipTretmana) {
        return DataTools.edit(tipoviTretmana, fileTipoviTretmana, poruka, oldtipTretmana, newtipTretmana);
    }

    // Pravljenje ključeva
    private boolean isTakenID(int id, ArrayList<TipTretmana> tipoviTretmana) {
        for (TipTretmana tipTretmana : tipoviTretmana)
            if (tipTretmana.ID == id)
                return true;
        return false;
    }

    public int getNewID() {
        int i = 0;
        while (isTakenID(i, tipoviTretmana)) i++;
        return i;
    }
}
