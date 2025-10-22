package app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.mindrot.jbcrypt.BCrypt;

import app.enums.Role;

import java.util.HashSet;
import java.util.Set;

@Entity @Table(name="users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> role = new HashSet<>();

    public User(String email, String password){
        this.email = email;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
        this.role.add(Role.USER);
        }

        public User(String email, String password, Role role){
            this.email = email;
            this.password = BCrypt.hashpw(password, BCrypt.gensalt(10));
            this.role.add(role);
        }

        public boolean checkPassword(String candidate){
        if(BCrypt.checkpw(candidate,password)){
            System.out.println("Passwords matches");
            return true;
        } else {
            System.out.println("Passwords doesn't match!");
            return false;
        }
        }


}
