package ua.goit.http.server.cli.services;

import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.IdleState;
import ua.goit.http.server.cli.PetState;
import ua.goit.http.server.cli.UserState;
import ua.goit.http.server.entity.ApiResponse;

import ua.goit.http.server.entity.user.User;
import ua.goit.http.server.utils.Methods;

import java.net.http.HttpResponse;


public class UserGetService extends CliState {
    public UserGetService(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        System.out.println("_________________");
        System.out.println("GET menu for USER:\nGET by: <USERNAME>, <LOGIN> or <LOGOUT>" +
                "\nBrowsing: <BACK>, <MAIN>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "USERNAME":
                System.out.println(doGetByUserName());
                init();
                break;
            case "LOGIN":
                System.out.println(doGetByLogin());
                init();
            case "LOGOUT":
                System.out.println(doGetByLogout());
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
        System.out.println("to USER menu -->");
        fsm.setState(new UserState(fsm));
    }


    private Object doGetByUserName() {
        fsm.setUrl("https://petstore.swagger.io/v2/user/");
        System.out.println("_________________");
        System.out.println("Input Username: \r");
        String idStr = fsm.getScanner().nextLine();
        HttpResponse<String> response;
        try {
            response = Methods.doGet(fsm.getUrl(), idStr);
            if (response.statusCode() == 200) {
                String result = response.body();
                return fsm.getGson().fromJson(result, User.class);
            }

        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("Incorrect ID: " + idStr);
            doGetByUserName();
        }
        System.out.println("User " + idStr + " not found");
        response = Methods.doGet(fsm.getUrl(), idStr);
        return fsm.getGson().fromJson(response.body(), ApiResponse.class);
    }

    private ApiResponse doGetByLogin() {
        fsm.setUrl("https://petstore.swagger.io/v2/user/");
        System.out.println("_________________");
        System.out.println("Input User First Name: \r");
        String username = fsm.getScanner().nextLine();
        System.out.println("Input User Password: \r");
        String password = fsm.getScanner().nextLine();
        String query = "login?username=" + username + "&password=" + password;
        HttpResponse<String> response = null;
        try {
            response = Methods.doGet(fsm.getUrl(), query);

        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("User " + username + " does not exist or incorrect password");
            doGetByUserName();
        }
        return fsm.getGson().fromJson(response.body(), ApiResponse.class);
    }

    private ApiResponse doGetByLogout() {
        fsm.setUrl("https://petstore.swagger.io/v2/user/");
        String query = "logout";
        HttpResponse<String> response = Methods.doGet(fsm.getUrl(), query);
        return fsm.getGson().fromJson(response.body(), ApiResponse.class);
    }
}
