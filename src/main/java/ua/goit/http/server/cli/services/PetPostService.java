package ua.goit.http.server.cli.services;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import ua.goit.http.server.cli.CliFSM;
import ua.goit.http.server.cli.CliState;
import ua.goit.http.server.cli.IdleState;
import ua.goit.http.server.cli.PetState;
import ua.goit.http.server.entity.ApiResponse;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.fields.Category;
import ua.goit.http.server.entity.pet.fields.PetStatus;
import ua.goit.http.server.entity.pet.fields.Tag;
import ua.goit.http.server.method.Methods;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;

public class PetPostService extends CliState {
    public PetPostService(CliFSM fsm) {
        super(fsm);
    }

    @Override
    public void unknownCommand(String cmd) {
        System.out.println("Unknown command: " + cmd);
        init();
    }

    public void init() {
        System.out.println("_________________");
        System.out.println("POST menu for PET:\n" +
                "Commands: <NEW> - to add new pet\n" +
                "          <IMAGE> - to upload an image to a pet\n" +
                "Browsing: <BACK>, <MAIN>, <EXIT>\r");
        String method = fsm.getScanner().nextLine();
        switch (method.toUpperCase()) {
            case "NEW":
                System.out.println(createNewPet());
                init();
                break;
            case "IMAGE":
                System.out.println(doPostImage());
                init();
            case "BACK":
                back();
                break;
            case "MAIN":
                main();
                break;
            case "EXIT":
                System.exit(0);
            default:
                unknownCommand(method);
        }
    }

    private void back() {
        System.out.println("to PET menu -->");
        fsm.setState(new PetState(fsm));
    }

    private void main() {
        System.out.println("to MAIN menu -->");
        fsm.setState(new IdleState(fsm));
    }

    private String createNewPet() {
        fsm.setUrl("https://petstore.swagger.io/v2/pet");
        Pet pet = new Pet();
        System.out.println("_________________");
        System.out.println("Create new Pet");
        System.out.println("input Pet ID: \r");
        pet.setId(fsm.getScanner().nextLong());

        System.out.println("input Pet name: \r");
        pet.setName(fsm.getScanner().nextLine());
        System.out.println("Incorrect data has been input");

//        System.out.println("Do you want to set up the tags? Y(yes)\r");
//        String tags = fsm.getScanner().nextLine();
//        while (!(tags.equalsIgnoreCase("y"))) {
//            List<Tag> tagList = null;
//            Tag tag = addTag();
//            tagList.add(tag);
//        }
//        pet.setTags(List.of(new Tag(0, "string")));
//        pet.setStatus(PetStatus.AVAILABLE);
//
        System.out.println("input Pet's category ID: \r");
        long catId = fsm.getScanner().nextLong();
        System.out.println("input the Pet's category name: \r");
        String categoryName = fsm.getScanner().nextLine();
        Category category = new Category(catId, categoryName);



        pet.setPhotoUrls(List.of("noPhotos"));
        pet.setCategory(category);
        pet.setTags(List.of(new Tag(0, "string")));
        pet.setStatus(PetStatus.AVAILABLE);
        String petJson = fsm.getGson().toJson(pet);

        return Methods.doPost(fsm.getUrl(), petJson);
    }

    private Tag addTag() {
        System.out.println("Add Tag's ID : ");
        int idTag = fsm.getScanner().nextInt();
        System.out.println("Add Tag's name");
        String nameString = fsm.getScanner().nextLine();
        Tag res = new Tag(idTag, nameString);
        return res;
    }

    private ApiResponse doPostImage() {

        System.out.println("_________________");
        System.out.println("Upload image");
        System.out.println("input Pet ID: \r");
        String petId = fsm.getScanner().nextLine();

        fsm.setUrl("https://petstore.swagger.io/v2/pet/" + petId + "/uploadImage");

        System.out.println("input additional metadata: \r");
        String additionalMetadata = fsm.getScanner().nextLine();

        System.out.println("input path to image \r");
        String path = "images/" + fsm.getScanner().nextLine();

        File file = new File(path);
        if (file.exists()) {
            return uploadImage(petId, additionalMetadata, new File(path));
        } else {
            System.out.println("File: <" + file.toString().substring(7) + "> has not been found ");
            System.out.println("Try again?\nType <Y> if yes");
            String answer = fsm.getScanner().nextLine().toLowerCase();

            if (answer.equals("y")) {
                doPostImage();
            } else {
                init();
            }
        }
        return null;
    }

    private ApiResponse uploadImage(String id, String additionalMetadata, File file) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost("https://petstore.swagger.io/v2/pet/" + id + "/uploadImage");
            HttpEntity entity = MultipartEntityBuilder.create()
                    .addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, "filename")
                    .addTextBody("additionalMetadata", additionalMetadata)
                    .build();
            request.setEntity(entity);
            CloseableHttpResponse execute = client.execute(request);
            byte[] arr = execute.getEntity().getContent().readAllBytes();
            String strArr = new String(arr);
            return fsm.getGson().fromJson(strArr, ApiResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}