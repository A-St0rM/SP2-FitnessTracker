package app.services;

import app.entities.User;
import app.exceptions.ValidationException;
import app.security.interfaces.ISecurityDAO;
import app.enums.Role;
import app.dtos.UserDTO;

import java.util.Set;
import java.util.stream.Collectors;

public class SecurityService {
    private final ISecurityDAO securityDAO;

    public SecurityService(ISecurityDAO securityDAO) {
        this.securityDAO = securityDAO;
    }

    public UserDTO login(String username, String password) throws ValidationException {
        User verified = securityDAO.getVerifiedUser(username, password);

        Set<Role> roles = verified.getRole()
                .stream()
                .collect(Collectors.toSet());

        return new UserDTO(verified.getEmail(), roles);
    }


    public UserDTO register(String username, String password) {
        User createdUser = securityDAO.createUser(username, password);

        Set<Role> roles = createdUser.getRole()
                .stream()
                .collect(Collectors.toSet());

        return new UserDTO(createdUser.getEmail(), roles);
    }
/*
    public void assignRole(String username, String role) {
        securityDAO.addUserRole(username, role);
    }

 */

    public boolean userHasAllowedRole(UserDTO user, Set<String> allowedRoles) {
        return user.getRoles().stream()
                .anyMatch(role -> allowedRoles.contains(role));
    }

    public boolean isOpenEndpoint(Set<String> allowedRoles) {
        // If the endpoint is not protected with any roles:
        if (allowedRoles.isEmpty())
            return true;

        // 1. Get permitted roles and Check if the endpoint is open to all with the ANYONE role
        if (allowedRoles.contains("ANYONE")) {
            return true;
        }
        return false;
    }
}
