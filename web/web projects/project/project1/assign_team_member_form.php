<?php
// assign_team_member_form.php
include 'dashboard.php';
require 'db.php.inc';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'ProjectLeader') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}
$task_id = $_GET['task_id'] ?? null;
if (!$task_id) {
    die("Task ID is required.");
}

try {
    $stmt = $pdo->prepare("SELECT * FROM tasks WHERE task_id = :task_id");
    $stmt->bindValue(':task_id', $task_id);
    $stmt->execute();
    $task = $stmt->fetch();

    $stmt = $pdo->query("SELECT user_id, full_name FROM users WHERE role = 'TeamMember'");
    $team_members = $stmt->fetchAll();
} catch (PDOException $e) {
    die("Error fetching data: " . $e->getMessage());
}
?>

<main>
    <h1>Allocate Team Members to Task</h1>
    <form action="process_allocation.php" method="post" class="team-allocation-form">
        <fieldset>
            <legend>Task Details</legend>
            <label>Task ID:</label>
            <input type="text" value="<?= htmlspecialchars($task['task_id']) ?>" readonly>
            <label>Task Name:</label>
            <input type="text" value="<?= htmlspecialchars($task['name']) ?>" readonly>
            <label>Start Date:</label>
            <input type="date" value="<?= date("Y-m-d");?>" name="start_date">
        </fieldset>
        <fieldset>
            <legend>Assign Team Member</legend>
            <label>Team Member:</label>
            <select name="team_member_id" required>
                <option value="">Select a team member</option>
                <?php foreach ($team_members as $member): ?>
                    <option value="<?= htmlspecialchars($member['user_id']) ?>">
                        <?= htmlspecialchars($member['full_name']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
            <label>Role:</label>
            <select name="role" required>
                <option value="">Select a role</option>
                <option value="Developer">Developer</option>
                <option value="Designer">Designer</option>
                <option value="Tester">Tester</option>
                <option value="Analyst">Analyst</option>
                <option value="Support">Support</option>
            </select>
            <label>Contribution Percentage:</label>
            <input type="number" name="contribution" min="1" max="100" required>
            <input type="hidden" name="task_id" value="<?= htmlspecialchars($task['task_id']) ?>">
            <button type="submit" class="confirm-button">Confirm Allocation</button>
        </fieldset>
    </form>
</main>
<?php include 'footer.php'; ?>