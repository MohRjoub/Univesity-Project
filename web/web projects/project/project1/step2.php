<?php
include('header.php');
$errorMessages = [];
if($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['fullname'])) {
    $_SESSION['user_info'] = $_POST;
}
if ($_SERVER['REQUEST_METHOD'] == 'POST' && isset($_POST['username'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $confirmPassword = $_POST['confirm_password'];

    if (strlen($username) < 6 || strlen($username) > 13) {
        $errorMessages[] = "Username must be between 6 and 13 characters.";
    }

    if (strlen($password) < 8 || strlen($password) > 12) {
        $errorMessages[] = "Password must be between 8 and 12 characters.";
    }

    if ($password !== $confirmPassword) {
        $errorMessages[] = "Passwords do not match.";
    }

    if (empty($errorMessages)) {
        $_SESSION['e_account_info'] = $_POST;
        echo '<meta http-equiv="refresh" content="0;url=step3.php">';
        exit;
    }
}
?>
<main>
    <h1>E-Account Creation</h1>
    <?php
    if (!empty($errorMessages)) {
        echo '<div class="error-container">';
        foreach ($errorMessages as $message) {
            echo "<p class='error-message'>$message</p>";
        }
        echo '</div>';
    }
    ?>
    <form action="step2.php" method="post" class="registerform">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <label for="confirm_password">Confirm Password:</label>
        <input type="password" id="confirm_password" name="confirm_password" required><br><br>

        <button type="submit">Proceed to Confirmation</button>
    </form>
<?php include('footer.php'); ?>
