package fun.smartflow.bizs.users;

import cc.xiaonuo.flow.annotation.FlowComponent;
import cc.xiaonuo.flow.engine.FlowContext;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@FlowComponent("userLogout")
public class UserLogout {

    public void handler(FlowContext context){
        StpUtil.logout();
        log.info("UserLogout");
    }
}
