<?php
include_once 'include/db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
$iid = "";
$rid = "";
$date = "";
$totalscore = "";
$passfail = "";
$scores = "";
$items = "";
if(isset($_POST['iid'])) {
  $iid = $_POST['iid'];
}
if(isset($_POST['rid'])) {
  $rid = $_POST['rid'];
}
if(isset($_POST['date'])) {
  $date = $_POST['date'];
}
if(isset($_POST['totalscore'])) {
  $totalscore = $_POST['totalscore'];
}
if(isset($_POST['passfail'])) {
  $passfail = $_POST['passfail'];
}
if(isset($_POST['scores'])) {
  $scores = $_POST['scores'];
}
if(isset($_POST['items'])) {
  $items = $_POST['items'];
}
if (!empty($items)) {
  $newInspection = new SubmitInspection();
  $json_return = $newInspection->submitComments($rid, $date, $items);
  echo json_encode($json_return);
} else {
  $newInspection = new SubmitInspection();
  $json_return = $newInspection->submitForm($iid, $rid, $date, $totalscore, $passfail, $scores);
  echo json_encode($json_return);
}


class SubmitInspection {
  private $db;

  public function __construct(){
    $this->db = new DbConnect();
  }

  public function submitForm($iid, $rid, $date, $totalscore, $passfail, $scores) {
    $json = array();
    $connection = $this->db->getDb();
    $scores = stripslashes($scores);
    $scores = json_decode($scores);
    $query = mysqli_prepare($connection, "INSERT INTO inspection (rid, iid, idate, totalscore, passfail) VALUES ('$rid', '$iid', '$date', '$totalscore', '$passfail')");
    mysqli_stmt_execute($query);
    $itemNum = 1;
    foreach ($scores as $val) {
      $string = "1";
      $score = $val->$string;
      $query = mysqli_prepare($connection, "INSERT INTO contains (itemnum, rid, idate, score) VALUES ('$itemNum', '$rid', '$date', '$score')");
      mysqli_stmt_execute($query);
      $itemNum++;
    }
    $json['success'] = 1;
    return $json;
  }

  public function submitComments($rid, $date, $items) {
    $json = array();
    $connection = $this->db->getDb();
    $items = stripslashes($items);
    $items = json_decode($items);
    foreach ($items as $val) {
      $itemnum = "itemnum";
      $comment = "comment";
      $itemNum = $val->$itemnum;
      $Comment = $val->$comment;
      $query = mysqli_prepare($connection, "INSERT INTO includes (itemnum, rid, idate, comment) VALUES ('$itemNum', '$rid', '$date', '$Comment')");
      mysqli_stmt_execute($query);
    }
    $json['success'] = 1;
    return $json;
  }
}


?>
