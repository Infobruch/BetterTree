package de.tillmannrohlfing.interfaces;

public interface ComparableContent <ContentType>{
    public default boolean isLess(ContentType pContent){
        return false;
    }
    public default boolean isEqual(ContentType pContent){
        return false;
    }
    public default boolean isGreater(ContentType pContent){
        return false;
    }
}
