<?php include('header.php'); ?>
    <main>
    <form action="step2.php" method="post" class="registerform">
        <fieldset>
            <legend>Sign Up</legend>
            <label for="fullname">Full Name</label>
            <input type="text" id="fullname" name="fullname" required>
            <label>Address</label>
            <label for="street">Street</label>
            <input type="text" id="street" name="street" required>
            <label for="city">City</label>
            <input type="text" id="city" name="city" required>
            <label for="country">Country</label>
            <input type="text" id="country" name="country" required>
            <label for="houseno">House Number</label>
            <input type="text" id="houseno" name="houseno" required>
            <label for="dateofbirth">Date of Birth</label>
            <input type="date" id="dateofbirth" name="dateofbirth" required>
            <label for="idno">ID Number</label>
            <input type="text" id="idno" name="idno" required>
            <label for="email">Email</label>
            <input type="email" id="email" name="email" required>
            <label for="phone">Phone</label>
            <input type="text" id="phone" name="phone" required>
            <label for="role">Role</label>
            <select id="role" name="role" required>
                <option value="Manager">Manager</option>
                <option value="ProjectLeader">Project Leader</option>
                <option value="TeamMember">Team Member</option>
            </select>
            <label for="qualification">Qualification</label>
            <input type="text" id="qualification" name="qualification" required>
            <label for="skills">Skills</label>
            <textarea type="text" id="skills" name="skills" required></textarea>
            <button type="submit">Register</button>
        </fieldset>
    </form>
<?php include('footer.php'); ?>