<?php
// dashboard.php
include 'header.php';
require 'db.php.inc';
if (!isset($_SESSION['user_id'])) {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}
$current_page = basename($_SERVER['PHP_SELF']);
$user_id = $_SESSION['user_id'];
try {
    $stmt = $pdo->prepare("
        SELECT COUNT(*) AS new_assignments 
        FROM tasks t
        JOIN team_assignments ta ON t.task_id = ta.task_id
        WHERE ta.user_id = :user_id AND ta.accepted = 0
    ");
    $stmt->bindValue(':user_id', $user_id, PDO::PARAM_INT);
    $stmt->execute();
    $result = $stmt->fetch();
    $new_assignments = $result['new_assignments'] ?? 0;
} catch (PDOException $e) {
    die("Error checking new assignments: " . $e->getMessage());
}
?>
<div class="container">
    <nav class="sidebar">
        <ul>
            <?php
            switch ($_SESSION['role']) {
                case 'Manager':
                    echo "<li><a href='add_project.php' class='" . ($current_page == 'add_project.php' ? 'active' : '') . "'>Add Project</a></li>";
                    echo "<li><a href='unassigned_projects.php' class='" . (($current_page == 'unassigned_projects.php') ||
                    ($current_page == 'allocate_team_leader.php') ? 'active' : '') . "'>Allocate Team Leader</a></li>";
                    echo "<li><a href='search_tasks.php' class='" . ($current_page == 'search_tasks.php' ? 'active' : '') . "'>Search for Tasks</a></li>";
                    break;
                case 'ProjectLeader':
                    echo "<li><a href='create_task.php' class='" . ($current_page == 'create_task.php' ? 'active' : '') . "'>Create Task</a></li>";
                    echo "<li><a href='assign_team_members.php' class='" . (($current_page == 'assign_team_members.php') || 
                    ($current_page == 'assign_team_member_form.php') || ($current_page == 'show_tasks.php') || 
                    ($current_page == 'process_allocation.php') ? 'active' : '') . "'>Assign Team Members</a></li>";
                    echo "<li><a href='search_tasks.php' class='" . ($current_page == 'search_tasks.php' ? 'active' : '') . "'>Search for Tasks</a></li>";
                    break;
                case 'TeamMember':
                    echo "<li>
                        <a href='view_tasks.php' class='" . 
                        (($current_page == 'view_tasks.php' || $current_page == 'show_task_details.php') ? 'active' :
                        ($new_assignments > 0 ? 'highlight-link' : '')) . "'>Assignments";
                        if ($new_assignments > 0) {
                            echo "<span class='badge'>" . htmlspecialchars($new_assignments) . "</span>";
                        }
                        echo "</a>
                        </li>";
                    echo "<li><a href='search_tasks.php' class='" . (($current_page == 'update_task.php') ||
                    ($current_page == 'search_tasks.php') || ($current_page == 'TMtask_details.php') ? 'active' : '') . "'>Search or Update Task</a></li>";
                    break;
            }
            ?>
        </ul>
    </nav>