package rs.moma.managers;

import rs.moma.entities.TipTretmana;
import rs.moma.entities.Tretman;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TretmaniTest {
    public static Tretmani       tretmani;
    public static TipoviTretmana tipoviTretmana;

    public static boolean areSame(Tretman tretman, int id, int tipID, String naziv, float cena, int trajanje) {
        return tretman.ID == id &&
               tretman.TipID == tipID &&
               tretman.Naziv.equals(naziv) &&
               tretman.Cena == cena &&
               tretman.Trajanje == trajanje;
    }

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        tretmani       = new Tretmani();
        tipoviTretmana = new TipoviTretmana();

        tipoviTretmana.add(new TipTretmana(0, "tip1"));
        tipoviTretmana.add(new TipTretmana(1, "tip2"));
        tipoviTretmana.add(new TipTretmana(2, "tip3"));

        tretmani.add(new Tretman(0, 0, "tretman1", 111.1f, 11 * 3));
        tretmani.add(new Tretman(1, 0, "tretman2", 222.2f, 22 * 3));
        tretmani.add(new Tretman(8, 0, "tretman3", 333.3f, 33 * 3));
        tretmani.add(new Tretman(3, 1, "tretman4", 244.4f, 44 * 3));
        tretmani.add(new Tretman(9, 1, "tretman5", 555.5f, 55 * 3));
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<Tretman> tr = tretmani.get();
        assertTrue(areSame(tr.get(0), 0, 0, "tretman1", 111.1f, 11 * 3));
        assertTrue(areSame(tr.get(1), 1, 0, "tretman2", 222.2f, 22 * 3));
        assertTrue(areSame(tr.get(2), 8, 0, "tretman3", 333.3f, 33 * 3));
        assertTrue(areSame(tr.get(3), 3, 1, "tretman4", 244.4f, 44 * 3));
        assertTrue(areSame(tr.get(4), 9, 1, "tretman5", 555.5f, 55 * 3));
        assertEquals(5, tr.size());
    }

    @Test
    public void testAddExisting() {
        assertFalse(tretmani.add(new Tretman(1, 2, "tretman6", 666.6f, 66 * 3)));
        assertEquals(5, tretmani.get().size());
    }

    @Test
    public void testAddNew() {
        assertTrue(tretmani.add(new Tretman(5, 2, "tretman6", 666.6f, 66 * 3)));
        ArrayList<Tretman> tr = tretmani.get();
        assertTrue(areSame(tr.get(5), 5, 2, "tretman6", 666.6f, 66 * 3));
        assertEquals(6, tr.size());
    }

    @Test
    public void testGetByID() {
        Tretman tretman = tretmani.get(8);
        assertTrue(areSame(tretman, 8, 0, "tretman3", 333.3f, 33 * 3));
    }

    @Test
    public void testRemove() {
        tretmani.remove(new Tretman(8, 0, "tretman3", 333.3f, 33 * 3));
        assertTrue(tretmani.get().stream().noneMatch(tretman -> tretman.ID == 8));
        assertEquals(4, tretmani.get().size());
    }

    @Test
    public void testEdit() {
        tretmani.edit(new Tretman(8, 0, "tretman3", 333.3f, 33 * 3), new Tretman(0, 2, "novo", 99, 99 * 3));
        assertTrue(tretmani.get().stream().anyMatch(tretman -> tretman.ID == 8 && areSame(tretman, 8, 2, "novo", 99, 99 * 3)));
        assertEquals(5, tretmani.get().size());
    }

    @Test
    public void testGetNewID() {
        assertEquals(2, tretmani.getNewID());
    }

    @Test
    public void testRemoveTip() {
        tretmani.removeTip(new TipTretmana(1, ""));
        assertEquals(3, tretmani.get().size());
    }

    @Test
    public void testFilter() {
        ArrayList<Tretman> tr = tretmani.filter(0, null, -1, -1, 200, 400);
        assertTrue(areSame(tr.get(0), 1, 0, "tretman2", 222.2f, 22 * 3));
        assertTrue(areSame(tr.get(1), 8, 0, "tretman3", 333.3f, 33 * 3));
        assertEquals(2, tr.size());
    }
}
