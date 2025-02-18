<?php
include 'dashboard.php';
// Redirect to login if not logged in
if (!isset($_SESSION['user_id'])) {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}
$user_name = $_SESSION['fullname'];
?>
    <main class="main-content">
    <div class="welcome-page">
        <div class="welcome-content">
            <h1 class="welcome-titel">Welcome, <?= htmlspecialchars($user_name) ?>!</h1>
            <p>We are thrilled to have you back. Explore your dashboard to manage your tasks and projects effectively.</p>
        </div>
    </div>
<?php include 'footer.php';