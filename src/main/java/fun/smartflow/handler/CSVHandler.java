package fun.smartflow.handler;

import cc.xiaonuo.common.exception.FlowException;
import cc.xiaonuo.flow.annotation.FlowComponent;
import cc.xiaonuo.flow.engine.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@FlowComponent("csvHandler")
@Slf4j
public class CSVHandler {

    public void handler(Map<String,String> fileNameMap, Map<String,String> bindKeyMap, FlowContext context) {
        try {
            HttpServletRequest request = context.getRequest();
            
            // 解析参数
            String valMap = fileNameMap.get("val");
            String fileKey = valMap.split("\\|")[1]; // 获取文件key

            // 获取上传文件
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile file = multipartRequest.getFile(fileKey);
            if(file == null){
                throw new FlowException("没有找到对应的文件");
            }

            // 获取绑定的参数
            // 解析参数
            String bindValMap = bindKeyMap.get("val");
            String bindKey = bindValMap.split("\\|")[1]; // 获取绑定key

            List<String[]> result = new ArrayList<>();
            // 使用 Apache Commons CSV 读取文件内容
            try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                 CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

                for (CSVRecord csvRecord : csvParser) {
                    // 将每行记录转换为 String[]
                    String[] values = new String[csvRecord.size()];
                    for (int i = 0; i < csvRecord.size(); i++) {
                        values[i] = csvRecord.get(i);
                    }
                    result.add(values);
                }
            } catch (IOException e) {
                throw new FlowException("读取文件时发生错误");
            }

            // 将数据存入上下文
            context.setVariable(bindKey, result);
            
        } catch (Exception e) {
            log.error("CSV处理数据失败", e);
            throw new FlowException("CSV处理数据失败");
        }
    }
}
