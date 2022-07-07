package ua.goit.http.server.exception;

import ua.goit.http.server.entity.pet.PetStatus;

public class NoSuchPetStatusException extends RuntimeException {

    public NoSuchPetStatusException(String str) {
        super("No such Enum value: " + str +
                "\nAccepted values are: " + PetStatus.getMsgForException() + " (ignoreCaseAble as well)");
    }
}
