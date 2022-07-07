package ua.goit.http.server.entity.pet;

import ua.goit.http.server.exception.NoSuchPetStatusException;


public enum PetStatus {
    AVAILABLE("available"),
    PENDING("pending"),
    SOLD("sold");
    private String status;

    PetStatus(String status){
        this.status = status;
    }

    public String getValue() {
        return name().toLowerCase();
    }

    public static PetStatus getEnumFromString(String query) {
        for (PetStatus st : PetStatus.values()) {
            if (st.status.equalsIgnoreCase(query)) {
                return st;
            }
        }
        throw new NoSuchPetStatusException(query);
    }

    public static String getMsgForException() {
        StringBuilder sb = new StringBuilder();

        for (PetStatus pl : PetStatus.values()) {
            sb.append(pl.getValue());
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
