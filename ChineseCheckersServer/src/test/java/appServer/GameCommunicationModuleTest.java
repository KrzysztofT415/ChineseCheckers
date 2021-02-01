package appServer;

import appServer.connectionDB.GameJDBCTemplate;
import games.Change;
import games.GameContext;
import games.boards.GameBoard;
import games.boards.StandardBoard;
import games.rules.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameCommunicationModuleTest {

    Game gameMock;
    ServerCommunicationService communicationServiceMock;

    GameCommunicationModule gameCommunicationModule;

    @Before
    public void setUp() {
        gameMock = mock(Game.class);
        GameContext gameContextMock = mock(GameContext.class);
        when(gameMock.getGameContext()).thenReturn(gameContextMock);
        GameBoard gameBoardMock = mock(GameBoard.class);
        when(gameContextMock.getBoard()).thenReturn(gameBoardMock);

        communicationServiceMock = mock(ServerCommunicationService.class);

        //gameCommunicationModule = new GameCommunicationModule(1, gameMock, communicationServiceMock);
        gameCommunicationModule = new GameCommunicationModule();
        gameCommunicationModule.setGame(gameMock);
        gameCommunicationModule.setCommunicationService(communicationServiceMock);
        gameCommunicationModule.setPlayerId(1);
    }

    @Test
    public void testClickWhenNotCurrentPlayer() {
        when(gameMock.getCurrentPlayerId()).thenReturn(5);
        int result = gameCommunicationModule.execute("CLICK 5;5");
        verify(communicationServiceMock, times(1)).send(anyString());
        Assert.assertEquals(0, result);
    }

    @Test
    public void testClickWhenCurrentPlayerNotOwnPawn() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        when(gameMock.getGameContext().getBoard().getCellState(anyInt(), anyInt())).thenReturn(2);
        int result = gameCommunicationModule.execute("CLICK 5;5");
        Assert.assertEquals(0, result);
    }

    @Test
    public void testClickCorrectMove() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        when(gameMock.getGameContext().getBoard().getCellState(anyInt(), anyInt())).thenReturn(1);
        GameBoard board = new StandardBoard();
        board.placePlayers(2);
        board.setCellState(3, 3, 1);
        when(gameMock.getGameContext().getBoard()).thenReturn(board);
        int result = gameCommunicationModule.execute("CLICK 3;3");
        verify(communicationServiceMock).send(eq("SET 6;4;3;7;3;4;7;2;4;7;2;3;7;3;2;7;4;2;7"));
        Assert.assertEquals(0, result);
        int result2 = gameCommunicationModule.execute("CLICK 4;3");
        verify(communicationServiceMock).send(eq("CLEAR"));
        verify(communicationServiceMock).send(eq("SET 2;3;3;0;4;3;1"));
        verify(communicationServiceMock).send(eq("MESSAGE Move saved"));
        Assert.assertEquals(0, result2);
    }

    @Test
    public void testClickOtherPawn() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        when(gameMock.getGameContext().getBoard().getCellState(anyInt(), anyInt())).thenReturn(1);
        GameBoard board = new StandardBoard();
        board.placePlayers(2);
        board.setCellState(3, 3, 1);
        board.setCellState(3, 0, 1);
        when(gameMock.getGameContext().getBoard()).thenReturn(board);
        int result = gameCommunicationModule.execute("CLICK 3;3");
        verify(communicationServiceMock).send(eq("SET 6;4;3;7;3;4;7;2;4;7;2;3;7;3;2;7;4;2;7"));
        Assert.assertEquals(0, result);
        int result2 = gameCommunicationModule.execute("CLICK 3;0");
        verify(communicationServiceMock).send(eq("CLEAR"));
        verify(communicationServiceMock).send(eq("SET 6;4;0;7;3;1;7;2;1;7;2;0;7;3;-1;7;4;-1;7"));
        Assert.assertEquals(0, result2);
    }

    @Test
    public void testClickIncorrectMove() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        when(gameMock.getGameContext().getBoard().getCellState(anyInt(), anyInt())).thenReturn(1);
        GameBoard board = new StandardBoard();
        board.placePlayers(2);
        board.setCellState(3, 3, 1);
        when(gameMock.getGameContext().getBoard()).thenReturn(board);
        int result = gameCommunicationModule.execute("CLICK 3;3");
        verify(communicationServiceMock).send(eq("SET 6;4;3;7;3;4;7;2;4;7;2;3;7;3;2;7;4;2;7"));
        Assert.assertEquals(0, result);
        int result2 = gameCommunicationModule.execute("CLICK 3;0");
        verify(communicationServiceMock).send(eq("CLEAR"));
        Assert.assertEquals(0, result2);
    }

    @Test
    public void testClickOnSameCell() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        when(gameMock.getGameContext().getBoard().getCellState(anyInt(), anyInt())).thenReturn(1);
        GameBoard board = new StandardBoard();
        board.placePlayers(2);
        board.setCellState(3, 3, 1);
        when(gameMock.getGameContext().getBoard()).thenReturn(board);
        int result = gameCommunicationModule.execute("CLICK 3;3");
        verify(communicationServiceMock).send(eq("SET 6;4;3;7;3;4;7;2;4;7;2;3;7;3;2;7;4;2;7"));
        Assert.assertEquals(0, result);
        int result2 = gameCommunicationModule.execute("CLICK 3;3");
        verify(communicationServiceMock).send(eq("CLEAR"));
        Assert.assertEquals(0, result2);
    }

    @Test
    public void testPassWhenNotCurrentPlayer() {
        when(gameMock.getCurrentPlayerId()).thenReturn(2);
        int result = gameCommunicationModule.execute("PASS");
        verify(gameMock, times(0)).nextPlayer();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testPassWhenCurrentPlayer() {
        when(gameMock.getCurrentPlayerId()).thenReturn(1);
        int result = gameCommunicationModule.execute("PASS");
        verify(gameMock, times(1)).nextPlayer();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testQuit() {
        int result = gameCommunicationModule.execute("QUIT");
        Assert.assertEquals(1, result);
    }

    @Test
    public void testGetPossibleMoves() throws NoSuchFieldException, IllegalAccessException {
        GameCommunicationModule gameCommunicationModuleMock = mock(GameCommunicationModule.class);
        when(gameCommunicationModuleMock.getPossibleMoves(anyInt(), anyInt())).thenCallRealMethod();
        Field gameField = GameCommunicationModule.class.getDeclaredField("game");
        gameField.setAccessible(true);
        Game gameMock = mock(Game.class);
        gameField.set(gameCommunicationModuleMock, gameMock);

        GameContext gameContextMock = mock(GameContext.class);
        when(gameMock.getGameContext()).thenReturn(gameContextMock);

        GameBoard board = new StandardBoard();
        board.placePlayers(2);
        board.setCellState(2, 2, 1);
        board.setCellState(3, 2, 2);

        when(gameContextMock.getBoard()).thenReturn(board);
        when(gameContextMock.getRulesOfType(any())).thenCallRealMethod();
        when(gameContextMock.getRules()).thenReturn(new GameRule[]{new MoveOneRule(), new NotLeaveFinalCornerRule()});

        Change[] returned = gameCommunicationModuleMock.getPossibleMoves(2, 2);
        Assert.assertEquals(4, returned.length);
    }
}
