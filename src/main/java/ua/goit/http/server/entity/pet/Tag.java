package ua.goit.http.server.entity.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Tag{
    private long id;
    private String name;
}
