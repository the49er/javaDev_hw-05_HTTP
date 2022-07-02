package ua.goit.http.server.entity.pet.fields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Category{
    private long id;
    private String name;
}
