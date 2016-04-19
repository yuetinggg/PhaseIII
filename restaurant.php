<?php
include_once 'include/db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
$permitID = "";
$permitExpiration = "";
$cuisine = "";
$rname = "";
$street = "";
$city = "";
$state = "";
$zipcode = "";
$county = "";
$phone = "";
$username = "";
$getRestaurantsOwned = "";
if(isset($_POST['permitID'])){
  $permitID = $_POST['permitID'];
}
if(isset($_POST['permitExpiration'])){
  $permitExpiration = $_POST['permitExpiration'];
}
if(isset($_POST['cuisine'])){
  $cuisine = $_POST['cuisine'];
}
if(isset($_POST['rname'])){
  $rname = $_POST['rname'];
}
if(isset($_POST['street'])){
  $street = $_POST['street'];
}
if(isset($_POST['city'])) {
  $city = $_POST['city'];
}
if(isset($_POST['state'])){
  $state = $_POST['state'];
}
if(isset($_POST['zipcode'])){
  $zipcode = $_POST['zipcode'];
}
if(isset($_POST['county'])) {
  $county = $_POST['county'];
}
if(isset($_POST['phone'])) {
  $phone = $_POST['phone'];
}
if(isset($_POST['username'])) {
  $username = $_POST['username'];
}
if(isset($_POST['getRestaurantsOwned'])) {
  $getRestaurantsOwned = $_POST['getRestaurantsOwned'];
}

if (!empty($rname)) {
  $restaurantObject = new Restaurant();
  $json_update = $restaurantObject->informationUpdate($permitID, $permitExpiration, $cuisine, $rname, $street, $city, $state, $zipcode, $county, $phone, $username);
  echo json_encode($json_update);
}

if (!empty($username) && !empty($getRestaurantsOwned)) {
  $restaurantObject = new Restaurant();
  $json_getRestaurants = $restaurantObject->getRestaurants($username);
  echo json_encode($json_getRestaurants);
}

class Restaurant {
  private $db;

  public function __construct(){
    $this->db = new DbConnect();
  }

  public function getRestaurants($username) {
    $json = array();
    $connection = $this->db->getDb();
    $email = "";
    $emailResult = mysqli_query($connection, "SELECT email FROM operatorowner WHERE username = '$username'");
    while ($row = $emailResult->fetch_assoc()) {
        $email = $row['email'];
    }
    $restaurants = mysqli_query($connection, "SELECT name FROM restaurant WHERE email = '$email'");
    while ($r = mysqli_fetch_assoc($restaurants)) {
        $json[] = $r;
    }
    return $json;
  }

  public function findRestaurant($restaurant, $connection) {
    $query = "select rid from restaurant where name = '$restaurant'";
    $rid = mysqli_query($connection, $query);
    if (mysqli_num_rows($rid) > 0) {
      return 1;
    } else {
      return 0;
    }
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

  public function informationUpdate($permitID, $permitExpiration, $cuisine, $rname, $street, $city, $state, $zipcode, $county, $phone, $username) {
    $json = array();
    $connection = $this->db->getDb();
    $exists = $this->findRestaurant($rname, $connection);
    if ($exists == 1) {
      $query = "update restaurant set phone = '$phone', county = '$county', city = '$city', state = '$state', zipcode = '$zipcode', cuisine = '$cuisine' where name = '$rname'";
      $updateRestaurant = mysqli_query($connection, $query);
      $rid = $this->findRID($rname, $connection);
      $query2 = "update healthpermit set hpid = '$permitID', expirationdate = '$permitExpiration' where rid = '$rid'";
      $updatePermit = mysqli_query($connection, $query2);
      if ($updateRestaurant != false && $updatePermit != false) {
        $json['success'] = 1;
      } else {
        $json['success'] = 0;
      }
    } else {
      $count = mysqli_query($connection, "SELECT * FROM restaurant");
      $result = mysqli_num_rows($count) + 1;
      $email = "";
      $emailResult = mysqli_query($connection, "SELECT email FROM operatorowner WHERE username = '$username'");
      while ($row = $emailResult->fetch_assoc()) {
          $email = $row['email'];
      }
      $query = "insert into restaurant (rid, phone, name, county, street, city, state, zipcode, cuisine, email) values ('$result', '$phone', '$rname', '$county', '$street', '$city', '$state', '$zipcode', '$cuisine', '$email')";
      $newRestaurant = mysqli_query($connection, $query);
      $query2 = "insert into healthpermit (hpid, expirationdate, id) values ('$permitID', '$permitExpiration', '$result')";
      $updatePermit = mysqli_query($connection, $query2);
      if ($newRestaurant != false && $updatePermit != false) {
        $json['success'] = 2;
      } else {
        $json['success'] = 0;
      }
    }

    mysqli_close($connection);
    return $json;

  }
}


?>
