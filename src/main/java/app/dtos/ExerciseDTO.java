package app.dtos;

import app.entities.Workout;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExerciseDTO {
    private int id;

    private String name;
    private String muscleGroup;
    private int reps;
    private double personalRecord;
    List<WorkoutDTO> workoutList;
}
