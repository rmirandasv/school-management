package com.rmiranda.schoolmanagement.service;

import java.util.Date;
import java.util.Optional;


import com.rmiranda.schoolmanagement.model.entity.User;
import com.rmiranda.schoolmanagement.model.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public void addUSer(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public Page<User> getAllUSers(Pageable pageable) {
        return userRepository.findByDeletedAtIsNull(pageable);
    }

    @Override
    public void updateUser(User user) {
        User dbUser = userRepository.getOne(user.getId());

        dbUser.setName(user.getName());
        dbUser.setLastName(user.getLastName());
        dbUser.setBirthday(user.getBirthday());
        dbUser.setEmail(user.getEmail());
        dbUser.setRoles(user.getRoles());
        dbUser.setActive(user.isActive());

        userRepository.save(dbUser);
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void deleteUserById(long id) {
        User user = userRepository.getOne(id);

        user.setDeletedAt(new Date());
        user.setActive(false);

        userRepository.save(user);

    }

    @Override
    public void resetPassword(long id, String password) {
        User user = userRepository.getOne(id);

        user.setPassword(encoder.encode(password));

        userRepository.save(user);
    }

    @Override
    public void addFailedAttempt(String username) {
        User user = userRepository.findUSerByUsername(username);

        if (user == null) {
            return;
        }

        if (user.getLoginAttempt() >= 4) {
            user.setLocked(true);
        }  else {
            user.setLoginAttempt(user.getLoginAttempt() + 1);
        }

        userRepository.save(user);
    }

    @Override
    public void unlockUser(User user) {
        user.setLocked(false);
        user.setLoginAttempt(0);
        userRepository.save(user);
    }

    @Override
    public void unlockUser(long id) {
        User user = userRepository.getOne(id);
        unlockUser(user);
    }

    @Override
    public void resetFailedAttempts(String username) {
        User user = userRepository.findUSerByUsername(username);

        user.setLoginAttempt(0);
        userRepository.save(user);
    }

}