package appServer;

import appClient.ClientCommunicationService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class PlayerTest {

    Player player;
    ServerCommunicationService communicationService;
    int id;
    @Before
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        player = mock(Player.class);
        communicationService = mock(ServerCommunicationService.class);
        id = 1;

        Field playerId = Player.class.getDeclaredField("playerId");
        playerId.setAccessible(true);
        playerId.set(player, id);
        Mockito.doCallRealMethod().when(player).getPlayerId();
        Field communicationS = Player.class.getDeclaredField("communicationService");
        communicationS.setAccessible(true);
        communicationS.set(player, communicationService);
        Mockito.doCallRealMethod().when(player).getCommunicationService();
    }


    @Test
    public void testGetPlayerId() {

        Assert.assertEquals(1, player.getPlayerId());
    }

    @Test
    public void testGetCommunicationService() {
        Assert.assertEquals(communicationService, player.getCommunicationService());
    }

}
