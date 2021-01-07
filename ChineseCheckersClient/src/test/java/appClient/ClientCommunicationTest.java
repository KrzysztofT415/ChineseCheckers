package appClient;

import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.swing.*;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;

public class ClientCommunicationTest {

    ClientCommunicationService communicationService;
    MainClient client;
    PrintWriter writer;
    SwingAppWindow window;
    SwingBoardView boardView;
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
        client = mock(MainClient.class);

        communicationService = mock(ClientCommunicationService.class);
        in = new Scanner("WELCOME 2");
        scanner.set(communicationService, in);
        writer = new PrintWriter(System.out, true);
        output.set(communicationService, writer);

        mainClient.set(communicationService, client);
        window = mock(SwingAppWindow.class);
        boardView = mock(SwingBoardView.class);

        Mockito.doCallRealMethod().when(communicationService).start();
        Mockito.when(client.getAppWindow()).thenReturn(window);
        Mockito.doCallRealMethod().when(communicationService).send(any());
        Mockito.doCallRealMethod().when(communicationService).sendClick(anyInt(), anyInt());
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
        Assert.assertThat(result, StringContains.containsString("Dummy message"));
    }

    @Test
    public void testSendClick() {
        communicationService.sendClick(1,2);
        String result = outContent.toString();
        Assert.assertThat(result, StringContains.containsString("CLICK 1;2"));

        communicationService.sendClick(-3,-5);
        result = outContent.toString();
        Assert.assertThat(result, StringContains.containsString("CLICK -3;-5"));
    }

    @Test
    public void testStartQuit() {
        communicationService.start();
        String result = outContent.toString();
        Assert.assertThat(result, StringContains.containsString("QUIT"));
    }

}


