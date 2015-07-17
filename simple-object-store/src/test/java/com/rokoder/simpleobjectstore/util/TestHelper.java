package com.rokoder.simpleobjectstore.util;

import java.lang.reflect.Constructor;

public class TestHelper {

    private TestHelper() {
        // Util class so no constructor
    }

    /**
     * Call this method from a test case and you are GOOD. This will make sure
     * you can have code coverage for private void constructors. This is useful
     * for utility classes who have only static methods and noone is going to
     * call the private constructor
     *
     * @param classType
     *            Class whose private consturcor needs to be called
     */
    public static void testPrivateVoidConstructor(Class<?> classType) {
        try {
            Constructor<?>[] cons = classType.getDeclaredConstructors();
            cons[0].setAccessible(true);
            cons[0].newInstance((Object[]) null);
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to call void constructor for class:" + classType, e);
        }
    }

}
