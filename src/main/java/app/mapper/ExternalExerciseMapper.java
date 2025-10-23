package app.mapper;

import app.dtos.ExternalExerciseDTO;
import app.entities.Exercise;

public class ExternalExerciseMapper {
    public static Exercise toEntity(ExternalExerciseDTO ext) {
        return Exercise.builder()
                .name(ext.name)
                .muscleGroup(ext.muscle)
                .equipment(ext.equipment)
                .instructions(ext.instructions)
                .build();
    }
}
