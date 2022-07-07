package ua.goit.http.server.exception;

import ua.goit.http.server.entity.order.OrderStatus;

public class NoSuchOrderStatusException extends RuntimeException {

    public NoSuchOrderStatusException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + OrderStatus.getMsgForException() + " (ignoreCaseAble as well)");
    }
}
