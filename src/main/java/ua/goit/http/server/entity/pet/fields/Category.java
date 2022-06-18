package ua.goit.http.server.entity.pet.fields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public
class Category{
    private int id;
    private String name;

}
