package ru.skypro.homework;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.entity.Users;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

/**
 * Класс настройки безопасности приложения
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    /** Пути разрешенные без аутентификации */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/image/**"
    };

    /**
     * Цпочка фильтров определяющих конфигурацию аутентификации пользователя
     * @param http - сущность для настройки параметров безопасности для http запросов
     * @return - сущность состоящая из цепочки фильтров, определяющих безопаснсоть
     * @throws Exception - исключение при не удачном создании цепочки фильтров
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests(
                        (authorization) ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET, "/ads")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Бин кодера {@code BCryptPasswordEncoder} с силой 12
     * @return - сущность которая кодирует и декодирует пароли
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Поиск пользователя в БД по email
     * @param userRepository - репозиторий, предназначенн для выполнения операций в БД
     * @return - сущность, кторая при удачном поиске пользователя попадет в {@code  Authentication}
     * @throws UsernameNotFoundException - отсутствие пользователя в БД
     */
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            Optional<Users> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
            throw new UsernameNotFoundException("User ‘" + email + "’ not found");
        };
    }
}
