package ua.goit.http.server.cli;

import lombok.extern.slf4j.Slf4j;
import ua.goit.http.server.cli.services.UserGetService;
import ua.goit.http.server.cli.services.UserPostService;
import ua.goit.http.server.entity.ApiResponse;

import ua.goit.http.server.entity.user.User;
import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;


@Slf4j
public class UserState extends CliState {

    public UserState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {

        System.out.println("_________________");
        System.out.println("Entity: User\nChoose command: <GET>, <POST>, <PUT>, <DELETE>" +
                "\nBrowsing: <BACK>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "GET":
                getService();
                break;
            case "POST":
                postService();
            case "PUT":
                System.out.println(doPut());
                init();
            case "DELETE":
                Utils.printApiResponse(doDelete());
                init();
            case "BACK":
                mainMenu();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);

        }
    }

    private void getService() {
        System.out.println("to GET menu for USER -->");
        fsm.setState(new UserGetService(fsm));
    }

    private ApiResponse doPut() {

        fsm.setUrl("https://petstore.swagger.io/v2/user/");
        User user = new User();
        System.out.println("_________________");
        System.out.println("Update USER");

        System.out.println("input User name: \r");
        String userName = fsm.getScanner().nextLine();

        System.out.println("input User ID: \r");
        String userIdString = fsm.getScanner().nextLine();

        long userId = 0;
        if (Utils.isLong(userIdString)) {
            userId = Long.parseLong(userIdString);
        } else {
            System.out.println("Incorrect input value\nUser has not been created");
            init();
        }


        System.out.println("input User First Name: \r");
        String firstName = fsm.getScanner().nextLine();

        System.out.println("input User Last Name: \r");
        String lastName = fsm.getScanner().nextLine();

        System.out.println("input User email: \r");
        String email = fsm.getScanner().nextLine();

        System.out.println("input User paswword: \r");
        String password = fsm.getScanner().nextLine();

        System.out.println("input User phone: \r");
        String phone = fsm.getScanner().nextLine();

        System.out.println("input User status: \r");
        String userStatusString = fsm.getScanner().nextLine();
        int userStatus = 0;
        if (Utils.isInt(userStatusString)) {
            userStatus = Integer.parseInt(userStatusString);
        } else {
            System.out.println("Incorrect input value\nUser has not been created");
            init();
        }
        user.setId(userId);
        user.setUsername(userName);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhone(phone);
        user.setUserStatus(userStatus);
        String petJson = fsm.getGson().toJson(user);
        String response = Methods.put(fsm.getUrl() + userName, petJson);
        return fsm.getGson().fromJson(response, ApiResponse.class);
    }


    private void postService() {
        System.out.println("to POST menu for USER -->");
        fsm.setState(new UserPostService(fsm));
    }


    private ApiResponse doDelete() {
        fsm.setUrl("https://petstore.swagger.io/v2/user/");
        System.out.println("_________________");
        System.out.println("input Username: \r");
        String username = fsm.getScanner().nextLine().toLowerCase();
        System.out.println("User with Username " + username + " will be deleted");
        System.out.println("To confirm deleting type <Y>");
        String deleteConfirmation = fsm.getScanner().nextLine();
        deleteConfirmation.toLowerCase();
        if (deleteConfirmation.equals("y")) {
            String apiResponse = Methods.delete(fsm.getUrl() + username);
            return fsm.getGson().fromJson(apiResponse, ApiResponse.class);
        } else {
            System.out.println("Deleting was canceled");
            init();
        }
        return null;
    }
}
