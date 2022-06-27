package ua.goit.http.server.cli;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.fields.Category;
import ua.goit.http.server.entity.pet.fields.PetStatus;
import ua.goit.http.server.entity.pet.fields.Tag;
import ua.goit.http.server.method.Methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.List;

@Slf4j
public class PetState extends CliState {
    private static final Gson GSON = new GsonBuilder().create();

    public PetState(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        fsm.setUrl("https://petstore.swagger.io/v2/pet");
        System.out.println("_________________");
        System.out.println("Entity: Pet\nChoose command: GET, POST, PUT, DELETE or BACK, EXIT\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "GET":
                System.out.println(doGetMethod());
                init();
                break;
            case "POST":
                init();
            case "BACK":
                back();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);

        }
    }

    private void back(){
        System.out.println("to MAIN menu -->");
        fsm.setState(new IdleState(fsm));
    }

    private Tag addTag() {
        System.out.println("Add Tag's ID : ");
        int idTag = fsm.getScanner().nextInt();
        System.out.println("Add Tag's name");
        String nameString = fsm.getScanner().nextLine();
        Tag res = new Tag(idTag, nameString);
        return res;
    }

    private Pet doGetMethod() {
        System.out.println("_________________");
        System.out.println("Input PET ID: \r");
        String idStr = "";
        String pet = "";
        try {
            idStr = fsm.getScanner().nextLine();
            int id = Integer.parseInt(idStr);
            if (id != 0) {
                pet = Methods.doGet(fsm.getUrl(), id);
            }else {
                System.out.println("Incorrect ID: " + id);
                doGetMethod();
            }
        }catch (NumberFormatException e) {
            System.out.println("Incorrect ID: " + idStr);
            doGetMethod();
        }

        return GSON.fromJson(pet, Pet.class);
    }

    private void doPostMethod() {
        System.out.println("_________________");
        System.out.println("Input ID: \r");
        System.out.println("_________________");
        System.out.println("Create new Pet");
        Pet pet2 = new Pet();

        System.out.println("input Pet ID: \r");
        pet2.setId(fsm.getScanner().nextInt());

        System.out.println("input Pet's category ID: \r");
        int catId = fsm.getScanner().nextInt();

        System.out.println("input the Pet's category name: \r");
        String catName = fsm.getScanner().nextLine();

        pet2.setCategory(new Category(catId, catName));

        System.out.println("input Pet's name: \r");
        String petName = fsm.getScanner().nextLine();
        pet2.setName(petName);

        System.out.println("Do you want to upload the Pet's photo?: <Y> yes) \r");
        String uploadPhoto = fsm.getScanner().nextLine();
        if (uploadPhoto.equalsIgnoreCase("y")) {
            System.out.println("Path to file: ");
            String path = fsm.getScanner().nextLine();
            File img = new File(path);
            String imgStr = "";
            try {
                InputStream bis = new FileInputStream(img);
                byte[] allBytes = bis.readAllBytes();
                imgStr = Base64.getEncoder().encodeToString(allBytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            pet2.setPhotoUrls(List.of(imgStr));
        } else {
            pet2.setPhotoUrls(List.of("noPhotos"));
        }
        System.out.println("Do you want to set up the tags?: Y(yes)\r");
        String tags = fsm.getScanner().nextLine();
        while (!(tags.equalsIgnoreCase("y"))) {
            List<Tag> tagList = null;
            Tag tag = addTag();
            tagList.add(tag);
        }
        pet2.setTags(List.of(new Tag(0, "string")));
        pet2.setStatus(PetStatus.AVAILABLE);
        fsm.setState(new PetState(fsm));
    }
}
