package app.dtos;

import app.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDTO {

    private Long id;
    private String email;
    private String password;
    private Set<Role> roles;

    public UserDTO(String email, Set<Role> roles) {
        this.email = email;
        this.roles = roles;
    }
}
