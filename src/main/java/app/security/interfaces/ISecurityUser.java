package app.security.interfaces;


public interface ISecurityUser {
    boolean verifyPassword(String pw);
    //void addRole(Role role);
    void removeRole(String role);
}
