package app.dtos;

import app.entities.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WorkoutDTO {
    private int id;
    private int userId;
    private String name;
    private String description;
    private List<ExerciseDTO> exercises;
}









