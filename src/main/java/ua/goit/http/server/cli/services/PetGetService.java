package ua.goit.http.server.cli.services;

import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.IdleState;
import ua.goit.http.server.cli.PetState;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.fields.PetStatus;
import ua.goit.http.server.method.Methods;
import java.util.Arrays;


public class PetGetService extends CliState {
    public PetGetService(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        System.out.println("_________________");
        System.out.println("GET menu for PET:\nGET by: <ID> or <STATUS>" +
                "\nBrowsing: <BACK>, <MAIN>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "ID":
                System.out.println(doGetById());
                System.out.println(fsm.getUrl());
                init();
                break;
            case "STATUS":
                printPets(doGetByStatus());
                init();
            case "BACK":
                back();
                break;
            case "MAIN":
                main();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);
        }
    }

    private void back() {
        System.out.println("to PET menu -->");
        fsm.setState(new PetState(fsm));
    }

    private void main() {
        System.out.println("to MAIN menu -->");
        fsm.setState(new IdleState(fsm));
    }

    private Pet doGetById() {
        fsm.setUrl("https://petstore.swagger.io/v2/pet/");
        System.out.println("_________________");
        System.out.println("Input PET ID: \r");
        String idStr = "";
        String pet = "";
        try {
            idStr = fsm.getScanner().nextLine();
            int id = Integer.parseInt(idStr);
            if (id != 0) {
                pet = Methods.doGet(fsm.getUrl(), idStr);
            } else {
                System.out.println("Incorrect ID: " + id);
                doGetById();
            }
        } catch (NumberFormatException e) {
            System.out.println("Incorrect ID: " + idStr);
            doGetById();
        }
        return fsm.getGson().fromJson(pet, Pet.class);
    }

    private Pet[] doGetByStatus() {
        System.out.println("_________________");
        System.out.println("Input PET status:\n <a> - available\n <p> - pending\n <s> - sold\n <c> - Cancel\r");
        String status = fsm.getScanner().nextLine();
        switch (status){
            case "a":
                status = String.valueOf(PetStatus.AVAILABLE).toLowerCase();
                break;
            case "p":
                status = String.valueOf(PetStatus.PENDING).toLowerCase();
                break;
            case "s":
                status = String.valueOf(PetStatus.SOLD).toLowerCase();
                break;
            case "c":
                init();
                break;
            default:
                unknownCommand(status);
        }
        fsm.setUrl("https://petstore.swagger.io/v2/pet/findByStatus?status=");
        String result = Methods.doGet(fsm.getUrl(), status);
        return fsm.getGson().fromJson(result, Pet[].class);
    }

    private void printPets(Pet[] list){
        Arrays.stream(list)
                .forEach(System.out::println);
    }


}
