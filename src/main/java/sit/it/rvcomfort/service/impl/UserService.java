package sit.it.rvcomfort.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.mapper.UserRegistrationMapper;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.UserRegistrationRequest;
import sit.it.rvcomfort.repository.UserJpaRepository;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("[UserService] load user {}", username);
        User user = userJpaRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.error("[UserService] user {} not found in database.", username);
            user = userJpaRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("")); //TODO: add message when controller advice finish
        }
        return user;
    }

    public User saveUser(UserRegistrationRequest user) {
        // STEP 1: Validation (Duplicate Data)
        userRegistrationValidate(user);

        // STEP 2: Encrypt Password
        log.info("[UserService] encoding user {}'s password", user.getUsername());
        String password = passwordEncoder.encode(user.getPassword());

        // STEP 3: Map to User Entity
        log.info("[UserService] mapped request of user {} to entity", user.getUsername());
        User registrationUser = UserRegistrationMapper.INSTANCE.from(user, password);

        // STEP 4: Save user
        log.info("[UserService] save user {} to database", user.getUsername());
        return userJpaRepository.save(registrationUser);
    }

    public User retrieveUser(String searchValue) {
        // STEP 1:  Return user from database but throw exception if user not found
        log.info("[UserService] retrieve user by {}", searchValue);
        return userJpaRepository.findUserByGivenValue(searchValue)
                .orElseThrow(() -> new RuntimeException("")); // TODO Change exception
    }

    private void userRegistrationValidate(UserRegistrationRequest user) {
        log.info("[UserService] start validate user {}", user.getUsername());
        User duplicateUser = userJpaRepository.findByUsername(user.getUsername()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] user with username {} is already exists", user.getUsername());
            throw new RuntimeException("Exception"); // TODO Change exception
        }

        duplicateUser = userJpaRepository.findByEmail(user.getEmail()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] user with email {} is already exists", user.getEmail());
            throw new RuntimeException("Exception"); // TODO Change exception
        }

        duplicateUser = userJpaRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] user name: {} {} is already exists.", user.getFirstName(), user.getLastName());
            throw new RuntimeException("Exception"); // TODO Change exception
        }
    }
}
