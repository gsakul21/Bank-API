package dev.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Service;

/*
 * This service handles the managment of a user in the service, essentially
 * CRUD operations for users. It is the utility to interact with the in-memory
 * object that applies the requests accordingly and shows the real time balances
 * of users.
 */

@Service
public class UserService {

    /*
     * This is the chosen in-memory object to retain information regarding users.
     * 
     * It is structured such that a user -> map of balances, where the map of
     * balances is such that it stores currency -> amount representing how much
     * money the user has in each currency.
     */
    private HashMap<String, HashMap<String, BigDecimal>> userMap;

    public UserService()
    {
        this.userMap = new HashMap<String, HashMap<String, BigDecimal>>();
    }


    /**
     * @param userId
     * @return HashMap<String, BigDecimal> the current balances of the user.
     * 
     * Given a userId tied to a specific user, retrieves the current balances linked
     * to the user. If the user doesn't exist, it creates one and starts with
     * an empty account (no money).
     */
    public HashMap<String, BigDecimal> getBalances(String userId)
    {
        if (userMap.containsKey(userId))
        {
            return userMap.get(userId);
        }

        HashMap<String, BigDecimal> balances = new HashMap<>();
        userMap.put(userId, balances);

        return balances;
    }

    /**
     * @param userId
     * @param newBalances
     * 
     * Given a userId pertaining to a specific user and a balance map, sets
     * the provided balance map to be the balance map of the user in the
     * in-memory object.
     */
    public void updateBalances(String userId, HashMap<String, BigDecimal> newBalances)
    {
        userMap.put(userId, newBalances);
    }

    /**
     * @param userId
     * 
     * Removes the (user, map of balances) pair from the in-memory object, essentially
     * deleting the user from the service.
     */
    public void deleteUser(String userId)
    {
        userMap.remove(userId);
    }

}
