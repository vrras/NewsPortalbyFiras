<?php 
include './config/koneksi.php';
include './helper/myFunction.php';

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	$flag = $_POST["flag"];
	 
	if($flag=="noupload"){
		//parameter post
		$id_berita = $_POST['id_berita'];
		$title = trim(str_replace('"',' ',$_POST['title']));
		$content = trim(str_replace('"',' ',$_POST['content']));

		$query = "UPDATE  tbl_berita 
		SET title = '$title',
		content = '$content'
		WHERE id_berita='$id_berita'";

		$exeQuery = mysqli_query($con, $query); 

		if($exeQuery){		
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

	}else{

		$file_path = "foto_berita/";
     
		$file_path = $file_path . basename( $_FILES['file']['name']);
		//parameter post
		$id_berita = $_POST['id_berita'];
		$title = trim(str_replace('"',' ',$_POST['title']));
		$content = trim(str_replace('"',' ' ,$_POST['content']));
		// $foto = "tes.jpg";
		$foto = $_FILES['file']['name'];
		
		$response=array();
		// Check if image file is a actual image or fake image
		if (isset($_FILES["file"])) 
		{
			if(move_uploaded_file($_FILES['file']['tmp_name'], $file_path)) {
	
				$query = "UPDATE tbl_berita SET title='$title', content='$content', foto='$foto' WHERE id_berita='$id_berita'";
	
				$exeQuery = mysqli_query($con, $query); 
			
				$response["msg"]=trim("Successfully uploaded.");
				$response["code"]=201;
				$response["status"]=true; 
				echo json_encode($response);
	
				thumbnailImage($foto);
	
			} else{
	
				$response["msg"]=trim("Error while uploading.");
				$response["code"]=400;
				$response["status"]=false; 
				echo json_encode($response);
	
			}	
		}
		else
		{
	
			$response["msg"]=trim("Required field missing.");
			$response["code"]=403;
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
