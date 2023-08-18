package com.heima.common.enums;

public enum  GenderEnum {


    NV (0,"女"),
    NAN (1,"男");

    private int value;
    private String name;

    GenderEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
