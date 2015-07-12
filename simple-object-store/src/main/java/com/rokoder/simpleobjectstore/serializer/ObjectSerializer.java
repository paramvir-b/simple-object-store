package com.rokoder.simpleobjectstore.serializer;

/**
 * Object serializer which converts object instances to @{link byte} arrays and vice versa
 */
public interface ObjectSerializer {

    /**
     * Converts class instances which extends {@link Object} to byte array
     *
     * @param obj Instance of java class
     * @return byte array
     */
    byte[] toBytes(Object obj);

    /**
     * Converts bytes to instance of java class
     *
     * @param bytes     Byte array containing serialized java class instance
     * @param classType Class type of the object to be returned
     * @param <T>       Generic type of the object to be returned
     * @return Intance of java class
     */
    <T> T fromBytes(byte[] bytes, Class<T> classType);
}
