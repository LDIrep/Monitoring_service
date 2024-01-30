package ru.ldi.monitoring_service.domain;

import ru.ldi.monitoring_service.out.ConsoleOutput;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class User {
    private final String username;
    private final String password;
    private boolean isAdmin;
    private final Map<String, MeterDataReading> meterDataReadings;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.meterDataReadings = initializeMeterDataReadings();
    }

    public User() {
        username = null;
        password = null;
        meterDataReadings = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Map<String, MeterDataReading> getMeterDataReadings() {
        return meterDataReadings;
    }

    // Метод для передачи показаний
    public void submitMeterData(MeterType meterType, int value) {
        String meterTypeName = meterType.getDisplayName();
        MeterDataReading meterDataReading = meterDataReadings.get(meterTypeName);
        if (meterDataReading != null) {
            meterDataReading.setValue(value);
        } else {
            throw new IllegalArgumentException("Ошибка: Счетчик " + meterType + " не найден.");
        }
    }

    // Метод для просмотра показаний за определенный месяц
    public void viewMeterDataByMonth(String month) {
        Map<String, MeterDataReading> readings = getMeterDataReadings();
        if (readings.containsKey(month)) {
            for (Map.Entry<String, MeterDataReading> entry : readings.entrySet()) {
                String monthString = entry.getValue().getDate().getMonth().toString();
                if (monthString.equals(month.toUpperCase(Locale.ROOT))) {
                    if (entry.getValue().getValues() != null) {
                        ConsoleOutput.println("Показания счетчика " + entry.getKey() + " за " + month + ": " + entry.getValue().getValues());
                    }
                }
            }
        } else ConsoleOutput.println("Показаний за данный месяц нет в базе");
    }

    private Map<String, MeterDataReading> initializeMeterDataReadings() {
        Map<String, MeterDataReading> readings = new HashMap<>();
        readings.put(MeterType.HEATING.getDisplayName(), new MeterDataReading(new HeatingMeter()));
        readings.put(MeterType.HOT_WATER.getDisplayName(), new MeterDataReading(new HotWaterMeter()));
        readings.put(MeterType.COLD_WATER.getDisplayName(), new MeterDataReading(new ColdWaterMeter()));
        return readings;
    }


}
