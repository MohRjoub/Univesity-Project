<?php
session_start();
if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}
include 'customerHeader.php';

require 'Ticket.php';
require 'dbconfig.in.php';

echo "<p>Welcome " . $_SESSION['name'] . "</p></header><hr>";
?>

<h2>Request Submitted Successfully</h2>
<p>Dear <?= $_SESSION['name']; ?> thank you for submitting your maintenance request.</p>
<p>Your ticket has been created in the system with reference number <?= $_GET['ticketId']; ?>.</p>
<p>Here is a summary of the information we have received:</p>
<ul>
    <li><strong>Full Name:</strong> <?= $_SESSION['name']; ?></li>
    <li><strong>Email:</strong> <?= $_SESSION['email']; ?></li>
    <li><strong>Location:</strong> <?= $_GET['location']; ?></li>
    <li><strong>Issue Description:</strong> <?= $_GET['description']; ?></li>
    <li><strong>Urgency Level:</strong> <?= $_GET['urgencyLevel']; ?></li>
    <li><strong>Photo Uploaded:</strong> 
        <?php 
        if (!empty($_GET['imageName'])) {
            echo " Yes<br><img src='" . $_GET['imageName'] . "' alt='Ticket Image' style='max-width: 400px;'>";
        } else {
            echo " No";
        }
        ?>
    </li>
</ul>
<p>Our maintenance team will respond to your request shortly.</p>

<?php include_once 'footer.php'; ?>
</body>
</html>
