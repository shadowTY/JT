package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.jt.service.FileService;
import com.jt.vo.EasyUIImage;


@Controller
public class FileController {
	@Autowired
	private FileService fileService;
	/**
	 *  实现jt项目文件上传
	 *  参数名称：uploadFile
	 *  参数路径：/pic/upload
	 *  数据返回值： {"error":0,"url":"图片的保存路径","width":图片的宽度,"height":图片的高度}
	 */
	@RequestMapping("/pic/upload")
	@ResponseBody
	public EasyUIImage uploadFile(MultipartFile uploadFile) {
		return fileService.updateFile(uploadFile);
	}

}
