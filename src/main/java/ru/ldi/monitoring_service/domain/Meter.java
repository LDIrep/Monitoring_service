package ru.ldi.monitoring_service.domain;

public abstract class Meter {
    private int value;

    public String getType() {
        return "Some type";
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Счетчик { " + getType() +
                " показания =" + value +
                '}';
    }
}