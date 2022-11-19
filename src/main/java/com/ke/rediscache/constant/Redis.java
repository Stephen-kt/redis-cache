package com.ke.rediscache.constant;

/**
 * @author stephen 2022/11/19 16:09
 */
public enum Redis {

    PREFIX("redis-cache:"),
    User("UserLogin::AllUser"),
    UserId("UserLogin::id-");

    private final String key;

    Redis(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
