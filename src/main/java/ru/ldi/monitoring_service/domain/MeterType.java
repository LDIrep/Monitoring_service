package ru.ldi.monitoring_service.domain;

public enum MeterType {
    HEATING("Отопление"),
    HOT_WATER("Горячая вода"),
    COLD_WATER("Холодная вода");

    private final String displayName;

    MeterType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
