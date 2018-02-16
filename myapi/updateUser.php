<?php 
include './config/koneksi.php';
include './helper/myFunction.php';

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {	 
    //parameter post
    $user = trim($_POST['username']);
    $password = md5(trim($_POST['password']));

    $query = "UPDATE  tbl_user 
    SET password = '$password'
    WHERE username='$user'";

    $queryCek = "SELECT  * 
    FROM tbl_user
    WHERE username='$user'";

    $exeQuery = mysqli_query($con, $query); 
    $cekQuery = mysqli_query($con, $queryCek); 

    $cnt =mysqli_num_rows($cekQuery);

    if($cnt>0){		
        $response["msg"]=trim("Data has been updated.");
        $response["code"]=200;
        $response["status"]=true; 
        echo json_encode($response);
    }else{
        $response["msg"]=trim("Failed updated.");
        $response["code"]=400;
        $response["status"]=false; 
        echo json_encode($response);		
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
