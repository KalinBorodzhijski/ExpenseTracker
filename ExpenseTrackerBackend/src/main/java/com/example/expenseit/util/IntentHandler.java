package com.example.expenseit.util;

import java.util.Map;

@FunctionalInterface
public
interface IntentHandler {
    String handle(Map<String, String> entities, int clientId);
}
