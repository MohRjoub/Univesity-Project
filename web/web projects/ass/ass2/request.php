<?php
session_start();
if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}

require 'dbconfig.in.php';
require 'Ticket.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $name = $_SESSION['name'];
    $email = $_POST['email'];
    $location = $_POST['sender-location'];
    $description = $_POST['issue-description'];
    $urgencyLevel = $_POST['urgency-level'];
    $photo = $_FILES['upload-photo'];

    $photoFileName = null;
    if ($photo && $photo['tmp_name']) {
        $allowedTypes = ['image/jpeg'];
        if (!in_array($photo['type'], $allowedTypes)) {
            die('Only JPEG images are allowed.');
        }

        $ticketId = null;
        try {
            $stmt = $pdo->prepare(
                "INSERT INTO tickets (customername, customeremail, location, description, emergencylevel, status, submitteddate, customerid) 
                 VALUES (:customername, :customeremail, :location, :description, :emergencylevel, :status, NOW(), :customerid)"
            );
            $stmt->bindValue(':customername', $name);
            $stmt->bindValue(':customeremail', $_SESSION['email']);
            $stmt->bindValue(':location', $location);
            $stmt->bindValue(':description', $description);
            $stmt->bindValue(':emergencylevel', $urgencyLevel);
            $stmt->bindValue(':status', 'pending');
            $stmt->bindValue(':customerid', $_SESSION['userid']);
            $stmt->execute();

            $ticketId = $pdo->lastInsertId();

            $photoFileName = "images/" . $ticketId . ".jpeg";
            move_uploaded_file($photo['tmp_name'], $photoFileName);

            $updateStmt = $pdo->prepare("UPDATE tickets SET imagename = :imagename WHERE id = :id");
            $updateStmt->bindValue(':imagename', $ticketId.".jpeg");
            $updateStmt->bindValue(':id', $ticketId);
            
            $updateStmt->execute();
            header("Location: confirm.php?ticketId={$ticketId}&location={$location}&description={$description}&urgencyLevel={$urgencyLevel}&imageName={$photoFileName}");
        } catch (PDOException $e) {
            die("Error saving ticket: " . $e->getMessage());
        }
    } else {
        try {
            $stmt = $pdo->prepare(
                "INSERT INTO tickets (customername, customeremail, location, description, emergencylevel, status, submitteddate, customerid) 
                 VALUES (:customername, :customeremail, :location, :description, :emergencylevel, :status, NOW(), :customerid)"
            );
            $stmt->bindValue(':customername', $name);
            $stmt->bindValue(':customeremail', $_SESSION['email']);
            $stmt->bindValue(':location', $location);
            $stmt->bindValue(':description', $description);
            $stmt->bindValue(':emergencylevel', $urgencyLevel);
            $stmt->bindValue(':status', 'pending');
            $stmt->bindValue(':customerid', $_SESSION['userid']);
            $stmt->execute();
            $ticketId = $pdo->lastInsertId();
            header("Location: confirm.php?ticketId={$ticketId}&location={$location}&description={$description}&urgencyLevel={$urgencyLevel}");
        } catch (PDOException $e) {
            die("Error saving ticket: " . $e->getMessage());
        }
    }

    exit();
}

include 'customerHeader.php';
echo "<p>Welcome " . $_SESSION['name'] . "</p></header><hr>";
?>
<h2>Submit Maintenance Request</h2>
<form method="post" action="<?= $_SERVER['PHP_SELF']; ?>" enctype="multipart/form-data">
    <fieldset>
        <legend>Submit Maintenance Request Form</legend>
        <p>
            <label for="full-name">Full Name</label>
            <input type="text" id="full-name" name="full-name" value="<?= $_SESSION['name']; ?>" disabled>
        </p>
        <p>
            <label for="email">Email Address</label>
            <input type="email" id="email" name="email" placeholder="Enter your email" required>
        </p>
        <p>
            <label for="sender-location">Location/Room Number</label>
            <input type="text" id="sender-location" name="sender-location" placeholder="Enter the room or location" required>
        </p>
        <p>
            <label for="issue-description">Issue Description</label>
            <textarea id="issue-description" name="issue-description" rows="4" placeholder="Describe the issue..." required></textarea>
        </p>
        <p>
            <label for="urgency-level">Urgency Level</label>
            <select id="urgency-level" name="urgency-level">
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High</option>
            </select>
        </p>
        <p>
            <label for="upload-photo">Upload a Photo of the Issue (optional)</label>
            <input type="file" id="upload-photo" name="upload-photo" accept="image/jpeg">
        </p>
        <p>
            <button type="submit">Submit Request</button>
        </p>
    </fieldset>
</form>
<?php include_once 'footer.php'; ?>
</body>
</html>
