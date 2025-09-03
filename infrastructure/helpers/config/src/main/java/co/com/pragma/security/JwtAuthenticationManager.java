package co.com.pragma.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationManager(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
  
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();

        try {
            Claims claims = jwtUtil.getClaimsFromToken(token);

            //Extrae los roles del token
            String roles = (String) claims.get("roles");
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (roles != null) {
                Arrays.stream(roles.split(","))
                      .map(String::trim)
                      .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                      .forEach(authorities::add);
            }

            return Mono.just(new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, authorities
            ));
        } catch (Exception e) {
            return Mono.empty(); // token inválido
        }
    }
    
    
}
