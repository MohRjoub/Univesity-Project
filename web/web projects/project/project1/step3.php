<?php
include('header.php');
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    if ($_POST['password'] == $_POST['confirm_password']) {
        $_SESSION['e_account_info'] = $_POST;
    } else {
        echo "Passwords do not match!";
        exit;
    }
}
$user_info = $_SESSION['user_info'];
$e_account_info = $_SESSION['e_account_info'];
?>
<main>
<body>
    <h1>Confirmation and Submission</h1>
    <form action="register.php" method="post" class="registerform">
        <h2>User Information</h2>
        <p>Name: <?php echo htmlspecialchars($user_info['fullname']); ?></p>
        <p>Flat/House No: <?php echo htmlspecialchars($user_info['houseno']); ?></p>
        <p>Street: <?php echo htmlspecialchars($user_info['street']); ?></p>
        <p>City: <?php echo htmlspecialchars($user_info['city']); ?></p>
        <p>Country: <?php echo htmlspecialchars($user_info['country']); ?></p>
        <p>Date of Birth: <?php echo htmlspecialchars($user_info['dateofbirth']); ?></p>
        <p>ID Number: <?php echo htmlspecialchars($user_info['idno']); ?></p>
        <p>E-mail Address: <?php echo htmlspecialchars($user_info['email']); ?></p>
        <p>Telephone: <?php echo htmlspecialchars($user_info['phone']); ?></p>
        <p>Role: <?php echo htmlspecialchars($user_info['role']); ?></p>
        <p>Qualification: <?php echo htmlspecialchars($user_info['qualification']); ?></p>
        <p>Skills: <?php echo htmlspecialchars($user_info['skills']); ?></p>

        <h2>E-Account Information</h2>
        <p>Username: <?php echo htmlspecialchars($e_account_info['username']); ?></p>

        <button type="submit">Confirm</button>
    </form>
<?php include('footer.php'); ?>