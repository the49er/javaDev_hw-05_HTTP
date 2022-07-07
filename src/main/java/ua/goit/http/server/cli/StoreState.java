package ua.goit.http.server.cli;


import ua.goit.http.server.cli.services.StoreGetService;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.order.Order;
import ua.goit.http.server.entity.order.OrderStatus;

import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;

import java.time.LocalDate;


public class StoreState extends CliState {

    public StoreState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    @Override
    public void init() {
        fsm.setUrl("https://petstore.swagger.io/v2/store/order");
        System.out.println("_________________");
        System.out.println("Entity: Order\nChoose command: <GET>, <POST>, <DELETE>" +
                "\nBrowsing: <BACK>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "GET":
                getService();
                break;
            case "POST":
                System.out.println(createNewOrder());
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

    private String createNewOrder() {
        Order order = new Order();
        System.out.println("_________________");
        System.out.println("Create new Order");
        System.out.println("input Order ID:");
        String strId = fsm.getScanner().nextLine();
        long orderId = 0;
        if (Utils.isLong(strId)) {
            orderId = Long.parseLong(strId);
        } else {
            System.out.println("Incorrect input value\nOrder has not been created");
            init();
        }

        System.out.println("input pet ID:");
        String petIdStr = fsm.getScanner().nextLine();
        long petId = 0;
        if (Utils.isLong(petIdStr)) {
            petId = Long.parseLong(petIdStr);
        } else {
            System.out.println("Incorrect input value\nOrder has not been created");
            init();
        }
        System.out.println("input Pet quantity: \r");
        String quantityStr = fsm.getScanner().nextLine();
        int quantity = 0;
        if (Utils.isInt(quantityStr)) {
            quantity = Integer.parseInt(quantityStr);
        } else {
            System.out.println("Incorrect input value\nOrder has not been created");
            init();
        }

        System.out.println("input Order status:\n <p> - placed\n <a> - approved\n <d> - delivered\n <c> - cancel\r");
        String enumStatus = fsm.getScanner().nextLine();
        OrderStatus status = null;
        switch (enumStatus) {
            case "a":
                status = OrderStatus.APPROVED;
                break;
            case "p":
                status = OrderStatus.PLACED;
                break;
            case "d":
                status = OrderStatus.DELIVERED;
                break;
            default:
                unknownCommand(enumStatus);
        }

        System.out.println("input complete status:\n <t> - true\n <f> - false \r");
        String completedStr = fsm.getScanner().nextLine();
        boolean completed = false;
        switch (completedStr) {
            case "t":
                completed = true;
                break;
            case "f":
                break;
            default:
                unknownCommand(completedStr);
        }
        order.setId(orderId);
        order.setPetId(petId);
        order.setQuantity(quantity);
        order.setShipDate(String.valueOf(LocalDate.now()));
        order.setStatus(status);
        order.setComplete(completed);
        String petJson = fsm.getGson().toJson(order);
        return Methods.doPost(fsm.getUrl(), petJson);
    }

    private void getService() {
        System.out.println("to GET menu for ORDER -->");
        fsm.setState(new StoreGetService(fsm));
    }

    private ApiResponse doDelete() {
        System.out.println("_________________");
        System.out.println("input Order ID: \r");
        String id = fsm.getScanner().nextLine();
        System.out.println("Order with ID " + id + " will be deleted");
        System.out.println("To confirm deleting type <Y>");
        String deleteConfirmation = fsm.getScanner().nextLine();
        if (deleteConfirmation.equalsIgnoreCase("y")) {
            String apiResponse = Methods.delete(fsm.getUrl() + "/" + id);
            return fsm.getGson().fromJson(apiResponse, ApiResponse.class);
        } else {
            System.out.println("Deleting was canceled");
            init();
        }
        return null;
    }
}
