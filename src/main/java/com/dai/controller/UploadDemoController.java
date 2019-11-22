package com.dai.controller;

import cn.novelweb.tool.http.Result;
import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @program: toolupload
 * @description: 上传文件、妙传、断点续传样例-demo
 * @author: Dai Yuanchuan
 * @create: 2019-10-30 08:24
 **/
@RestController
@Slf4j
@RequestMapping(value = "/uploader")
public class UploadDemoController {

    static {
        // 修改文件上传的默认路径(如果需要用到的话)
        LocalUpload.defaultPath = System.getProperty("user.dir")
                + File.separatorChar + "log"
                + File.separatorChar + "uploadedFiles";
    }

    @RequestMapping(value = "defaultPathCheckFile", method = RequestMethod.GET)
    public Result defaultPathCheckFile(String md5, String fileName) {
        // 使用默认上传路径 验证 秒传、断点 文件 配置
        log.info("当前默认的上传路径为:{}", LocalUpload.defaultPath);
        try {
            return LocalUpload.checkFileMd5(md5, fileName);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("上传失败");
        }
    }

    @RequestMapping(value = "checkFile", method = RequestMethod.GET)
    public Result checkFile(String md5, String fileName) {
        // 自定义 分片配置文件 和 上传的缓存文件 路径
        String path = System.getProperty("user.dir") + File.separatorChar + "log" + File.separatorChar;
        String confFilePath = path + "confFile";
        String tmpFilePath = path + "file";
        try {
            return LocalUpload.checkFileMd5(md5, fileName, confFilePath, tmpFilePath);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("上传失败");
        }
    }

    @RequestMapping(value = "demo1",
            consumes = "multipart/*", headers = "content-type=multipart/form-data",
            produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadDemo1(UploadFileParam param, HttpServletRequest request) {
        // 使用自定义 分片、缓存 文件路径 分片上传、断点续传 文件
        // 这个 自定义的上传路径 要与 checkFile 方法内的路径一直
        // 不然会影响 断点续传 功能
        String path = System.getProperty("user.dir") + File.separatorChar + "log" + File.separatorChar;
        String confFilePath = path + "confFile";
        String tmpFilePath = path + "file";
        try {
            // 这里的 chunkSize(分片大小) 要与前端传过来的大小一致
            return LocalUpload.fragmentFileUploader(param, confFilePath,
                    tmpFilePath, 5242880L, request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("上传失败");
        }
    }


    @RequestMapping(value = "demo2",
            consumes = "multipart/*", headers = "content-type=multipart/form-data",
            produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadDemo2(UploadFileParam param, HttpServletRequest request) {
        // 使用默认上传路径上传分片文件
        log.info("当前默认的上传路径为:{}", LocalUpload.defaultPath);
        try {
            return LocalUpload.fragmentFileUploader(param, 5242880L, request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("上传失败");
        }
    }

    @RequestMapping(value = "demo3", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadDemo3(UploadFileParam param) {
        // 普通的文件上传程序、不使用分片、断点续传(自定义上传路径)
        try {
            return LocalUpload.regularFileUploader(param, LocalUpload.defaultPath);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Result.error("上传失败");
        }
    }

    @RequestMapping(value = "demo4", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadDemo4(UploadFileParam param, HttpServletRequest request) {
        // 普通的文件上传程序、不使用分片、断点续传(使用默认的文件上传路径)
        // 默认路径为  log/uploader  /年/月/日/当前时间毫秒数.mp4
        try {
            log.info("当前默认的上传路径为:{}", LocalUpload.defaultPath);
            return LocalUpload.regularFileUploader(param);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
        }
    }
}
