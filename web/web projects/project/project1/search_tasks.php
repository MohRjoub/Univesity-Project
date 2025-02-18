<?php
include 'dashboard.php';
require 'db.php.inc';

$user_role = $_SESSION['role'];
$user_id = $_SESSION['user_id'];
$sort = $_GET['sort'] ?? 'task_id';
$order = $_GET['order'] ?? 'asc';

$valid_columns = ['task_id', 'title', 'project_name', 'status', 'priority', 'start_date', 'end_date', 'completion_percentage'];
if (!in_array($sort, $valid_columns)) {
    $sort = 'task_id';
}
$order = ($order === 'asc') ? 'asc' : 'desc';
try {
    $query = "";
    if ($user_role === 'Manager') {
        $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                         t.status, t.priority, t.start_date, t.end_date, t.completion_percentage
                  FROM tasks t
                  JOIN projects ON t.project_id = projects.project_id ORDER BY $sort $order";
    }
    if ($user_role === 'TeamMember') {
        $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                     t.status, t.priority, t.start_date, t.end_date, t.completion_percentage, ta.assignment_id
              FROM tasks t
              JOIN projects ON t.project_id = projects.project_id JOIN team_assignments ta on ta.task_id = t.task_id 
              WHERE ta.user_id = :user_id AND ta.accepted = 1 ORDER BY $sort $order";
    } if ($user_role === 'ProjectLeader') {
        $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                     t.status, t.priority, t.start_date, t.end_date, t.completion_percentage
              FROM tasks t
              JOIN projects ON t.project_id = projects.project_id WHERE projects.team_leader_id = :user_id ORDER BY $sort $order";
    }

    $stmt = $pdo->prepare($query);
    if ($user_role !== 'Manager') {
        $stmt->bindValue(':user_id', $user_id);
    }
    $stmt->execute();
    $tasks = $stmt->fetchAll();

    if ($_SERVER['REQUEST_METHOD'] === 'GET') {
        $task_id = $_GET['task_id'] ?? '';
        $task_name = $_GET['task_name'] ?? '';
        $project_name = $_GET['project_name'] ?? '';
        $priority = $_GET['priority'] ?? '';
        $status = $_GET['status'] ?? '';
        $due_date = $_GET['end_date'] ?? '';

        if ($task_id || $task_name || $project_name || $priority || $status || $due_date) {
            if ($user_role === 'Manager') {
                $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                                 t.status, t.priority, t.start_date, t.end_date, t.completion_percentage
                          FROM tasks t
                          JOIN projects ON t.project_id = projects.project_id";
            }
            if ($user_role === 'TeamMember') {
                $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                            t.status, t.priority, t.start_date, t.end_date, t.completion_percentage, ta.assignment_id
                      FROM tasks t
                      JOIN projects ON t.project_id = projects.project_id 
                      JOIN team_assignments ta on ta.task_id = t.task_id WHERE ta.user_id = :user_id AND ta.accepted = 1";
            } if ($user_role === 'ProjectLeader') {
                $query = "SELECT t.task_id, t.name AS task_name, projects.title AS project_name, 
                            t.status, t.priority, t.start_date, t.end_date, t.completion_percentage
                        FROM tasks t
                        JOIN projects ON t.project_id = projects.project_id WHERE projects.team_leader_id = :user_id";
            }

            if ($task_id) {
                $query .= " AND t.task_id = :task_id";
            }
            if ($task_name) {
                $query .= " AND t.name LIKE :task_name";
            }
            if ($project_name) {
                $query .= " AND projects.title LIKE :project_name";
            }
            if ($priority) {
                $query .= " AND t.priority = :priority";
            }
            if ($status) {
                $query .= " AND t.status = :status";
            }
            if ($due_date) {
                $query .= " AND t.end_date = :end_date";
            }

            $stmt = $pdo->prepare($query);
            if ($user_role !== 'Manager') {
                $stmt->bindValue(':user_id', $user_id);
            }
            if ($task_id) {
                $stmt->bindValue(':task_id', $task_id);
            }
            if ($task_name) {
                $stmt->bindValue(':task_name', "%$task_name%");
            }
            if ($project_name) {
                $stmt->bindValue(':project_name', "%$project_name%");
            }
            if ($priority) {
                $stmt->bindValue(':priority', $priority);
            }
            if ($status) {
                $stmt->bindValue(':status', $status);
            }
            if ($due_date) {
                $stmt->bindValue(':end_date', $due_date);
            }
            $stmt->execute();
            $tasks = $stmt->fetchAll();
        }
    }

} catch (PDOException $e) {
    die("Error fetching tasks: " . $e->getMessage());
}
?>
    <main class="search-container">
        <form action="search_tasks.php" method="get" class="search-form">
            <fieldset>
                <legend>Search for Task</legend>
                <div class="search-fields">
                    <input type="text" name="task_id" placeholder="Task ID">
                    <input type="text" name="task_name" placeholder="Task Name">
                    <input type="text" name="project_name" placeholder="Project Name">
                    <select name="status" id="status">
                        <option value="">Status</option>
                        <option value="Pending">Pending</option>
                        <option value="In Progress">In Progress</option>
                        <option value="Completed">Completed</option>
                    </select>
                    <select name="priority" id="priority">
                        <option value="">Priority</option>
                        <option value="Low">Low</option>
                        <option value="Medium">Medium</option>
                        <option value="High">High</option>
                    </select>
                    <input type="date" name="end_date" placeholder="Due Date">
                    <button type="submit" class="searc-btn">Search</button>
                </div>
            </fieldset>
        </form>
        <table class="search-tasks-table">
            <thead>
                <tr>
                    <th><a href="?sort=task_id&order=<?= ($sort === 'task_id' && $order === 'asc') ? 'desc' : 'asc' ?>">Task ID</a></th>
                    <th><a href="?sort=title&order=<?= ($sort === 'title' && $order === 'asc') ? 'desc' : 'asc' ?>">Title</a></th>
                    <th><a href="?sort=project_name&order=<?= ($sort === 'project_name' && $order === 'asc') ? 'desc' : 'asc' ?>">Project</a></th>
                    <th><a href="?sort=status&order=<?= ($sort === 'status' && $order === 'asc') ? 'desc' : 'asc' ?>">Status</a></th>
                    <th><a href="?sort=priority&order=<?= ($sort === 'priority' && $order === 'asc') ? 'desc' : 'asc' ?>">Priority</a></th>
                    <th><a href="?sort=start_date&order=<?= ($sort === 'start_date' && $order === 'asc') ? 'desc' : 'asc' ?>">Start Date</a></th>
                    <th><a href="?sort=end_date&order=<?= ($sort === 'end_date' && $order === 'asc') ? 'desc' : 'asc' ?>">Due Date</a></th>
                    <th><a href="?sort=completion_percentage&order=<?= ($sort === 'completion_percentage' && $order === 'asc') ? 'desc' : 'asc' ?>">Completion %</a></th>
                    <?php if ($user_role == 'TeamMember') : ?>
                        <th>Action</th>
                    <?php endif; ?>
                </tr>
            </thead>
            <tbody>
                <?php foreach ($tasks as $task): ?>
                    <tr>
                        <td>
                            <a href="<?php
                            if ($user_role == 'Manager' || $user_role == 'ProjectLeader') {
                                echo 'task_details.php';
                            } else {
                                echo 'TMtask_details.php';
                            }?>?task_id=<?= $task['task_id'] ?>
                            <?php 
                                if($user_role == 'TeamMember'){
                                    echo '&ta_id=' . $task['assignment_id'];
                                }?>"><?= htmlspecialchars($task['task_id']) ?>
                            </a>
                        </td>  
                        <td><?= htmlspecialchars($task['task_name']) ?></td>
                        <td><?= htmlspecialchars($task['project_name']) ?></td>
                        <td class="<?= strtolower($task['status']) ?>"><?= htmlspecialchars($task['status']) ?></td>
                        <td class="<?= strtolower($task['priority']) ?>"><?= htmlspecialchars($task['priority']) ?></td>
                        <td><?= htmlspecialchars($task['start_date']) ?></td>
                        <td><?= htmlspecialchars($task['end_date']) ?></td>
                        <td><?= htmlspecialchars($task['completion_percentage']) ?>%</td>
                        <?php if ($user_role == 'TeamMember') : ?>
                            <td><a href="update_task.php?task_id=<?= $task['task_id'] ?>&ta_id=<?= $task['assignment_id']?>">Update</a></td>
                        <?php endif; ?>
                    </tr>
                <?php endforeach; ?>
            </tbody>
        </table>
<?php include 'footer.php'; ?>
