package app.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises",
        indexes = {@Index(name = "idx_exercise_name", columnList = "name")})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String muscleGroup;
    private String equipment;

    @Column(unique = true, nullable = false)
    private String externalId;

    @Column(length = 2000)
    private String instructions;  // beskrivelse fra API
}

