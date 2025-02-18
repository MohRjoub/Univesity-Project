<?php
// process_allocation.php
include 'dashboard.php';
require 'db.php.inc';

$errors = [];
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'ProjectLeader') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}


if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $task_id = $_POST['task_id'];
    $user_id = $_POST['team_member_id'];
    $role = $_POST['role'];
    $contribution = $_POST['contribution'];
    $start_date = $_POST['start_date'];

    if (empty($task_id) || empty($user_id) || empty($role) || empty($contribution)) {
        $errors[] = "All fields are required.";
    }

    if ($contribution <= 0 || $contribution > 100) {
        $errors[] = "Contribution percentage must be between 1 and 100.";
    }

    try {
        $stmt = $pdo->prepare("SELECT SUM(contribution_percentage) AS total FROM team_assignments WHERE task_id = :task_id");
        $stmt->bindValue(':task_id', $task_id);
        $stmt->execute();
        $result = $stmt->fetch();
        $total_contribution = $result['total'] ?? 0;

        if (($total_contribution + $contribution) > 100) {
            $errors[] = "Total contribution percentage for the task cannot exceed 100%.";
        }

       if (empty($errors)) {
            $stmt = $pdo->prepare("INSERT INTO team_assignments (task_id, user_id, role, contribution_percentage, start_date)
            VALUES (:task_id, :user_id, :role, :contribution, :start_date)");
            $stmt->bindValue(':task_id', $task_id);
            $stmt->bindValue(':user_id', $user_id);
            $stmt->bindValue(':role', $role);
            $stmt->bindValue(':contribution', $contribution);
            $stmt->bindValue(':start_date', $start_date);
            $stmt->execute();

            $stmt = $pdo->prepare("SELECT project_id FROM tasks WHERE task_id = :task_id");
            $stmt->bindValue(':task_id', $task_id);
            $stmt->execute();
            $project_id = $stmt->fetch()['project_id'];
            ?>
            <main>
            <?php
            echo "<p class='positive-response'>Team member successfully assigned to Task $task_id as $role.</p>";
        }
    } catch (PDOException $e) {
         $errors[] = "Error assigning team member: " . $e->getMessage();
    }
}
?>
    <?php
    if (!empty($errors)) {
        echo '<div class="error-container">';
        foreach ($errors as $message) {
            echo "<p class='error-message'>$message</p>";
        }
        echo '</div>';
    }
    ?>
    <?php if (empty($errors)): ?>
        <form action="assign_team_member_form.php" method="GET" class="assign-another-form">
            <input type="hidden" name="task_id" value="<?= $_POST['task_id'] ?>">
            <button type="submit" class="confirm-button">Assign another team member</button>
        </form>
        <form action="show_tasks.php" method="POST" class="finish-allocation-form">
            <input type="hidden" name="project_id" value="<?= $project_id ?>">
            <button type="submit" class="confirm-button">Finish Allocation</button>
        </form>
    <?php endif; ?>
<?php
include 'footer.php';
?>
