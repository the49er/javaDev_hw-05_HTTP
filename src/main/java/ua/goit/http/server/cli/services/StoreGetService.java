package ua.goit.http.server.cli.services;

import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.StoreState;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.order.Order;
import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;

import java.net.http.HttpResponse;


public class StoreGetService extends CliState {
    public StoreGetService(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        System.out.println("_________________");
        System.out.println("GET menu for ORDER:\nGET by: <ID>, <STATUS>" +
                "\nBrowsing: <BACK>, <MAIN>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "ID":
                System.out.println(doGetByOrderId());
                init();
                break;
            case "STATUS":
                System.out.println(doGetByStatus());
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
        System.out.println("to STORE menu -->");
        fsm.setState(new StoreState(fsm));
    }


    private Object doGetByOrderId() {
        fsm.setUrl("https://petstore.swagger.io/v2/store/order/");
        System.out.println("_________________");
        System.out.println("Input ID: \r");
        String idStr = fsm.getScanner().nextLine();
        if (!Utils.isLong(idStr)){
            System.out.println("Incorrect ID: " + idStr);
            doGetByOrderId();
        }
        HttpResponse<String> response;

        try {
            response = Methods.doGet(fsm.getUrl(), idStr);
            System.out.println(response.statusCode());
            if (response.statusCode() == 200) {
                String result = response.body();
                return fsm.getGson().fromJson(result, Order.class);
            }

        } catch (NumberFormatException | NullPointerException e) {
            System.out.println("Incorrect ID: " + idStr);
            doGetByOrderId();
        }
        System.out.println("User " + idStr + " not found");
        response = Methods.doGet(fsm.getUrl(), idStr);
        return fsm.getGson().fromJson(response.body(), ApiResponse.class);
    }


    private String doGetByStatus() {
        fsm.setUrl("https://petstore.swagger.io/v2/store/");
        String query = "inventory";
        HttpResponse<String> response = Methods.doGet(fsm.getUrl(), query);

        return response.body();
    }
}
