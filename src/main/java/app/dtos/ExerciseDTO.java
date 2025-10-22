package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExerciseDTO {
    private Long id;

    private String name;
    private String muscleGroup;
    private String instructions;
    private String externalId;
    private String equipment;

}
