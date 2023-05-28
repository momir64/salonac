package rs.moma;

import javax.swing.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class DataTools {
    public static String fileZakazaniTretmani = "data/zakazani_tretmani.csv";
    public static String fileTipoviTretmana   = "data/tipovi_tretmana.csv";
    public static String fileZaposleni        = "data/zaposleni.csv";
    public static String fileTretmani         = "data/tretmani.csv";
    public static String fileKlijenti         = "data/klijenti.csv";
    public static String fileIsplate          = "data/isplate.csv";
    public static String fileSalon            = "data/salon.csv";
    public static String SP1                  = ";";
    public static String SP2                  = "|";

    public enum ETipZaposlenog {
        KOZMETICAR,
        RECEPCIONER,
        MENADZER
    }

    public enum ENivoSpreme {
        OPSTA,
        STRUCNA,
        STRUKOVNA,
        AKADEMSKA
    }

    public enum EStanjeTermina {
        ZAKAZAN,
        IZVRSEN,
        OTKAZAO_KLIJENT,
        OTKAZAO_SALON,
        NIJE_SE_POJAVIO
    }

    public enum EPol {
        MALE,
        FEMALE,
        OTHER
    }

    public static <T> ArrayList<T> toArrayList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> ArrayList<T> toArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    protected static <T> void save(ArrayList<T> entiteti, String fileName, String tip) {
        try {
            StringBuilder data = new StringBuilder();
            for (T entiet : entiteti)
                data.append(entiet).append('\n');
            Files.write(Paths.get(fileName), data.toString().getBytes());
        } catch (Exception e) {
            System.err.printf("Desila se greška dok se čuvao dati %s!\n", tip);
        }
    }

    public static <T> void add(ArrayList<T> entiteti, String fileName, String tip, T entitet) {
        if (entiteti.stream().anyMatch(obj -> obj.equals(entitet)))
            System.err.printf("Dati %s već postoji!\n", tip);
        else {
            entiteti.add(entitet);
            save(entiteti, fileName, tip);
        }
    }

    public static <T> void remove(ArrayList<T> entiteti, String fileName, String tip, T entitet) {
        if (entiteti.stream().noneMatch(obj -> obj.equals(entitet)))
            System.err.printf("Dati %s ne postoji!\n", tip);
        else {
            entiteti.removeIf(obj -> obj.equals(entitet));
            save(entiteti, fileName, tip);
        }
    }

    public static <T> void edit(ArrayList<T> entiteti, String fileName, String tip, T entitet, T newEntitet) {
        if (entiteti.stream().noneMatch(obj -> obj.equals(entitet)))
            System.err.printf("Dati %s ne postoji!\n", tip);
        else {
            entiteti.removeIf(obj -> obj.equals(entitet));
            entiteti.add(newEntitet);
            save(entiteti, fileName, tip);
        }
    }

    public static boolean isInputValid(JTextField... inputs) {
        for (JTextField input : inputs)
            if (input.getText().contains(SP1) || input.getText().contains(SP2))
                return false;
        return true;
    }

    public static boolean isInputFilled(JTextField... inputs) {
        for (JTextField input : inputs)
            if (input.getText().trim().equals(""))
                return false;
        return true;
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
