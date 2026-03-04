import java.io.*;
import java.util.*;
import java.text.*;

// DataRecord class to hold each row of the dataset
class DataRecord {
    private Date date;          // Release date
    private double sales;       // Sales value
    private Double movingAvg;   // 3-day moving average (nullable)

    // Constructor initializes date and sales
    public DataRecord(Date date, double sales) {
        this.date = date;
        this.sales = sales;
    }

    // Getter methods
    public Date getDate() { return date; }
    public double getSales() { return sales; }
    public Double getMovingAvg() { return movingAvg; }

    // Setter for moving average
    public void setMovingAvg(Double movingAvg) { this.movingAvg = movingAvg; }
}

public class SalesProcessor {
    public static void main(String[] args) {
        run(); // Entry point calls run method
    }

    // Method to handle user input and file validation
    public static void run() {
        Scanner sc = new Scanner(System.in);
        String path = "";

        while (true) {
            System.out.print("Enter dataset file path: ");
            path = sc.nextLine();

            File file = new File(path);

            // Validate file existence and extension
            if (!file.exists() || !path.endsWith(".csv")) {
                System.out.println("Invalid file. Try again.");
                continue;
            }

            // Try reading the file
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                processData(br); // Process the dataset
                break;           // Exit loop after successful processing
            } catch (IOException e) {
                System.out.println("Error reading file. Try again.");
            }
        }

        sc.close(); // Close scanner
    }

    // Method to process CSV data
    private static void processData(BufferedReader br) throws IOException {
        List<DataRecord> records = new ArrayList<>();
        String line;
        br.readLine(); // Skip header row

        // Read each line of the CSV
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            // Ensure enough columns exist
            if (parts.length < 13) continue;

            try {
                // release_date is column 13 (index 12)
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(parts[12].trim());

                // total_sales is column 8 (index 7)
                double sales = Double.parseDouble(parts[7].trim());

                // Add record to list
                records.add(new DataRecord(date, sales));
            } catch (Exception e) {
                // Skip invalid rows silently
                continue;
            }
        }

        // Sort records by date
        records.sort(Comparator.comparing(DataRecord::getDate));

        // Calculate 3-day moving average
        for (int i = 0; i < records.size(); i++) {
            if (i < 2) {
                records.get(i).setMovingAvg(null);
            } else {
                double avg = (records.get(i).getSales() +
                              records.get(i-1).getSales() +
                              records.get(i-2).getSales()) / 3;
                records.get(i).setMovingAvg(avg);
            }
        }

        // Print results in a formatted table
        System.out.printf("%-12s | %-10s | %-15s%n", "Date", "Sales", "3-Day Moving Avg");
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

        for (DataRecord r : records) {
            String avg = (r.getMovingAvg() == null) ? "-" : String.format("%.2f", r.getMovingAvg());
            System.out.printf("%-12s | %-10.2f | %-15s%n",
                              fmt.format(r.getDate()), r.getSales(), avg);
        }
    }
}
