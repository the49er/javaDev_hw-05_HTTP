package ua.goit.http.server.entity.order;


import ua.goit.http.server.exception.NoSuchOrderStatusException;

public enum OrderStatus {
    PLACED("placed"),
    APPROVED("approved"),
    DELIVERED("delivered");
    private String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return name().toLowerCase();
    }

    public static OrderStatus getEnumFromString(String query) {
        for (OrderStatus st : OrderStatus.values()) {
            if (st.status.equalsIgnoreCase(query)) {
                return st;
            }
        }
        throw new NoSuchOrderStatusException(query);
    }

    public static String getMsgForException() {
        StringBuilder sb = new StringBuilder();

        for (OrderStatus st : OrderStatus.values()) {
            sb.append(st.getValue());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}