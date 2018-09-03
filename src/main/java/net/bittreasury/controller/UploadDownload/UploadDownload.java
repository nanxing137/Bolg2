package net.bittreasury.controller.UploadDownload;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
public class UploadDownload {

	private static final Logger logger = LoggerFactory.getLogger(UploadDownload.class);
	private static final String realPath;
	static {
		String temp = null;
		try {
			temp = new File(ResourceUtils.getURL("classpath:").getPath()).getAbsolutePath() + File.separator + "static"
					+ File.separator + "files" + File.separator;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		realPath = temp;
	}

	@RequestMapping("API/uploadDownload")
	public String uploadDownload() {
		return "multipart";
	}

	// 单文件上传
	@RequestMapping(value = "API/upload")
	@ResponseBody
	public String upload(@RequestParam("file") MultipartFile file) {
		try {
			if (file.isEmpty()) {
				return "文件为空";
			}
			// 获取文件名
			String fileName = file.getOriginalFilename();
			logger.info("上传的文件名为：" + fileName);
			// 获取文件的后缀名
			String suffixName = fileName.substring(fileName.lastIndexOf("."));
			logger.info("文件的后缀名为：" + suffixName);

			// 设置文件存储路径
			// String filePath = "classpath:static";
			String filePath = realPath;
			String path = filePath + fileName + suffixName;
			System.out.println(path);
			File dest = new File(path);
			// 检测是否存在目录
			if (!dest.getParentFile().exists()) {
				dest.getParentFile().mkdirs();// 新建文件夹
			}
			file.transferTo(dest);// 文件写入
			return fileName + suffixName;
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "上传失败";
	}

	// 多文件上传
	@RequestMapping(value = "API/batch/upload", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(HttpServletRequest request) {
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			if (!file.isEmpty()) {
				try {
					byte[] bytes = file.getBytes();
					stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
					stream.write(bytes);
					stream.close();

				} catch (Exception e) {
					stream = null;
					return "You failed to upload " + i + " => " + e.getMessage();
				}
			} else {
				return "You failed to upload " + i + " because the file was empty.";
			}
		}
		return "upload successful";
	}

	// 文件下载相关代码
//	@RequestMapping("API/download/{fileName}")
	@RequestMapping("API/download")
	public String downloadFile(@RequestParam("fileName") String fileName, HttpServletRequest request,
			HttpServletResponse response) {
		// String fileName = "aim_test.txt";// 设置文件名，根据业务需要替换成要下载的文件名
		if (fileName != null) {
			// 设置文件路径
			// String realPath = "classpath:static"+File.separator+"files";
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/force-download");// 设置强制下载不打开
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];
				FileInputStream fis = null;
				BufferedInputStream bis = null;
				try {
					fis = new FileInputStream(file);
					bis = new BufferedInputStream(fis);
					OutputStream os = response.getOutputStream();
					int i = bis.read(buffer);
					while (i != -1) {
						os.write(buffer, 0, i);
						i = bis.read(buffer);
					}
					System.out.println("success");
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (bis != null) {
						try {
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (fis != null) {
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
}
