package appClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.AppWindow;
import view.BoardView;

import static org.mockito.Mockito.*;

public class GameCommunicationModuleTest {

    MainClient appMock;
    AppWindow appWindowMock;
    BoardView boardViewMock;
    GameCommunicationModule gameCommunicationModule;

    @Before
    public void setUp() {
        appMock = mock(MainClient.class);
        appWindowMock = mock(AppWindow.class);
        boardViewMock = mock(BoardView.class);

        when(appMock.getAppWindow()).thenReturn(appWindowMock);
        when(appWindowMock.getBoardView()).thenReturn(boardViewMock);

        gameCommunicationModule = new GameCommunicationModule(appMock);
    }

    @Test
    public void testResolveParameters() {
        int[][] result = gameCommunicationModule.resolveParameters("2;1;0;0;2;0;1");
        Assert.assertArrayEquals(new int[][] {{1, 0, 0}, {2, 0, 1}}, result);
    }

    @Test
    public void testExecuteStart() {
        int result = gameCommunicationModule.execute("START 2;0;0;1;0;1;2");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteSet() {
        int result = gameCommunicationModule.execute("SET 1;1;0;3");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteClear() {
        int result = gameCommunicationModule.execute("CLEAR");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteMessage() {
        int result = gameCommunicationModule.execute("MESSAGE dummy");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteLost() {
        int result = gameCommunicationModule.execute("LOST");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteWon() {
        int result = gameCommunicationModule.execute("WON 2");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testExecuteLeave() {
        int result = gameCommunicationModule.execute("LEAVE");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testExecuteEnd() {
        int result = gameCommunicationModule.execute("END");
        Assert.assertEquals(1, result);
    }
}