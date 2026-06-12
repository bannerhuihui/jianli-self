package com.aitalentagent.api.common;

import java.util.UUID;

public final class Ids {

    private Ids() {
    }

    public static String next(String prefix) {
        return prefix + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }

    public static String requestId() {
        return next("req");
    }
}
