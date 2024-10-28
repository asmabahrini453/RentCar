package com.stage.rentcar.User.auth;

import com.stage.rentcar.User.token.Token;
import com.stage.rentcar.User.token.TokenRepository;
import com.stage.rentcar.User.User;
import com.stage.rentcar.User.UserRepository;
import com.stage.rentcar.User.email.EmailService;
import com.stage.rentcar.User.email.EmailTemplateName;
import com.stage.rentcar.role.Role;
import com.stage.rentcar.role.RoleFactory;
import com.stage.rentcar.role.RoleRepository;
import com.stage.rentcar.security.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;

import javax.management.relation.RoleNotFoundException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository ;
    private final PasswordEncoder passwordEncoder ;
    private final UserRepository userRepository ;
    private final TokenRepository tokenRepository ;
    private final EmailService emailService ;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    private final AuthenticationManager authenticationManager  ; //it is a spring interface
    // we configured the authenticationManager in BeansConfig
    private final JwtService jwtService ;


    @Autowired
    private RoleFactory roleFactory;
    public void register(RegistrationRequest request) throws MessagingException, RoleNotFoundException {

        var user = User.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(determineRoles(request.getRoles()))
                .archive(false)
                .cin(request.getCin())
                .adresse(request.getAdresse())
                .tel(request.getTel())
                .dateNaiss(request.getDateNaiss())
                .genre(request.getGenre())
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }
    private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(roleFactory.getInstance("client"));
        } else {
            for (String role : strRoles) {
                roles.add(roleFactory.getInstance(role));
            }
        }
        return roles;
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newToken,
                "Activation de compte"
        );
    }

    private String generateAndSaveActivationToken(User user) {
        //generate token
        String generatedToken= generateActivationCode(6); // 6 digits long token-> activation code
        var token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken ;
    }

    private String generateActivationCode(int length) {
        String chars = "0123456789" ;
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for(int i=0 ; i<length ; i++){
            int randomIndex= secureRandom.nextInt(chars.length());
            codeBuilder.append(chars.charAt(randomIndex));
        }
        return codeBuilder.toString() ;
    }



    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user = (User) auth.getPrincipal();
        var claims = new HashMap<String, Object>();
        claims.put("fullName", user.fullName());
        var jwtToken = jwtService.generateToken(claims, user);
        var roles = user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList());
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .roles(roles)
                .userId(user.getId())
                .build();
    }
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token).orElseThrow(()->new RuntimeException("Invalid token"));
       //if token is expired
        if(LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation token has expired. A new token has been sent to the same email address");
        }
        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(()->new UsernameNotFoundException("User not found"));
        user.setEnabled(true);

        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);

    }
}
