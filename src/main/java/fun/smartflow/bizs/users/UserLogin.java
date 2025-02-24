package fun.smartflow.bizs.users;


import cc.xiaonuo.flow.annotation.FlowComponent;
import cc.xiaonuo.flow.engine.FlowContext;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@FlowComponent("userLogin")
public class UserLogin {

    public void handler(Map<String,Object> currentMap, FlowContext context){
        String username = (String) currentMap.get("username");
        StpUtil.login(username);
        log.info("用户{}登录成功", username);
    }
}
