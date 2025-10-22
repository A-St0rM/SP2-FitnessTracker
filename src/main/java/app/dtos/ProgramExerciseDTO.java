package app.dtos;

import app.entities.Exercise;
import app.entities.WorkoutProgram;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class ProgramExerciseDTO {

        private Long id;
        private Long programId;
        private Long exerciseId;
        private int orderIndex;

        private Integer sets;
        private Integer reps;
        private String notes;

}
