package com.zheng.travel.browser.article.utils;

import com.zheng.travel.browser.auth.util.SpringContextUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.model.ObjectMetadata;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 阿里云OSS工具类
 *
 * @author lianhuinan
 * @since 2022/4/12 16:30
 */
public class OssUtil {
    /**
     * 访问域名
     */
    private final static String ossAccessDomainUrl;
    /**
     * bucket 名称
     */
    private final static String ossBucketName;
    /**
     * 封装了 accessKeyId 和 accessKeySecret 的客户端对象
     */
    private final static OSSClient client;

    static {
        // 面试题: 如何实现在一个非Spring容器管理的对象中, 从Spring容器中拿到其他Bean对象.
        // Environment 对象的作用就是管理 Spring 容器运行时的各种配置内容
        Environment environment = SpringContextUtil.getBean(Environment.class);
        String endpointUrl = environment.getProperty("aliyun.oss.endpoint");
        ossBucketName = environment.getProperty("aliyun.oss.bucket-name");
        ossAccessDomainUrl = environment.getProperty("aliyun.oss.domain");
        String keyId = environment.getProperty("aliyun.accessKey.id");
        String keySecret = environment.getProperty("aliyun.accessKey.secret");
        client = new OSSClient(endpointUrl, CredentialsProviderFactory.newDefaultCredentialProvider(keyId, keySecret),
                new ClientConfiguration());
    }

    /**
     * @param fileName 用户文件名称
     * @return 实际的cos上文件名称
     */
    private static String getRealFileName(String saveFolder, String fileName) {
        return StringUtils.isNotEmpty(saveFolder) ? saveFolder + "/" + fileName : fileName;
    }

    public static String upload(String saveFolder, String contentType, String fileName, InputStream input) {
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(contentType) || null == input) {
            return null;
        }
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentType(contentType);
        String filePath = getRealFileName(saveFolder, fileName);
        try {
            client.putObject(ossBucketName, filePath, input, objectMeta);
            return ossAccessDomainUrl + filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String upload(String saveFolder, String contentType, String fileName, long contentLength, InputStream input) {
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(contentType) || contentLength <= 0 || null == input) {
            return null;
        }
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(contentLength);
        objectMeta.setContentType(contentType);
        String filePath = getRealFileName(saveFolder, fileName);
        try {
            client.putObject(ossBucketName, filePath, input, objectMeta);
            return ossAccessDomainUrl + "/" + filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String upload(String saveFolder, String fileMainName, MultipartFile multipartFile) {
        if (null != multipartFile && !multipartFile.isEmpty()) {
            try {
                String filename = multipartFile.getOriginalFilename();
                String extFileName;
                if (StringUtils.isNotEmpty(filename)) {
                    extFileName = filename.substring(filename.lastIndexOf("."));
                } else {
                    extFileName = ".jpg";
                }
                return upload(saveFolder, multipartFile.getContentType(), fileMainName + extFileName, multipartFile.getSize(), multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String upload4SpecialName(String saveFolder, String fileName, MultipartFile multipartFile) {
        if (null != multipartFile && !multipartFile.isEmpty()) {
            try {
                return upload(saveFolder, multipartFile.getContentType(), fileName, multipartFile.getSize(), multipartFile.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String uploadImgByBase64(String saveFolder, String fileName, String base64ImgContent) {
        if (StringUtils.isEmpty(fileName) || StringUtils.isEmpty(base64ImgContent)) {
            return null;
        }
        try {
            Matcher matcher = Pattern.compile("^data.(.*?);base64,").matcher(base64ImgContent);
            if (matcher.find()) {
                base64ImgContent = base64ImgContent.replace(matcher.group(), "");
            }
            byte[] bytes = Base64Utils.decodeFromString(base64ImgContent);
            return upload(saveFolder, "image/jpg", fileName, bytes.length, new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void delete(String fileUrl) {
        if (StringUtils.isEmpty(fileUrl)) {
            return;
        }
        try {
            fileUrl = fileUrl.replaceFirst(ossAccessDomainUrl, "");
            client.deleteObject(ossBucketName, fileUrl);
        } catch (OSSException | ClientException e) {
            e.printStackTrace();
        }
    }

    public static String getByFileName(String pathFile) {
        return ossAccessDomainUrl + client.getObject(ossBucketName, pathFile).getKey();
    }
}