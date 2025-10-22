package app.mapper;

import app.dtos.ProgramExerciseDTO;
import app.entities.Exercise;
import app.entities.ProgramExercise;
import app.entities.WorkoutProgram;

public class ProgramExerciseMapper {

    public static ProgramExerciseDTO toDTO(ProgramExercise programExercise) {
        ProgramExerciseDTO dto = new ProgramExerciseDTO();
        dto.setId(programExercise.getId());
        dto.setProgramId(programExercise.getProgram().getId());
        dto.setExerciseId(programExercise.getExercise().getId());
        dto.setOrderIndex(programExercise.getOrderIndex());
        dto.setSets(programExercise.getSets());
        dto.setReps(programExercise.getReps());
        dto.setNotes(programExercise.getNotes());
        return dto;
    }

    public static ProgramExercise toEntity(ProgramExerciseDTO dto) {
        ProgramExercise entity = new ProgramExercise();
        entity.setId(dto.getId());
        entity.setOrderIndex(dto.getOrderIndex());
        entity.setSets(dto.getSets());
        entity.setReps(dto.getReps());
        entity.setNotes(dto.getNotes());
        //Program og exercise indsættes i Controller
        return entity;
    }

}
