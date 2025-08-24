package com.nullbyte.overseer.util;

import java.util.Set;

public class Util {
    public static <T> boolean toggle(T value, Set<T> set) {
        if (set.contains(value)) {
            set.remove(value);
            return false;
        } else {
            set.add(value);
            return true;
        }
    }
}
