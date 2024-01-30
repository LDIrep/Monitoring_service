package ru.ldi.monitoring_service.presentation;

import ru.ldi.monitoring_service.domain.MeterType;
import ru.ldi.monitoring_service.domain.User;
import ru.ldi.monitoring_service.usecase.MonitoringService;
import ru.ldi.monitoring_service.usecase.UserActionsUseCase;
import ru.ldi.monitoring_service.in.ConsoleInput;
import ru.ldi.monitoring_service.out.ConsoleOutput;
import ru.ldi.monitoring_service.usecase.UserAuthenticationUseCase;
import ru.ldi.monitoring_service.usecase.UserRegistrationUseCase;

import java.util.List;
import java.util.Map;

public class ConsoleUI {
    private ConsoleInput consoleInput;
    private ConsoleOutput consoleOutput;
    private UserAuthenticationUseCase userAuthenticationUseCase;
    private UserRegistrationUseCase userRegistrationUseCase;
    private UserActionsUseCase userActionsUseCase;

    public ConsoleUI(MonitoringService monitoringService) {
        this.consoleInput = monitoringService.getConsoleInput();
        this.consoleOutput = monitoringService.getConsoleOutput();
            this.userAuthenticationUseCase = monitoringService.getUserAuthenticationUseCase();
        this.userRegistrationUseCase = monitoringService.getUserRegistrationUseCase();
        this.userActionsUseCase = monitoringService.getUserActionsUseCase();
    }

    public void start() {
        while (true) {
            consoleOutput.println("1. ������������������");
            consoleOutput.println("2. �����");
            consoleOutput.println("3. �����");
            consoleOutput.println("4. �������� ������ ��������");
            consoleOutput.println("5. ����������� ������ �������� �� ������");
            consoleOutput.println("6. ����������� ������� ������ ��������");
            consoleOutput.println("7. ����������� ��� �������� ������������� (������ ��� ���������������)");
            consoleOutput.println("8. �������� ���� ������������ (������ ��� ���������������)");
            consoleOutput.println("0. ����� �� ���������");

            int choice = consoleInput.readInt();
            consoleInput.readLine();

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                case 3:
                    logoffUser();
                    break;
                case 4:
                    submitMeterData();
                    break;
                case 5:
                    viewMeterDataByMonth();
                    break;
                case 6:
                    viewMeterDataHistory();
                    break;
                case 7:
                    viewReadingsFromAllUsers();
                    break;
                case 8:
                    changeUserRole();
                    break;
                case 0:
                    System.exit(0);
                    break;
                default:
                    consoleOutput.println("������������ �����. ����������, ��������� �������.");
            }
        }
    }

    private void registerUser() {
        consoleOutput.println("������� ��� ������������: ");
        String username = consoleInput.readLine();
        consoleOutput.println("������� ������: ");
        String password = consoleInput.readLine();

        userRegistrationUseCase.registerUser(username, password);
    }

    private void loginUser() {
        consoleOutput.println("������� ��� ������������: ");
        String username = consoleInput.readLine();
        consoleOutput.println("������� ������: ");
        String password = consoleInput.readLine();

        userAuthenticationUseCase.loginUser(username, password);
    }

    private void logoffUser() {
        userAuthenticationUseCase.logoffUser();
    }

    private void submitMeterData() {
        try {
            consoleOutput.println("������� ��� �������� (HEATING, HOT_WATER, COLD_WATER): ");
            String meterTypeString = consoleInput.readLine();
            System.out.println(meterTypeString);
            MeterType meterType = MeterType.valueOf(meterTypeString.toUpperCase());
            consoleOutput.println("������� ��������� ��������: ");
            int value = consoleInput.readInt();
            consoleInput.readLine();
            userActionsUseCase.submitMeterData(meterType, value);
            consoleOutput.println("������ �������� ������� ��������.");
        } catch (Exception e){
            consoleOutput.println("�� ����� ������������ ��� ��������");
        }

    }

    private void viewMeterDataByMonth() {
        try {
            consoleOutput.println("������� ����� (��������, JANUARY): ");
            String month = consoleInput.readLine();

            userActionsUseCase.viewMeterDataByMonth(month);
        } catch (Exception e){
            consoleOutput.println("�� ����� ������������ �����");
        }

    }

    private void viewMeterDataHistory() {
        consoleOutput.println("������� ��� �������� (HEATING, HOT_WATER, COLD_WATER): ");
        String meterTypeString = consoleInput.readLine();
        MeterType meterType = MeterType.valueOf(meterTypeString.toUpperCase());

        userActionsUseCase.viewMeterDataHistory(meterType);
    }

    private void changeUserRole(){
        try {
            consoleOutput.println("������� ��� ������������, �������� ������ �������� �����");
            String name = consoleInput.readLine();
            consoleOutput.println("������� ��������������: ������� true, ���� ������� ������� �������������: ������� false");
            boolean answer = Boolean.parseBoolean(consoleInput.readLine());
            userActionsUseCase.changeUserRole(name,answer);
        } catch (Exception e){
            consoleOutput.println("�������� ����");
        }
    }

    private void viewReadingsFromAllUsers(){
        consoleOutput.println("������ ������������� � �� ���������");
        userActionsUseCase.viewReadingsFromAllUsers();
    }
}
