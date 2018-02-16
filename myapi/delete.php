<?php 
 require_once './config/koneksi.php';

 if($_SERVER['REQUEST_METHOD'] == 'POST')
 {
	$id_berita = $_POST['id_berita'];
	 
	$queryAwal = mysqli_query($con, "SELECT * FROM tbl_berita WHERE id_berita  = '$id_berita'"); 
	 
	$cekAwal = mysqli_num_rows($queryAwal);

	if($cekAwal>0){
		$query = "DELETE FROM tbl_berita WHERE id_berita  = '$id_berita'";
		$exeQuery = mysqli_query($con, $query);
	
		$cekQuery = mysqli_query($con, "SELECT * FROM tbl_berita WHERE id_berita  = '$id_berita'"); 
		$cekData = mysqli_num_rows($cekQuery);

		$b=mysqli_fetch_array($cekQuery);

		$response=array();

		if($cekData==0){
			// if (file_exists($_SERVER['DOCUMENT_ROOT']."/myapi/foto_berita/".$b['foto'])) {
			// 	unlink($_SERVER['DOCUMENT_ROOT']."/myapi/foto_berita/".$b['foto']); //sekarang jalankan perintah unlink untuk hapus gambar dari folder, ambil data dan didepannya ditambahkan paramter tempatt dimana folder gambar tersimpan
			// }
			// if (file_exists($_SERVER['DOCUMENT_ROOT']."/myapi/thumbnails/".$b['foto'])) {
			// 	unlink($_SERVER['DOCUMENT_ROOT']."/myapi/thumbnails/".$b['foto']); //sekarang jalankan perintah unlink untuk hapus gambar dari folder, ambil data dan didepannya ditambahkan paramter tempatt dimana folder gambar tersimpan
			// }
			
			$response["msg"]=trim("Data has been deleted.");
			$response["code"]=200;
			$response["status"]=true;
			echo json_encode($response);

		}else{
			$response["msg"]=trim("Delete failed.");
			$response["code"]=400;
			$response["status"]=false;
			echo json_encode($response);
		}
	}else{
		$response["msg"]=trim("Data not found.");
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
