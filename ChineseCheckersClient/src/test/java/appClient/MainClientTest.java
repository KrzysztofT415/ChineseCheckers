package appClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.AppWindow;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

public class MainClientTest {

    MainClient client;
    AppWindow appWindow;
    ClientCommunicationService communicationService;

    @Before
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        client = mock(MainClient.class);

        Field window = MainClient.class.getDeclaredField("appWindow");
        window.setAccessible(true);
        window.set(client, appWindow);
        Mockito.doCallRealMethod().when(client).getAppWindow();

        Field communication = MainClient.class.getDeclaredField("communicationService");
        communication.setAccessible(true);
        communication.set(client, communicationService);
        Mockito.doCallRealMethod().when(client).getCommunicationService();
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
