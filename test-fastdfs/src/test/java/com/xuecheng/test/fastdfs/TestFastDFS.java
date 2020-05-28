package com.xuecheng.test.fastdfs;

import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Description: 测试FastDFS
 *
 * @author zygui
 * @date Created on 2020/5/23 10:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFastDFS {

    // 上传文件
    @Test
    public void testUpload() {
        try {
            // 加载fastdfs-client.properties文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            // 定义TrackerClient, 用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            // 向storage服务器上传文件
            // 本地上传文件的路径
            String filePath = "F:/old.jpg";
            // 上传成功后,拿到文件id
            String fileId = storageClient1.upload_file1(filePath, "jpg", null);
            System.out.println("fileId = " + fileId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 下载文件
    @Test
    public void testDownload() {
        try {
            // 加载fastdfs-client.properties文件
            ClientGlobal.initByProperties("config/fastdfs-client.properties");
            // 定义TrackerClient, 用于请求TrackerServer
            TrackerClient trackerClient = new TrackerClient();
            // 连接tracker
            TrackerServer trackerServer = trackerClient.getConnection();
            // 获取Storage
            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            // 创建storageClient
            StorageClient1 storageClient1 = new StorageClient1(trackerServer, storageServer);
            // 下载文件
            // 文件id
            String fileId = "group1/M00/00/00/rBFhCF7HnMiATcyJAAfd4SujPJk480_big.jpg";
            byte[] bytes = storageClient1.download_file1(fileId);
            // 使用输出流保存文件
            FileOutputStream fileOutputStream = new FileOutputStream(new File("C:/old.jpg"));
            fileOutputStream.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
