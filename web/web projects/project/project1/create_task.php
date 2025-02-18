<?php
// create_task.php
require 'db.php.inc';
include 'dashboard.php';
if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'ProjectLeader') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}
?>
<main>
<?php
$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $name = $_POST['name'];
    $description = $_POST['description'];
    $project_id = $_POST['project_id'];
    $startdate = $_POST['startdate'];
    $enddate = $_POST['enddate'];
    $effort = $_POST['effort'];
    $status = $_POST['status'];
    $priority = $_POST['priority'];

    try {
        $stmt = $pdo->prepare("SELECT * FROM projects WHERE project_id = :project_id");
        $stmt->bindValue(':project_id', $project_id);
        $stmt->execute();
        $project = $stmt->fetch();
        $projectStartDate = $project['start_date'];
        $projectEndDate = $project['end_date'];
        if (strtotime($enddate) > strtotime($projectEndDate)) {
            $errors[] = "End Date must not exceed the Project's End Date.";
        }
        if (strtotime($startdate) < strtotime($projectStartDate)) {
            $errors[] = "Start Date must not be earlier than the Project's Start Date.";
        }

        if (strtotime($enddate) <= strtotime($startdate)) {
            $errors[] = "End Date must be later than the Start Date.";
        }

        if ($effort <= 0) {
            $errors[] = "Effort must be a positive numeric value.";
        }
        if (empty($errors)) {
            $stmt = $pdo->prepare("INSERT INTO tasks (name, description, project_id, start_date, end_date, effort, priority, status) 
            VALUES (:name, :description, :project_id, :startdate, :enddate, :effort, :priority, :status)");
            $stmt->bindValue(':name', $name);
            $stmt->bindValue(':description', $description);
            $stmt->bindValue(':project_id', $project_id);
            $stmt->bindValue(':startdate', $startdate);
            $stmt->bindValue(':enddate', $enddate);
            $stmt->bindValue(':effort', $effort);
            $stmt->bindValue(':priority', $priority);
            $stmt->bindValue(':status', $status);
            $stmt->execute();
            echo "<p class='positive-response'>Task $name successfully created</p>";
        }
    } catch (PDOException $e) {
        $errors[] = $e->getMessage();
    }
}
?>
    <main>
    <h1>Create Task</h1>
    <?php
    if (!empty($errors)) {
        echo '<div class="error-container">';
        foreach ($errors as $message) {
            echo "<p class='error-message'>$message</p>";
        }
        echo '</div>';
    }
    ?>
    <form action="<?php echo $_SERVER['PHP_SELF']; ?>" method="post" class="create-task-form">
        <fieldset>
            <legend>Task Details</legend>
            <label>Task Name</label>
            <input type="text" name="name" required>
            
            <label>Description</label>
            <textarea name="description"></textarea>
            
            <label>Project</label>
            <select name="project_id" required>
                <?php
                    $stmt = $pdo->query("SELECT project_id, title FROM projects WHERE team_leader_id = $_SESSION[user_id]");
                    while ($project = $stmt->fetch()) {
                        $project_id = $project['project_id'];
                        $title = $project['title'];
                        echo "<option value='$project_id'>$title</option>";
                    }
                ?>
            </select>
            <label for="startdate">Start Date</label>
            <input type="date" id="startdate" name="startdate" required>
            <label for="enddate">End Date</label>
            <input type="date" id="enddate" name="enddate" required>
            <label for="effort">Effort</label>
            <input type="number" id="effort" name="effort" required>
            <label for="status">Status</label>
            <select id="status" name="status" required>
                <option value="Pending">Pending</option>
                <option value="In Progress">In Progress</option>
                <option value="Completed">Completed</option>
            </select>
            <label for="priority">Priority</label>
            <select id="priority" name="priority" required>
                <option value="Low">Low</option>
                <option value="Medium">Medium</option>
                <option value="High">High</option>
            </select>
            <button type="submit">Create Task</button>
        
        </fieldset>
    </form>
<?php include 'footer.php'; ?>
