package app.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity @Table(name="workout_programs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkoutProgram {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    private String description;

    @OneToMany(mappedBy="program", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    @OrderBy("orderIndex ASC")
    private List<ProgramExercise> items = new ArrayList<>();
}
