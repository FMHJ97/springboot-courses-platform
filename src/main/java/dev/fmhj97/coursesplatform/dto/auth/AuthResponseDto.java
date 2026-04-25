package dev.fmhj97.coursesplatform.dto.auth;

public class AuthResponseDto {

    private final String token;

    /**
     * Constructor
     * @param token
     */
    public AuthResponseDto(String token) {
        this.token = token;
    }

    // Getter

    public String getToken() {
        return token;
    }
}
