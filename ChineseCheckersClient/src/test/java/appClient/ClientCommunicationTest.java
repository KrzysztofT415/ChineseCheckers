package appClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.AppWindow;
import view.BoardView;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.*;

public class ClientCommunicationTest {

    ClientCommunicationService communicationService;
    MainClient client;
    PrintWriter writer;
    AppWindow window;
    BoardView boardView;
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    Scanner in;

    @Before
    public void setUp() throws Exception {

        System.setOut(new PrintStream(outContent));

        Field scanner = ClientCommunicationService.class.getDeclaredField("in");
        scanner.setAccessible(true);
        Field output = ClientCommunicationService.class.getDeclaredField("out");
        output.setAccessible(true);

        Field mainClient = ClientCommunicationService.class.getDeclaredField("app");
        mainClient.setAccessible(true);
        client = Mockito.mock(MainClient.class);

        communicationService = Mockito.mock(ClientCommunicationService.class);
        in = new Scanner("WELCOME 2");
        scanner.set(communicationService, in);
        writer = new PrintWriter(System.out, true);
        output.set(communicationService, writer);

        mainClient.set(communicationService, client);
        window = Mockito.mock(AppWindow.class);
        boardView = Mockito.mock(BoardView.class);

        Mockito.doCallRealMethod().when(communicationService).start();
        Mockito.when(client.getAppWindow()).thenReturn(window);
        Mockito.doCallRealMethod().when(communicationService).send(any());
        Mockito.when(window.getBoardView()).thenReturn(boardView);
    }

    @Test
    public void testSetPlayer() {
        communicationService.start();
        Mockito.verify(window).setPlayer(2);
    }

    @Test
    public void testSend() {
        communicationService.send("Dummy message");
        String result = outContent.toString();
        Assert.assertTrue(result.contains("Dummy message"));
    }

    @Test
    public void testStartQuit() {
        communicationService.start();
        String result = outContent.toString();
        Assert.assertTrue(result.contains("QUIT"));
    }

}


