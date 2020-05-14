package com.jzkj.gyxg.util;

import com.jzkj.gyxg.common.HttpRequestParamter;
import com.jzkj.gyxg.common.PreReadUploadConfig;
import com.jzkj.gyxg.exception.AjaxOperationFailException;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class FileUploadUtil {
    /**
     * 上传图片
     *
     * @param file
     * @return
     * @throws AjaxOperationFailException
     */
    public static String uploadImage(MultipartFile file, String imgType) throws AjaxOperationFailException {
        if (file == null) {
            throw new AjaxOperationFailException("未选择文件");
        }
        String suffix = HttpRequestParamter.getString("suffix");

        String originalFilename = file.getOriginalFilename();
        if(StringUtil.isNull(originalFilename))
            return "";
        String fileName = imgType + DateUtil.format(new Date(), "yyyyMMddHHmmss") + StringUtil.getRandomString(4, 2) + "."
                + (StringUtil.isEmpty(suffix) ?  originalFilename.substring(originalFilename.lastIndexOf(".") + 1): suffix);
        String filePath = StringUtil.IMGPATH + fileName;

        File destFile = new File(filePath);
        try {
            if (!destFile.exists()) {
                if (!destFile.getParentFile().exists()) {
                    if (!destFile.getParentFile().mkdirs()) {
                        throw new AjaxOperationFailException("无法创建文件夹");
                    }
                }
                if (!destFile.createNewFile()) {
                    throw new AjaxOperationFailException("无法创建文件");
                }
            }

            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AjaxOperationFailException("无法写入文件");
        }

        return fileName;
    }

    public static String uploadImg(MultipartFile file, String imgType, PreReadUploadConfig uploadConfig) throws AjaxOperationFailException {
        if (file == null) {
            throw new AjaxOperationFailException("请选择图片");
        }
//        String fileName = file.getOriginalFilename();
        String suffix = HttpRequestParamter.getString("suffix");

        String originalFilename = file.getOriginalFilename();
        if(StringUtil.isNull(originalFilename))
            return "";
        String fileName = imgType + DateUtil.format(new Date(), "yyyyMMddHHmmss") + StringUtil.getRandomString(4, 2) + "."
                + (StringUtil.isEmpty(suffix) ?  originalFilename.substring(originalFilename.lastIndexOf(".") + 1): suffix);
        try {
            FileTool.uploadFiles(file.getBytes(), uploadConfig.getUploadPath(), fileName);
//            FileTool.uploadFiles(file.getBytes(), "D:/home/image", fileName);
        } catch (Exception e) {
        }
        String url = StringUtil.IMGURL + "/static/" + fileName;
        return url;
    }

    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");

            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);

            for(int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }

            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
