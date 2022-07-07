package ua.goit.http.server.cli;

public class IdleState extends CliState{
     public IdleState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
    }

    @Override
    public void setPetState() {
        System.out.println("to PET menu -->");
        fsm.setState(new PetState(fsm));
    }

    @Override
    public void setStoreState() {
        System.out.println("to STORE menu -->");
        fsm.setState(new StoreState(fsm));
    }

    @Override
    public void setUserState() {
        System.out.println("to USER menu -->");
        fsm.setState( new UserState(fsm));
    }
}
