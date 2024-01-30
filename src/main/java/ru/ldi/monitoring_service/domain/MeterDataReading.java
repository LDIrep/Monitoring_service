package ru.ldi.monitoring_service.domain;

import java.time.LocalDate;
import java.util.HashMap;

public class MeterDataReading {
    private final Meter meter;
    private LocalDate date;
    private HashMap<LocalDate, Integer> values;

    public MeterDataReading(Meter meter) {
        this.meter = meter;
        this.date = LocalDate.now();
        values = null;
    }

    public Meter getMeter() {
        return meter;
    }

    public LocalDate getDate() {
        return date;
    }

    public HashMap<LocalDate, Integer> getValues() {
        return values;
    }

    public void setValue(int newValue) {
        LocalDate currentDate = LocalDate.now();
        if (values == null) {
            values = new HashMap<>();
            meter.setValue(newValue);
            values.put(currentDate, newValue);
            System.out.println("Счётчик зарегистрирован. Показания счетчика " + meter.getType() + " успешно изменены на " + newValue +
                    " для даты " + currentDate);
            date = currentDate;
            return;
        }
        if (canChangeValue()) {
            if (newValue >= values.get(date)) {
                meter.setValue(newValue);
                values.put(currentDate, newValue);
                date = currentDate;
                System.out.println("Показания счетчика " + meter.getType() + " успешно изменены на " + newValue +
                        " для даты " + date);
            } else {
                System.out.println("Ошибка: Новое значение должно быть больше или равно предыдущему");
            }
        } else {
            System.out.println("Ошибка: Изменение показаний счетчика разрешено только раз в месяц");
        }
    }

    private boolean canChangeValue() {
        return LocalDate.now().getMonth() != date.getMonth();
    }


    @Override
    public String toString() {
        return "Показания счетчика " + meter.getType() + " за дату " + date + ": " + meter.getValue();
    }
}