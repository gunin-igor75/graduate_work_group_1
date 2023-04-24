package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

  private final UserDetailsService userDetailsService;
  private final PasswordEncoder encoder;
  private final UserRepository userRepository;

  public AuthServiceImpl(@Qualifier("userDetailsServiceImp") UserDetailsService userDetailsService,
                         PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.userDetailsService = userDetailsService;
    this.encoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean login(String userName, String password) {
    Optional<ru.skypro.homework.model.User> optionalUser = userRepository.findByEmail(userName);
    if (optionalUser.isEmpty()) {
      return false;
    }
    try {
      UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
      return encoder.matches(password, userDetails.getPassword());
    } catch (UsernameNotFoundException e) {
      log.warn("Wrong username or password");
    }
    return false;
  }

  @Override
  public boolean register(RegisterReq registerReq, Role role) {
    Optional<ru.skypro.homework.model.User> optionalUser = userRepository.findByEmail(registerReq.getUsername());
    if (optionalUser.isPresent()) {
      return false;
    }
    ru.skypro.homework.model.User user = User.from(registerReq);
    user.setRole(role);
    userRepository.save(user);
    return true;
  }
}
