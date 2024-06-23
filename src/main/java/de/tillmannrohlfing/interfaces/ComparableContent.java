package de.tillmannrohlfing.interfaces;

/**
 * The ComparableContent interface defines a contract for comparing objects of a generic type ContentType.
 * It provides three default methods: isLess, isEqual, and isGreater.
 * These methods should be overridden by the implementing class to provide the correct comparison logic.
 *
 * @param <ContentType> the type of objects that this object may be compared to
 */
public interface ComparableContent <ContentType>{

    /**
     * Checks if this object is less than the specified object.
     * The default implementation always returns false and should be overridden by the implementing class.
     *
     * @param pContent the object to be compared with
     * @return true if this object is less than the specified object, false otherwise
     */
    public default boolean isLess(ContentType pContent){
        return false;
    }

    /**
     * Checks if this object is equal to the specified object.
     * The default implementation always returns false and should be overridden by the implementing class.
     *
     * @param pContent the object to be compared with
     * @return true if this object is equal to the specified object, false otherwise
     */
    public default boolean isEqual(ContentType pContent){
        return false;
    }

    /**
     * Checks if this object is greater than the specified object.
     * The default implementation always returns false and should be overridden by the implementing class.
     *
     * @param pContent the object to be compared with
     * @return true if this object is greater than the specified object, false otherwise
     */
    public default boolean isGreater(ContentType pContent){
        return false;
    }
}