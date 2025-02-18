<?php 
include 'dashboard.php';
require 'db.php.inc';
$errors = [];
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'ProjectLeader') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}
$projectLeaderId = $_SESSION['user_id'];
if( $_SERVER['REQUEST_METHOD'] === 'POST' ){
    $project_id = $_POST['project_id'];
    try {
        $stmt = $pdo->prepare("SELECT * FROM tasks s WHERE project_id = :project_id order by (
        SELECT COUNT(*) FROM team_assignments WHERE task_id = s.task_id ) ASC");
        $stmt->bindValue(':project_id', $project_id);
        $stmt->execute();
        $tasks = $stmt->fetchAll();
    } catch (PDOException $e) {
        $errors[] = "Error fetching team members: " . $e->getMessage();
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
    <?php if (empty($tasks)): ?>
        <p>No tasks available.</p>
    <?php else: ?>
        <h2>Tasks</h2>
        <table border="1">
            <thead>
                <tr>
                    <th>Task ID</th>
                    <th>Task Name</th>
                    <th>Strat Date</th>
                    <th>Status</th>
                    <th>Priority</th>
                    <th>Team Allocation</th>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($tasks as $task): ?>
                    <tr>
                        <td><?= $task['task_id'] ?></td>
                        <td><?= $task['name'] ?></td>
                        <td><?= $task['start_date'] ?></td>
                        <td><?= $task['status'] ?></td>
                        <td><?= $task['priority'] ?></td>
                        <td>
                            <a href="assign_team_member_form.php?task_id=<?= $task['task_id'] ?>" class="assign_team_member_link">Assign Team Members</a>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
    <?php endif; 
    include 'footer.php';?>