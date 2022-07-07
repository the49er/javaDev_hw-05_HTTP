package ua.goit.http.server.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.Scanner;

public class CliFSM {

    CliState state;
    @Getter
    Scanner scanner;
    @Getter
    Gson gson;
    @Setter
    @Getter
    String url;

    public CliFSM(){
        state = new IdleState(this);
        scanner = new Scanner(System.in);
        gson = new GsonBuilder().setPrettyPrinting().create();
        init();
    }

    private void init() {
        while (true) {
            System.out.println("_________________");
            System.out.println("MAIN menu");
            System.out.println("Choose an entity: <PET>, <STORE>, <USER>\nBrowsing: <EXIT>\r");
            String command = scanner.nextLine();

            switch (command.toLowerCase()) {
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

    public void setPetState(){
        state.setPetState();
    }
    public void setStoreState(){
        state.setStoreState();
    }
    public void setUserState(){
        state.setUserState();
    }

    public void setState (CliState state) {
        this.state = state;
        state.init();
    }
}
