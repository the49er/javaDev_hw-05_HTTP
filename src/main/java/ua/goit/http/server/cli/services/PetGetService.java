package ua.goit.http.server.cli.services;

import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.PetState;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.PetStatus;
import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;

import java.net.http.HttpResponse;
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
                init();
                break;
            case "STATUS":
                printPets(doGetByStatus());
                init();
            case "BACK":
                previousMenu();
                break;
            case "MAIN":
                mainMenu();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);
        }
    }

    private void previousMenu() {
        System.out.println("to PET menu -->");
        fsm.setState(new PetState(fsm));
    }


    private Object doGetById() {
        fsm.setUrl("https://petstore.swagger.io/v2/pet/");
        System.out.println("_________________");
        System.out.println("Input PET ID: \r");
        String idStr = "";
        HttpResponse<String> response;
        try {
            idStr = fsm.getScanner().nextLine();
            if(Utils.isLong(idStr)) {
                response = Methods.doGet(fsm.getUrl(), idStr);
                if (response.statusCode() == 200) {
                    String result = response.body()
                            .replaceAll("sold", "SOLD")
                            .replaceAll("pending", "PENDING")
                            .replaceAll("available", "AVAILABLE");
                    return fsm.getGson().fromJson(result, Pet.class);
                }
            }
        }catch (NumberFormatException | NullPointerException e) {
            System.out.println("Incorrect ID: " + idStr);
            doGetById();
        }
        System.out.println("Incorrect ID: " + idStr);
        response = Methods.doGet(fsm.getUrl(), idStr);
        return fsm.getGson().fromJson(response.body(), ApiResponse.class);
    }

    private Pet[] doGetByStatus() {
        System.out.println("_________________");
        System.out.println("Input PET status:\n <a> - available\n <p> - pending\n <s> - sold\n <c> - Cancel\r");
        String status = fsm.getScanner().nextLine();
        switch (status) {
            case "a":
                status = PetStatus.AVAILABLE.getValue();
                break;
            case "p":
                status = PetStatus.PENDING.getValue();
                break;
            case "s":
                status = PetStatus.SOLD.getValue();
                break;
            case "c":
                init();
                break;
            default:
                unknownCommand(status);
        }
        fsm.setUrl("https://petstore.swagger.io/v2/pet/findByStatus?status=");
        String result = Methods.doGet(fsm.getUrl(), status).body()
                .replaceAll("sold", "SOLD")
                .replaceAll("pending", "PENDING")
                .replaceAll("available", "AVAILABLE");
        return fsm.getGson().fromJson(result, Pet[].class);
    }

    private void printPets(Pet[] list) {
        Arrays.stream(list)
                .forEach(System.out::println);
    }


}
