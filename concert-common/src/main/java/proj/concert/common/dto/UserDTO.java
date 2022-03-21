package proj.concert.common.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * DTO class to represent users.
 * <p>
 * A UserDTO describes a user in terms of:
 * username  the user's unique username.
 * password  the user's password.
 */
public class UserDTO {

    private String username;
    private String password;

    protected UserDTO() {
    }

    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return new EqualsBuilder()
                .append(username, userDTO.username)
                .append(password, userDTO.password)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(username)
                .append(password)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
