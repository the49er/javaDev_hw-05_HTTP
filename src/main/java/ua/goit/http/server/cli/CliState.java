package ua.goit.http.server.cli;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CliState {
    protected final CliFSM fsm;
    public void init(){}
    public void setPetState(){}
    public void setIdleState(){}
    public void setStoreState(){}
    public void setUserState(){}
    public void postMethodResponse(){}
    public void putMethod(){}
    public void putMethodResponse(){}
    public void deleteMethodResponse(){}
    public void unknownCommand(String cmd) {}
    public void show(){}

}
