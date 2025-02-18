<?php
session_start();
if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Assign Ticket</title>
</head>
<body>
<?php 
include 'managerHeader.php';

echo "<p>Welcome ". $_SESSION['name'] ."</p><hr>";
require 'dbconfig.in.php';

$ticketId = $_GET['id'];

$sql = "SELECT * FROM tickets WHERE id = :id";
$statement = $pdo->prepare($sql);
$statement->bindValue(':id', $ticketId);
$statement->execute();
$ticket = $statement->fetch();

if (!$ticket) {
    echo "Invalid ticket ID.";
    exit();
}

if ($ticket['status'] == 'assigned') {
    echo "<p>This ticket has already been assigned.</p>";
    header("Refresh: 3; url=view.php?id=$ticketId");
    exit();
} elseif ($ticket['status'] == 'completed') {
    echo "<p>This ticket has already been completed.</p>";
    header("Refresh: 3; url=view.php?id=$ticketId");
    exit();
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $staffId = $_POST['assignMember'];
    $ticketId = $_POST['ticketId'];

    if (empty($staffId) || empty($ticketId)) {
        echo "Invalid input.";
        exit();
    }

    $staffSql = "SELECT name FROM user WHERE id = :id";
    $staffStmt = $pdo->prepare($staffSql);
    $staffStmt->bindValue(':id', $staffId);
    $staffStmt->execute();
    $staff = $staffStmt->fetch();

    if (!$staff) {
        echo "Invalid staff selected.";
        exit();
    }

    $staffId = $staff['id'];

    $updateSql = "UPDATE tickets SET status = 'assigned', assignedstaff = :staffId, assigneddate = NOW() WHERE id = :ticketId";
    $updateStatement = $pdo->prepare($updateSql);
    $updateStatement->bindValue(':staffId', $staffId);
    $updateStatement->bindValue(':ticketId', $ticketId);

    if ($updateStatement->execute()) {
        echo "Ticket successfully assigned!";
        header("Location: view.php?id=$ticketId");
        exit();
    } else {
        echo "Failed to assign the ticket.";
    }
}
?>

<h2>Assign Ticket #<?= $ticketId ?></h2>
<form method="post" action="<?= $_SERVER['PHP_SELF'] . "?id=$ticketId"; ?>">
    <fieldset>
        <legend>Assign Form</legend>
        <input type="hidden" name="ticketId" value="<?= ($ticket['id']); ?>">
        <p><strong>Issue Description:</strong> <?= ($ticket['description']); ?></p>
        <p><strong>Urgency Level:</strong> <?= ($ticket['emergencylevel']); ?></p>
        <p><strong>Date Submitted:</strong> <?= ($ticket['submitteddate']); ?></p>
        <p>
            <label for="assign-member">Assign to Staff Member</label>
            <select id="assign-member" name="assignMember" required>
                <option value="">Select Staff</option>
                <?php 
                $staffSql = "SELECT id, name FROM user WHERE usertype = :usertype";
                $staffStmt = $pdo->prepare($staffSql);
                $staffStmt->bindValue(':usertype', 'staff');
                $staffStmt->execute();
                while ($staff = $staffStmt->fetch()) {
                    echo "<option value='" . $staff['id'] . "'>" . $staff['name'] . "</option>";
                }
                ?>
            </select>
        </p>
        <p>
            <button type="submit">Assign</button>
        </p>
    </fieldset>
</form>
<?php include_once 'footer.php'; ?>
</body>
</html>
