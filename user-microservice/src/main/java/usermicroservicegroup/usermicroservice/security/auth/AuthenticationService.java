package usermicroservicegroup.usermicroservice.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import usermicroservicegroup.usermicroservice.entities.User;
import usermicroservicegroup.usermicroservice.repositories.UserRepository;
import usermicroservicegroup.usermicroservice.security.config.JwtService;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest) {
        var user = User.builder()
                .name(registerRequest.getName())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role("client")
                .build();

        java.util.Map<String, Object> extraRoleClaim = new HashMap<>();
        extraRoleClaim.put("role", user.getRole());
        extraRoleClaim.put("name", user.getName());
        extraRoleClaim.put("id", user.getId());

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(extraRoleClaim, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getName(), request.getPassword())
        );

        var user = userRepository.findFirstByName(request.getName());

        java.util.Map<String, Object> extraRoleClaim = new HashMap<>();
        extraRoleClaim.put("role", user.getRole());
        extraRoleClaim.put("name", user.getName());
        extraRoleClaim.put("id", user.getId());

        var jwtToken = jwtService.generateToken(extraRoleClaim, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
