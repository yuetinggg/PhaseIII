<?php
include_once 'include/db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
$restaurantName = "";
if(isset($_POST['restaurantName'])) {
  $restaurantName = $_POST['restaurantName'];
}

if(!empty($restaurantName)) {
  $newInquiry = new Inspection();
  $json_return = $newInquiry->getInspections($restaurantName);
  echo json_encode($json_return);
}

class Inspection {
  private $db;

  public function __construct(){
    $this->db = new DbConnect();
  }

  public function getRid($restaurantName, $connection) {
    $query = "select rid from restaurant where name = '$restaurantName'";
    $rid = mysqli_query($connection, $query);
    $result = "";
    while ($row = $rid->fetch_assoc()) {
        $result = $row['rid'];
    }
    return $result;
  }

  public function getInspections($restaurantName) {
    $connection = $this->db->getDb();
    $rid = $this->getRid($restaurantName, $connection);
    $json = array();
    $dates = array();
    $dateQuery = mysqli_prepare($connection, "SELECT * FROM inspection WHERE rid = '$rid' ORDER BY idate DESC LIMIT 2");
    mysqli_stmt_execute($dateQuery);
    mysqli_stmt_store_result($dateQuery);
    mysqli_stmt_bind_result($dateQuery, $rid, $iid, $idate, $totalscore, $passfail);
    $x = 0;
    if (mysqli_stmt_num_rows($dateQuery) == 2) {
      while(mysqli_stmt_fetch($dateQuery)) {
        $json[$x] = array('totalscore' => $totalscore, 'passfail' => $passfail, 'idate' => $idate);
        $dates[$x] = $idate;
        $x++;
      }
    } else {
      while(mysqli_stmt_fetch($dateQuery)) {
        $json[$x]= array('totalscore' => $totalscore, 'passfail' => $passfail, 'idate' => $idate);
        $dates[$x] = $idate;
        $x++;
      }
    }
    mysqli_stmt_free_result($dateQuery);
    for ($i = 0; $i < $x; $i++) {
      $dateSearch = $dates[$i];
      $everythingQuery = mysqli_prepare($connection, "SELECT itemnum, score FROM contains WHERE rid = '$rid' AND idate = '$dateSearch' ORDER BY itemnum ASC");
      mysqli_stmt_execute($everythingQuery);
      mysqli_stmt_store_result($everythingQuery);
      mysqli_stmt_bind_result($everythingQuery, $itemnum, $score);
      $y = 1;
      while($row = mysqli_stmt_fetch($everythingQuery)) {
        $json[$i][$y] = $score;
        $y++;
      }
      mysqli_stmt_free_result($everythingQuery);
    }
    return $json;
  }
}

?>
