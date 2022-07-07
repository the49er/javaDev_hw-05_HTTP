package ua.goit.http.server.cli.services;


import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.IdleState;
import ua.goit.http.server.cli.UserState;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.user.User;
import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;


public class UserPostService extends CliState {
    public UserPostService(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        System.out.println("_________________");
        System.out.println("POST menu for USER:\n" +
                "Commands: <NEW> - to add new user\n" +
                "          <LIST> - to add new Users from list\n" +
                "Browsing: <BACK>, <MAIN>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "NEW":
                System.out.println(createNewUser());
                init();
                break;
            case "LIST":
                System.out.println(createNewUsersFromList());
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


    private ApiResponse createNewUser() {
        fsm.setUrl("https://petstore.swagger.io/v2/user");
        User user = new User();
        System.out.println("_________________");
        System.out.println("Create new USER");

        System.out.println("input User ID: \r");
        String userIdString = fsm.getScanner().nextLine();

        long userId = 0;
        if (Utils.isLong(userIdString)) {
            userId = Long.parseLong(userIdString);
        } else {
            System.out.println("Incorrect input value\nUser has not been created");
            init();
        }

        System.out.println("input User name: \r");
        String userName = fsm.getScanner().nextLine().toLowerCase();

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

        System.out.println("input User status (integer value): \r");
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
        String response = Methods.doPost(fsm.getUrl(), petJson);
        return fsm.getGson().fromJson(response, ApiResponse.class);
    }


    private ApiResponse createNewUsersFromList() {

        System.out.println("_________________");
        System.out.println("Create new users");
        System.out.println("Path to file \r");
        String path = "files/out/" + fsm.getScanner().nextLine();

        fsm.setUrl("https://petstore.swagger.io/v2/user/createWithList");
        String response = "";
        response = Methods.doPostFromFile(fsm.getUrl(), path);
        return fsm.getGson().fromJson(response, ApiResponse.class);
    }
}