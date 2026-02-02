package edu.wgu.d.emsbackend.auth;

import java.util.UUID;

public record AuthMeResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        String role
) {}
