<?php
// show_task_details.php
include 'dashboard.php';
require 'db.php.inc';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'TeamMember') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$teamMemberId = $_SESSION['user_id'];
$taskId = $_GET['task_id'];
$assignmentId = $_GET['ta_id'];

try {
    $stmt = $pdo->prepare("SELECT t.task_id, t.name, t.description, t.priority, t.status, t.effort, ta.role, 
    t.completion_percentage, t.start_date, t.end_date FROM tasks t
        JOIN team_assignments ta ON t.task_id = ta.task_id
        WHERE t.task_id = :task_id AND ta.assignment_id = :assignment_id");
    $stmt->bindValue(':task_id', $taskId);
    $stmt->bindValue(':assignment_id', $assignmentId);
    $stmt->execute();
    $task = $stmt->fetch();
} catch (PDOException $e) {
    die("Error fetching tasks: " . $e->getMessage());
}
?>
<main>
    <h2>Task Details</h2>
    <form class="task-details-form">
        <fieldset>
            <legend>Task Details</legend>

            <label>Task ID:</label>
            <input type="text" value="<?= htmlspecialchars($task['task_id']) ?>" readonly>

            <label>Task Title:</label>
            <input type="text" value="<?= htmlspecialchars($task['name']) ?>" readonly>

            <label>Description:</label>
            <textarea readonly><?= htmlspecialchars($task['description']) ?></textarea>

            <label>Priority:</label>
            <input type="text" value="<?= htmlspecialchars($task['priority']) ?>" readonly>

            <label>Status:</label>
            <input type="text" value="<?= htmlspecialchars($task['status']) ?>" readonly>

            <label>Completion %:</label>
            <input type="text" value="<?= htmlspecialchars($task['completion_percentage']) ?>" readonly>

            <label>Total Effort:</label>
            <input type="text" value="<?= htmlspecialchars($task['effort']) ?> man-months" readonly>

            <label>Role:</label>
            <input type="text" value="<?= htmlspecialchars($task['role']) ?>" readonly>

            <label>Start Date:</label>
            <input type="date" value="<?= htmlspecialchars($task['start_date']) ?>" readonly>

            <label>End Date:</label>
            <input type="date" value="<?= htmlspecialchars($task['end_date']) ?>" readonly>
        </fieldset>

        <input type="hidden" name="task_id" value="<?= htmlspecialchars($task['task_id']) ?>">
        <input type="hidden" name="role" value="<?= htmlspecialchars($task['role']) ?>">
    </form>
<?php include 'footer.php'; ?>

