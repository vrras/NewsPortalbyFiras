<?php

function thumbnailImage($nama_file) {
    $d = "foto_berita/";
    $t = "thumbnails/";
	$image	 = $d . $nama_file;
	
	$gambar_asli = imagecreatefromjpeg($image);
	$lebar 		 = imageSX($gambar_asli);
	$tinggi 	 = imageSY($gambar_asli);
	
	$tmb_lebar   = 500;
	$tmb_tinggi  = 500;
	
	$image_tmb   = imagecreatetruecolor($tmb_lebar,$tmb_tinggi);
	imagecopyresampled($image_tmb,$gambar_asli,0,0,0,0,$tmb_lebar,$tmb_tinggi,$lebar,$tinggi);
	
	imagejpeg($image_tmb,$t . $nama_file);
	
	
	imagedestroy($gambar_asli);
	imagedestroy($image_tmb);
	
	return $image_asli;
  }

  ?>