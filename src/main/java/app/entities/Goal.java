package app.entities;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String goalDescription;
    private boolean achieved;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

}
