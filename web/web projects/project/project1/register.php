<?php
session_start();
require 'db.php.inc';

if (!isset($_SESSION['user_info']) || !isset($_SESSION['e_account_info'])) {
    die("Session data not found. Please complete the registration process.");
}

$user_info = $_SESSION['user_info']; // Contains user information
$e_account_info = $_SESSION['e_account_info']; // Contains e-account details

try {
    $sql = "INSERT INTO users (full_name, house_no, street, city, country, date_of_birth, id_number, email, phone, role, qualification, skills, username, password)
            VALUES (:full_name, :house_no, :street, :city, :country, :date_of_birth, :id_number, :email, :phone, :role, :qualification, :skills, :username, :password)";
    $stmt = $pdo->prepare($sql);

    $stmt->bindValue(':full_name', $user_info['fullname']);
    $stmt->bindValue(':house_no', $user_info['houseno']);
    $stmt->bindValue(':street', $user_info['street']);
    $stmt->bindValue(':city', $user_info['city']);
    $stmt->bindValue(':country', $user_info['country']);
    $stmt->bindValue(':date_of_birth', $user_info['dateofbirth']);
    $stmt->bindValue(':id_number', $user_info['idno']);
    $stmt->bindValue(':email', $user_info['email']);
    $stmt->bindValue(':phone', $user_info['phone']);
    $stmt->bindValue(':role', $user_info['role']);
    $stmt->bindValue(':qualification', $user_info['qualification']);
    $stmt->bindValue(':skills', $user_info['skills']);
    $stmt->bindValue(':username', $e_account_info['username']);
    $stmt->bindValue(':password', $e_account_info['password']);

    $stmt->execute();

    $user_id = $pdo->lastInsertId();

} catch (PDOException $e) {
    die("Database error: " . $e->getMessage());
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles.css">
    <title>Registration Complete</title>
</head>
<body>
    <h1>Registration Complete</h1>
    <p class="positive-response">Your User ID: <?php echo htmlspecialchars($user_id); ?></p>
    <p><a href="login.php">Go to Login Page</a></p>
</body>
</html>

