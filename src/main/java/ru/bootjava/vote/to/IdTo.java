package ru.bootjava.vote.to;

import org.springframework.util.Assert;

public class IdTo extends BaseTo {
    public IdTo(Integer id) {
        super(id);
    }

    public int id() {
        Assert.notNull(id, "Entity must have id");
        return id;
    }
}