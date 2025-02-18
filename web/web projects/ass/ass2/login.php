<?php
session_start();
require 'dbconfig.in.php';

if (isset($_POST['login'])) {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $sql = "SELECT * FROM user WHERE email = :email";
    $statement = $pdo->prepare($sql);
    $statement->bindValue(':email', $email);
    $statement->execute();
    $row = $statement->fetch();

    if ($row && $password == $row['password']) {
        $_SESSION['email'] = $email;
        $_SESSION['name'] = $row['name'];
        $userType = $row['usertype'];
        $_SESSION['userid'] = $row['id'];
        $_SESSION['usertype'] = $userType; 
        if ($userType === 'manager') {
            header("Location: managerDashboard.php");
            exit();
        } elseif ($userType === 'customer') {
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
        exit();
    } else {
        echo "<p>Invalid credentials</p>";
        header("Refresh: 3; url=loginPage.php");
        exit();
    }
}
?>
