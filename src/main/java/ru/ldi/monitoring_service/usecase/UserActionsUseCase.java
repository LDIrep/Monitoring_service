package ru.ldi.monitoring_service.usecase;


import ru.ldi.monitoring_service.domain.MeterDataReading;
import ru.ldi.monitoring_service.domain.MeterType;
import ru.ldi.monitoring_service.domain.User;
import ru.ldi.monitoring_service.out.ConsoleOutput;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * Класс, реализующий бизнес-логику для выполнения действий пользователей в системе мониторинга.
 */
public class UserActionsUseCase {
    private MonitoringService monitoringService;
    private ConsoleOutput consoleOutput;

    public UserActionsUseCase(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
        this.consoleOutput = monitoringService.getConsoleOutput();
    }

    /**
     * Получение актуальных данных по счетчикам текущего пользователя.
     */
    public void getLatestMeterData() {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, MeterDataReading> meterDataReadings = currentUser.getMeterDataReadings();
            for (MeterDataReading meterDataReading : meterDataReadings.values()) {
                consoleOutput.println(meterDataReading.toString());
            }
        } catch (Exception e) {
            consoleOutput.println("Ошибка: Никто не вошел в систему.");
        }
    }

    /**
     * Подача показаний счетчика текущим пользователем.
     *
     * @param meterType Тип счетчика.
     * @param value     Значение показаний.
     */
    public void submitMeterData(MeterType meterType, int value) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            currentUser.submitMeterData(meterType, value);
            logAuditEvent("Пользователь " + currentUser.getUsername() + " подал показания счетчика: " + meterType.getDisplayName() + " - " + value);
        } catch (Exception e) {
            consoleOutput.println("Ошибка: Никто не вошел в систему.");
        }

    }

    /**
     * Просмотр показаний счетчиков за определенный месяц текущим пользователем.
     *
     * @param month Месяц, за который запрашиваются показания.
     */
    public void viewMeterDataByMonth(String month) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            currentUser.viewMeterDataByMonth(month);
            logAuditEvent("Пользователь: " + currentUser.getUsername() + " запросил показания счетчиков за месяц: " + month.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            consoleOutput.println("Ошибка: Никто не вошел в систему.");
        }

    }

    /**
     * Просмотр истории показаний определенного счетчика текущим пользователем.
     *
     * @param meterType Тип счетчика, историю которого запрашивают.
     */
    public void viewMeterDataHistory(MeterType meterType) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, MeterDataReading> meterDataReadings = currentUser.getMeterDataReadings();
            MeterDataReading meterDataReading = meterDataReadings.get(meterType.getDisplayName());
            if (meterDataReading != null) {
                Map<LocalDate, Integer> history = meterDataReading.getValues();
                for (Map.Entry<LocalDate, Integer> entry : history.entrySet()) {
                    consoleOutput.println("Cчетчик: " + meterType.getDisplayName() +
                            " Месяц: " + entry.getKey().getMonth() +
                            ", Показания: " + entry.getValue());
                }
                logAuditEvent("Пользователь: " + currentUser.getUsername() +
                        " запросил историю показания счетчика " + meterType.getDisplayName());
            } else {
                consoleOutput.println("Ошибка: Счетчик " + meterType.getDisplayName() + " не найден.");

            }
        } catch (Exception e) {
            consoleOutput.println("");
        }
    }
    /**
     * Просмотр показаний счетчиков всех пользователей текущим администратором.
     */
    public void viewReadingsFromAllUsers() {
        try {
            Map<String, User> users = monitoringService.getUsers();
            User currentUser = monitoringService.getCurrentUser();
            if (currentUser.isAdmin()) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    consoleOutput.println("Пользователь: " + entry.getKey() + " " + entry.getValue().getMeterDataReadings());
                }
                logAuditEvent("Администратор: " + currentUser.getUsername() + " запросил показания счетчиков всех пользователей");
            }
        } catch (Exception e) {
            consoleOutput.println("Ошибка: Никто не вошел в систему.");
        }
    }
    /**
     * Изменение роли пользователя текущим администратором.
     *
     * @param username Имя пользователя, роль которого изменяется.
     * @param isAdmin  Новая роль (true - администратор, false - обычный пользователь).
     */
    public void changeUserRole(String username, boolean isAdmin) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, User> users = monitoringService.getUsers();
            if (users.containsKey(username)){
                if (currentUser != null && currentUser.isAdmin()) {
                    users.get(username).setAdmin(isAdmin);
                    consoleOutput.println("Администратор " + currentUser.getUsername() + " поменял роль " + username);
                }
            } else  consoleOutput.println("Такого пользователя нет");
        } catch (Exception e) {
            consoleOutput.println("Ошибка: Никто не вошел в систему.");
        }

    }

    public void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        consoleOutput.println("Запись в аудит: " + event);
    }
}
