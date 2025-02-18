<?php
session_start();
if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}
if ($_SESSION['usertype'] == 'manager'){
include 'managerHeader.php';
} else if ($_SESSION['usertype'] == 'customer'){
include 'customerHeader.php';
} else {
    echo "<header>
    <img src='../ass1/logo.png' alt='logo' width='80' height='70'>
    <h1>Rjoub For Maintenance Services</h1>
    <nav>
        <a href='./ticketsys.php'>Home</a> | 
        <a href='./staffDashboard.php'>View Tickets</a>
    </nav>";
}

echo "<p>Welcome ". $_SESSION['name'] ."</p></header><hr>";
require 'dbconfig.in.php';
require 'Ticket.php';

if (!isset($_GET['id']) || !is_numeric($_GET['id'])) {
    echo "<p'>Invalid ticket ID.</p>";
    exit();
}

$ticketId = $_GET['id'];

$sql = "SELECT * FROM tickets WHERE id = :id";
$statement = $pdo->prepare($sql);
$statement->bindValue(':id', $ticketId);
$statement->execute();
$row = $statement->fetch();

if (!$row) {
    echo "<p>No ticket found with the provided ID.</p>";
    exit(); 
}

$sql = "SELECT * FROM user WHERE id = :id";
$statement = $pdo->prepare($sql);
$statement->bindValue(':id', $row['assignedstaff']);
$statement->execute();

$staff = $statement->fetch();
$assignedStaffName = $staff ? $staff['name'] : '';

$ticket = new Ticket(
    $row['id'],
    $row['customername'],
    $row['description'],
    $row['submitteddate'],
    $row['status'],
    $row['emergencylevel'],
    $row['assigneddate'],
    $assignedStaffName,
    $row['imagename'],
    $row['customeremail'],
    $row['location']
);

echo $ticket->displayTicketPage();
include_once 'footer.php';
echo '</body></html>';
?>
