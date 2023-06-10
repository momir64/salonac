package rs.moma.managers;

import rs.moma.entities.TipTretmana;
import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TipoviTretmanaTest {
    public static TipoviTretmana tipoviTretmana;

    @Before
    public void setUp() throws Exception {
        TestHelper.ClearDatabase();
        tipoviTretmana = new TipoviTretmana();
        tipoviTretmana.add(new TipTretmana(0, "tip1"));
        tipoviTretmana.add(new TipTretmana(1, "tip2"));
        tipoviTretmana.add(new TipTretmana(2, "tip3"));
    }

    @After
    public void cleanUp() throws Exception {
        TestHelper.ClearDatabase();
    }

    @Test
    public void testRead() {
        ArrayList<TipTretmana> tipovi = tipoviTretmana.get();
        assertEquals(0, tipovi.get(0).ID);
        assertEquals(1, tipovi.get(1).ID);
        assertEquals(2, tipovi.get(2).ID);
        assertEquals("tip1", tipovi.get(0).Tip);
        assertEquals("tip2", tipovi.get(1).Tip);
        assertEquals("tip3", tipovi.get(2).Tip);
        assertEquals(3, tipoviTretmana.get().size());
    }

    @Test
    public void testAddExisting() {
        assertFalse(tipoviTretmana.add(new TipTretmana(1, "tip4")));
        assertEquals(3, tipoviTretmana.get().size());
    }

    @Test
    public void testAddNew() {
        assertTrue(tipoviTretmana.add(new TipTretmana(3, "tip4")));
        ArrayList<TipTretmana> tipovi = tipoviTretmana.get();
        assertEquals(3, tipovi.get(3).ID);
        assertEquals("tip4", tipovi.get(3).Tip);
        assertEquals(4, tipoviTretmana.get().size());
    }

    @Test
    public void testGetByID() {
        TipTretmana tip = tipoviTretmana.get(1);
        assertEquals(1, tip.ID);
        assertEquals("tip2", tip.Tip);
    }

    @Test
    public void testRemove() {
        assertTrue(tipoviTretmana.remove(new TipTretmana(1, "")));
        assertTrue(tipoviTretmana.get().stream().noneMatch(tip -> tip.ID == 1));
        assertEquals(2, tipoviTretmana.get().size());
    }

    @Test
    public void testEdit() {
        assertTrue(tipoviTretmana.edit(new TipTretmana(1, ""), new TipTretmana(9, "novo")));
        assertTrue(tipoviTretmana.get().stream().anyMatch(tip -> tip.ID == 1 && tip.Tip.equals("novo")));
        assertEquals(3, tipoviTretmana.get().size());
    }

    @Test
    public void testGetNewID() {
        assertEquals(3, tipoviTretmana.getNewID());
    }
}