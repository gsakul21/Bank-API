package dev.codescreen.domain;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {

    @Id
    private String userId;

    @ElementCollection
    private Map<String, BigDecimal> balances;

    public User() {}

    public User(String userId)
    {
        this.userId = userId;
        this.balances = new HashMap<String, BigDecimal>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, BigDecimal> getBalances() {
        return balances;
    }

    public void setBalance(Map<String, BigDecimal> balances) {
        this.balances = balances;
    }

}
