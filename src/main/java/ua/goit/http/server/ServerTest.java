package ua.goit.http.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ua.goit.http.server.entity.pet.fields.Category;
import ua.goit.http.server.entity.pet.Pet;
import ua.goit.http.server.entity.pet.fields.PetStatus;
import ua.goit.http.server.entity.pet.fields.Tag;
import ua.goit.http.server.method.Methods;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Paths;
import java.util.List;


public class ServerTest {
    static HttpClient client = HttpClient.newHttpClient();
    static final Gson GSON = new GsonBuilder().create();

    public static void main(String[] args) throws Exception {
        //ServerSocket server = new ServerSocket(20000);
        //Socket connection = server.accept();
//        String uri = "https://petstore.swagger.io/v2/pet";
//        Pet pet2 = new Pet();
//        pet2.setId(1);
//        pet2.setCategory(new Category(1, "string"));
//        pet2.setName("Shimi");
//        pet2.setPhotoUrls(List.of("asdasd"));
//        pet2.setTags(List.of(new Tag(0, "string")));
//        pet2.setStatus(PetStatus.AVAILABLE);
//        System.out.println(GSON.toJson(pet2));
//
//
//        ServerTest serverTest = new ServerTest();
//
//        serverTest.doPost(uri);
//        System.out.println(serverTest.doPost(uri));
//
//        String json = serverTest.doGet(uri, 2);
//        System.out.println(json);
//
//        Pet pet = GSON.fromJson(json, Pet.class);
//        String body = GSON.toJson(pet);
//        System.out.println(body);

        String url = "https://petstore.swagger.io/v2/pet/findByStatus?status=";
        String status = String.valueOf(PetStatus.AVAILABLE).toLowerCase();
        String result = Methods.doGet(url, status);
        System.out.println(result);

    }



    private Response sendGet(String uri) throws Exception {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("http://petstore.swagger.io/v2/pet/1")
                .build();
        Response response = client.newCall(request).execute();
        return response;

    }

    private static String doGet(String url, int id) {
        HttpRequest getReq = HttpRequest.newBuilder()
                .uri(URI.create(url + "/" + id))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = client.send(getReq, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }
    private static String doPost(String url){
        HttpResponse<String> response = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("pet.json")))
                    .header("Content-Type", "application/json")
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        }catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return response.body();
    }

}
