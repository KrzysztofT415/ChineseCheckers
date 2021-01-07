package appServer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerTest {

    Player player;
    ServerCommunicationService communicationService;
    @Before
    public void setUp() {
        player = mock(Player.class);
        communicationService = mock(ServerCommunicationService.class);
        when(player.getPlayerId()).thenReturn(1);
        when(player.getCommunicationService()).thenReturn(communicationService);
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
