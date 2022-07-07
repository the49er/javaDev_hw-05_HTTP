package ua.goit.http.server.cli;

import lombok.extern.slf4j.Slf4j;
import ua.goit.http.server.cli.services.PetGetService;
import ua.goit.http.server.cli.services.PetPostService;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.Category;
import ua.goit.http.server.entity.pet.PetStatus;
import ua.goit.http.server.entity.pet.Tag;
import ua.goit.http.server.exception.NoSuchPetStatusException;
import ua.goit.http.server.utils.Methods;
import ua.goit.http.server.utils.Utils;

import java.util.List;

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

    private String doPut() {

        fsm.setUrl("https://petstore.swagger.io/v2/pet");
        Pet pet = new Pet();
        System.out.println("_________________");
        System.out.println("Create new Pet");
        System.out.println("input Pet ID:");
        String strId = fsm.getScanner().nextLine();
        long petId = 0;
        if (Utils.isLong(strId)) {
            petId = Long.parseLong(strId);
        } else {
            System.out.println("Incorrect input value\nPet has not been created");
            init();
        }
        System.out.println("input Pet name: \r");
        String petName = fsm.getScanner().nextLine();

        System.out.println("input Pet category ID: \r");
        String catId = fsm.getScanner().nextLine();
        long petCategoryId = 0;
        if (Utils.isLong(catId)) {
            petCategoryId = Long.parseLong(catId);
        } else {
            System.out.println("Incorrect input value\nPet has not been created");
            init();
        }

        System.out.println("input Pet's category (type of an animal): \r");
        String categoryName = fsm.getScanner().nextLine();

        Category category = new Category(petCategoryId, categoryName);

        System.out.println("input Pet status: \r");
        String enumStatus = fsm.getScanner().nextLine();
        PetStatus status = null;
        try {
            status = PetStatus.getEnumFromString(enumStatus);
        } catch (NoSuchPetStatusException e) {
            System.out.println("Incorrect status value: " + enumStatus +
                    "\nAccepted values are: " + PetStatus.getMsgForException());
            System.out.println("Pet has not been created");
            init();
        }
        pet.setId(petId);
        pet.setName(petName);
        pet.setPhotoUrls(List.of("noPhotos"));
        pet.setCategory(category);
        pet.setTags(List.of(new Tag(1, "tag1"), new Tag(2, "tag2")));
        pet.setStatus(status);
        String petJson = fsm.getGson().toJson(pet);
        return Methods.put(fsm.getUrl(), petJson);

    }

    private void postService() {
        System.out.println("to POST menu for PET -->");
        fsm.setState(new PetPostService(fsm));
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
        if (deleteConfirmation.equals("y")) {
            String apiResponse = Methods.delete(fsm.getUrl() + id);
            return fsm.getGson().fromJson(apiResponse, ApiResponse.class);
        } else {
            System.out.println("Deleting was canceled");
            init();
        }
        return null;
    }

}
