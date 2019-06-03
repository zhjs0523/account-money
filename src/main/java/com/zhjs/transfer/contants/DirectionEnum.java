package com.zhjs.transfer.contants;

public enum DirectionEnum {
    DECREASE(-1),
    INCREASE(1);

    private final int value;

    DirectionEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
