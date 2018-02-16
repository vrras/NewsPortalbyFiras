<?php
//def 
// definisikan koneksi ke database
$server = "localhost";
$username = "id2405461_vrras";
$password = "12345";
$database = "id2405461_berita_firas";

// $username = "root";
// $password = "";
// $database = "berita-firas";
//cek koneksi
// Koneksi dan memilih database di server
$con =mysqli_connect($server,$username,$password,$database) or die("Connection failed.");
//mysqli_select_db($database) or die("Database tidak bisa dibuka");
?>

