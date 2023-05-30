package rs.moma;

import rs.moma.entities.ClassWithID;
import rs.moma.gui.ComboKeyValue;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static <T extends ClassWithID> void edit(ArrayList<T> entiteti, String fileName, String tip, T oldEntitet, T newEntitet) {
        if (entiteti.stream().noneMatch(obj -> obj.equals(oldEntitet)))
            System.err.printf("Dati %s ne postoji!\n", tip);
        else {
            newEntitet.setID(oldEntitet.getID());
            entiteti.removeIf(obj -> obj.equals(oldEntitet));
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

    public static void addOnChangeDo(JTextField txt, Runnable what) {
        txt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                what.run();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                what.run();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                what.run();
            }
        });
    }

    public static int txtToInt(JTextField txt) {
        return txt.getText().trim().equals("") ? -1 : Integer.parseInt(txt.getText());
    }

    public static LocalDate strToDate(String datum) {
        String[] data = datum.split("\\.");
        if (datum.contains("_") || datum.trim().equals("") || data.length < 3) return null;
        try {
            return LocalDate.of(Integer.parseInt(data[2]), Integer.parseInt(data[1]), Integer.parseInt(data[0]));
        } catch (Exception e) {
            return null;
        }
    }

    public static Object getSelectedValue(JComboBox<ComboKeyValue> box) {
        return ((ComboKeyValue) box.getSelectedItem()).getValue();
    }
}
