package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name="program_exercises",
        uniqueConstraints = @UniqueConstraint(name="uk_program_order", columnNames={"program_id","order_index"}))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProgramExercise {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional=false) @JoinColumn(name="program_id")
    private WorkoutProgram program;

    @ManyToOne(optional=false) @JoinColumn(name="exercise_id")
    private Exercise exercise;

    @Column(name="order_index", nullable=false)
    private int orderIndex;

    private Integer sets;
    private Integer reps;
    private String notes;
}

