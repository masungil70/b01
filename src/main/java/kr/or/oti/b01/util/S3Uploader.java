package kr.or.oti.b01.util;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;
	
	@Value("${cloud.aws.s3.bucket}")
	public String bucket;

	public String upload(String filepath)  throws RuntimeException {
		File targetFile = new File(filepath);
		
		//S3에 로컬 파일을 업로드합니다  
		String uploadImageUrl = putS3(targetFile, targetFile.getName());
		
		//S3에 업로드 된 로컬 파일을 삭제한다.
		removeOriginalFile(targetFile);
		
		//S3에 파일을 업로드 URL 경로를 리턴합니다
		return uploadImageUrl;
	}

	private String putS3(File targetFile, String name) {
		amazonS3Client.putObject(new PutObjectRequest(bucket, name, targetFile)
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		return amazonS3Client.getUrl(bucket, name).toString();
	}
	
	private void removeOriginalFile(File targetFile) {
		if (targetFile.exists() && targetFile.delete()) {
			log.info("잘 삭제 되었습니다");
			return ;
		}
		log.info("파일 삭제 실패했습니다");
	}
	
	//S3에 업로드된 파일을 삭제한다
	public void removeS3File(String fileName) {
		//삭제 요청 객체 선언 
		amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}
}
