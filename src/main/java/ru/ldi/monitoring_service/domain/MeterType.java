package ru.ldi.monitoring_service.domain;

public enum MeterType {
    HEATING("���������"),
    HOT_WATER("������� ����"),
    COLD_WATER("�������� ����");

    private final String displayName;

    MeterType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
