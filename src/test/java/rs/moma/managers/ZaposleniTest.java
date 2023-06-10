package rs.moma.managers;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import rs.moma.entities.Tretman;
import rs.moma.entities.Zaposlen;
import rs.moma.helper.DataTools;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static rs.moma.helper.DataTools.EPol.*;
import static rs.moma.helper.DataTools.ENivoSpreme.*;
import static rs.moma.helper.DataTools.ETipZaposlenog.*;

public class ZaposleniTest {
    public static Zaposleni zaposleni;

    public static boolean areSame(Zaposlen zaposlen, int id, String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username,
                                  String lozinka, boolean aktivan, DataTools.ETipZaposlenog tipZaposlenog, DataTools.ENivoSpreme sprema, float koeficijentSprema,
                                  int godineStaza, float koeficijentStaz, float plataOsnova, String bonus, int[] zaduzeniTipoviTretmana) {
        return zaposlen.ID == id &&
               zaposlen.Ime.equals(ime) &&
               zaposlen.Prezime.equals(prezime) &&
               zaposlen.Pol == pol &&
               zaposlen.Telefon.equals(telefon) &&
               zaposlen.Adresa.equals(adresa) &&
               zaposlen.Username.equals(username) &&
               zaposlen.Lozinka.equals(lozinka) &&
               zaposlen.Aktivan == aktivan &&
               zaposlen.TipZaposlenog == tipZaposlenog &&
               zaposlen.Sprema == sprema &&
               zaposlen.KoeficijentSprema == koeficijentSprema &&
               zaposlen.GodineStaza == godineStaza &&
               zaposlen.KoeficijentStaz == koeficijentStaz &&
               zaposlen.PlataOsnova == plataOsnova &&
               zaposlen.Bonus.equals(bonus) &&
               Arrays.equals(zaposlen.ZaduzeniTipoviTretmana, zaduzeniTipoviTretmana);
    }

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        zaposleni = new Zaposleni();

