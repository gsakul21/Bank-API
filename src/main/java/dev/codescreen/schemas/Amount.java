package dev.codescreen.schemas;

/*
 * As defined in the provided service specification file, object representing the 
 * Amount schema. Contains key details for a load or authorization request pertaining
 * to the asset of the request.
 */
public class Amount {

    // The amount being requested as well as its currency.
    private String amount;
    private String currency;

    /*
     * Enumerated type specifying what type of transaction is requested. See
     * {@link DebitCredit} for more details. 
     */
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
