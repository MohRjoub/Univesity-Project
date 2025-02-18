<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
</head>
<body>
    <header>
    <img src="../ass1/logo.png" alt="logo" width="80" height="70">
    <h1>Rjoub For Maintenance Services</h1>
    </header>
    <hr>
    <h2>Login</h2>
    <form action="login.php" method="POST">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br><br>
        
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>
        
        <button type="submit" name="login">Login</button>
    </form>
    <hr>

    <?php include 'footer.php'; ?>
</body>
</html>
