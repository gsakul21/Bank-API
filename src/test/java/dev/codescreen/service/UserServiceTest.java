package dev.codescreen.service;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

@Autowired
private UserService userService;

/*
 * Test to see if a user is made correctly and with the right default balances.
 */

@Test
void createUserTest() throws Exception
{
    HashMap<String, BigDecimal> balances = userService.getBalances("test");
    assertTrue(balances.isEmpty());
}

/*
 * Testing that balances are updated properly through service.
 */

@Test
void updateUserTest()
{
    HashMap<String, BigDecimal> balances = userService.getBalances("test2");

    balances.put("USD", new BigDecimal(50));
    balances.put("INR", new BigDecimal(75));

    userService.updateBalances("test2", balances);

    HashMap<String, BigDecimal> balancesUpdated = userService.getBalances("test2");

    assertTrue(balances.equals(balancesUpdated));
}

/*
 * Test to ensure that user's are properly deleted from the in-memory store
 */

@Test
void deleteUserTest()
{
    HashMap<String, BigDecimal> balances = userService.getBalances("test2");

    balances.put("USD", new BigDecimal(50));
    balances.put("INR", new BigDecimal(75));

    userService.deleteUser("test2");

    HashMap<String, BigDecimal> balancesNew = userService.getBalances("test2");

    /*
     * Check like this since if we deleted user and getBalances again, it won't
     * be in the map and will be added once again as an empty map thus being different 
     */

    assertFalse(balances.equals(balancesNew));
}

}
