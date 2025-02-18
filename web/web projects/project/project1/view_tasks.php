<?php
// view_tasks.php
include 'dashboard.php';
require 'db.php.inc';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'TeamMember') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$teamMemberId = $_SESSION['user_id'];

try {
    $stmt = $pdo->prepare("
            SELECT 
            tasks.task_id, 
            tasks.name AS task_name, 
            ta.assignment_id, 
            tasks.start_date, 
            tasks.status, 
            projects.title AS project_name
        FROM tasks
        INNER JOIN team_assignments ta ON tasks.task_id = ta.task_id
        INNER JOIN projects ON tasks.project_id = projects.project_id
        WHERE ta.user_id = :user_id AND ta.accepted = 0");
    $stmt->bindValue(':user_id', $teamMemberId);
    $stmt->execute();
    $tasks = $stmt->fetchAll();
} catch (PDOException $e) {
    die("Error fetching tasks: " . $e->getMessage());
}
?>
    <main>
        <?php if (empty($tasks)): ?>
            <p>You have no assigned tasks.</p>
        <?php else: ?>
            <h2>Assigned Tasks</h2>
            <table class="tasks-table">
                <thead>
                    <tr>
                        <th>Task ID</th>
                        <th>Task Name</th>
                        <th>Project Name</th>
                        <th>Start Date</th>
                        <th>Confirm</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($tasks as $task): ?>
                        <tr>
                            <td><?= $task['task_id'] ?></td>
                            <td><?= $task['task_name'] ?></td>
                            <td><?= $task['project_name'] ?></td>
                            <td><?= $task['start_date'] ?></td>
                            <td>
                                <a href="show_task_details.php?task_id=<?= $task['task_id'] ?>&ta_id=<?= $task['assignment_id'] ?>" class="assign_team_member_link">Confirm</a>
                            </td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        <?php endif; ?>
<?php include 'footer.php'; ?>
