package com.baexxbin.wishrise.member.domain;

public enum Rank {
    BRONZE(1, "BRONZE"),
    SILVER(2, "SILVER"),
    GOLD(3, "GOLD"),
    PLATINUM(4, "PLATINUM"),
    DIAMOND(5, "DIAMOND");

    private final int level;
    private final String name;

    Rank(int level, String name) {
        this.level = level;
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
}
