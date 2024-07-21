package com.example.boot.util;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * zip 文件拆分工具，例如邮件附件的拆分
 * @author wuka
 */
public class ZipFileSplitUtil {

    public static List<File> splitVolumeCompressFiles(int sizeThreshold, List<File> sourceFiles, String destDirPath, String zipFileName) throws Exception {
        List<File> zipFiles = new ArrayList<>();
        if (Objects.isNull(sourceFiles) && sourceFiles.isEmpty()) {
            return zipFiles;
        }
        // 目录不存在则创建
        File dir = new File(destDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (ZipFile zipFile = new ZipFile(destDirPath + File.separator + zipFileName + ".zip")) {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            zipFile.createSplitZipFile(sourceFiles, parameters, true, sizeThreshold * 1024L * 1024L);
            List<File> splitZipFiles = zipFile.getSplitZipFiles();
            if (Objects.nonNull(splitZipFiles) && !splitZipFiles.isEmpty()) {
                zipFiles = splitZipFiles;
            }
        }
        return zipFiles;
    }

    public static void main(String[] args) throws Exception {
        List<File> attah = new ArrayList<>();
        attah.add(new File("E:\\javaWorkspace\\boot-vue\\规范.zip"));
        List<File> fileList = splitVolumeCompressFiles(10, attah, "D:/data", "");
        System.out.println(fileList.size());
    }
}
