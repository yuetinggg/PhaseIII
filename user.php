<?php
include_once 'db.php';
ini_set('display_errors', 'On');
error_reporting(E_ALL);
class User{
  private $db;
  private $db_table = "registereduser";

  public function __construct(){
    $this->db = new DbConnect();
  }
  public function isLoginExist($username, $password, $connection){
    $query = "select * from " . $this->db_table . " where username = '$username' AND password = '$password' Limit 1";
    $result = mysqli_query($connection, $query);
    if(mysqli_num_rows($result) > 0){
      return 1;
    }
    $query = "select password from " . $this->db_table . " where username = '$username' Limit 1";
    $result = mysqli_query($connection, $query);
    if (mysqli_num_rows($result) == 0) {
      return 2;
    } else {
      return 3;
    }
  }

  public function login($username, $password){
    $json = array();
    $connection = $this->db->getDb();
    $canUserLogin = $this->isLoginExist($username, $password, $connection);
    if($canUserLogin == 1){
      $newQuery = mysqli_query($connection, "select * from inspector where username = '$username' Limit 1");
      $json['success'] = 1;
      if(mysqli_num_rows($newQuery) >0) {
        $json['isInspector'] = 1;
      } else {
        $json['isInspector'] = 0;
      }
    } elseif ($canUserLogin == 2){
      $json['success'] = 0;
      $json['errorMessage'] = "No such user id.";
    } else {
      $json['success'] = 0;
      $json['errorMessage'] = "Wrong user id and password combination.";
    }
    mysqli_close($connection);
    return $json;
  }
}
?>
