package view.swing;

import org.junit.Before;
import org.junit.Test;
import view.CellState;
import view.swing.SwingCellView;

import java.awt.*;

import static org.junit.Assert.*;

public class CellViewTest {

    SwingCellView cellView;

    @Before
    public void setUp() {
        cellView = new SwingCellView(0,0,1,1,0,6);
    }

    @Test
    public void testArgs() {
        assertEquals(0, cellView.getX());
        assertEquals(0, cellView.getY());
        assertEquals(1, cellView.getGridX());
        assertEquals(1, cellView.getGridY());
        assertEquals(CellState.EMPTY, cellView.getCellState());
        assertEquals(new Color(205,221,221), cellView.getColor());
    }

    @Test
    public void testSetState() {
        cellView.setCellState(5);
        assertEquals(CellState.PLAYER5, cellView.getCellState());
        assertEquals(new Color(171,54,143), cellView.getColor());
    }
}
