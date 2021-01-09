package appClient;

import view.AppWindow;
import view.swing.SwingAppWindow;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainClient {

    private Socket socket;
    private AppWindow appWindow;
    private ClientCommunicationService communicationService;


    public MainClient() {
        while(true) {
            String input = JOptionPane.showInputDialog("Ip address of server host (ip:port) :");
            if (input == null) break;

            try {
                String ip = input.split(":")[0];
                int port = Integer.parseInt(input.split(":")[1]);

                this.socket = new Socket(InetAddress.getByName(ip), port);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Connection refused, try again");
                continue;
            }
            System.out.println("Client connected to : "+input);

            this.communicationService = new ClientCommunicationService(this, this.socket);
            this.communicationService.connectModule(new GameCommunicationModule(this));
            this.appWindow = new SwingAppWindow(this);
            this.start();
            break;
        }
    }

    public void start() {
        try {
            communicationService.start();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { this.socket.close();
            } catch (IOException e) { e.printStackTrace(); }
            this.appWindow.close();
        }
    }

    public AppWindow getAppWindow() {
        return this.appWindow;
    }

    public ClientCommunicationService getCommunicationService() {
        return this.communicationService;
    }

    public static void main(String[] args) {
        new MainClient();
    }
}