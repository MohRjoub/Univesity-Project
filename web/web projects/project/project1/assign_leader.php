<?php
// assign_leader.php
require 'db.php.inc';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $project_id = $_POST['project_id'];
    $user_id = $_POST['user_id'];

    try {
        $stmt = $pdo->prepare("INSERT INTO team_assignments (task_id, user_id, role) VALUES (?, ?, 'ProjectLeader')");
        $stmt->execute([$project_id, $user_id]);
        echo "Team Leader assigned successfully.";
    } catch (PDOException $e) {
        echo "Error: " . $e->getMessage();
    }
    exit();
}

$project_id = $_GET['project_id'] ?? '';
$stmt = $pdo->query("SELECT user_id, full_name FROM users WHERE role = 'ProjectLeader'");
echo "<h2>Assign Team Leader</h2>";
echo "<form method='post'>
        <input type='hidden' name='project_id' value='$project_id'>
        <label for='user_id'>Team Leader:</label>
        <select name='user_id' id='user_id'>";
while ($user = $stmt->fetch()) {
    echo "<option value='{$user['user_id']}'>{$user['full_name']}</option>";
}
echo "</select>
      <button type='submit'>Assign</button>
      </form>";
?>
