package dev.schemas;

/*
 * Enumerated type to represent the status of a processed authorization request.
 * Two options, both mapped to their respective ordinals.
 */
public enum ResponseCode {
    APPROVED,
    DECLINED
}
