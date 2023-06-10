package rs.moma.managers;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import rs.moma.entities.Isplata;
import rs.moma.entities.Zaposlen;
import rs.moma.helper.NazivVrednostVreme;
import rs.moma.entities.helper.RadnikPlata;

import java.util.stream.IntStream;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static rs.moma.helper.DataTools.ENivoSpreme.AKADEMSKA;
import static rs.moma.helper.DataTools.ENivoSpreme.STRUCNA;
import static rs.moma.helper.DataTools.EPol.FEMALE;
import static rs.moma.helper.DataTools.EPol.MALE;
import static rs.moma.helper.DataTools.ETipZaposlenog.KOZMETICAR;
import static rs.moma.helper.DataTools.ETipZaposlenog.RECEPCIONER;

public class IsplateTest {
    public static Isplate   isplate;
    public static Zaposleni zaposleni;

    public static boolean areSame(RadnikPlata rp, int radnikID, float plata) {
        return rp.RadnikID == radnikID && rp.Plata == plata;
    }

    public static boolean areSame(RadnikPlata rp1, RadnikPlata rp2) {
        return rp1.RadnikID == rp2.RadnikID && rp1.Plata == rp2.Plata;
    }

    public static boolean areSame(Isplata isplata, LocalDateTime month, ArrayList<RadnikPlata> plate) {
        return isplata.Mesec.isEqual(month) && isplata.Plate.size() == plate.size() &&
               IntStream.range(0, plate.size()).allMatch(i -> areSame(isplata.Plate.get(i), plate.get(i)));
    }

    public static boolean areSame(NazivVrednostVreme isplata, String naziv, float vrednost, LocalDateTime vreme) {
        return isplata.Naziv.equals(naziv) && Math.abs(isplata.Vrednost - vrednost) < 0.01 && isplata.Vreme.isEqual(vreme);
    }

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        isplate   = new Isplate();
        zaposleni = new Zaposleni();

        zaposleni.add(new Zaposlen(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 10, 10, 100, 10000, "", new int[]{0, 1, 2}));
        zaposleni.add(new Zaposlen(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true, RECEPCIONER, AKADEMSKA, 20, 20, 70, 20000, "", new int[]{-1}));

        isplate.add(new Isplata(LocalDate.now().minusMonths(0).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2200)))));
        isplate.add(new Isplata(LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1200), new RadnikPlata(1, 2300)))));
        isplate.add(new Isplata(LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2400)))));
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<Isplata> is = isplate.get();
        assertTrue(areSame(is.get(0), LocalDate.now().minusMonths(0).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2200)))));
        assertTrue(areSame(is.get(1), LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1200), new RadnikPlata(1, 2300)))));
        assertTrue(areSame(is.get(2), LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2400)))));
        assertEquals(3, is.size());
    }

    @Test
    public void testAdd() {
        isplate.add(new Isplata(LocalDate.now().minusMonths(3).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1300), new RadnikPlata(1, 2500)))));
        ArrayList<Isplata> is = isplate.get();
        assertTrue(areSame(is.get(3), LocalDate.now().minusMonths(3).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1300), new RadnikPlata(1, 2500)))));
        assertEquals(4, isplate.get().size());
    }

    @Test
    public void testRemove() {
        assertTrue(isplate.remove(new Isplata(LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1300), new RadnikPlata(1, 2500))))));
        assertTrue(isplate.get().stream().noneMatch(isplata -> isplata.Mesec.isEqual(LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0))));
        assertEquals(2, isplate.get().size());
    }

    @Test
    public void testEdit() {
        assertTrue(isplate.edit(new Isplata(LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1300), new RadnikPlata(1, 2500)))),
                                new Isplata(LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 2300), new RadnikPlata(1, 2300))))));
        ArrayList<Isplata> is = isplate.get();
        assertTrue(areSame(is.get(2), LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 2300), new RadnikPlata(1, 2300)))));
        assertEquals(3, isplate.get().size());
    }

    @Test
    public void testGetRashodi() {
        ArrayList<NazivVrednostVreme> nvv = isplate.getRashodi(LocalDate.now().withDayOfMonth(1).minusMonths(1).atTime(0, 0), LocalDateTime.now());
        assertTrue(areSame(nvv.get(0), "Isplata Ime1 Prezime1", -1000, LocalDate.now().withDayOfMonth(1).atTime(0, 0)));
        assertTrue(areSame(nvv.get(1), "Isplata Ime2 Prezime2", -2200, LocalDate.now().withDayOfMonth(1).atTime(0, 0)));
        assertTrue(areSame(nvv.get(2), "Isplata Ime1 Prezime1", -1200, LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0)));
        assertTrue(areSame(nvv.get(3), "Isplata Ime2 Prezime2", -2300, LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0)));
        assertEquals(4, nvv.size());
    }

    @Test
    public void testGetRashodiVrednost() {
        assertEquals(6700, isplate.getRashodiVrednost(LocalDate.now().withDayOfMonth(1).minusMonths(1).atTime(0, 0), LocalDateTime.now()), 0.01);
    }

    @Test
    public void testGeneratePlate() {
        ArrayList<RadnikPlata> rp = isplate.generatePlate();
        assertTrue(areSame(rp.get(0), 0, 11020));
        assertTrue(areSame(rp.get(1), 1, 21480));
        assertEquals(2, rp.size());
    }

    @Test
    public void testIsplatiNone() {
        isplate.isplati();
        ArrayList<Isplata> is = isplate.get();
        assertTrue(areSame(is.get(0), LocalDate.now().minusMonths(0).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2200)))));
        assertTrue(areSame(is.get(1), LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1200), new RadnikPlata(1, 2300)))));
        assertTrue(areSame(is.get(2), LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2400)))));
        assertEquals(3, is.size());
    }

    @Test
    public void testIsplatiNew() {
        isplate.remove(new Isplata(LocalDate.now().minusMonths(0).withDayOfMonth(1).atTime(0, 0), new ArrayList<>()));
        isplate.remove(new Isplata(LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0), new ArrayList<>()));
        isplate.isplati();
        ArrayList<Isplata> is = isplate.get();
        assertTrue(areSame(is.get(0), LocalDate.now().minusMonths(2).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 1000), new RadnikPlata(1, 2400)))));
        assertTrue(areSame(is.get(1), LocalDate.now().minusMonths(1).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 11020), new RadnikPlata(1, 21480)))));
        assertTrue(areSame(is.get(2), LocalDate.now().minusMonths(0).withDayOfMonth(1).atTime(0, 0), new ArrayList<>(Arrays.asList(new RadnikPlata(0, 11020), new RadnikPlata(1, 21480)))));
        assertEquals(3, is.size());
    }
}