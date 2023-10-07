package ru.bootjava.vote.util.validation;

import lombok.experimental.UtilityClass;
import ru.bootjava.vote.HasId;
import ru.bootjava.vote.error.IllegalRequestDataException;
import ru.bootjava.vote.model.Restaurant;
import ru.bootjava.vote.model.Vote;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Set;

@UtilityClass
public class ValidationUtil {

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    //  Conservative when you reply, but accept liberally (http://stackoverflow.com/a/32728226/548473)
    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static void checkZoneId(Restaurant restaurant) {
        Set<String> zoneIds = ZoneId.getAvailableZoneIds();
        String zoneId = restaurant.getZoneId();
        if (!zoneIds.contains(zoneId)) {
            throw new IllegalRequestDataException(zoneId + " must be valid");
        }
    }
}