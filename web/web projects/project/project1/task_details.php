<?php
include 'dashboard.php';
require 'db.php.inc';

$task_id = $_GET['task_id'] ?? null;

if (!$task_id) {
    die("Task ID is required.");
}

try {
    $taskStmt = $pdo->prepare("
        SELECT tasks.task_id, tasks.name AS task_name, tasks.description, tasks.start_date, tasks.end_date,
               tasks.completion_percentage, tasks.status, tasks.priority, projects.title AS project_name
        FROM tasks
        INNER JOIN projects ON tasks.project_id = projects.project_id
        WHERE tasks.task_id = :task_id
    ");
    $taskStmt->bindValue(':task_id', $task_id, PDO::PARAM_INT);
    $taskStmt->execute();
    $task = $taskStmt->fetch(PDO::FETCH_ASSOC);

    if (!$task) {
        die("Task not found.");
    }

    $teamStmt = $pdo->prepare("
        SELECT users.user_id, users.full_name, team_assignments.start_date,
               team_assignments.contribution_percentage, team_assignments.end_date
        FROM team_assignments
        INNER JOIN users ON team_assignments.user_id = users.user_id
        WHERE team_assignments.task_id = :task_id
    ");
    $teamStmt->bindValue(':task_id', $task_id, PDO::PARAM_INT);
    $teamStmt->execute();
    $teamMembers = $teamStmt->fetchAll(PDO::FETCH_ASSOC);
} catch (PDOException $e) {
    die("Error fetching task details: " . $e->getMessage());
}
?>
    <main class="task-details-container">
        <section class="task-details">
            <h2>Task Information</h2>
            <p><strong>Task ID:</strong> <?= htmlspecialchars($task['task_id']) ?></p>
            <p><strong>Task Name:</strong> <?= htmlspecialchars($task['task_name']) ?></p>
            <p><strong>Description:</strong> <?= htmlspecialchars($task['description']) ?></p>
            <p><strong>Project:</strong> <?= htmlspecialchars($task['project_name']) ?></p>
            <p><strong>Start Date:</strong> <?= htmlspecialchars($task['start_date']) ?></p>
            <p><strong>End Date:</strong> <?= htmlspecialchars($task['end_date']) ?></p>
            <p><strong>Completion Percentage:</strong> <?= htmlspecialchars($task['completion_percentage']) ?>%</p>
            <p><strong>Status:</strong> <?= htmlspecialchars($task['status']) ?></p>
            <p><strong>Priority:</strong> <?= htmlspecialchars($task['priority']) ?></p>
        </section>

        <section class="team-members">
            <h2>Team Members</h2>
            <table class="team-table">
                <thead>
                    <tr>
                        <th>Photo</th>
                        <th>Member ID</th>
                        <th>Name</th>
                        <th>Start Date</th>
                        <th>End Date</th>
                        <th>Effort (%)</th>
                    </tr>
                </thead>
                <tbody>
                    <?php foreach ($teamMembers as $member): ?>
                        <tr>
                            <td>
                                <a href="profile.php?user_id=<?= $member['user_id']?>"><img src="./images/user.jpg" alt="Photo of <?= htmlspecialchars($member['full_name']) ?>" class="team-photo"></a>
                            </td>
                            <td><?= htmlspecialchars($member['user_id']) ?></td>
                            <td><?= htmlspecialchars($member['full_name']) ?></td>
                            <td><?= htmlspecialchars($member['start_date']) ?></td>
                            <td class=<?php $member['end_date'] ?? 'in-progress' ?>>
                                <?= htmlspecialchars($member['end_date'] ?? 'In Progress') ?>
                            </td>
                            <td><?= htmlspecialchars($member['contribution_percentage']) ?>%</td>
                        </tr>
                    <?php endforeach; ?>
                </tbody>
            </table>
        </section>
<?php include 'footer.php'; ?>
