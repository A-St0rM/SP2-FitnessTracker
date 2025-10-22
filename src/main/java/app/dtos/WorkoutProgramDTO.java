package app.dtos;

import app.entities.ProgramExercise;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkoutProgramDTO {

    private Long id;
    private String name;
    private String description;
    private List<ProgramExerciseDTO> items = new ArrayList<>();

}
