package ru.ldi.monitoring_service.usecase;


import ru.ldi.monitoring_service.domain.MeterDataReading;
import ru.ldi.monitoring_service.domain.MeterType;
import ru.ldi.monitoring_service.domain.User;
import ru.ldi.monitoring_service.out.ConsoleOutput;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * �����, ����������� ������-������ ��� ���������� �������� ������������� � ������� �����������.
 */
public class UserActionsUseCase {
    private MonitoringService monitoringService;
    private ConsoleOutput consoleOutput;

    public UserActionsUseCase(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
        this.consoleOutput = monitoringService.getConsoleOutput();
    }

    /**
     * ��������� ���������� ������ �� ��������� �������� ������������.
     */
    public void getLatestMeterData() {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, MeterDataReading> meterDataReadings = currentUser.getMeterDataReadings();
            for (MeterDataReading meterDataReading : meterDataReadings.values()) {
                consoleOutput.println(meterDataReading.toString());
            }
        } catch (Exception e) {
            consoleOutput.println("������: ����� �� ����� � �������.");
        }
    }

    /**
     * ������ ��������� �������� ������� �������������.
     *
     * @param meterType ��� ��������.
     * @param value     �������� ���������.
     */
    public void submitMeterData(MeterType meterType, int value) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            currentUser.submitMeterData(meterType, value);
            logAuditEvent("������������ " + currentUser.getUsername() + " ����� ��������� ��������: " + meterType.getDisplayName() + " - " + value);
        } catch (Exception e) {
            consoleOutput.println("������: ����� �� ����� � �������.");
        }

    }

    /**
     * �������� ��������� ��������� �� ������������ ����� ������� �������������.
     *
     * @param month �����, �� ������� ������������� ���������.
     */
    public void viewMeterDataByMonth(String month) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            currentUser.viewMeterDataByMonth(month);
            logAuditEvent("������������: " + currentUser.getUsername() + " �������� ��������� ��������� �� �����: " + month.toUpperCase(Locale.ROOT));
        } catch (Exception e) {
            consoleOutput.println("������: ����� �� ����� � �������.");
        }

    }

    /**
     * �������� ������� ��������� ������������� �������� ������� �������������.
     *
     * @param meterType ��� ��������, ������� �������� �����������.
     */
    public void viewMeterDataHistory(MeterType meterType) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, MeterDataReading> meterDataReadings = currentUser.getMeterDataReadings();
            MeterDataReading meterDataReading = meterDataReadings.get(meterType.getDisplayName());
            if (meterDataReading != null) {
                Map<LocalDate, Integer> history = meterDataReading.getValues();
                for (Map.Entry<LocalDate, Integer> entry : history.entrySet()) {
                    consoleOutput.println("C������: " + meterType.getDisplayName() +
                            " �����: " + entry.getKey().getMonth() +
                            ", ���������: " + entry.getValue());
                }
                logAuditEvent("������������: " + currentUser.getUsername() +
                        " �������� ������� ��������� �������� " + meterType.getDisplayName());
            } else {
                consoleOutput.println("������: ������� " + meterType.getDisplayName() + " �� ������.");

            }
        } catch (Exception e) {
            consoleOutput.println("");
        }
    }
    /**
     * �������� ��������� ��������� ���� ������������� ������� ���������������.
     */
    public void viewReadingsFromAllUsers() {
        try {
            Map<String, User> users = monitoringService.getUsers();
            User currentUser = monitoringService.getCurrentUser();
            if (currentUser.isAdmin()) {
                for (Map.Entry<String, User> entry : users.entrySet()) {
                    consoleOutput.println("������������: " + entry.getKey() + " " + entry.getValue().getMeterDataReadings());
                }
                logAuditEvent("�������������: " + currentUser.getUsername() + " �������� ��������� ��������� ���� �������������");
            }
        } catch (Exception e) {
            consoleOutput.println("������: ����� �� ����� � �������.");
        }
    }
    /**
     * ��������� ���� ������������ ������� ���������������.
     *
     * @param username ��� ������������, ���� �������� ����������.
     * @param isAdmin  ����� ���� (true - �������������, false - ������� ������������).
     */
    public void changeUserRole(String username, boolean isAdmin) {
        try {
            User currentUser = monitoringService.getCurrentUser();
            Map<String, User> users = monitoringService.getUsers();
            if (users.containsKey(username)){
                if (currentUser != null && currentUser.isAdmin()) {
                    users.get(username).setAdmin(isAdmin);
                    consoleOutput.println("������������� " + currentUser.getUsername() + " ������� ���� " + username);
                }
            } else  consoleOutput.println("������ ������������ ���");
        } catch (Exception e) {
            consoleOutput.println("������: ����� �� ����� � �������.");
        }

    }

    public void logAuditEvent(String event) {
        monitoringService.getAuditLog().add(event);
        consoleOutput.println("������ � �����: " + event);
    }
}
