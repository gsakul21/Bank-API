package dev.codescreen.service;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private HashMap<String, HashMap<String, BigDecimal>> userMap;

    public UserService()
    {
        this.userMap = new HashMap<String, HashMap<String, BigDecimal>>();
    }

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

    public void updateBalances(String userId, HashMap<String, BigDecimal> newBalances)
    {
        userMap.put(userId, newBalances);
    }

    public void deleteUser(String userId)
    {
        userMap.remove(userId);
    }

}
