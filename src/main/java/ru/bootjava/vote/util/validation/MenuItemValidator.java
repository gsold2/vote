package ru.bootjava.vote.util.validation;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.error.IllegalRequestDataException;

import java.util.Collection;

@UtilityClass
public class MenuItemValidator {

    public static <E> void checkThatMenuEmpty(Collection<E> collection, int restaurantId){
        if (collection.size() > 0) {
            throw new IllegalRequestDataException("Restaurant id=" + restaurantId + " already has menuItems up today. This method is not applicable.");
        }
    }
}