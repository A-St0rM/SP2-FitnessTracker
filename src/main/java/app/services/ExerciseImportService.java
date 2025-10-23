package app.services;

import app.DAO.ExerciseDAO;
import app.DAO.ProgramDAO;
import app.dtos.ExternalExerciseDTO;
import app.entities.Exercise;
import app.entities.ProgramExercise;
import app.mapper.ExternalExerciseMapper;

public class ExerciseImportService {
    private final ExerciseDAO exerciseDAO;
    private final ProgramDAO programDAO;

    public ExerciseImportService(ExerciseDAO exerciseDAO, ProgramDAO programDAO) {
        this.exerciseDAO = exerciseDAO;
        this.programDAO = programDAO;
    }

    public Exercise findOrCreateFromExternal(ExternalExerciseDTO dto) {
        return exerciseDAO
                .findByNameMuscleEquipment(dto.name, dto.muscle, dto.equipment)
                .orElseGet(() -> exerciseDAO.create(ExternalExerciseMapper.toEntity(dto)));
    }

    public ProgramExercise addExerciseToProgram(long programId, Exercise ex, Integer sets, Integer reps) {
        // Lader ProgramDAO stå for transaktion og persist
        return programDAO.addExerciseToProgram(programId, ex, sets, reps);
    }
}
