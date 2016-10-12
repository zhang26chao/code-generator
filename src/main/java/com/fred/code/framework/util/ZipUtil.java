/*
 * Copyright (C), 2002-2015, 苏宁易购电子商务有限公司
 * FileName: ZipCompressUtil.java
 * Author:   张超(14080608)
 * Date:     2015-12-28 下午5:47:57
 * Description: //模块目的、功能描述      
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名             修改时间            版本号                  描述
 */
package com.fred.code.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉
 * 
 * @author 张超(14080608)
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class ZipUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);

    /**
     * 
     * 功能描述: <br>
     * 文件压缩,文件名不能包含中文，否则乱码
     * 
     * @param targetFilePath
     * @param originFilePath
     * @throws Exception
     * @author 张超(14080608)
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public static boolean pack(String targetFilePath, String... originFilePath) {
        File targetFile = new File(targetFilePath);
        targetFile.getParentFile().mkdirs();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(targetFile));
            for (String originFile : originFilePath) {
                packZipFile(new File(originFile), zos, StringUtils.EMPTY);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("压缩文件异常!!", e);
        } finally {
            IOUtils.closeQuietly(zos);
        }
        return false;
    }

    private static void packZipFile(File inFile, ZipOutputStream zos, String dir) throws Exception {
        if (inFile.isDirectory()) {
            File[] files = inFile.listFiles();
            for (File file : files) {
                packZipFile(file, zos,
                        (StringUtils.isEmpty(dir) ? StringUtils.EMPTY : dir + File.separator) + inFile.getName());
            }
        } else {
            String entryName = (StringUtils.isEmpty(dir) ? StringUtils.EMPTY : dir + File.separator) + inFile.getName();
            ZipEntry entry = new ZipEntry(entryName);
            InputStream is = null;
            try {
                zos.putNextEntry(entry);
                is = new FileInputStream(inFile);
                IOUtils.copyLarge(is, zos);
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    public static void main(String[] args) {
        try {
            pack("D:\\opt\\epp\\log\\ps\\export\\test.zip",
                    "D:\\opt\\epp\\log\\ps\\export\\eppsps_paymentsuccess+ac2pm+2015-01-31+001.txt",
                    "D:\\opt\\epp\\log\\ps\\export\\eppsps_paymentsuccess+ac2pm+2015-01-31+002.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
