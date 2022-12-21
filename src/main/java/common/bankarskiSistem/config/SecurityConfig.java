package common.bankarskiSistem.config;


import common.bankarskiSistem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;

    private final UserRepository userRepository;

    private final static String[] AUTH_WHITELIST = {
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/**/auth/**",
            "/swagger-ui/index.html/*",
            "/v3/api-docs/",
            "/v3/**",
            "/swagger-ui/",
            "/swagger-resources/",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/users/update").hasAuthority("USER_ADMIN")
                .antMatchers("/users/add").hasAuthority("USER_ADMIN")
                .antMatchers("/users/delete").hasAuthority("USER_ADMIN")
                .antMatchers("/users").hasAuthority("USER_ADMIN")
                .antMatchers("/bank/add").hasAnyAuthority("BANK_ADMIN", "USER_ADMIN")
                .antMatchers("/bank/update").hasAnyAuthority("BANK_ADMIN", "USER_ADMIN")
                .antMatchers("/bank/delete").hasAnyAuthority("BANK_ADMIN", "USER_ADMIN")
                .antMatchers("/bank/get").hasAnyAuthority("BANK_ADMIN", "USER_ADMIN")
                .antMatchers("/users/addBankAccount").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/deleteBankAccount").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/deleteAllBankAccounts").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/getBankAccount").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/getBankAccountBalance").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/getAllBankAccounts").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/getAllBalance").hasAuthority("ACCOUNT_ADMIN")
                .antMatchers("/users/payOut").hasAuthority("USER_PAYMENT")
                .antMatchers("/users/payIn").hasAuthority("USER_PAYMENT")
                .antMatchers("/users/transfer").hasAuthority("USER_TRANSFER")
                .antMatchers("/users/**").authenticated()
                .antMatchers("/bank/**").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return personalID -> userRepository.findByPersonalId(personalID)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
