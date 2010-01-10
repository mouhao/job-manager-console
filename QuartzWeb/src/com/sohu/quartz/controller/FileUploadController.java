package com.sohu.quartz.controller;

import com.sohu.quartz.pojo.FileBean;
import com.sohu.quartz.utils.FileUploadHelper;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SongJiao
 * Date: 2010-1-9
 * Time: 22:45:45
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class FileUploadController {
    private static Logger logger = Logger.getLogger(FileUploadController.class);

    @Resource
    private FileUploadHelper fileUploadHelper;

    public FileUploadHelper getFileUploadHelper() {
        return fileUploadHelper;
    }

    public void setFileUploadHelper(FileUploadHelper fileUploadHelper) {
        this.fileUploadHelper = fileUploadHelper;
    }

    @RequestMapping("/showUploadPage.do")
    public String show() {
        return "upload";
    }

    @RequestMapping("/upload.do")
    public String upload(
            @RequestParam("file") MultipartFile file, Model model)
            throws Exception {

        //cast to multipart file so we can get additional information
        File dirPath = new File(this.fileUploadHelper.getUploadDir());
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        String sep = System.getProperty("file.separator");
        if (logger.isDebugEnabled()) {
            logger.debug("uploading to: " + this.fileUploadHelper.getUploadDir() + sep +
                    file.getOriginalFilename());
        }
        File uploadedFile = new File(this.fileUploadHelper.getUploadDir() + sep
                + file.getOriginalFilename());
        FileCopyUtils.copy(file.getBytes(), uploadedFile);
        logger.info("********************************");
        logger.info(uploadedFile.getAbsolutePath());
        logger.info(uploadedFile.length());
        logger.info("********************************");
        logger.info("unzip files.....................");
        this.fileUploadHelper.unzip();
        logger.info("done............................");
        List<File> dirs=this.fileUploadHelper.getSubDirs();
        List<FileBean> filebeans=new ArrayList<FileBean>();
        for(File f:dirs){
            FileBean fb=new FileBean();
            fb.setFilename(f.getName());
            DateFormat df=new SimpleDateFormat("yyy-MM-dd HH:mm:ss E");
            fb.setLastmodified(df.format(new Date(f.lastModified())));
            filebeans.add(fb);
        }


        model.addAttribute("dirs",filebeans);
        return "upload";
    }

    protected void initBinder(HttpServletRequest request,
                              ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }
}
