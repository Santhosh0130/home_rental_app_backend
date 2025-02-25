package com.example.home_rental_app1.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.home_rental_app1.jwtConfig.JwtAuthFilter;
import com.example.home_rental_app1.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //For CORS Configuration
        // CorsConfiguration configuration = new CorsConfiguration();
        // configuration.addAllowedOrigin("http://localhost:5173");
        // configuration.addAllowedMethod("*");
        // configuration.addAllowedHeader("*");
        // configuration.setAllowCredentials(true);

        // UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // source.registerCorsConfiguration("/**", configuration);

        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(configurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    // @Bean
    // public CorsConfigurationSource configurationSource() {
    //     // List<String> orgins = new ArrayList<>();
    //     // orgins.add("http://localhost:5173");
    //     // orgins.add("http://192.168.197.81:5173");
    //     CorsConfiguration configuration = new CorsConfiguration();
    //     // configuration.addAllowedOrigin("http://localhost:5173");
    //     // // configuration.setAllowedOrigins(orgins);
    //     configuration.addAllowedOrigin("https://home-rental-app-frontend.onrender.com");
    //     configuration.addAllowedMethod("*");
    //     configuration.addAllowedHeader("*");
    //     configuration.setAllowCredentials(true);

    //     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    //     source.registerCorsConfiguration("/**", configuration);
    //     return source;
    // }

    @Bean
    public CorsConfigurationSource configurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedOrigins(List.of(
            // "https://home-rental-app-frontend.onrender.com",
            "https://homerentalappfrontend.onrender.com/",
            "https://home-rental-app-frontend.vercel.app/",
            "https://home-rental-app-frontend-g2vcfclfd-santhoshs-projects-03bf8968.vercel.app/",
            "https://home-rental-app-frontend-g2vcfclfd-santhoshs-projects-03bf8968.vercel.app/"
        ));
        config.setExposedHeaders(List.of(HttpHeaders.SET_COOKIE));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
