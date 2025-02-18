<?php

class Ticket
{
    private $id;
    private $customerName;
    private $customerEmail;
    private $location;
    private $description;
    private $submittedDate;
    private $status;
    private $emergencyLevel;
    private $assignedDate;
    private $assignedStaff;
    private $imageName;

    
    public function __construct($id, $customerName, $description, $submittedDate, $status,
     $emergencyLevel, $assignedDate, $assignedStaff, $imageName, $customerEmail, $location) {
        $this->id = $id;
        $this->customerName = $customerName;
        $this->description = $description;
        $this->submittedDate = $submittedDate;
        $this->status = $status;
        $this->emergencyLevel = $emergencyLevel;
        $this->assignedDate = $assignedDate;
        $this->assignedStaff = $assignedStaff;
        $this->imageName = $imageName;
        $this->customerEmail = $customerEmail;
        $this->location = $location;
    }

    public function getId() {
        return $this->id;
    }
    public function getCustomerName() { 
        return $this->customerName; 
    }
    public function getCustomerEmail() { 
        return $this->customerEmail; 
    }
    public function getLocation() { 
        return $this->location; 
    }
    public function getDescription() { 
        return $this->description; 
    }
    public function getSubmittedDate() { 
        return $this->submittedDate; 
    }
    public function getStatus() { 
        return $this->status; 
    }
    public function getEmergencyLevel() { 
        return $this->emergencyLevel; 
    }
    public function getAssignedDate() { 
        return $this->assignedDate; 
    }
    public function getAssignedStaff() { 
        return $this->assignedStaff; 
    }
    public function getImageName() { 
        return $this->imageName; 
    }

    public function displayTable() {
        return "<tr>
            <td>".$this->id."</td>
            <td>".$this->description."</td>
            <td>".$this->submittedDate."</td>
            <td>".$this->customerName."</td>
            <td>".$this->emergencyLevel."</td>
            <td>".$this->status."</td>
            <td><a href='assign.php?id=".$this->id."'><img src='./icons/assign.jpg' alt='Assign'></a>
            <a href='view.php?id=".$this->id."'><img src='./icons/view.jpg' alt='View'></a></td>
        </tr>";
    }

    public function displayTableForCustomer() {
        return "<tr>
            <td>".$this->id."</td>
            <td>".$this->description."</td>
            <td>".$this->submittedDate."</td>
            <td>".$this->customerName."</td>
            <td>".$this->emergencyLevel."</td>
            <td>".$this->status."</td>
            <td><a href='view.php?id=".$this->id."'><img src='./icons/view.jpg' alt='View'></a></td>
        </tr>";
    }

    public function displayTableForStaff() {
        return "<tr>
            <td>".$this->id."</td>
            <td>".$this->description."</td>
            <td>".$this->submittedDate."</td>
            <td>".$this->customerName."</td>
            <td>".$this->emergencyLevel."</td>
            <td>".$this->status."</td>
            <td><a href='view.php?id=".$this->id."'><img src='./icons/view.jpg' alt='View'></a>
            <a href='do.php?id=".$this->id."'><img src='./icons/do.png' alt='View' width='40' height='31'></a></td>
        </tr>";
    }

    public function displayTicketPage() {
        return "<main>
            <h1>View Ticket: #" . $this->id . "</h1>
            <p>Here is a summary of the information about " . $this->id . "</p> 
            <ul>
            <li><strong>Submitted By Customer:</strong> " . $this->customerName . "</li>
            <li><strong>Email:</strong> " . $this->customerEmail . "</li>
            <li><strong>Location:</strong> " . $this->location . "</li>
            <li><strong>Issue Description:</strong> " . $this->description . "</li>
            <li><strong>Emergency Level:</strong> " . $this->emergencyLevel . "</li>
            <li><strong>Submitted Date:</strong> " . $this->submittedDate . "</li>
            <li><strong>Ticket Status:</strong> " . $this->status . "</li>
            <li><strong>Assigned to:</strong> " . $this->assignedStaff . "</li>
            <li><strong>Photo Uploaded:</strong>" . 
                (!empty($this->imageName) 
                    ? " Yes<br><img src='./images/" . $this->imageName . "' alt='Ticket Image' style='max-width: 400px;'>"
                    : " No") . "
            </li>
            </ul>
        </main>";

    }
}
?>
