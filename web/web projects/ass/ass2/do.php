<?php
session_start();
if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}
?>
<header>
    <img src="../ass1/logo.png" alt="logo" width="80" height="70">
    <h1>Rjoub For Maintenance Services</h1>
    <nav>
        <a href="./ticketsys.php">Home</a> | 
        <a href="./staffDashboard.php">View Tickets</a>
    </nav>

<?php
require 'Ticket.php';
require 'dbconfig.in.php';

echo "<p>Welcome " . $_SESSION['name'] . "</p></header><hr>";

$sql = "SELECT * FROM tickets WHERE id = :id";
$stmt = $pdo->prepare($sql);
$stmt->bindValue(':id', $_GET['id']);
$stmt->execute();
$tickets = $stmt->fetchAll();

if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST['ticketId'])) {
    $updateSql = "UPDATE tickets SET status = 'completed' WHERE id = :ticketId";
    $updateStmt = $pdo->prepare($updateSql);
    $updateStmt->bindValue(':ticketId', $_POST['ticketId']);
    if ($updateStmt->execute()) {
        echo "<p>Ticket " . $_POST['ticketId'] . " marked as Complete.</p>";
    } else {
        echo "<p>Error updating the ticket status.</p>";
    }
    header("Refresh: 3; url=staffDashboard.php");
    exit();
}
?>

<h2>Do Tickets</h2>
<table border="1">
    <thead>
        <tr>
            <th>Ticket ID</th>
            <th>Description</th>
            <th>Submitted Date</th>
            <th>Emergency Level</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
    </thead>
    <tbody>
        <?php if (!empty($tickets)): ?>
            <?php foreach ($tickets as $ticket): ?>
                <tr>
                    <td><?= $ticket['id'] ?></td>
                    <td><?= $ticket['description'] ?></td>
                    <td><?= $ticket['submitteddate'] ?></td>
                    <td><?= $ticket['emergencylevel'] ?></td>
                    <td><?= $ticket['status'] ?></td>
                    <td>
                        <?php if ($ticket['status'] !== 'completed'): ?>
                            <form method="post" action="">
                                <input type="hidden" name="ticketId" value="<?= $ticket['id'] ?>">
                                <button type="submit">Mark as Complete</button>
                            </form>
                        <?php else: ?>
                            Completed
                        <?php endif; ?>
                    </td>
                </tr>
            <?php endforeach; ?>
        <?php else: ?>
            <tr>
                <td colspan="6">No tickets assigned to you.</td>
            </tr>
        <?php endif; ?>
    </tbody>
</table>

<?php include_once 'footer.php'; ?>
</body>
</html>
