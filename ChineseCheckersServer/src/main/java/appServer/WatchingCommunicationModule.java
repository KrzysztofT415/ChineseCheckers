package appServer;

public class WatchingCommunicationModule implements CommunicationModule {

    private final ServerCommunicationService communicationService;
    private int move = -1;
    private final String[] moves = {"SET 2;0;1;1;0;0;0","SET 2;0;1;0;1;1;1","SET 2;1;1;0;1;0;1"};
    private final String[] moves2 = {"SET 2;0;1;0;0;0;1","SET 2;0;1;1;1;1;0","SET 2;1;1;1;1;0;0"};

    WatchingCommunicationModule(ServerCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Override
    public int execute(String command) {
        if (command.startsWith("NEXT")) {
            move += 1;
            if (move >= moves.length) move = moves.length - 1;
            this.communicationService.send(moves[move]);

        } else if (command.startsWith("BACK")) {
            if (move < 0) move = 0;
            this.communicationService.send(moves2[move]);
            move -= 1;
            if (move < 0) move = -1;

        } else if (command.startsWith("QUIT")) {
            return 1;
        }
        return 0;
    }
}
