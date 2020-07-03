package pl.coderslab;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.math.NumberUtils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TaskManager {

    static String[][] tasks;

    public static void main(String[] args) {
        String fileName = "tasks.csv";
        tasks = loadDataToTab(fileName);
        String[] select = {"add", "remove", "list", "exit"};
        showOptions(select);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();

            switch (input) {
                case "exit":
                    saveTabToFile(fileName, tasks);
                    System.out.println(ConsoleColors.RED + "Bye, bye.");
                    System.exit(0);
                    break;
                case "add":
                    addTask();
                    break;
                case "remove":
                    remove(tasks, removeNumber());
                    System.out.println("Wpis został usunięty.");
                    break;
                case "list":
                    listTab(tasks);
                    break;
                default:
                    System.out.println("Wybierz poprawną komendę.");
            }
            showOptions(select);
        }
    }

    public static void showOptions(String[] tab) {
        System.out.println(ConsoleColors.BLUE);
        System.out.println("Please select an option: " + ConsoleColors.RESET);
        for (String option : tab) {
            System.out.println(option);
        }
    }

    public static int removeNumber() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please select number to remove.");

        String n = scanner.nextLine();
        while (!numberIsZero(n)) {
            System.out.println("Incorrect argument passed. Please give number greater or equal 0");
            scanner.nextLine();
        }
        return Integer.parseInt(n);
    }

    private static void remove(String[][] tab, int idTask) {
        try {
            if (idTask < tab.length) {
                tasks = ArrayUtils.remove(tab, idTask);
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Element not exist in tab");
        }
    }

    public static boolean numberIsZero(String input) {
        if (NumberUtils.isParsable(input)) {
            return Integer.parseInt(input) >= 0;
        }
        return false;
    }

    public static void listTab(String[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + " : ");
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please add task description");
        String description = scanner.nextLine();
        System.out.println("Please add task due date");
        String taskDate = scanner.nextLine();
        System.out.println("Is your task is important: true/false");
        String important = scanner.nextLine();
        tasks = Arrays.copyOf(tasks, tasks.length + 1);
        tasks[tasks.length - 1] = new String[3];
        tasks[tasks.length - 1][0] = description;
        tasks[tasks.length - 1][1] = taskDate;
        tasks[tasks.length - 1][2] = important;
    }


    public static String[][] loadDataToTab(String fileName) {
        Path dir = Paths.get(fileName);
        String[][] tab = null;
        if (!Files.exists(dir)) {
            System.out.println("File not exist.");
            System.exit(0);
        }
        try {
            List<String> strings = Files.readAllLines(dir);
            tab = new String[strings.size()][strings.get(0).split(",").length];

            for (int i = 0; i < strings.size(); i++) {
                String[] split = strings.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    tab[i][j] = split[j];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tab;
    }

    public static void saveTabToFile(String fileName, String[][] tab) {
        Path dir = Paths.get(fileName);
        String[] lines = new String[tasks.length];
        for (int i = 0; i < tab.length; i++) {
            lines[i] = String.join(",", tab[i]);
        }
        try {
            Files.write(dir, Arrays.asList(lines));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

