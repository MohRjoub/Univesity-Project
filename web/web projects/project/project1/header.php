<?php 
session_start();
// Check if the user is logged in
$isLoggedIn = isset($_SESSION['user_id']);
$userType = $isLoggedIn ? $_SESSION['role'] : null;
$name = $isLoggedIn ? $_SESSION['fullname'] : null;
?>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="styles.css">
    <title>TAP</title>
</head>
<body>
<header class="header-container">
<div class="header-left">
    <h1 class="titel">Task Allocator Pro</h1>
</div>
<div class="header-right">
    <nav class="nav-container">
        <?php if (isset($_SESSION['user_id'])): ?>
            <a href="./logout.php" class="nav-btn">Logout</a>
        <?php else: ?>
            <a href="./login.php" class="nav-btn">Login</a>
            <a href="./registration.php" class="nav-btn">Sign-Up</a>
        <?php endif; ?>
        <?php if (isset($_SESSION['user_id'])): ?>
            <a href="./profile.php" class="profile-link">
                <img src="./images/user.jpg" alt="User Profile" class="profile-image">
                <span class="profile-name"><?= htmlspecialchars($_SESSION['fullname']) ?></span>
            </a>
        <?php endif; ?>
    </nav>
</div>
</header>