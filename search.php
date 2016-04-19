<?php
include_once 'db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
class search{
  private $db;
  private $db_table = "restaurant";

  public function __construct(){
    $this->db = new DbConnect();
  }

  public function getRestaurants($name, $score, $lessThan, $zipcode, $cuisine) {
    $json = array();
    $connection = $this->db->getDb();
    $restaurantQuery = "";
    if (!($name === "") && !($cuisine === "Any Cuisine")) {
      if ($lessThan === "1") {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.name = '$name' AND r.zipcode = '$zipcode' AND r.cuisine = '$cuisine' AND i.totalscore <= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      } else {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.name = '$name' AND r.zipcode = '$zipcode' AND r.cuisine = '$cuisine' AND i.totalscore >= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      }
    } else if (($name === "") && !($cuisine === "Any Cuisine")) {
      if ($lessThan === "1") {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.zipcode = '$zipcode' AND r.cuisine = '$cuisine' AND i.totalscore <= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      } else {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.zipcode = '$zipcode' AND r.cuisine = '$cuisine' AND i.totalscore >= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      }
    } else if (($name === "") && ($cuisine === "Any Cuisine")) {
      if ($lessThan === "1") {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.zipcode = '$zipcode' AND i.totalscore <= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      } else {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.zipcode = '$zipcode' AND i.totalscore >= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      }
    } else if (!($name === "") && ($cuisine === "Any Cuisine")) {
      if ($lessThan === "1") {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.name = '$name' AND r.zipcode = '$zipcode' AND i.totalscore <= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      } else {
        $restaurantQuery = mysqli_prepare($connection, "SELECT r.name, r.county, r.street, r.city, r.state, r.zipcode, r.cuisine, i.totalscore, i.idate FROM restaurant r JOIN inspection i ON r.rid = i.rid WHERE 1 AND r.name = '$name' AND r.zipcode = '$zipcode' AND i.totalscore >= '$score' GROUP BY r.name ORDER BY i.totalscore DESC");
      }
    }
    mysqli_stmt_execute($restaurantQuery);
    mysqli_stmt_store_result($restaurantQuery);
    mysqli_stmt_bind_result($restaurantQuery, $rname, $rcounty, $rstreet, $rcity, $rstate, $rzipcode, $rcuisine, $rtotalscore, $ridate);
    while(mysqli_stmt_fetch($restaurantQuery)) {
        $temp = array();
        $temp['rname'] = $rname;
        $temp['rcounty'] = $rcounty;
        $temp['rstreet'] = $rstreet;
        $temp['rcity'] = $rcity;
        $temp['rstate'] = $rstate;
        $temp['rzipcode'] = $rzipcode;
        $temp['rcuisine'] = $rcuisine;
        $temp['rtotalscore'] = $rtotalscore;
        $temp['ridate'] = $ridate;
        $json[] = $temp;
    }
    mysqli_close($connection);
    return $json;

  }

  public function searchResults1($name, $score, $zipcode, $cuisine){
    $json = array();
    $connection = $this->db->getDb();
    $stmt = "SELECT * FROM restaurant, inspection WHERE restaurant.rid = inspection.rid AND totalscore >= 50 AND name = '$name' AND zipcode = '$zipcode' AND cuisine = '$cuisine'";
    $result = $connection->query($stmt);
    while($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    mysqli_close($connection);
    return $json;
  }

  public function searchResults2($score, $zipcode, $cuisine){
    $json = array();
    $connection = $this->db->getDb();
    $stmt = "SELECT * FROM restaurant, inspection WHERE restaurant.rid = inspection.rid AND totalscore >= 50 AND zipcode = '$zipcode' AND cuisine = '$cuisine'";
    $result = $connection->query($stmt);
    while($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    mysqli_close($connection);
    return $json;
  }

  public function getCuisine() {
    $json = array();
    $connection = $this->db->getDb();
    $stmt = "SELECT * FROM cuisines";
    $result = $connection->query($stmt);
    while($row = mysqli_fetch_assoc($result)) {
        $json[] = $row;
    }
    mysqli_close($connection);
    return $json;
  }
}
?>
