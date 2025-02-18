<?php
// assign_team_members.php
include 'dashboard.php';
require 'db.php.inc';

$errors = [];
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'ProjectLeader') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$projectLeaderId = $_SESSION['user_id'];

try {
    $stmt = $pdo->prepare("SELECT project_id, title FROM projects WHERE team_leader_id = :team_leader_id");  
    $stmt->bindValue(':team_leader_id', $projectLeaderId);
    $stmt->execute();
    $projects = $stmt->fetchAll();
} catch (PDOException $e) {
    $errors[] = "Error fetching projects: " . $e->getMessage();
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
        <?php if (empty($projects)): ?>
            <p>No projects available.</p>
        <?php else: ?>
          <form action="show_tasks.php" method="post" class="project-selection-form">
            <fieldset>
                <legend>Select project</legend>
                <select name="project_id" id="project_id">
                    <?php foreach ($projects as $project): ?>
                        <option value="<?= $project['project_id'] ?>"><?= htmlspecialchars($project['title']) . "-" .
                        $project['project_id'] ?></option>
                    <?php endforeach; ?>
                </select>
                <button type="submit" class="confirm-button">Select</button>
            </fieldset>
          </form>
        <?php endif; 
        include 'footer.php';?>