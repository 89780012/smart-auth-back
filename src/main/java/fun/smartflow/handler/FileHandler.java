package fun.smartflow.handler;

import cc.xiaonuo.common.exception.FlowException;
import cc.xiaonuo.flow.annotation.FlowComponent;
import cc.xiaonuo.flow.engine.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@FlowComponent("fileHandler")
@Slf4j
public class FileHandler {

    @Value("${file.uploadPath}")
    private String uploadPath;

    public void handler(Map<String,String> fileNameMap, Map<String,String> filePathMap, FlowContext context) {
        try {
            HttpServletRequest request = context.getRequest();
            
            // 解析参数
            String valMap = fileNameMap.get("val");
            String fileKey = valMap.split("\\|")[1]; // fileName|file|表单中文件key

            // 获取上传文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile(fileKey);
            if(file == null){
                throw new FlowException("没有找到对应的文件");
            }
            String ext = file.getOriginalFilename().split("\\.")[1];  //后缀

            // 确保上传目录存在
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 生成唯一文件名避免冲突
            String uniqueFileName = UUID.randomUUID().toString() + "." + ext;
            String filePath = uploadPath + File.separator + uniqueFileName;
            String fileName = uniqueFileName;
            // 保存文件
            file.transferTo(new File(filePath));
            
            // 解析bindKey并存储文件路径到上下文
            String val2Map = filePathMap.get("val");
            String bindKey = val2Map.split("\\|")[1];
            context.setVariable(bindKey, fileName);
            
            log.info("File saved successfully: {}", filePath);
            
        } catch (IOException e) {
            log.error("Failed to handle file upload", e);
            throw new RuntimeException("文件上传处理失败", e);
        } catch (ClassCastException e) {
            log.error("Request is not multipart request", e);
            throw new RuntimeException("请求类型错误,不是文件上传请求", e);
        }
    }
}
