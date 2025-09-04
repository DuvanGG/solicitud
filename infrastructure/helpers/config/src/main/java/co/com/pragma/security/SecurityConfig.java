package co.com.pragma.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public ReactiveAuthenticationManager authenticationManager() {
        return new JwtAuthenticationManager(jwtUtil);
    }

    @Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http,
			JwtAuthenticationManager authManager) {
	    AuthenticationWebFilter authenticationWebFilter =
	            new AuthenticationWebFilter(authManager);

	    authenticationWebFilter.setServerAuthenticationConverter(exchange -> {
	        String token = extractToken(exchange);
	        if (token != null) {
	            return Mono.just(new UsernamePasswordAuthenticationToken(null, token));
	        }
	        return Mono.empty();
	    });

	    return http
	            .csrf(ServerHttpSecurity.CsrfSpec::disable)
	            .authorizeExchange(exchange -> exchange
	                    .pathMatchers("/api/v1/login").permitAll()
	                    .pathMatchers(HttpMethod.GET, "/api/v1/solicitud/**").hasAnyRole("ASESOR")
	                    .pathMatchers(HttpMethod.POST, "/api/v1/solicitud/**").hasAnyRole("CLIENTE")
	                    .pathMatchers(HttpMethod.GET, "/api/v2/solicitud/**").hasAnyRole("ASESOR")
	                    .anyExchange().authenticated()
	            )
				.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
	            .build();
    
	}

	private String extractToken(ServerWebExchange exchange) {
	    String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        return authHeader.substring(7);
	    }
	    return null;
	}
}
