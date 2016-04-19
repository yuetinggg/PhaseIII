<?php
require_once 'include/search.php';
require_once 'include/user.php';
$name = "";
$score = "";
$lessThan = "";
$zipcode = "";
$cuisine = "";
$id = "";
$password = "";
$getCuisine = "";
if(isset($_POST['id'])){
  $id = $_POST['id'];
}
if(isset($_POST['password'])){
  $password = $_POST['password'];
}
if(isset($_POST['name'])){
  $name = $_POST['name'];
}
if(isset($_POST['score'])){
  $score = $_POST['score'];
}
if(isset($_POST['lessThan'])){
  $lessThan = $_POST['lessThan'];
}
if(isset($_POST['zipcode'])){
  $zipcode = $_POST['zipcode'];
}
if(isset($_POST['cuisine'])) {
  $cuisine = $_POST['cuisine'];
}
if(isset($_POST['getCuisine'])) {
  $getCuisine = $_POST['getCuisine'];
}
$searchObject = new search();
if(isset($_POST['lessThan'])) {
  $json_restaurants = $searchObject->getRestaurants($name, $score, $lessThan, $zipcode, $cuisine);
  echo json_encode($json_restaurants);
}

$userObject = new User();

if(!empty($id) && !empty($password)){
  $json_login = $userObject->login($id, $password);
  echo json_encode($json_login);
}
// Instance of a User class
if(!empty($getCuisine)) {
  $json_cuisine = $searchObject->getCuisine();
  echo json_encode($json_cuisine);
}



?>
