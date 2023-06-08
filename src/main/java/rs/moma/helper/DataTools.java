package rs.moma.helper;

import rs.moma.gui.helper.NameValue;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataTools {
    public static final String fileZakazaniTretmani = "data/zakazani_tretmani.csv";
    public static final String fileTipoviTretmana   = "data/tipovi_tretmana.csv";
    public static final String fileZaposleni        = "data/zaposleni.csv";
    public static final String fileTretmani         = "data/tretmani.csv";
    public static final String fileKlijenti         = "data/klijenti.csv";
    public static final String fileIsplate          = "data/isplate.csv";
    public static final String fileSalon            = "data/salon.csv";
    public static final String SP1                  = ";";
    public static final String SP2                  = "║";
    public static final String SP3                  = "┼";
    public static final String SP4                  = "─";

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

    public static String getPolName(EPol pol) {
        if (pol == EPol.MALE) return "Muško";
        if (pol == EPol.FEMALE) return "Žensko";
        return "Ostalo";
    }

    public static String getTipZaposlenogName(ETipZaposlenog tip) {
        if (tip == ETipZaposlenog.RECEPCIONER) return "Recepcioner";
        if (tip == ETipZaposlenog.KOZMETICAR) return "Kozmetičar";
        return "Menadžer";
    }

    public static String getSpremaName(ENivoSpreme sprema) {
        if (sprema == ENivoSpreme.AKADEMSKA) return "Akademska";
        if (sprema == ENivoSpreme.STRUKOVNA) return "Strukovna";
        if (sprema == ENivoSpreme.STRUCNA) return "Stručna";
        return "Opšta";
    }

    public static String getStanjeName(EStanjeTermina stanje) {
        if (stanje == EStanjeTermina.IZVRSEN) return "Izvršen";
        if (stanje == EStanjeTermina.ZAKAZAN) return "Zakazan";
        if (stanje == EStanjeTermina.OTKAZAO_SALON) return "Otkazao salon";
        if (stanje == EStanjeTermina.OTKAZAO_KLIJENT) return "Otkazao klijent";
        return "Nije se pojavio";
    }

    public static <T> ArrayList<T> toArrayList(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> ArrayList<T> toArrayList(T[] array) {
        return new ArrayList<>(Arrays.asList(array));
    }

    public static <T> boolean save(ArrayList<T> entiteti, String fileName, String tip) {
        try {
            StringBuilder data = new StringBuilder();
            for (T entiet : entiteti)
                data.append(entiet).append('\n');
            Files.write(Paths.get(fileName), data.toString().getBytes());
            return true;
        } catch (Exception e) {
            System.err.printf("Desila se greška dok se čuvao dati %s!\n", tip);
            return false;
        }
    }

    public static <T> boolean add(ArrayList<T> entiteti, String fileName, String tip, T entitet) {
        if (entiteti.stream().anyMatch(obj -> obj.equals(entitet))) {
            System.err.printf("Dati %s već postoji!\n", tip);
            return false;
        } else {
            entiteti.add(entitet);
            return save(entiteti, fileName, tip);
        }
    }

    public static <T> boolean remove(ArrayList<T> entiteti, String fileName, String tip, T entitet) {
        if (entiteti.stream().noneMatch(obj -> obj.equals(entitet))) {
            System.err.printf("Dati %s ne postoji!\n", tip);
            return false;
        } else {
            entiteti.removeIf(obj -> obj.equals(entitet));
            return save(entiteti, fileName, tip);
        }
    }

    public static <T extends ClassWithID> boolean edit(ArrayList<T> entiteti, String fileName, String tip, T oldEntitet, T newEntitet) {
        if (entiteti.stream().noneMatch(obj -> obj.equals(oldEntitet))) {
            System.err.printf("Dati %s ne postoji!\n", tip);
            return false;
        } else {
            newEntitet.setID(oldEntitet.getID());
            entiteti.removeIf(obj -> obj.equals(oldEntitet));
            entiteti.add(newEntitet);
            return save(entiteti, fileName, tip);
        }
    }

    public static boolean isInputValid(JComponent... inputs) {
        for (JComponent input : inputs)
            if (input instanceof JTextField && (((JTextField) input).getText().trim().equals("")
                                                || ((JTextField) input).getText().contains(SP1)
                                                || ((JTextField) input).getText().contains(SP2)
                                                || ((JTextField) input).getText().contains(SP3)
                                                || ((JTextField) input).getText().contains(SP4))) return false;
            else if (input instanceof JComboBox && ((JComboBox<?>) input).getSelectedItem() == null) return false;
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
        return txt.getText().trim().equals("") ? -1 : txt.getText().trim().equals(".") ? 0 : Integer.parseInt(txt.getText());
    }

    public static float txtToFloat(JTextField txt) {
        return txt.getText().trim().equals("") ? -1 : txt.getText().trim().equals(".") ? 0 : Float.parseFloat(txt.getText());
    }

    public static LocalDate getDatum(JTextField datumTxt) {
        return strToDate(datumTxt.getText());
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

    public static int getTime(JTextField txt) {
        return Integer.parseInt(txt.getText().replace("h", ""));
    }

    public static LocalDateTime getTermin(JTextField datumTxt, JComboBox<NameValue> vremeBox) {
        return getDatum(datumTxt).atTime((Integer) getSelectedValue(vremeBox), 0);
    }

    public static LocalDateTime getTermin(JTextField datumTxt, JTextField vremeBox) {
        return getDatum(datumTxt).atTime(getTime(vremeBox), 0);
    }

    public static Object getSelectedValue(JComboBox<NameValue> box) {
        return ((NameValue) box.getSelectedItem()).getValue();
    }

    public static String simplifyFloatToString(float d) {
        if (d == (int) d) return String.format("%d", (long) d);
        else return String.format("%s", d);
    }

    public static void fillPolBox(JComboBox<NameValue> polBox) {
        polBox.setModel(new DefaultComboBoxModel<>(new NameValue[]{new NameValue("Muško", EPol.MALE), new NameValue("Žensko", EPol.FEMALE), new NameValue("Ostalo", EPol.OTHER)}));
    }

    public static String getMonthName(int month) {
        String[] monthNames = {"Januar", "Februar", "Mart", "April", "Maj", "Jun", "Jul", "Avgust", "Septembar", "Oktobar", "Novembar", "Decembar"};
        return new String(monthNames[((month % 12) + 12) % 12]);
    }
}
