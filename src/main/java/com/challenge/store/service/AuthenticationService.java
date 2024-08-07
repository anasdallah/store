package com.challenge.store.service;

import static com.challenge.store.constant.RoleEnum.ROLE_CUSTOMER;

import java.util.HashSet;
import java.util.Set;

import com.challenge.store.constant.ApiErrors;
import com.challenge.store.constant.RoleEnum;
import com.challenge.store.dao.entity.Role;
import com.challenge.store.dao.entity.User;
import com.challenge.store.dao.repository.RoleRepository;
import com.challenge.store.dao.repository.UserRepository;
import com.challenge.store.dto.LoginRequest;
import com.challenge.store.dto.LoginResponse;
import com.challenge.store.dto.RegisterRequest;
import com.challenge.store.exception.ServiceException;
import com.challenge.store.security.jwt.JwtUtil;
import com.challenge.store.security.model.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    public LoginResponse login(LoginRequest loginRequest) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String jwt = jwtUtil.generateToken(userDetails);


        return new LoginResponse(jwt);
    }

    @Transactional
    public String register(RegisterRequest registerRequest) {

        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        Set<String> roles = registerRequest.getRoles();

        if (userRepository.findByUsername(username).isPresent()) {
            throw ServiceException.badRequest(ApiErrors.USERNAME_IS_EXISTS, username);
        }

        User user = new User(username, encoder.encode(password));

        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(String.valueOf(ROLE_CUSTOMER));
        }

        final Set<Role> daoRoles = new HashSet<>();

        roles.forEach(role -> {
            Role roleToAdd = roleRepository.findByName(RoleEnum.valueOf(role))
                    .orElseThrow(() -> ServiceException
                            .badRequest(ApiErrors.INVALID_FIELD_VALUE, "role: " + role));
            daoRoles.add(roleToAdd);
        });

        user.setRoles(daoRoles);
        return userRepository.save(user).getUsername();
    }
}
