package ua.goit.http.server.cli;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CliState {
    protected final CliFSM fsm;
    public void init() {}

    public void mainMenu() {
        System.out.println("to MAIN menu -->");
        fsm.setState(new IdleState(fsm));
    }

    public void setPetState(){}
    public void setStoreState(){}
    public void setUserState(){}
    public void unknownCommand(String cmd) {}

}
