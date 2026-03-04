const fs = require('fs');
const csv = require('csv-parser');
const readline = require('readline');
const { DataRecord, computeMovingAverage } = require('./dataRecord');

// Function to process CSV file
function processCSV(filePath) {
  const records = [];

  fs.createReadStream(filePath)
    .pipe(csv())
    .on('data', (row) => {
      try {
        const dateStr = row['release_date'];
        const salesStr = row['total_sales'];

        if (!dateStr || !salesStr) return;

        const date = new Date(dateStr);
        const sales = parseFloat(salesStr);

        if (!isNaN(date.getTime()) && !isNaN(sales)) {
          records.push(new DataRecord(date, sales));
        }
      } catch (err) {
        // Skip invalid rows silently
      }
    })
    .on('end', () => {
      // Sort records by date
      records.sort((a, b) => a.getDate() - b.getDate());

      // Compute moving averages
      computeMovingAverage(records);

      // Print formatted table
      console.log("Date         | Sales      | 3-Day Moving Avg");
      records.forEach(r => {
        const dateStr = r.getDate().toISOString().split('T')[0];
        const avgStr = r.getMovingAvg() === null ? '-' : r.getMovingAvg().toFixed(2);
        console.log(`${dateStr} | ${r.getSales().toFixed(2)} | ${avgStr}`);
      });

      // Exit program after success
      process.exit(0);
    })
    .on('error', () => {
      console.log("Invalid file. Try again.");
      askForPath();
    });
}

// Function to prompt user for dataset path
function askForPath() {
  const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
  });

  rl.question("Enter dataset file path: ", (filePath) => {
    rl.close();

    if (!fs.existsSync(filePath.trim())) {
      console.log("Invalid file. Try again.");
      askForPath();
    } else {
      processCSV(filePath.trim());
    }
  });
}

// Program entry point
askForPath();

