package ru.bootjava.vote.to;

public class DishTo extends NamedTo {
    int priceInCoins;

    public DishTo(Integer id, String name, int priceInCoins) {
        super(id, name);
        this.priceInCoins = priceInCoins;
    }
}
