package ua.goit.http.server.cli;

public class StoreState extends CliState{

    public StoreState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
    }

    @Override
    public void init() {
        System.out.println("Choose URL: \r");

        String name = fsm.getScanner().nextLine();
        if (name.equals("1")) {
            fsm.setState(new IdleState(fsm));
        }else{
            System.out.println("No URL");
            init();
        }

    }
}
