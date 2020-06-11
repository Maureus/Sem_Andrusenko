package struc;

public class User {
    private int userID;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String email;
    private String telephone;
    private int avatarID;
    private int roleId;

    public User(){
    }

    public User(String name, String surname, String login, String password, int roleId) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.roleId = roleId;
    }

    public User(int userID, String name, String surname, String login, String password, String email, String telephone, int roleId) {
        this.userID = userID;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.telephone = telephone;
        this.roleId = roleId;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public int getAvatar() {
        return avatarID;
    }

    public void setAvatar(int avatarID) {
        this.avatarID = avatarID;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return name + " " +surname;
    }
}
