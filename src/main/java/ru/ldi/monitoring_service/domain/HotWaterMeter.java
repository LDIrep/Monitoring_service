package ru.ldi.monitoring_service.domain;

public class HotWaterMeter extends Meter {

    private int value;

    @Override
    public String getType() {
        return MeterType.HOT_WATER.getDisplayName();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void setValue(int value) {
        this.value = value;
    }
}
