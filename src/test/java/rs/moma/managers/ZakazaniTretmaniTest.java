package rs.moma.managers;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import rs.moma.entities.*;
import rs.moma.helper.DataTools.*;
import rs.moma.helper.NazivVrednostVreme;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static rs.moma.helper.DataTools.EPol.*;
import static rs.moma.helper.DataTools.ENivoSpreme.*;
import static rs.moma.helper.DataTools.EStanjeTermina.*;
import static rs.moma.helper.DataTools.ETipZaposlenog.KOZMETICAR;

public class ZakazaniTretmaniTest {
    public static ZakazaniTretmani zakazaniTretmani;
    public static TipoviTretmana   tipoviTretmana;
    public static Zaposleni        zaposleni;
    public static Klijenti         klijenti;
    public static Tretmani         tretmani;

    public static boolean areSame(NazivVrednostVreme isplata, String naziv, float vrednost, LocalDateTime vreme) {
        return isplata.Naziv.equals(naziv) && Math.abs(isplata.Vrednost - vrednost) < 0.01 && isplata.Vreme.isEqual(vreme);
    }

    public static boolean areSame(ZakazaniTretman tretman, int id, int tretmanID, float cena, int trajanje, LocalDateTime vreme, EStanjeTermina stanje, int klijentID, int kozmeticarID, boolean karticaLojalnosti) {
        return tretman.ID == id &&
               tretman.TretmanID == tretmanID &&
               tretman.Trajanje == trajanje &&
               tretman.Vreme.isEqual(vreme) &&
               tretman.Stanje == stanje &&
               tretman.KlijentID == klijentID &&
               tretman.KozmeticarID == kozmeticarID &&
               tretman.KarticaLojalnosti == karticaLojalnosti &&
               tretman.Cena == cena;
    }

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        zakazaniTretmani = new ZakazaniTretmani();
        tipoviTretmana   = new TipoviTretmana();
        zaposleni        = new Zaposleni();
        klijenti         = new Klijenti();
        tretmani         = new Tretmani();

        tipoviTretmana.add(new TipTretmana(0, "tip1"));
        tipoviTretmana.add(new TipTretmana(1, "tip2"));

        tretmani.add(new Tretman(0, 1, "tretman1", 111.1f, 11));
        tretmani.add(new Tretman(1, 0, "tretman2", 222.2f, 22));
        tretmani.add(new Tretman(2, 1, "tretman3", 333.3f, 33));

