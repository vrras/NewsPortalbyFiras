<?php

    include './config/koneksi.php';

    $sql=mysqli_query($con,"SELECT *, fullname as publisher, DATE_FORMAT(publish_date, '%d-%m-%Y') as tgl FROM tbl_berita a INNER JOIN tbl_user b ON a.id_user = b.id_user ORDER BY publish_date DESC");

    if(isset($_GET["id"])){
        $id=$_GET["id"];

        $sql=mysqli_query($con,"SELECT * FROM tbl_berita WHERE id_berita='$id'");
    }

    $response=array();
    $cek=mysqli_num_rows($sql);
    if($cek >0){
        $response["berita"]=array();

        while ($row=mysqli_fetch_array($sql)){

            $data=array();
            $data["id_berita"]=$row["id_berita"];
            $data["title"]=$row["title"];
            $data["content"]=$row["content"];
            $data["foto"]=$row["foto"];
            $data["publisher"]=$row["publisher"];
            $data["publish_date"]=$row["tgl"];
            $data["update_date"]=$row["update_date"];
            
            $response["msg"]="News found.";
            $response["code"]=200;
            $response["status"]=true;    
            array_push($response["berita"],$data);
        }

        echo json_encode($response);

    }else{
        $response["msg"]="News not found.";
        $response["code"]=404;
        $response["status"]=false; 
        echo json_encode($response);
    } 

?>
