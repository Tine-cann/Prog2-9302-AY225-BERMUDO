import java.util.Date;

// Class to represent a single data record from the dataset
public class DataRecord {
    private Date date;          // Release date of the record
    private double sales;       // Sales value
    private Double movingAvg;   // 3-day moving average (nullable)

    // Constructor initializes date and sales
    public DataRecord(Date date, double sales) {
        this.date = date;
        this.sales = sales;
    }

    // Getter for date
    public Date getDate() { return date; }

    // Getter for sales
    public double getSales() { return sales; }

    // Getter for moving average
    public Double getMovingAvg() { return movingAvg; }

    // Setter for moving average
    public void setMovingAvg(Double movingAvg) { this.movingAvg = movingAvg; }
}
