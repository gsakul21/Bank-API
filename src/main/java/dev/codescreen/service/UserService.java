package dev.codescreen.service;

import org.springframework.stereotype.Service;

import dev.codescreen.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

}
