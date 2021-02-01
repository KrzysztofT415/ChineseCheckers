package appServer;

import appServer.connectionDB.GameJDBCTemplate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import static org.mockito.Mockito.*;

public class GameTest {

    Game game;
    Player player1,player2;
    ServerCommunicationService communicationService;
    Field field;

    @Before
    public void setUp() throws Exception {
        game = new Game();
        communicationService = mock(ServerCommunicationService.class);
        player1 = mock(Player.class);
        player2 = mock(Player.class);
        when(player1.getPlayerId()).thenReturn(1);
        when(player2.getPlayerId()).thenReturn(2);
        when(player2.getCommunicationService()).thenReturn(communicationService);
        when(player1.getCommunicationService()).thenReturn(communicationService);
        doNothing().when(communicationService).send(anyString());

        field = game.getClass().getDeclaredField("players");
        field.setAccessible(true);
    }

    @Test
    public void testConnectPlayer() throws IllegalAccessException {
        game.connectPlayer(player1, 0);
        Player[] players = (Player[]) field.get(game);
        Assert.assertEquals(2, players.length);
        Assert.assertNotNull(players[0]);
        Assert.assertNull(players[1]);
    }

    @Test
    public void testGetWinners() {
       Assert.assertEquals(1, game.getWinners());
    }


    @Test
    public void testGetCurrentPlayerId() {
        game.connectPlayer(player1, 0);
        game.connectPlayer(player2, 1);
        game.play();
        Assert.assertTrue(isOneOrTwo(game.getCurrentPlayerId()));
    }

    private boolean isOneOrTwo(int x) {
        return x == 1 || x == 2;
    }

}
