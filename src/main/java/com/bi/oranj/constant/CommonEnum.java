package com.bi.oranj.constant;

/**
 * Created by harshavardhanpatil on 5/30/17.
 */
public enum CommonEnum {

    SUCCESS("SUCCESS"),
    START_SECOND_OF_THE_DAY("00:00:00"),
    LAST_SECOND_OF_THE_DAY("23:59:59"),
    SPACE(" ");

    private final String value;

    private CommonEnum(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