        zaposleni.add(new Zaposlen(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2}));
        zaposleni.add(new Zaposlen(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true, RECEPCIONER, AKADEMSKA, 0, 20, 0, 20000, "", new int[]{-1}));
        zaposleni.add(new Zaposlen(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true, KOZMETICAR, OPSTA, 0, 30, 0, 30000, "", new int[]{2}));
        zaposleni.add(new Zaposlen(9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false, MENADZER, STRUKOVNA, 0, 40, 0, 40000, "", new int[]{-1}));
        zaposleni.add(new Zaposlen(5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true, KOZMETICAR, STRUCNA, 0, 50, 0, 50000, "", new int[]{0, 1}));
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<Zaposlen> zp = zaposleni.get();
        assertTrue(areSame(zp.get(0), 0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2}));
        assertTrue(areSame(zp.get(1), 1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true, RECEPCIONER, AKADEMSKA, 0, 20, 0, 20000, "", new int[]{-1}));
        assertTrue(areSame(zp.get(2), 3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true, KOZMETICAR, OPSTA, 0, 30, 0, 30000, "", new int[]{2}));
        assertTrue(areSame(zp.get(3), 9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false, MENADZER, STRUKOVNA, 0, 40, 0, 40000, "", new int[]{-1}));
        assertTrue(areSame(zp.get(4), 5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true, KOZMETICAR, STRUCNA, 0, 50, 0, 50000, "", new int[]{0, 1}));
        assertEquals(5, zp.size());
    }

    @Test
    public void testAddExisting() {
        assertFalse(zaposleni.add(new Zaposlen(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true, RECEPCIONER, AKADEMSKA, 0, 20, 0, 20000, "", new int[]{-1})));
        assertEquals(5, zaposleni.get().size());
        assertFalse(zaposleni.add(new Zaposlen(999, "dfs", "sfd", MALE, "11111", "sad", "username2", "asd", false, MENADZER, STRUKOVNA, 0, 40, 0, 40000, "", new int[]{-1})));
        assertEquals(5, zaposleni.get().size());
    }

    @Test
    public void testAddNew() {
        assertTrue(zaposleni.add(new Zaposlen(6, "Ime6", "Prezime6", FEMALE, "666666", "aresa6", "username6", "lozinka6", true, RECEPCIONER, AKADEMSKA, 0, 60, 0, 60000, "", new int[]{6, 7, 8})));
        ArrayList<Zaposlen> zp = zaposleni.get();
        assertTrue(areSame(zp.get(5), 6, "Ime6", "Prezime6", FEMALE, "666666", "aresa6", "username6", "lozinka6", true, RECEPCIONER, AKADEMSKA, 0, 60, 0, 60000, "", new int[]{6, 7, 8}));
        assertEquals(6, zp.size());
    }

    @Test
    public void testGetByID() {
        Zaposlen klijent = zaposleni.get(9);
        assertTrue(areSame(klijent, 9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false, MENADZER, STRUKOVNA, 0, 40, 0, 40000, "", new int[]{-1}));
    }

    @Test
    public void testRemove() {
        assertTrue(zaposleni.remove(new Zaposlen(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", false, MENADZER, STRUKOVNA, 0, 40, 0, 40000, "", new int[]{-1})));
        assertTrue(zaposleni.get().stream().noneMatch(tretman -> tretman.ID == 3));
        assertEquals(4, zaposleni.get().size());
    }

    @Test
    public void testEdit() {
        assertTrue(zaposleni.edit(new Zaposlen(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true, KOZMETICAR, OPSTA, 0, 30, 0, 30000, "", new int[]{2}),
                                  new Zaposlen(8, "Ime8", "Prezime8", MALE, "88888", "aresa8", "username8", "lozinka8", true, KOZMETICAR, STRUCNA, 0, 80, 0, 80000, "", new int[]{2, 4, 6})));
        assertTrue(zaposleni.get().stream().anyMatch(klijent -> klijent.ID == 3 && areSame(klijent, 3, "Ime8", "Prezime8", MALE, "88888", "aresa8", "username8", "lozinka8", true,
                                                                                           KOZMETICAR, STRUCNA, 0, 80, 0, 80000, "", new int[]{2, 4, 6})));
        assertEquals(5, zaposleni.get().size());
    }

    @Test
    public void testGetNewID() {
        assertEquals(2, zaposleni.getNewID());
    }

    @Test
    public void testGetRadi() {
        ArrayList<Zaposlen> zp = zaposleni.getRadi(new Tretman(0, 0, "tretman1", 111.1f, 11));
        assertTrue(areSame(zp.get(0), 0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2}));
        assertTrue(areSame(zp.get(1), 5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true, KOZMETICAR, STRUCNA, 0, 50, 0, 50000, "", new int[]{0, 1}));
        assertEquals(2, zp.size());
    }

    @Test
    public void testGetKozmeticari() {
        ArrayList<Zaposlen> zp = zaposleni.getKozmeticari();
        assertTrue(areSame(zp.get(0), 0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true, KOZMETICAR, STRUCNA, 0, 10, 0, 10000, "", new int[]{0, 1, 2}));
        assertTrue(areSame(zp.get(1), 3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true, KOZMETICAR, OPSTA, 0, 30, 0, 30000, "", new int[]{2}));
        assertTrue(areSame(zp.get(2), 5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true, KOZMETICAR, STRUCNA, 0, 50, 0, 50000, "", new int[]{0, 1}));
        assertEquals(3, zp.size());
    }

    @Test
    public void testPrijava() {
        assertEquals(zaposleni.get(5), zaposleni.prijava("username5", "lozinka5"));
        assertNull(zaposleni.prijava("username4", "lozinka4"));
        assertNull(zaposleni.prijava("username7", "lozinka7"));
    }
}
