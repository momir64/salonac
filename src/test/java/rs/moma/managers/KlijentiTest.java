package rs.moma.managers;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import rs.moma.entities.*;
import rs.moma.helper.DataTools;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static rs.moma.helper.DataTools.EPol.*;
import static rs.moma.helper.DataTools.EStanjeTermina.ZAKAZAN;

public class KlijentiTest {
    public static Klijenti         klijenti;
    public static ZakazaniTretmani zakazaniTretmani;

    public static boolean areSame(Klijent klijent, int id, String ime, String prezime, DataTools.EPol pol, String telefon, String adresa, String username, String lozinka, boolean aktivan) {
        return klijent.ID == id &&
               klijent.Ime.equals(ime) &&
               klijent.Prezime.equals(prezime) &&
               klijent.Pol == pol &&
               klijent.Telefon.equals(telefon) &&
               klijent.Adresa.equals(adresa) &&
               klijent.Username.equals(username) &&
               klijent.Lozinka.equals(lozinka) &&
               klijent.Aktivan == aktivan;
    }

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        klijenti         = new Klijenti();
        zakazaniTretmani = new ZakazaniTretmani();

        klijenti.add(new Klijent(0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true));
        klijenti.add(new Klijent(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true));
        klijenti.add(new Klijent(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true));
        klijenti.add(new Klijent(9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false));
        klijenti.add(new Klijent(5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true));

        zakazaniTretmani.add(new ZakazaniTretman(0, 3, 244.4f, 44 * 3, LocalDate.now().atTime(11, 11), ZAKAZAN, 1, 3, false));
        zakazaniTretmani.add(new ZakazaniTretman(1, 3, 244.4f, 44 * 3, LocalDate.now().atTime(22, 22), ZAKAZAN, 1, 4, true));
        zakazaniTretmani.add(new ZakazaniTretman(3, 3, 544.4f, 44 * 3, LocalDate.now().atTime(14, 14), ZAKAZAN, 3, 4, false));
        zakazaniTretmani.add(new ZakazaniTretman(5, 3, 144.4f, 44 * 3, LocalDate.now().atTime(17, 17), ZAKAZAN, 5, 4, false));

        new Salon("salon", 0, 23, 300);
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<Klijent> kl = klijenti.get();
        assertTrue(areSame(kl.get(0), 0, "Ime1", "Prezime1", MALE, "00000", "aresa1", "username1", "lozinka1", true));
        assertTrue(areSame(kl.get(1), 1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true));
        assertTrue(areSame(kl.get(2), 3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true));
        assertTrue(areSame(kl.get(3), 9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false));
        assertTrue(areSame(kl.get(4), 5, "Ime5", "Prezime5", OTHER, "44444", "aresa5", "username5", "lozinka5", true));
        assertEquals(5, kl.size());
    }

    @Test
    public void testAddExisting() {
        assertFalse(klijenti.add(new Klijent(1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true)));
        assertEquals(5, klijenti.get().size());
        assertFalse(klijenti.add(new Klijent(999, "dfs", "sfd", MALE, "11111", "sad", "username2", "asd", false)));
        assertEquals(5, klijenti.get().size());
    }

    @Test
    public void testAddNew() {
        assertTrue(klijenti.add(new Klijent(6, "Ime6", "Prezime6", FEMALE, "666666", "aresa6", "username6", "lozinka6", true)));
        ArrayList<Klijent> kl = klijenti.get();
        assertTrue(areSame(kl.get(5), 6, "Ime6", "Prezime6", FEMALE, "666666", "aresa6", "username6", "lozinka6", true));
        assertEquals(6, kl.size());
    }

    @Test
    public void testGetByID() {
        Klijent klijent = klijenti.get(9);
        assertTrue(areSame(klijent, 9, "Ime4", "Prezime4", MALE, "33333", "aresa4", "username4", "lozinka4", false));
    }

    @Test
    public void testRemove() {
        klijenti.remove(new Klijent(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true));
        assertTrue(klijenti.get().stream().noneMatch(tretman -> tretman.ID == 3));
        assertEquals(4, klijenti.get().size());
    }

    @Test
    public void testEdit() {
        klijenti.edit(new Klijent(3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true),
                      new Klijent(8, "Ime8", "Prezime8", MALE, "88888", "aresa8", "username8", "lozinka8", true));
        assertTrue(klijenti.get().stream().anyMatch(klijent -> klijent.ID == 3 && areSame(klijent, 3, "Ime8", "Prezime8", MALE, "88888", "aresa8", "username8", "lozinka8", true)));
        assertEquals(5, klijenti.get().size());
    }

    @Test
    public void testGetNewID() {
        assertEquals(2, klijenti.getNewID());
    }

    @Test
    public void testIsUsernameTaken() {
        assertTrue(klijenti.isUsernameTaken("username2"));
        assertFalse(klijenti.isUsernameTaken("username6"));
    }

    @Test
    public void testGetLojalne() {
        ArrayList<Klijent> kl = klijenti.getLojalne();
        assertTrue(areSame(kl.get(0), 1, "Ime2", "Prezime2", FEMALE, "11111", "aresa2", "username2", "lozinka2", true));
        assertTrue(areSame(kl.get(1), 3, "Ime3", "Prezime3", MALE, "22222", "aresa3", "username3", "lozinka3", true));
        assertEquals(2, kl.size());
    }

    @Test
    public void testPrijava() {
        assertEquals(klijenti.get(5), klijenti.prijava("username5", "lozinka5"));
        assertNull(klijenti.prijava("username4", "lozinka4"));
        assertNull(klijenti.prijava("username7", "lozinka7"));
    }
}
