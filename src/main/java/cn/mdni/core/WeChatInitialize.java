package cn.mdni.core;

import com.rocoinfo.weixin.WeChatInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <dl>
 * <dd>Description: 微信初始化工具类</dd>
 * <dd>Company: 美得你信息技术有限公司</dd>
 * <dd>@date：2017/4/5 下午1:11</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Component
@Lazy(false)
public class WeChatInitialize {

    Logger logger = LoggerFactory.getLogger(WeChatInitialize.class);

    /**
     * 使用默认的access_token和param_manager初始化微信
     */
    @PostConstruct
    public void init() {
        logger.info("start init wechat configuration......");
        WeChatInitializer init = new WeChatInitializer();
        init.init();
        logger.info("end init wechat configuration......");
    }
}
