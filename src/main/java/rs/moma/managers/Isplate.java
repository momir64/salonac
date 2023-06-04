package rs.moma.managers;

import rs.moma.entities.Isplata;
import rs.moma.helper.DataTools;
import rs.moma.helper.NazivVrednostVreme;
import rs.moma.helper.RadnikPlata;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static rs.moma.helper.DataTools.fileIsplate;
import static rs.moma.helper.DataTools.toArrayList;

public class Isplate {
    private final String poruka = "isplata";

    // CRUD
    public ArrayList<Isplata> get() {
        ArrayList<Isplata> isplate = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileIsplate), StandardCharsets.UTF_8);
            for (String line : lines)
                isplate.add(new Isplata(line));
        } catch (Exception e) {
            System.err.println("Desila se greška prilikom čitanja isplata!");
        }
        return isplate;
    }

    public void add(Isplata isplata) {
        DataTools.add(get(), fileIsplate, poruka, isplata);
    }

    public void remove(Isplata isplata) {
        DataTools.remove(get(), fileIsplate, poruka, isplata);
    }

    public void edit(Isplata oldIsplata, Isplata newIsplata) {
        DataTools.edit(get(), fileIsplate, poruka, oldIsplata, newIsplata);
    }

    // Specijalne get metode
    public ArrayList<NazivVrednostVreme> getRashodi(LocalDateTime from, LocalDateTime to) {
        ArrayList<Isplata> isplate = toArrayList(get().stream().filter(isplata -> (from == null || isplata.Mesec.isAfter(from) || isplata.Mesec.isEqual(from))
                                                                                  && (to == null || isplata.Mesec.isBefore(to) || isplata.Mesec.isEqual(to))));
        ArrayList<NazivVrednostVreme> rashodi = new ArrayList<>();
        for (Isplata isplata : isplate)
            for (RadnikPlata radnikPlata : isplata.Plate)
                rashodi.add(new NazivVrednostVreme("Isplata " + new Zaposleni().get(radnikPlata.RadnikID).getDisplayName(), -radnikPlata.Plata, isplata.Mesec));
        return rashodi;
    }

    public double getRashodiVrednost(LocalDateTime from, LocalDateTime to) {
        return getRashodi(from, to).stream().mapToDouble(nvv -> -nvv.Vrednost).sum();
    }

    // Isplate
    public ArrayList<RadnikPlata> generatePlate() {
        return toArrayList(new Zaposleni().get().stream().map(zaposleni -> new RadnikPlata(zaposleni.ID, zaposleni.getPlata())));
    }

    public void isplati() {
        Optional<Isplata> poslednjaIsplata = get().stream().max(Isplata::compareTo);
        LocalDateTime     trenutniMesec    = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        if (!poslednjaIsplata.isPresent())
            add(new Isplata(trenutniMesec, generatePlate()));
        else
            for (LocalDateTime mesec = poslednjaIsplata.get().Mesec.plusMonths(1); mesec.isBefore(trenutniMesec) || mesec.isEqual(trenutniMesec); mesec = mesec.plusMonths(1))
                 add(new Isplata(mesec, generatePlate()));
    }
}
