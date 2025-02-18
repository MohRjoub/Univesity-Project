<?php
include 'dashboard.php';
require 'db.php.inc';

if (!isset($_SESSION['user_id'])) {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$user_id = $_GET['user_id'] ?? $_SESSION['user_id'];

try {
    $stmt = $pdo->prepare("SELECT user_id, username, password, role, full_name, email, phone, 
            qualification, skills, house_no, street, city, country, 
            date_of_birth, id_number 
        FROM users 
        WHERE user_id = :user_id
    ");
    $stmt->bindValue(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();

    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    
} catch (PDOException $e) {
    die("Error fetching user details: " . $e->getMessage());
}
?>
    <main class="profile-container">
        <div class="profile-card">
            <img src="./images/user.jpg" alt="User Profile" class="user-image">
            <h2><?= htmlspecialchars($user['full_name']) ?></h2>
            <p><strong>Username:</strong> <?= htmlspecialchars($user['username']) ?></p>
            <p><strong>User Id</strong> <?= htmlspecialchars($user['user_id']) ?></p>
            <p><strong>Role:</strong> <?= htmlspecialchars($user['role']) ?></p>
            <p><strong>Email:</strong> <?= htmlspecialchars($user['email']) ?></p>
            <p><strong>Phone:</strong> <?= htmlspecialchars($user['phone']) ?></p>
            <p><strong>Qualification:</strong> <?= htmlspecialchars($user['qualification']) ?></p>
            <p><strong>Skills:</strong> <?= htmlspecialchars($user['skills']) ?></p>
            <p><strong>Address:</strong> <?= htmlspecialchars($user['house_no']) ?>, <?= htmlspecialchars($user['street']) ?>, <?= htmlspecialchars($user['city']) ?>, <?= htmlspecialchars($user['country']) ?></p>
            <p><strong>Date of Birth:</strong> <?= htmlspecialchars($user['date_of_birth']) ?></p>
            <p><strong>ID Number:</strong> <?= htmlspecialchars($user['id_number']) ?></p>
        </div>
        <?php
include 'footer.php';
