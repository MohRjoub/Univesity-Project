<?php
// unassigned_projects.php
include 'dashboard.php';
require 'db.php.inc';

try {
    $stmt = $pdo->query("SELECT project_id, title, start_date, end_date FROM projects WHERE team_leader_id IS NULL ORDER BY start_date ASC");
    $projects = $stmt->fetchAll();
} catch (PDOException $e) {
    echo "Error fetching projects: " . $e->getMessage();
    die();
}
?>
    <main>
    <h1>Unassigned Projects</h1>
    <?php if (empty($projects)) {
        echo "<p>No unassigned projects available.</p>";
    } else { 
        echo "<table border='1' class='unassigned-projects'>
            <thead>
                <tr>
                    <th>Project Title</th>
                    <th>Start Date</th>
                    <th>End Date</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>";
                foreach ($projects as $project) {
                    echo "<tr>
                        <td>" . htmlspecialchars($project['title']) . "</td>
                        <td>" . htmlspecialchars($project['start_date']) . "</td>
                        <td>" . htmlspecialchars($project['end_date']) . "</td>
                        <td>
                            <a href='allocate_team_leader.php?project_id=" . $project['project_id'] . "'>Assign Team Leader</a>
                        </td>
                    </tr>";
                }
            echo "</tbody>
        </table>";
    }
include 'footer.php';