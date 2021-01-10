package appServer;

import games.Change;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

public class GameCommunicationModuleTest {
    GameCommunicationModule communicationModule;
    @Before
    public void setUp() {
        communicationModule = mock(GameCommunicationModule.class);
        Mockito.doCallRealMethod().when(communicationModule).changesInfoToString(any());
    }


    @Test
    public void changesInfoToStringTest() {
        String expected = "SET 2;0;1;2;4;-1;2";
        Change[] change = {new Change(0,1,2), new Change(4,-1,2)};
        Assert.assertEquals(expected, communicationModule.changesInfoToString(change));
    }
}
