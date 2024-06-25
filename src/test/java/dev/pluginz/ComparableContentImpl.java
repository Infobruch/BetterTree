package dev.pluginz;

import dev.pluginz.interfaces.ComparableContent;

/**
 * The ComparableContentImpl class is an implementation of the ComparableContent interface.
 * It provides methods for comparing objects of type ComparableContentImpl.
 * This class is used as the content type for the BTree in this project.
 */
public class ComparableContentImpl implements ComparableContent<ComparableContentImpl> {
    private Integer value;

    /**
     * ComparableContentImpl constructor.
     * @param value the integer value that this object represents.
     */
    public ComparableContentImpl(Integer value) {
        this.value = value;
    }

    /**
     * Checks if this object is less than the specified object.
     * @param pContent the object to be compared with.
     * @return true if this object is less than the specified object, false otherwise.
     */
    @Override
    public boolean isLess(ComparableContentImpl pContent) {
        return value < pContent.value;
    }

    /**
     * Checks if this object is equal to the specified object.
     * @param pContent the object to be compared with.
     * @return true if this object is equal to the specified object, false otherwise.
     */
    @Override
    public boolean isEqual(ComparableContentImpl pContent) {
        return value.equals(pContent.value);
    }

    /**
     * Checks if this object is greater than the specified object.
     * @param pContent the object to be compared with.
     * @return true if this object is greater than the specified object, false otherwise.
     */
    @Override
    public boolean isGreater(ComparableContentImpl pContent) {
        return value > pContent.value;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return value.toString();
    }
}