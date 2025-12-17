package app.mapper;

import app.dtos.UserDTO;
import app.entities.User;

public class UserMapper {

    public static UserDTO toDto(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setRoles(user.getRole());
        userDTO.setPassword(user.getPassword());
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    public static User toEntity(UserDTO userDTO){
        User user = new User();

        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());
        user.setRole(userDTO.getRoles());

        return user;
    }
}
