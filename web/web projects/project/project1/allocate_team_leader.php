<?php
// allocate_team_leader.php
include 'dashboard.php';
require 'db.php.inc';
$errors = [];
if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $project_id = $_POST['project_id'];
    $leader_id = $_POST['leader_id'];

    if (empty($leader_id)) {
        $errors[] = "Error: Team Leader selection is required.";
        die();
    }

    try {
        $stmt = $pdo->prepare("UPDATE projects SET team_leader_id = :team_leader_id WHERE project_id = :project_id");
        $stmt->bindValue(':team_leader_id', $leader_id);
        $stmt->bindValue(':project_id', $project_id);
        $stmt->execute();

        echo "<p class='positive-response'>Team Leader successfully allocated to Project $project_id.</p>";
    } catch (PDOException $e) {
        $errors[] = $e->getMessage();
    }
}
$project_id = $_GET['project_id'] ?? null;

try {
    $stmt = $pdo->prepare("SELECT * FROM projects WHERE project_id = :project_id");
    $stmt->bindValue(':project_id', $project_id);
    $stmt->execute();
    $project = $stmt->fetch();

    if (!$project) {
        $errors[] = "Project not found.";
        die();
    }

    $stmt = $pdo->query("SELECT user_id, full_name FROM users WHERE role = 'ProjectLeader'");
    $leaders = $stmt->fetchAll();

    $stmt = $pdo->prepare("SELECT title, file_path FROM project_documents WHERE project_id = :project_id");
    $stmt->bindValue(':project_id', $project_id);
    $stmt->execute();
    $documents = $stmt->fetchAll();
} catch (PDOException $e) {
    $errors[] = $e->getMessage();
    die();
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
    <h1>Allocate Team Leader to Project</h1>
    <form action="<?= $_SERVER['PHP_SELF']; ?>" method="post" class="allocateleaderform">
        <fieldset>
            <legend>Project Details</legend>
            <label>Project ID:</label>
            <input type="text" value="<?= htmlspecialchars($project['project_id']) ?>" disabled>
            <label>Project Title:</label>
            <input type="text" value="<?= htmlspecialchars($project['title']) ?>" disabled>
            <label>Project Description:</label>
            <textarea disabled><?= htmlspecialchars($project['description']) ?></textarea>
            <label>Customer Name:</label>
            <input type="text" value="<?= htmlspecialchars($project['customer_name']) ?>" disabled>
            <label>Total Budget:</label>
            <input type="text" value="<?= htmlspecialchars($project['total_budget']) ?>" disabled>
            <label>Start Date:</label>
            <input type="date" value="<?= htmlspecialchars($project['start_date']) ?>" disabled>
            <label>End Date:</label>
            <input type="date" value="<?= htmlspecialchars($project['end_date']) ?>" disabled>
        </fieldset>
        <fieldset>
            <legend>Select Team Leader</legend>
            <label>Team Leader:</label>
            <select name="leader_id" required>
                <option value="">Select a team leader</option>
                <?php foreach ($leaders as $leader): ?>
                    <option value="<?= htmlspecialchars($leader['user_id']) ?>">
                        <?= htmlspecialchars($leader['full_name']) ?> - <?= htmlspecialchars($leader['user_id']) ?>
                    </option>
                <?php endforeach; ?>
            </select>
            <button type="submit" class="confirm-button">Confirm Allocation</button>
        </fieldset>
        <input type="hidden" name="project_id" value="<?= htmlspecialchars($project['project_id']) ?>">
    </form>
    <section class="supporting-documents">
        <h2>Supporting Documents</h2>
        <ul>
            <?php foreach ($documents as $document): ?>
                <li>
                    <a href="<?= htmlspecialchars($document['file_path']) ?>">
                        <?= htmlspecialchars($document['title']) ?>
                    </a>
                </li>
            <?php endforeach; ?>
        </ul>
    </section>
<?php include 'footer.php'; ?>
