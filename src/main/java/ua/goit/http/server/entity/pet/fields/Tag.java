package ua.goit.http.server.entity.pet.fields;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Tag{
    private int id;
    private String name;
}
