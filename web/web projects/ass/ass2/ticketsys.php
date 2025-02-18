<?php
session_start();

if (!isset($_SESSION['email'])) {
    header("Location: loginPage.php");
    exit();
}

$userType = $_SESSION['usertype'];

if ($userType == 'manager') {
    header("Location: managerDashboard.php");
    exit();
} elseif ($userType == 'customer') {
    header("Location: customerDashboard.php");
    exit();
} elseif ($userType === 'staff') {
    header("Location: staffDashboard.php");
    exit();
} else {
    session_destroy();
    header("Location: loginPage.php?error=Invalid user type.");
    exit();
}
?>
