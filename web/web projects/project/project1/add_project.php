<?php
include('dashboard.php');
require 'db.php.inc';

if (!isset($_SESSION['user_id']) || $_SESSION['role'] !== 'Manager') {
        echo '<meta http-equiv="refresh" content="0;url=login.php">';
    exit();
}

$errorMessages = [];

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $projectId = $_POST['project_id'];
    $projectTitle = $_POST['project_title'];
    $projectDescription = $_POST['project_description'];
    $customerName = $_POST['customer_name'];
    $totalBudget = $_POST['total_budget'];
    $startDate = $_POST['start_date'];
    $endDate = $_POST['end_date'];
    $documentTitles = $_POST['titles'];
    $supportingDocuments = $_FILES['documents'];
    if (!preg_match('/^[A-Z]{4}-\d{5}$/', $projectId)) {
        $errorMessages[] = "Project ID must start with 4 uppercase alphabetic characters followed by a dash (-) and 5 digits.";
    }

    if ($totalBudget <= 0) {
        $errorMessages[] = "Total Budget must be a positive numeric value.";
    }

    if (strtotime($endDate) <= strtotime($startDate)) {
        $errorMessages[] = "End Date must be later than the Start Date.";
    }

    if (count($supportingDocuments['name']) > 3) {
        $errorMessages[] = "You can upload a maximum of three files.";
    }

    for ($i = 0; $i < count($supportingDocuments['name']); $i++) {
        if ($supportingDocuments['size'][$i] > 2097152) {
            $errorMessages[] = "File size must not exceed 2MB.";
        }
    }

    if (empty($errorMessages)) {
        try {
            $stmt = $pdo->prepare("INSERT INTO projects (project_id, title, description, customer_name, total_budget, start_date, end_date) 
                                   VALUES (:project_id, :title, :description, :customer_name, :budget, :start_date, :end_date)");
            $stmt->bindValue(':project_id', $projectId);
            $stmt->bindValue(':title', $projectTitle);
            $stmt->bindValue(':description', $projectDescription);
            $stmt->bindValue(':customer_name', $customerName);
            $stmt->bindValue(':budget', $totalBudget);
            $stmt->bindValue(':start_date', $startDate);
            $stmt->bindValue(':end_date', $endDate);
            $stmt->execute();


            $projectDocumentStmt = $pdo->prepare("INSERT INTO project_documents (project_id, title, file_path) VALUES (:project_id, :title, :file_path)");

            for ($i = 0; $i < count($supportingDocuments['name']); $i++) {
                if (!empty($supportingDocuments['name'][$i])) {
                    $fileName = basename($supportingDocuments['name'][$i]);
                    $fileTmpName = $supportingDocuments['tmp_name'][$i];
                    $fileType = strtolower(pathinfo($fileName, PATHINFO_EXTENSION));
                    $uniqueFileName = $projectId . $i . ".$fileType";
                    $destination = "documents/" . $uniqueFileName;

                    if (move_uploaded_file($fileTmpName, $destination)) {
                        $projectDocumentStmt->bindValue(':project_id', $projectId);
                        $projectDocumentStmt->bindValue(':title', $documentTitles[$i]);
                        $projectDocumentStmt->bindValue(':file_path', $destination);
                        $projectDocumentStmt->execute();
                    } else {
                        $errorMessages[] = "Failed to upload file: $fileName";
                    }
                }
            }

            echo "<p class='positive-response'>Project successfully added with supporting documents.</p>";

        } catch (Exception $e) {
            $errorMessages[] = "Error: " . $e->getMessage();
        }
    }
}
?>
    <main>
        <?php
    if (!empty($errorMessages)) {
        echo '<div class="error-container">';
        foreach ($errorMessages as $message) {
            echo "<p class='error-message'>$message</p>";
        }
        echo '</div>';
    }
    ?>
    <form action="<?php echo $_SERVER['PHP_SELF']?>" method="post" enctype="multipart/form-data" class="add-project-form">
        <fieldset class="project-details">
        <legend>Add Project</legend>
        <label for="project_id">Project ID</label>
        <input type="text" id="project_id" name="project_id" required 
        title="Project ID should start with 4 uppercase alphabetic characters followed by a dash (-) and 5 digits." placeholder="ex ABCD-11111"><br>

        <label for="project_title">Project Title</label>
        <input type="text" id="project_title" name="project_title" required><br>

        <label for="project_description">Project Description</label>
        <textarea id="project_description" name="project_description" required></textarea><br>

        <label for="customer_name">Customer Name</label>
        <input type="text" id="customer_name" name="customer_name" required><br>

        <label for="total_budget">Total Budget</label>
        <input type="number" id="total_budget" name="total_budget" required><br>

        <label for="start_date">Start Date</label>
        <input type="date" id="start_date" name="start_date" required><br>

        <label for="end_date">End Date</label>
        <input type="date" id="end_date" name="end_date" required><br>
        </fieldset>

        <fieldset class="project-documents">
        <legend>Supporting Documents</legend>
            <label for="document1">Document 1 Title</label>
            <input type="text" id="title1" name="titles[]">
            <input type="file" id="document1" name="documents[]" accept=".pdf,.docx,.png,.jpg">
        <br>
            <label for="document2">Document 2 Title</label>
            <input type="text" id="title2" name="titles[]">
            <input type="file" id="document2" name="documents[]" accept=".pdf,.docx,.png,.jpg">
        <br>
            <label for="document3">Document 3 Title</label>
            <input type="text" id="title3" name="titles[]">
            <input type="file" id="document3" name="documents[]" accept=".pdf,.docx,.png,.jpg">
        <br>
        <button type="submit" class="add-project">Add Project</button>
        </fieldset>

    </form>
<?php include('footer.php'); ?>