        zaposleni.add(new Zaposlen(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2}));
        zaposleni.add(new Zaposlen(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true, KOZMETICAR, STRUKOVNA, 0, 20, 0, 20000, "", new int[]{0, 1}));

        klijenti.add(new Klijent(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true));
        klijenti.add(new Klijent(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true));

        zakazaniTretmani.add(new ZakazaniTretman(0, 0, 111.1f, 11, LocalDate.now().atTime(11, 0), ZAKAZAN, 0, 0, false));
        zakazaniTretmani.add(new ZakazaniTretman(1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        zakazaniTretmani.add(new ZakazaniTretman(3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        zakazaniTretmani.add(new ZakazaniTretman(9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        zakazaniTretmani.add(new ZakazaniTretman(5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(0), 0, 0, 111.1f, 11, LocalDate.now().atTime(11, 0), ZAKAZAN, 0, 0, false));
        assertTrue(areSame(zt.get(1), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        assertTrue(areSame(zt.get(2), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(3), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertTrue(areSame(zt.get(4), 5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertEquals(zt.size(), 5);
    }

    @Test
    public void testAddExisting() {
        assertFalse(zakazaniTretmani.add(new ZakazaniTretman(5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false)));
        assertEquals(zakazaniTretmani.get().size(), 5);
    }

    @Test
    public void testAddNew() {
        assertTrue(zakazaniTretmani.add(new ZakazaniTretman(2, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false)));
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(5), 2, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertEquals(zt.size(), 6);
    }

    @Test
    public void testGetByID() {
        ZakazaniTretman tretman = zakazaniTretmani.get(3);
        assertTrue(areSame(tretman, 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
    }

    @Test
    public void testRemove() {
        assertTrue(zakazaniTretmani.remove(new ZakazaniTretman(5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false)));
        assertTrue(zakazaniTretmani.get().stream().noneMatch(tretman -> tretman.ID == 5));
        assertEquals(zakazaniTretmani.get().size(), 4);
    }

    @Test
    public void testEdit() {
        assertTrue(zakazaniTretmani.edit(new ZakazaniTretman(5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false),
                                         new ZakazaniTretman(0, 0, 355.5f, 55, LocalDate.now().atTime(10, 0), OTKAZAO_SALON, 1, 1, true)));
        assertTrue(zakazaniTretmani.get().stream().anyMatch(tretman -> tretman.ID == 5 && areSame(tretman, 5, 0, 355.5f, 55, LocalDate.now().atTime(10, 0), OTKAZAO_SALON, 1, 1, true)));
        assertEquals(zakazaniTretmani.get().size(), 5);
    }

    @Test
    public void testGetNewID() {
        assertEquals(2, zakazaniTretmani.getNewID());
    }

    @Test
    public void testRemoveTretman() {
        assertTrue(zakazaniTretmani.removeTretman(new Tretman(0, 0, "tretman1", 111.1f, 11)));
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(0), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        assertTrue(areSame(zt.get(1), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertTrue(areSame(zt.get(2), 5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertEquals(zt.size(), 3);
    }

    @Test
    public void testRemoveKozmeticar() {
        assertTrue(zakazaniTretmani.removeKlijent(new Klijent(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true)));
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(0), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(1), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertTrue(areSame(zt.get(2), 5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertEquals(zt.size(), 3);
    }

    @Test
    public void testRemoveTip() {
        assertTrue(zakazaniTretmani.removeKozmeticar(new Zaposlen(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2})));
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(0), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        assertTrue(areSame(zt.get(1), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(2), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertEquals(zt.size(), 3);
    }

    @Test
    public void testFilter1() {
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.filter(-1, -1, 150, 300);
        assertTrue(areSame(zt.get(0), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        assertTrue(areSame(zt.get(1), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertEquals(zt.size(), 2);
    }

    @Test
    public void testFilter2() {
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.filter(null, null, LocalDate.now().atTime(12, 0), LocalDate.now().atTime(17, 0), null);
        assertTrue(areSame(zt.get(0), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true));
        assertTrue(areSame(zt.get(1), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(2), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertEquals(zt.size(), 3);
    }

    @Test
    public void testFilter3() {
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.filter(1, null, null);
        assertTrue(areSame(zt.get(0), 0, 0, 111.1f, 11, LocalDate.now().atTime(11, 0), ZAKAZAN, 0, 0, false));
        assertTrue(areSame(zt.get(1), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(2), 5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertEquals(zt.size(), 3);
    }

    @Test
    public void testGetPrihodi() {
        ArrayList<NazivVrednostVreme> nvv = zakazaniTretmani.getPrihodi(LocalDate.now().atTime(12, 0), LocalDate.now().atTime(17, 0));
        assertTrue(areSame(nvv.get(0), "tretman2", 199.98f, LocalDate.now().atTime(12, 0)));
        assertTrue(areSame(nvv.get(1), "tretman1", 333.3f, LocalDate.now().atTime(15, 0)));
        assertTrue(areSame(nvv.get(2), "tretman2", 219.96f, LocalDate.now().atTime(17, 0)));
        assertEquals(3, nvv.size());
    }

    @Test
    public void testGetPrihodiVrednost() {
        assertEquals(753.24f, zakazaniTretmani.getPrihodiVrednost(LocalDate.now().atTime(12, 0), LocalDate.now().atTime(17, 0)), 0.01);
    }

    @Test
    public void testGetNewestDate() {
        assertEquals(LocalDate.now(), zakazaniTretmani.getNewestDate());
    }

    @Test
    public void testGetOldestDate() {
        assertEquals(LocalDate.now(), zakazaniTretmani.getOldestDate());
    }

    @Test
    public void testOtkaziTretman() {
        zakazaniTretmani.otkaziTretman(new ZakazaniTretman(0, 0, 111.1f, 11, LocalDate.now().atTime(11, 0), ZAKAZAN, 0, 0, false), true);
        zakazaniTretmani.otkaziTretman(new ZakazaniTretman(1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), ZAKAZAN, 0, 1, true), false);
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(0), 3, 0, 333.3f, 33, LocalDate.now().atTime(15, 0), ZAKAZAN, 1, 1, false));
        assertTrue(areSame(zt.get(1), 9, 1, 244.4f, 44, LocalDate.now().atTime(17, 0), ZAKAZAN, 1, 1, true));
        assertTrue(areSame(zt.get(2), 5, 2, 555.5f, 55, LocalDate.now().atTime(19, 0), ZAKAZAN, 1, 0, false));
        assertTrue(areSame(zt.get(3), 0, 0, 111.1f, 11, LocalDate.now().atTime(11, 0), OTKAZAO_SALON, 0, 0, false));
        assertTrue(areSame(zt.get(4), 1, 1, 222.2f, 22, LocalDate.now().atTime(12, 0), OTKAZAO_KLIJENT, 0, 1, true));
        assertEquals(zt.size(), 5);
    }

    @Test
    public void testUpdateStanjeTretmana() {
        assertTrue(zakazaniTretmani.add(new ZakazaniTretman(2, 2, 555.5f, 55, LocalDate.now().minusDays(1).atTime(19, 0), ZAKAZAN, 1, 0, false)));
        zakazaniTretmani.updateStanjeTretmana();
        ArrayList<ZakazaniTretman> zt = zakazaniTretmani.get();
        assertTrue(areSame(zt.get(5), 2, 2, 555.5f, 55, LocalDate.now().minusDays(1).atTime(19, 0), IZVRSEN, 1, 0, false));
        assertEquals(zt.size(), 6);
    }
}
