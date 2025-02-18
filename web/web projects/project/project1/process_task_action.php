<?php
include 'dashboard.php';
require 'db.php.inc';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $task_id = $_POST['task_id'];
    $action = $_POST['action'];
    $role = $_POST['role'];
    $user_id = $_SESSION['user_id'];

    if (!$task_id || !$action) {
        die("Error: Invalid task or action.");
    }

    try {
        if ($action === 'accept') {
            $stmt = $pdo->prepare("
                UPDATE tasks 
                SET status = 'Active'
                WHERE task_id = :task_id;
            ");
            $stmt->bindValue(':task_id', $task_id);
            $stmt->execute();

            $stmt = $pdo->prepare("
                UPDATE team_assignments 
                SET accepted = 1
                WHERE task_id = :task_id AND user_id = :user_id AND role = :role;
            ");
            $stmt->bindValue(':task_id', $task_id);
            $stmt->bindValue(':user_id', $user_id);
            $stmt->bindValue(':role', $role);
            $stmt->execute();

            echo "<main><p class='positive-response'>Task successfully accepted and activated.</p>";
        } elseif ($action === 'reject') {
            $stmt = $pdo->prepare("DELETE FROM team_assignments 
                WHERE task_id = :task_id AND user_id = :user_id AND role = :role");
            $stmt->bindValue(':task_id', $task_id);
            $stmt->bindValue(':user_id', $user_id);
            $stmt->bindValue(':role', $role);
            $stmt->execute();

            echo "<main><p class='positive-response'>Task assignment successfully rejected.</p>";
        } else {
            die("Error: Invalid action.");
        }
    } catch (PDOException $e) {
        die("Error processing task action: " . $e->getMessage());
    }
}
include 'footer.php';
?>
