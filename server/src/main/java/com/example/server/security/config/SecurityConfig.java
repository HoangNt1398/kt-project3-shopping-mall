package com.example.server.security.config;

import com.example.server.security.filter.JwtAuthenticationFilter;
import com.example.server.security.filter.JwtVerificationFilter;
import com.example.server.security.handler.*;
import com.example.server.security.jwt.JwtTokenizer;
import com.example.server.security.util.CustomAuthorityUtils;
import com.example.server.security.userdetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils customAuthorityUtils;

    @Autowired
    @Lazy
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private final PasswordEncoder passwordEncoder; // Inject PasswordEncoder từ PasswordEncoderConfig

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        log.info("AuthenticationManager đã được khởi tạo: {}", authenticationManager);

        // Định nghĩa JwtAuthenticationFilter
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, jwtTokenizer);
        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new UserAuthenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(new UserAuthenticationFailureHandler());

        // Định nghĩa JwtVerificationFilter
        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer, customAuthorityUtils);

        http
                // Disable CSRF vì ứng dụng sử dụng JWT
                .csrf(csrf -> csrf.disable())

                // Cấu hình CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Quản lý session không trạng thái
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Cấu hình các yêu cầu HTTP được phép truy cập
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.PATCH, "/users/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasRole("BUYER")
                        .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                        .requestMatchers("/orders/**").hasRole("BUYER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/sellers/**").hasRole("SELLER")
                        .requestMatchers(HttpMethod.GET, "/sellers/**").hasRole("SELLER")
                        .requestMatchers("/carts/**").hasRole("BUYER")
                        .anyRequest().permitAll()
                )

                // Cấu hình xử lý ngoại lệ
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new UserAuthenticationEntryPoint())
                        .accessDeniedHandler(new UserAccessDeniedHandler())
                )

                // Cấu hình logout
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .logoutSuccessHandler(new CustomLogoutHandler())
                )

                // Cấu hình AuthenticationProvider
                .authenticationProvider(authenticationProvider())

                // Thêm các filter JWT
                .addFilter(jwtAuthenticationFilter)
                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);

        return http.build();
    }

    // Định nghĩa AuthenticationManager sử dụng AuthenticationConfiguration
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Định nghĩa DaoAuthenticationProvider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsServiceImpl);
        authProvider.setPasswordEncoder(passwordEncoder); // Sử dụng PasswordEncoder đã inject
        return authProvider;
    }

    // Định nghĩa cấu hình CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Thay đổi theo nhu cầu
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PATCH", "DELETE", "PUT"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Location", "Refresh", "Access-Control-Allow-Origin"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
