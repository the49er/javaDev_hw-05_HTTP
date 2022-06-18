package ua.goit.http.server.cli;

public class IdleState extends CliState{
     protected IdleState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
    }

    @Override
    public void setPetState() {
        System.out.println("PET method is processing");
        fsm.setState(new PetState(fsm));
    }

    @Override
    public void setStoreState() {
        System.out.println("STORE method is processing");
        fsm.setState(new StoreState(fsm));
    }

    @Override
    public void setUserState() {
        System.out.println("USER method is processing");
    }
}
