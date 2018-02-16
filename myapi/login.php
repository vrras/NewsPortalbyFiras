<?php 
include './config/koneksi.php';

if($_SERVER['REQUEST_METHOD'] == 'POST')
{
    if(trim($_POST['username'])==""){

        $response["msg"]=trim("Fill your username in here.");
        $response["code"]=400;
        $response["status"]=false;
        echo json_encode($response);

    } else if(trim($_POST['password'])==""){

        $response["msg"]=trim("Fill your password in here.");
        $response["code"]=400;
        $response["status"]=false;
        echo json_encode($response);

    } else {

        //parameter post
        $user = trim($_POST['username']);
        $pass = md5(trim($_POST['password']));

        $query = mysqli_query($con,"SELECT * FROM tbl_user WHERE username='$user' AND password='$pass'");

        $response=array();
        $count = mysqli_num_rows($query);

        if($count>0) {
            $response["user"]=array();
            
            while ($row=mysqli_fetch_array($query)){

                $data=array();
                $data["id_user"]=$row["id_user"];
                $data["fullname"]=$row["fullname"];
                $data["username"]=$row["username"];
                $data["access"]=$row["access"];
                $data["insert_date"]=$row["insert_date"];
                $data["update_date"]=$row["update_date"];
                $data["last_active_date"]=$row["last_active_date"];
                
                $response["msg"]=trim("Login success.");
                $response["code"]=200;
                $response["status"]=true;
                array_push($response["user"],$data);
            }

            echo json_encode($response);

        } else {

            $response["msg"]=trim("Login failed.");
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
