package app.mapper;

import app.dtos.UserDTO;
import app.entities.User;

public class UserMapper {


    public UserDTO toDto(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());

        return userDTO;
    }

    public User toEntity(UserDTO userDTO){
        User user = new User();

        user.setEmail(userDTO.getEmail());
        user.setId(userDTO.getId());

        return user;
    }
}
