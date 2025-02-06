package fun.smartflow.bizs.users;

import cc.xiaonuo.flow.annotation.FlowComponent;
import cc.xiaonuo.flow.engine.FlowContext;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import cn.hutool.core.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@FlowComponent("userExport")
public class UserExport {


    @Value("${file.domain}")
    private String domain;

    @Value("${file.uploadPath}")
    private String uploadPath;

    /**
     * @param domain
     * @param dataList
     * @param context
     */
    public void handler(String domain ,Object dataList ,FlowContext context){
        List<Object> dataList2 = (List<Object>)dataList;

        String fileName = UUID.randomUUID().toString() +  ".csv";

        String finalUploadPath = uploadPath + "/" + fileName;

        CsvWriter writer = CsvUtil.getWriter(finalUploadPath, CharsetUtil.CHARSET_UTF_8);
        // 获取表头
        String[] headers = new String[]{"用户ID", "用户名", "邮箱", "手机号", "微信ID","状态", "创建时间", "更新时间"};
        writer.write(headers);

        // 写入数据
        for (Object data : dataList2) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            Collection<String> keys = dataMap.keySet();
            String[] row = new String[keys.size()];
            int i=0;
            for (String key : keys){
                row[i++] = String.valueOf(dataMap.get(key));
            }
            writer.write(row);
        }

        context.setVariable("download_url", domain + "/files/" + fileName);
    }
}
