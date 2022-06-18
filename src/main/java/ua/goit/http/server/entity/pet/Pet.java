package ua.goit.http.server.entity.pet;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import ua.goit.http.server.entity.pet.fields.Category;
import ua.goit.http.server.entity.pet.fields.PetStatus;
import ua.goit.http.server.entity.pet.fields.Tag;

import java.util.List;

@Data
@RequiredArgsConstructor
@ToString
public class Pet {
    private int id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Tag> tags;
    private PetStatus status;
}
