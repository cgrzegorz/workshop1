package pl.coderslab;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.lang3.math.NumberUtils;

public class TaskManager {
    public static String[][] loadTasks() {
        File file = new File("tasks.csv");
        String[][] arr = new String[0][];

        try {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String[] taskFields = scan.nextLine().split(",");
                arr = Arrays.copyOf(arr, arr.length + 1);
                arr[arr.length - 1] = taskFields;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }

        return arr;
    }

    public static void list(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            StringBuilder taskInfo = new StringBuilder();
            taskInfo.append(i).append(" : ");
            for (int j = 0; j < tab[i].length; j++) {
                taskInfo.append(tab[i][j]);
            }
            System.out.println(taskInfo);
        }
    }

    public static void remove(String[][] tasks) {
        boolean isValidTaskNumber = false;
        Scanner scan = new Scanner(System.in);
        String taskNumber;

        System.out.println(ConsoleColors.BLUE);
        System.out.println("Podaj numer Taska: " + "Zakres to 0 do " + (tasks.length - 1));

        while (!isValidTaskNumber) {
            taskNumber = scan.nextLine();
            int numberTask = Integer.parseInt(taskNumber);
            if (NumberUtils.isParsable(taskNumber) && numberTask >= 0 && numberTask < tasks.length) {
                isValidTaskNumber = true;
                try (FileWriter fileWriter = new FileWriter("tasks.csv")) {
                    for (int i = 0; i < tasks.length; i++) {
                        if (i != Integer.parseInt(taskNumber)) {
                            StringBuilder taskRow = new StringBuilder();
                            for (int j = 0; j < tasks[i].length; j++) {
                                taskRow.append(tasks[i][j]);
                                if (j < tasks[i].length - 1) {
                                    taskRow.append(",");
                                }
                            }
                            fileWriter.append(taskRow);
                            fileWriter.append("\n");
                        }
                    }
                    System.out.println("Usunięto zadanie :)");
                } catch (IOException ex) {
                    System.out.println("Błąd zapisu do pliku.");
                }
            } else {
                System.out.println(ConsoleColors.RED);
                System.out.println("Nie ma takiego numeru zadania!");
            }
        }


    }

    public static void add() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Dodaj opis");
        String description = scanner.nextLine();
        System.out.println("Podaj Datę");
        String dueDate = scanner.nextLine();
        System.out.println("Priorytet?: true/false");
        String isImportant = scanner.nextLine();

        try (FileWriter fileWriter = new FileWriter("tasks.csv", true)) {
            fileWriter.append("\n");
            fileWriter.append(description).append(", ").append(dueDate).append(", ").append(isImportant);

            System.out.println("Dodano zadanie :)");
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }
    }

    public static void main(String[] args) {
        String[] options = {"add", "remove", "list", "exit"};
        boolean shouldExit = false;

        while (!shouldExit) {
            String[][] tasks = loadTasks();
            Scanner scan = new Scanner(System.in);

            System.out.println(ConsoleColors.BLUE);
            System.out.println("Please select an option: ");
            System.out.println(ConsoleColors.RESET);

            for (String option : options) {
                System.out.println(option);
            }

            String option = "";
            boolean isValidOption = false;

            while (!isValidOption) {
                option = scan.nextLine();
                if (Arrays.asList(options).contains(option)) {
                    isValidOption = true;
                } else {
                    System.out.println("Nie ma takiej Opcji, podaj właściwą z listy");
                }
            }

            switch (option) {
                case "add":
                    add();
                    break;
                case "list":
                    list(tasks);
                    break;
                case "exit":
                    System.out.println(ConsoleColors.RED);
                    System.out.println("Bye,bye");
                    shouldExit = true;
                    break;
                case "remove":
                    remove(tasks);
                    break;
                default:
                    System.out.println("Please select a correct option.");
            }

        }
    }
}
