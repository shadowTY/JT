package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.EasyUIImage;

@Service
// 将配置文件交给spring容器管理
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	@Value("${image.localDirPath}")
	private String localDirPath; //  ="D:/project/image/";
	@Value("${image.localUrlPath}")
	private String localUrlPath; //  = "http://image.jt.com/";
	/**
	 *  要求： 1.校验图片类型jpg png gif
	 *  	2.木马.exe.jpg
	 *  	3.分文件存储  yyyy/mm/dd
	 *  	4.防止文件重名 uuid.jpg 
	 *  	
	 */
	@Override
	public EasyUIImage updateFile(MultipartFile uploadFile) {
		EasyUIImage easyUIImage = new EasyUIImage();
		// 1. 判断文件类型
		String fileName = uploadFile.getOriginalFilename();
		// 将字符统一转化为小写
		fileName = fileName.toLowerCase();
		if (!fileName.matches("^.+\\.(jpg|png|gif)$")) {
			easyUIImage.setError(1);
			return easyUIImage;
		}
		// 2.判断文件是否为恶意程序 利用宽高判断
		try {
			BufferedImage bufferedImage = ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			if (width==0 || height==0) {
				easyUIImage.setError(1);
				return easyUIImage;
			}
			// 3.创建文件夹 yyyy/MM/dd
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			String localDir = localDirPath + dateDir;
			File dirFile = new File(localDir);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			// 4.防止文件重名，修改文件名称
			String uuid = UUID.randomUUID().toString();
			// 获取.的位置
			int index = fileName.lastIndexOf(".");
			// 从index开始一直到最后截取字符串
			String fileType = fileName.substring(index);
			// 名称拼接： uuid.jpg
			String realFileName = uuid+fileType;
			
			// 最终实现文件的上传   文件夹/文件名
			String realFilePath = localDir + "/" + realFileName;
			File realFile = new File(realFilePath);
			uploadFile.transferTo(realFile);
			System.err.println("文件上传成功");
			easyUIImage.setHeight(height);
			easyUIImage.setWidth(width);
			
			// 为页面准备一个url地址
			String url = localUrlPath + dateDir + "/" + realFileName;
			easyUIImage.setUrl(url);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			easyUIImage.setError(1);
			return easyUIImage;
		}
		return easyUIImage;
	}

}
