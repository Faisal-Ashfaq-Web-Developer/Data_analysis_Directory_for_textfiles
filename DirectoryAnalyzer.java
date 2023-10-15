import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DirectoryAnalyzer {

    public static void main(String[] args) {
        String directoryPath = "C:\\Users\\faisa\\OneDrive\\Desktop\\Hero Vired Course\\HTML & CSS\\Text files";

        // Task 1: Recursively traverse the directory and its subdirectories
        List<File> textFiles = listTextFiles(directoryPath);

        // Task 2: Calculate the total size of all ".txt" files
        long totalSize = calculateTotalSize(textFiles);

        // Task 3: Identify and list all unique words in ".txt" files
        List<String> uniqueWords = extractUniqueWords(textFiles);

        // Task 4: Count the frequency of each unique word and display the top 10 most frequent words
        Map<String, Integer> wordFrequency = countWordFrequency(uniqueWords);
        displayTopWords(wordFrequency, 10);

        // Task 5: Create "input.txt" with sample text data
        createInputFile("input.txt");

        // Task 6: Read "input.txt" and validate records
        List<String> validRecords = new ArrayList<>();
        List<String> invalidRecords = new ArrayList<>();
        readAndValidateRecords("input.txt", validRecords, invalidRecords);

        // Task 7: Transform valid records and write them to "output.txt"
        writeValidRecords("output.txt", validRecords);

        // Task 8: Create a summary report
        createSummaryReport(validRecords.size(), invalidRecords.size());
    }

    // Task 1: Recursively traverse the directory and its subdirectories to find ".txt" files
    private static List<File> listTextFiles(String directoryPath) {
        List<File> textFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(file -> file.getFileName().toString().endsWith(".txt"))
                    .forEach(file -> textFiles.add(file.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textFiles;
    }

    // Task 2: Calculate the total size of all ".txt" files
    private static long calculateTotalSize(List<File> textFiles) {
        long totalSize = 0;
        for (File file : textFiles) {
            totalSize += file.length();
        }
        System.out.println("Total size of .txt files: " + totalSize + " bytes");
        return totalSize;
    }

    // Task 3: Identify and list all unique words in ".txt" files
    private static List<String> extractUniqueWords(List<File> textFiles) {
        List<String> uniqueWords = new ArrayList<>();
        for (File file : textFiles) {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            uniqueWords.add(word.toLowerCase());
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return uniqueWords;
    }

    // Task 4: Count the frequency of each unique word and display the top n words
    private static Map<String, Integer> countWordFrequency(List<String> words) {
        Map<String, Integer> wordFrequency = new HashMap<>();
        for (String word : words) {
            wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
        }
        return wordFrequency;
    }

    private static void displayTopWords(Map<String, Integer> wordFrequency, int n) {
        System.out.println("Top " + n + " most frequent words:");
        wordFrequency.entrySet().stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .limit(n)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    // Task 5: Create "input.txt" with sample text data
    private static void createInputFile(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            writer.println("Name,Email,Phone");
            writer.println("John,john@example.com,1234567890");
            writer.println("Alice,alice@example.com,9876543210");
            writer.println("InvalidRecord"); // Add an invalid record
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task 6: Read "input.txt" and validate records
    private static void readAndValidateRecords(String filename, List<String> validRecords, List<String> invalidRecords) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isValidRecord(line)) {
                    validRecords.add(line);
                } else {
                    invalidRecords.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Validation method for records in "input.txt"
    private static boolean isValidRecord(String record) {
        return record.matches("[\\w]+,[\\w@.]+,[0-9]+");
    }

    // Task 7: Transform valid records and write them to "output.txt"
    private static void writeValidRecords(String filename, List<String> validRecords) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (String record : validRecords) {
                String[] parts = record.split(",");
                String transformedRecord = "Name: " + parts[0] + ", Email: " + parts[1] + ", Phone: " + parts[2];
                writer.println(transformedRecord);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Task 8: Create a summary report
    private static void createSummaryReport(int validRecordCount, int invalidRecordCount) {
        System.out.println("Summary Report:");
        System.out.println("Valid Records: " + validRecordCount);
        System.out.println("Invalid Records: " + invalidRecordCount);
    }
}
