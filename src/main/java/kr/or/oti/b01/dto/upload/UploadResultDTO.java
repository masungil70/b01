package kr.or.oti.b01.dto.upload;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import kr.or.oti.b01.util.S3Uploader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UploadResultDTO {
	private String uuid;
	private String filename;
	private String realFilename;
	private boolean img;
	
	public String getLink() {
		return img ? "s_" + realFilename : realFilename; 
	}
	
	public UploadResultDTO(String uploadPath, MultipartFile file, S3Uploader s3Uploader) {
		this.uuid = UUID.randomUUID().toString();
		this.filename = file.getOriginalFilename();
		this.realFilename = this.uuid + "_" + this.filename; 
		this.img = false;
		
		Path path = Paths.get(uploadPath, realFilename);
		
		try {
			file.transferTo(path);
			
			if (file.getContentType().startsWith("image/")) {
				
				Path thumbFile = Paths.get(uploadPath, "s_" + realFilename);
				
				//섬네일 이미지 저장 
				Thumbnailator.createThumbnail(path.toFile(), thumbFile.toFile(), 200, 200);
				
				this.img = true;
			}
			
			//S3로 파일을 업로드 한다.
			this.realFilename = s3Uploader.upload(uploadPath + File.separator + this.realFilename);
			
			log.info("S3에 업로드된 URL = " + this.realFilename);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
