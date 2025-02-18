<?php
// login.php
include 'header.php';
require 'db.php.inc';
$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    try {
        $stmt = $pdo->prepare("SELECT * FROM users WHERE username = ?");
        $stmt->execute([$username]);
        $user = $stmt->fetch();
        if ($user && $password == $user['password']) {
            $_SESSION['user_id'] = $user['user_id'];
            $_SESSION['fullname'] = $user['full_name'];
            $_SESSION['role'] = $user['role'];
            echo '<meta http-equiv="refresh" content="0;url=dashboard2.php">';
        } else {
            $errors[] = "Invalid username or password.";
        }
    } catch (PDOException $e) {
        echo "Error: " . $e->getMessage();
        $errors[] = "An error occurred.". $e->getMessage();
    }
}
?>
<main>
<?php
    if (!empty($errors)) {
        echo '<div class="error-container">';
        foreach ($errors as $message) {
            echo "<p class='error-message'>$message</p>";
        }
        echo '</div>';
    }
    ?>
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" class="login-form">
        <fieldset>
            <legend>Login</legend>
            <label for="username">Username</label>
            <input type="text" id="username" name="username" required>

            <label for="password">Password</label>
            <input type="password" id="password" name="password" required>

            <button type="submit">Login</button>
        </fieldset>
    </form>
<?php include('footer.php');