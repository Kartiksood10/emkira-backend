package com.project.emkira.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    // Response when user logs in
    // return new LoginResponse()
    // .setToken(jwtToken) (jwtToken = jwtService.generateToken())
    // .setExpiresIn(jwtService.getExpirationTime());
    // {"token":"", expiresIn:""}

    private String token;
    private long expiresIn;
        // Getter for token
        public String getToken() {
            return token;
        }

        // Setter for token
        public LoginResponse setToken(String token) {
            this.token = token;
            return this;
        }

        // Getter for expiresIn
        public long getExpiresIn() {
            return expiresIn;
        }

        // Setter for expiresIn
        public LoginResponse setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
            return this;
        }
    }

