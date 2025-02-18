<?php
define('DBHOST', 'localhost');
define('DBNAME', 'ticketSystem');
define('DBUSER', 'root');
define('DBPASS', '');
define('DBCONNSTRING',"mysql:host=". DBHOST. ";dbname=". DBNAME);
try {
    $pdo = new PDO(DBCONNSTRING,DBUSER,DBPASS);
    }
    catch (PDOException $e) {
    die( $e->getMessage() );
    }
?>