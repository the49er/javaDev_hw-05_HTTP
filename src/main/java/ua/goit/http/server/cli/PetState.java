package ua.goit.http.server.cli;

import lombok.extern.slf4j.Slf4j;
import ua.goit.http.server.cli.services.PetGetService;
import ua.goit.http.server.cli.services.PetPostService;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.method.Methods;

@Slf4j
public class PetState extends CliState {

    public PetState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        fsm.setUrl("https://petstore.swagger.io/v2/pet/");
        System.out.println("_________________");
        System.out.println("Entity: Pet\nChoose command: <GET>, <POST>, <PUT>, <DELETE>" +
                "\nBrowsing: <BACK>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "GET":
                getService();
                break;
            case "POST":
                postService();
            case "PUT":
                put();
            case "DELETE":
                printApiResponse(doDelete());
                init();
            case "BACK":
                back();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);

        }
    }

    private void put() {
        System.out.println("to PUT menu for PET -->");

    }

    private void postService() {
        System.out.println("to POST menu for PET -->");
        fsm.setState(new PetPostService(fsm));
    }

    private void back() {
        System.out.println("to MAIN menu -->");
        fsm.setState(new IdleState(fsm));
    }

    private void getService() {
        System.out.println("to GET menu for PET -->");
        fsm.setState(new PetGetService(fsm));
    }

    private ApiResponse doDelete() {
        System.out.println("_________________");
        System.out.println("input Pet ID: \r");
        String id = fsm.getScanner().nextLine();
        System.out.println("Pet with ID " + id + " will be deleted");
        System.out.println("To confirm deleting type <Y>");
        String deleteConfirmation = fsm.getScanner().nextLine();
        deleteConfirmation.toLowerCase();
        if(deleteConfirmation.equals("y")){
            String apiResponse = Methods.delete(fsm.getUrl()+id);
            return fsm.getGson().fromJson(apiResponse, ApiResponse.class);
        }else {
            System.out.println("Deleting was canceled");
            init();
        }
        return null;
    }

    private void printApiResponse(ApiResponse apiResponse){
        if(apiResponse != null){
            System.out.println(apiResponse);
        }else {
            System.out.println("PET does not exist or has been already deleted before");
        }
    }
}
