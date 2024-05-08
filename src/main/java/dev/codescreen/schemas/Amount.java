package dev.codescreen.schemas;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
/*
 * As defined in the provided service specification file, object representing the 
 * Amount schema. Contains key details for a load or authorization request pertaining
 * to the asset of the request.
 */
public class Amount {

    // The amount being requested as well as its currency.
    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String amount;

    @NotNull
    @NotEmpty
    @Size(min = 1)
    private String currency;

    /*
     * Enumerated type specifying what type of transaction is requested. See
     * {@link DebitCredit} for more details. 
     */
    @NotNull
    private DebitCredit debitOrCredit;

    public Amount(String amount, String currency, DebitCredit debitOrCredit)
    {
        this.amount = amount;
        this.currency = currency;
        this.debitOrCredit = debitOrCredit;
    }

    // Various Getters + Setters for different attributes of the schema.

    public String getAmount()
    {
        return amount;
    }

    public String getCurrency()
    {
        return currency;
    }

    public DebitCredit getDebitOrCredit()
    {
        return debitOrCredit;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public void setCurrency(String currency)
    {
        this.currency = currency;
    }

    public void setDebitOrCredit(DebitCredit debitOrCredit)
    {
        this.debitOrCredit = debitOrCredit;
    }
}
