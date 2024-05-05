package dev.codescreen.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import dev.codescreen.domain.User;
import dev.codescreen.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public User findUser(String userId)
    {
        try
        {
            return userRepository.getOne(userId);
        }
        catch(EntityNotFoundException e)
        {
            return createUser(userId);
        }
    }

    public User createUser(String userId)
    {
        User new_user = new User(userId);
        userRepository.saveAndFlush(new_user);
        return new_user;
    }

    public void updateUser(User updatedUser)
    {
        userRepository.saveAndFlush(updatedUser);
    }

    public void deleteUser(User user)
    {
        ArrayList<User> toDelete = new ArrayList<>();
        toDelete.add(user);
        userRepository.deleteInBatch(toDelete);
    }

}
