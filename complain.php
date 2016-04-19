<?php
include_once 'include/db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
$restaurant = "";
$date = "";
$firstName = "";
$lastName = "";
$phone = "";
$description = "";
if(isset($_POST['restaurant'])){
  $restaurant = $_POST['restaurant'];
}
if(isset($_POST['date'])){
  $date = $_POST['date'];
}
if(isset($_POST['firstName'])){
  $firstName = $_POST['firstName'];
}
if(isset($_POST['lastName'])){
  $lastName = $_POST['lastName'];
}
if(isset($_POST['phone'])){
  $phone = $_POST['phone'];
}
if(isset($_POST['description'])) {
  $description = $_POST['description'];
}

$complainObject = new Complain();
$json_complain = $complainObject->complaining($restaurant, $date, $firstName, $lastName, $phone, $description);
echo json_encode($json_complain);

class Complain {
  private $db;

  public function __construct(){
    $this->db = new DbConnect();
  }

  public function findRID($restaurant, $connection) {
    $query = "select rid from restaurant where name = '$restaurant'";
    $rid = mysqli_query($connection, $query);
    $result = "";
    while ($row = $rid->fetch_assoc()) {
        $result = $row['rid'];
    }
    return $result;
  }

  public function complaining($restaurant, $date, $firstName, $lastName, $phone, $description) {
    $json = array();
    $connection = $this->db->getDb();
    $rid = $this->findRID($restaurant, $connection);
    $insertComplaint = mysqli_prepare($connection, "insert into complaint (rid, phone, cdate, description) values ('$rid', '$phone', '$date', '$description')");
    mysqli_stmt_execute($insertComplaint);
    $insertCustomer = mysqli_prepare($connection, "insert into customer (phone, firstname, lastname) values ('$phone', '$firstName', '$lastName')");
    mysqli_stmt_execute($insertComplaint);
    $json['success'] = 1;
    mysqli_close($connection);
    return $json;

  }
}


?>
