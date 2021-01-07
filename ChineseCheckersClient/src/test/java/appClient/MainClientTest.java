package appClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import view.AppWindow;

import static org.mockito.Mockito.*;

public class MainClientTest {

    MainClient client;
    AppWindow appWindow;
    ClientCommunicationService communicationService;

    @Before
    public void setUp() {
        client = mock(MainClient.class);
        when(client.getAppWindow()).thenReturn(appWindow);
        when(client.getCommunicationService()).thenReturn(communicationService);
    }

    @Test
    public void testGetAppWindow() {
        client.start();
        Assert.assertEquals(client.getAppWindow(), appWindow);
    }

    @Test
    public void testGetClientCommunicationService() {
        Assert.assertEquals(client.getCommunicationService(), communicationService);
    }
}
