package dev.codescreen.domain;

/*
 * Custom enumerated type created to help indicate and provide more information
 * about the events stored in the database. Four entries that have values
 * corresponding to their ordinals.
 */
public enum TransactionStatus {
    AUTH_SUCCESS,
    AUTH_FAIL,
    LOAD_SUCCESS,
    LOAD_FAIL 
}
