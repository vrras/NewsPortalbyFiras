<?php 
 include './config/koneksi.php';

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
 	//parameter post
 	$nama = trim($_POST['fullname']);
 	$user = trim($_POST['username']);
    $pass = md5(trim($_POST['password']));
     
    $cekUser = mysqli_query($con, "SELECT * FROM tbl_user WHERE username='$user'");
    $countUser = mysqli_num_rows($cekUser);

    $response=array();
    if($countUser>0){

        $response["msg"]=trim("Username already exist.");
        $response["code"]=400;
        $response["status"]=false;
        echo json_encode($response);

    }else{

        $query = "INSERT INTO tbl_user (fullname, username, password) VALUES ('$nama','$user','$pass')";

        $exeQuery = mysqli_query($con, $query); 
        
        $cekQuery = mysqli_query($con, "SELECT * FROM tbl_user WHERE username='$user'");
        $countQuery = mysqli_num_rows($cekQuery);

        if($countQuery>0){
            $response["user"]=array();

            while ($row=mysqli_fetch_array($cekQuery)){

                $data=array();
                $data["id_user"]=$row["id_user"];
                $data["fullname"]=$row["fullname"];
                $data["username"]=$row["username"];
                $data["access"]=$row["access"];
                $data["insert_date"]=$row["insert_date"];
                $data["update_date"]=$row["update_date"];
                $data["last_active_date"]=$row["last_active_date"];
                
                $response["msg"]=trim("You have been registered.");
                $response["code"]=201;
                $response["status"]=true;
                array_push($response["user"],$data);
            }

            echo json_encode($response);

        }else{
            $response["msg"]=trim("Failed to registered.");
            $response["code"]=400;
            $response["status"]=false;
            echo json_encode($response);
        }

    }

 }
 else
 {
    $response["msg"]=trim("Forbidden.");
    $response["code"]=403;
    $response["status"]=false;
    echo json_encode($response);
 }

 ?>
