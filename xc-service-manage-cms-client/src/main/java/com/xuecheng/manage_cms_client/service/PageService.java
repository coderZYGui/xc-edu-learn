package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Description: 接收消息之后,将文件从GridFs中下载下来
 *
 * @author zygui
 * @date Created on 2020/5/15 15:59
 */
@Service
public class PageService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    private static  final Logger LOGGER = LoggerFactory.getLogger(PageService.class);

    /**
     * 何时调用: 当cms Client接收到消息,该方法就会被调用
     * 保存html页面到服务器物理路径上(将html文件保存到服务器指定目录)
     * @param pageId 页面id
     */
    public void savePageToServerPath(String pageId) {

        // 根据传过来的页面id来获取当前页面
        CmsPage cmsPage = this.findCmsPageById(pageId);
        // 通过当前页面的属性, 获取html的文件Id
        String htmlFileId = cmsPage.getHtmlFileId();

        // 从GridFs中查询html文件(以流的方式返回)
        InputStream inputStream = this.getFileById(htmlFileId);
        if (inputStream == null) {
            LOGGER.error("getFileById InputStream is null, htmlFileId:{}", htmlFileId);
            return;
        }

        // 得到站点的信息(根据站点id来得到)
        CmsSite cmsSite = this.findCmsSiteById(cmsPage.getSiteId());
        // 得到站点的物理路径
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();

        // 得到页面在物理服务器上的物理路径
        String pagePath = sitePhysicalPath + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

        // 将html文件保存到服务器的物理路径上
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            // 将文件的输入流,将文件输出到服务器的物理路径上
            IOUtils.copy(inputStream, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭流
            try {
                inputStream.close();
                fileOutputStream.close();
            } catch (Exception e ) {
                e.printStackTrace();
            }
        }
    }

    // 根据文件id从GridFs中查询文件内容
    public InputStream getFileById(String filedId) {
        // 文件对象
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(filedId)));
        // 打开下载流
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
        // 定义GridFsResource
        GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 根据页面id查询页面信息
    public CmsPage findCmsPageById(String pageId) {
        Optional<CmsPage> optional = cmsPageRepository.findById(pageId);
        if (optional.isPresent()) {
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }

    // 根据站点id查询站点信息
    public CmsSite findCmsSiteById(String siteId) {
        Optional<CmsSite> optional = cmsSiteRepository.findById(siteId);
        if (optional.isPresent()) {
           return optional.get();
        }
        return null;
    }

}
