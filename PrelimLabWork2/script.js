// Hardcoded accounts
const accounts = [
    { username: "admin", password: "1234" },
    { username: "student1", password: "pass1" },
    { username: "student2", password: "pass2" },
    { username: "student3", password: "pass3" },
    { username: "student4", password: "pass4" }
];

// Beep sound
const beep = new Audio("beep.mp3");

// Array to store all successful logins
let attendanceRecords = [];

// Form submission event
document.getElementById("loginForm").addEventListener("submit", function(event){
    event.preventDefault();

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    // Check if username and password match any account
    let isValidUser = accounts.some(acc => acc.username === username && acc.password === password);

    if(isValidUser){
        loginSuccess(username);
    } else {
        loginFailed();
    }
});

// Login failed function
function loginFailed(){
    const message = document.getElementById("message");
    message.textContent = "Incorrect username or password!";
    message.style.color = "red";
    message.style.display = "block";
    beep.play();

    // Hide message after 3 seconds
    setTimeout(() => { message.style.display = "none"; }, 3000);
}

// Login success function
function loginSuccess(username){
    const now = new Date();
    const timestamp = now.toLocaleString();

    // Save this login to the attendance array
    attendanceRecords.push({ username, timestamp });

    // Show welcome message
    const message = document.getElementById("message");
    message.textContent = `Welcome, ${username}!`;
    message.style.color = "green";
    message.style.display = "block";

    // Display timestamp and attendance status
    document.getElementById("timestamp").textContent = `Login Time: ${timestamp}`;
    document.getElementById("attendanceStatus").textContent = "Attendance recorded successfully.";
    document.getElementById("attendanceStatus").style.color = "green";

    // Show download and logout buttons
    document.getElementById("downloadBtn").style.display = "block";
    document.getElementById("logoutBtn").style.display = "block";

    // Hide welcome message after 3 seconds
    setTimeout(() => { message.style.display = "none"; }, 3000);

    // Update the real-time attendance table
    updateAttendanceTable();
}

// Function to update the attendance table in real-time
function updateAttendanceTable(){
    const tbody = document.querySelector("#attendanceTable tbody");

    // If table does not exist in HTML, skip
    if(!tbody) return;

    // Clear current table content
    tbody.innerHTML = "";

    // Add each record as a new row
    attendanceRecords.forEach(record => {
        const row = document.createElement("tr");

        const usernameCell = document.createElement("td");
        usernameCell.textContent = record.username;

        const timestampCell = document.createElement("td");
        timestampCell.textContent = record.timestamp;

        row.appendChild(usernameCell);
        row.appendChild(timestampCell);

        tbody.appendChild(row);
    });
}

// Download attendance as CSV
document.getElementById("downloadBtn").addEventListener("click", function(){
    if(attendanceRecords.length === 0){
        alert("No attendance records found.");
        return;
    }

    let csvData = "Username,Login Time\n";
    attendanceRecords.forEach(record => {
        csvData += `${record.username},${record.timestamp}\n`;
    });

    const blob = new Blob([csvData], { type: "text/csv" });
    const link = document.createElement("a");
    link.href = URL.createObjectURL(blob);
    link.download = "attendance_summary.csv";
    link.click();
});

// Logout / New Login
document.getElementById("logoutBtn").addEventListener("click", function(){
    // Clear messages
    document.getElementById("message").style.display = "none";
    document.getElementById("timestamp").textContent = "";
    document.getElementById("attendanceStatus").textContent = "";

    // Clear input fields
    document.getElementById("username").value = "";
    document.getElementById("password").value = "";

    // Hide logout button
    this.style.display = "none";
});

