package ua.goit.http.server.cli;

import lombok.Getter;
import lombok.Setter;
import ua.goit.http.server.method.Method;

import java.util.Scanner;

public class CliFSM {

    CliState state;
    @Getter
    Scanner scanner;
    @Setter
    @Getter
    String url;

    public CliFSM(){
        state = new IdleState(this);
        scanner = new Scanner(System.in);
        starInputLoop();
    }

    private void starInputLoop() {
        while (true) {
            System.out.println("Choose an entity\r");
            String command = scanner.nextLine();

            switch (command) {
                case "pet":
                    setPetState();
                    break;
                case "store":
                    setStoreState();
                    break;
                case "user":
                    setUserState();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    unknownCommand(command);
            }
        }
    }

    private void unknownCommand(String cmd) {
        state.unknownCommand(cmd);
    }
    private void show(){

    }
    public void setPetState(){
        state.setPetState();
    }
    public void setStoreState(){
        state.setStoreState();
    }
    public void setUserState(){
        state.setUserState();
    }
    public void postMethodResponse(){
        state.postMethodResponse();
    }
    public void putMethod(){
        state.putMethod();
    }
    public void putMethodResponse(){
        state.putMethodResponse();
    }
    public void deleteMethodResponse(){
        state.deleteMethodResponse();
    }

    public void setState (CliState state) {
        this.state = state;
        state.init();
    }
}
