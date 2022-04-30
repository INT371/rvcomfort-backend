package sit.it.rvcomfort.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.it.rvcomfort.exception.list.DuplicateDataException;
import sit.it.rvcomfort.exception.list.NotFoundException;
import sit.it.rvcomfort.mapper.UserMapper;
import sit.it.rvcomfort.model.entity.User;
import sit.it.rvcomfort.model.request.UserRegistrationRequest;
import sit.it.rvcomfort.repository.UserJpaRepository;

import java.text.MessageFormat;

import static sit.it.rvcomfort.exception.response.ExceptionResponse.ERROR_CODE.*;

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
            user = userJpaRepository.findByEmail(username)
                    .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND,
                            MessageFormat.format("not have user with username {0} in database.", username)));
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
        User registrationUser = UserMapper.INSTANCE.from(user, password);

        // STEP 4: Save user
        log.info("[UserService] save user {} to database", user.getUsername());
        return userJpaRepository.save(registrationUser);
    }

    public User retrieveUser(String searchValue) {
        // STEP 1:  Return user from database but throw exception if user not found
        log.info("[UserService] retrieve user by {}", searchValue);
        return userJpaRepository.findUserByGivenValue(searchValue)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND,
                        MessageFormat.format("user with username {0} not found in database.", searchValue)));
    }

    private void userRegistrationValidate(UserRegistrationRequest user) {
        log.info("[UserService] start validate user {}", user.getUsername());
        User duplicateUser = userJpaRepository.findByUsername(user.getUsername()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] user with username {} is already exists", user.getUsername());
            throw new DuplicateDataException(DUPLICATE_USERNAME,
                    MessageFormat.format("User with username {0} is already exists", user.getUsername()));
        }

        duplicateUser = userJpaRepository.findByEmail(user.getEmail()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] user with email {} is already exists", user.getEmail());
            throw new DuplicateDataException(DUPLICATE_EMAIL,
                    MessageFormat.format("User with email {0} is already exists", user.getEmail()));
        }

        duplicateUser = userJpaRepository.findByFirstNameAndLastName(user.getFirstName(), user.getLastName()).orElse(null);
        if (duplicateUser != null) {
            log.error("[UserService] User with name: {} {} is already exists.", user.getFirstName(), user.getLastName());
            throw new DuplicateDataException(DUPLICATE_FULL_NAME,
                    MessageFormat.format("User with name {0} {1} is already exists", user.getFirstName(), user.getLastName()));
        }
    }
}
