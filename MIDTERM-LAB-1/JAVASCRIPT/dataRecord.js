class DataRecord {
  constructor(date, sales) {
    this.date = date;          // Release date
    this.sales = sales;        // Sales value
    this.movingAvg = null;     // 3-day moving average (nullable)
  }

  setMovingAvg(avg) {
    this.movingAvg = avg;
  }

  getDate() {
    return this.date;
  }

  getSales() {
    return this.sales;
  }

  getMovingAvg() {
    return this.movingAvg;
  }
}

// Function to compute 3-day moving average
function computeMovingAverage(records) {
  for (let i = 0; i < records.length; i++) {
    if (i < 2) {
      records[i].setMovingAvg(null);
    } else {
      const avg = (
        records[i].getSales() +
        records[i - 1].getSales() +
        records[i - 2].getSales()
      ) / 3;
      records[i].setMovingAvg(avg);
    }
  }
}

module.exports = { DataRecord, computeMovingAverage };
