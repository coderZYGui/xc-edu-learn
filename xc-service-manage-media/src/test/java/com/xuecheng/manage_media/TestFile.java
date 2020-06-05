package com.xuecheng.manage_media;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Description:
 *
 * @author zygui
 * @date Created on 2020/6/5 10:41
 */
public class TestFile {

    // 测试文件分块
    @Test
    public void testChunk() throws Exception {
        // 源文件地址
        File sourceFile = new File("F:\\xuechengzaixian\\ffmpeg_test\\lucene.avi");
        // 块文件目录
        String chunkFileFolder = "F:\\xuechengzaixian\\ffmpeg_test\\chunks\\";
        // 定义块文件大小
        long chunkFileSize = 1 * 1024 * 1024;
        // 块数
        long chunkFileNum = (long) Math.ceil(sourceFile.length() * 1.0 / chunkFileSize);

        // 创建读文件对象
        RandomAccessFile rafRead = new RandomAccessFile(sourceFile, "r");
        //缓冲区
        byte[] b = new byte[1024];
        for (int i = 0; i < chunkFileNum; i++) {
            //块文件
            File chunkFile = new File(chunkFileFolder + i);
            //创建向块文件的写对象
            RandomAccessFile rafWrite = new RandomAccessFile(chunkFile, "rw");
            int len;

            while ((len = rafRead.read(b)) != -1) {

                rafWrite.write(b, 0, len);
                //如果块文件的大小达到 1M开始写下一块儿
                if (chunkFile.length() >= chunkFileSize) {
                    break;
                }
            }
            rafWrite.close();
        }
        rafRead.close();
    }

    // 测试文件合并(一般捕获异常,不抛出,这里为了方便演示采用抛异常)
    @Test
    public void testMergeFile() throws IOException {
        //块文件目录
        String chunkFileFolderPath = "F:\\xuechengzaixian\\ffmpeg_test\\chunks\\";
        //块文件目录对象
        File chunkFileFolder = new File(chunkFileFolderPath);
        //块文件列表
        File[] files = chunkFileFolder.listFiles();
        //将块文件排序，按名称升序
        List<File> fileList = Arrays.asList(files);
        Collections.sort(fileList, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (Integer.parseInt(o1.getName()) > Integer.parseInt(o2.getName())) {
                    return 1;
                }
                return -1;
            }
        });

        //合并文件
        File mergeFile = new File("F:\\xuechengzaixian\\ffmpeg_test\\lucene_merge.avi");
        //创建新文件
        boolean newFile = mergeFile.createNewFile();

        //创建写对象
        RandomAccessFile raf_write = new RandomAccessFile(mergeFile, "rw");

        byte[] b = new byte[1024];
        for (File chunkFile : fileList) {
            //创建一个读块文件的对象
            RandomAccessFile raf_read = new RandomAccessFile(chunkFile, "r");
            int len = -1;
            while ((len = raf_read.read(b)) != -1) {
                raf_write.write(b, 0, len);
            }
            raf_read.close();
        }
        raf_write.close();
    }
}
