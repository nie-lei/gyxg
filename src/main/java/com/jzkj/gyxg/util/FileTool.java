package com.jzkj.gyxg.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * @Description: 文件工具类
 * @author: Yangxf
 * @date: 2019/4/17 16:06
 */
public class FileTool {

    /**
     * @Function: 图片上传
     * @author: YangXueFeng
     * @Date: 2019/4/18 14:13
     */
    public static void uploadFiles(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * @Function: 创建新的文件名
     * @author: YangXueFeng
     * @Date: 2019/4/17 17:57
     */
    public static String renameToUUID(String fileName) {
        return UUID.randomUUID() + "." + fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}