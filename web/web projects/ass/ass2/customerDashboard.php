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

$filters = [];
$params = [];

if (isset($_GET)) {
    $filters[] = "status = 'pending'";
}

if ($_SERVER["REQUEST_METHOD"] == "POST" && (!empty($_POST['description']) || 
!empty($_POST['submittedDate']) || !empty($_POST['status']) || !empty($_POST['emergencyLevel']))) {
    $filters = [];
    if (!empty($_POST['description'])) {
        $filters[] = "description LIKE :description";
        $params[':description'] = "%" . $_POST['description'] . "%";
    }
    if (!empty($_POST['submittedDate'])) {
        $filters[] = "DATE(submitteddate) = :submittedDate";
        $params[':submittedDate'] = $_POST['submittedDate'];
    }
    if (!empty($_POST['status'])) {
        $filters[] = "status = :status";
        $params[':status'] = $_POST['status'];
    }
    if (!empty($_POST['emergencyLevel'])) {
        $filters[] = "emergencylevel = :emergencyLevel";
        $params[':emergencyLevel'] = $_POST['emergencyLevel'];
    }
}


$sql = "SELECT * FROM tickets WHERE customerid = :id";
if (!empty($filters)) {
    $sql .= " AND " . implode(" AND ", $filters);
}
$statement = $pdo->prepare($sql);
$statement->bindValue(':id', $_SESSION['userid']);
foreach ($params as $key => $value) {
    $statement->bindValue($key, $value);
}
$statement->execute();

$statusSql = "SELECT DISTINCT status FROM tickets";
$statusStatement = $pdo->prepare($statusSql);
$statusStatement->execute();

$emergencySql = "SELECT DISTINCT emergencylevel FROM tickets";
$emergencyStatement = $pdo->prepare($emergencySql);
$emergencyStatement->execute();
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Tickets List</title>
</head>
<body>
    <h1>Tickets List</h1>
    <form method="POST" action="<?= $_SERVER['PHP_SELF']; ?>">
        <fieldset>
            <legend>Advanced Ticket Search</legend>

            <label for="description">Description:</label>
            <input type="text" id="description" name="description">

            <label for="submittedDate">Submitted Date:</label>
            <input type="date" id="submittedDate" name="submittedDate">

            <label for="status">Status:</label>
            <select id="status" name="status">
                <option value="">All</option>
                <?php while ($status = $statusStatement->fetch()) {
                    echo "<option value='" . $status['status'] . "'>" . $status['status'] . "</option>";
                } ?>
            </select>

            <label for="emergencyLevel">Emergency Level:</label>
            <select id="emergencyLevel" name="emergencyLevel">
                <option value="">All</option>
                <?php while ($level = $emergencyStatement->fetch()) {
                    echo "<option value='" . $level['emergencylevel'] . "'>" . $level['emergencylevel'] . "</option>";
                } ?>
            </select>

            <button type="submit">Filter</button>
        </fieldset>
    </form>

    <?php
    echo "<table border=1>";
    echo "<thead>
        <tr>
            <th>Ticket ID</th>
            <th>Issue Description</th>
            <th>Submitted Date</th>
            <th>Customer Name</th>
            <th>Emergency Level</th>
            <th>Status</th>
            <th>Ticket Action</th>
        </tr>
    </thead>";
    echo "<tbody>";

    while ($row = $statement->fetch()) {
        $ticket = new Ticket(
            $row['id'],
            $row['customername'],
            $row['description'],
            $row['submitteddate'],
            $row['status'],
            $row['emergencylevel'],
            $row['assigneddate'],
            $row['assignedstaff'],
            $row['imagename'],
            $row['customeremail'],
            $row['location']
        );
        echo $ticket->displayTableForCustomer();
    }

    if ($statement->rowCount() == 0) {
        echo "<tr><td colspan='7'>No tickets found.</td></tr>";
    }

    echo "</tbody></table>";
    include_once 'footer.php';
    ?>
</body>
</html>
