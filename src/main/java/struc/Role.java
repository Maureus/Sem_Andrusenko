package struc;

public enum Role {
    GUEST(1, "GUEST"),
    STUDENT(2, "Student"),
    TEACHER(3, "Teacher"),
    ADMINISTRATOR(4, "Administrator");

    private int roleId;
    private String roleName;

    Role(final int roleId, final String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }


    public static Role getRole(final int index) {
        for (int i = 0; i < Role.values().length; i++) {
            if (index == Role.values()[i].getRoleId()) {
                return Role.values()[i];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return roleName;
    }
}
