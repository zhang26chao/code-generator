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

public class ZipUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipUtil.class);

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
