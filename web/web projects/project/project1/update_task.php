<?php
include 'dashboard.php';
require 'db.php.inc';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'TeamMember') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$errors = [];
$success_message = "";

if ($_SERVER['REQUEST_METHOD'] === 'GET' && isset($_GET['task_id'])) {
    $task_id = $_GET['task_id'];
    $assignment_id = $_GET['ta_id'];

    try {
        $stmt = $pdo->prepare("
            SELECT tasks.*, projects.title AS project_name 
            FROM tasks 
            INNER JOIN projects ON tasks.project_id = projects.project_id 
            WHERE tasks.task_id = :task_id
        ");
        $stmt->bindValue(':task_id', $task_id, PDO::PARAM_INT);
        $stmt->execute();
        $task = $stmt->fetch(PDO::FETCH_ASSOC);

        if (!$task) {
            $errors[] = "Task not found.";
        }
    } catch (PDOException $e) {
        $errors[] = "Error fetching task: " . $e->getMessage();
    }
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $task_id = $_POST['task_id'];
    $progress = $_POST['progress'];
    $status = $_POST['status'];
    $assignment_id = $_POST['assignment_id'];

    if ($progress === null || $progress === '') {
        $errors[] = "Progress is required.";
    } elseif (!is_numeric($progress) || $progress < 0 || $progress > 100) {
        $errors[] = "Progress must be a percentage between 0 and 100.";
    }

    if (!$status) {
        $errors[] = "Status is required.";
    }

    if ($status == 'Completed' && $progress < 100) {
        $errors[] = "Progress must be 100% for task to be marked as completed.";
    } if ($status == 'Pending' && $progress > 0) {
        $errors[] = "Progress must be 0% for task to be marked as pending.";
    } if ($status == 'In Progress' && ($progress == 0 || $progress == 100)) {
        $errors[] = "Progress must be greater than 0% and less than 100% for task to be marked as in progress.";
    } 
    if (empty($errors)) {
        try {
            $stmt = $pdo->prepare("
                UPDATE tasks 
                SET completion_percentage = :progress, status = :status 
                WHERE task_id = :task_id
            ");
            $stmt->bindValue(':progress', $progress, PDO::PARAM_INT);
            $stmt->bindValue(':status', $status, PDO::PARAM_STR);
            $stmt->bindValue(':task_id', $task_id, PDO::PARAM_INT);
            $stmt->execute();

            if ($status == 'Completed') {
                $stmt = $pdo->prepare("UPDATE team_assignments SET end_date = NOW() WHERE assignment_id = :assignment_id");
                $stmt->bindValue(':assignment_id', $assignment_id);
                $stmt->execute();
            }

            $success_message = "Task updated successfully!";
        } catch (PDOException $e) {
            $errors[] = "Error updating task: " . $e->getMessage();
        }
    }
}
?>
    <main>
        <?php if (!empty($errors)): ?>
            <div class="error-container">
                <?php foreach ($errors as $error): ?>
                    <p class="error-message"><?= htmlspecialchars($error) ?></p>
                <?php endforeach; ?>
            </div>
        <?php endif; ?>

        <?php if ($success_message): ?>
            <div class="success-container">
                <p class="positive-response"><?= htmlspecialchars($success_message) ?></p>
                <meta http-equiv="refresh" content="2;url=search_tasks.php">
            </div>
        <?php endif; ?>

        <?php if (!empty($task)): ?>
            <form action="update_task.php" method="post" class="update-task-form">
                <fieldset>
                    <legend>Update Task</legend>
                    <label for="task_id">Task ID</label>
                    <input type="text" name="task_id" value="<?= htmlspecialchars($task['task_id']) ?>" readonly>

                    <label for="task_name">Task Name</label>
                    <input type="text" id="task_name" value="<?= htmlspecialchars($task['name']) ?>" readonly>

                    <label for="project_name">Project Name</label>
                    <input type="text" id="project_name" value="<?= htmlspecialchars($task['project_name']) ?>" readonly>

                    <label for="progress">Progress</label>
                    <input type="range" name="progress" id="progress" min="0" max="100" value="<?= htmlspecialchars($task['completion_percentage']) ?>" 
                        oninput="this.nextElementSibling.value = this.value + '%';">
                    <input type="text" value="<?= htmlspecialchars($task['completion_percentage']) . "%" ?>" readonly>

                    <label for="status">Status</label>
                    <select name="status" id="status">
                        <option value="Pending" <?= $task['status'] === 'Pending' ? 'selected' : '' ?>>Pending</option>
                        <option value="In Progress" <?= $task['status'] === 'In Progress' ? 'selected' : '' ?>>In Progress</option>
                        <option value="Completed" <?= $task['status'] === 'Completed' ? 'selected' : '' ?>>Completed</option>
                    </select>
                    <input type="hidden" name="assignment_id" value="<?= htmlspecialchars($assignment_id) ?>">
                    <button type="submit">Save Changes</button>
                    <a href="search_tasks.php">Cancel</a>
                </fieldset>
            </form>
        <?php endif; ?>
<?php include 'footer.php'; ?>
