package cn.mdni.business.service.userservice;

import cn.mdni.core.SystemConstants;
import cn.mdni.core.SystemPropertyHolder;
import cn.mdni.commons.http.HttpUtils;
import cn.mdni.commons.json.JsonUtils;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 调用认证中心获取用户 Service
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/07.
 */
@Service
public class UserService {

    private static final String REDIS_CACHE_KEY_DESIGNER = "designer_list";   //设计师缓存key
    private static final String REDIS_CACHE_KEY_AUDITOR = "auditor_list";     //审计员缓存key
    private static final int CACHESECONDS = 2 * 60 * 60;                      //缓存有效时间2小时
    //redis连接池
    @Autowired
    private JedisPool jedisPool;

    /**
     * 调用认证中心获取用户
     *
     * @param roleName
     */
    public List<Map<String, String>> buildAccount(String  roleName) {
        List<Map<String, String>> accountList = new ArrayList<Map<String, String>>();
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String resultJson;
            if(SystemPropertyHolder.getRoleDesignerName().equals( roleName ) ){
                resultJson = jedis.get( REDIS_CACHE_KEY_DESIGNER );
            }else{
                resultJson = jedis.get( REDIS_CACHE_KEY_AUDITOR );
            }
            //转为集合
            accountList = JsonUtils.fromJson(resultJson, List.class);

            //调用接口获取数据信息，并存入redis
            if( accountList == null || accountList.size() < 1 ){
                Map<String ,Object> param = Maps.newHashMap();
                param.put("appid", SystemPropertyHolder.getOauthCenterAppid());
                param.put("secret", SystemPropertyHolder.getOauthCenterSecret());

                String appTokenRespResult = HttpUtils.post(SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthAppTokenUrl(), param);
                Map<String, Object> appTokenResultMap = JsonUtils.fromJson(appTokenRespResult, Map.class);
                Map<String, String> appTokenData = (Map<String, String>) appTokenResultMap.get("data");
                param.put("accessToken", appTokenData.get("access_token"));
                param.put("roleName",roleName);
                //请求用户账号接口
                String appUserRespResult = HttpUtils.post(SystemConstants.URL_PREFIX + SystemPropertyHolder.getOauthAppUserUrl(), param);
                Map<String, Object> appUserResultMap = JsonUtils.fromJson(appUserRespResult, Map.class);
                Map<String, Object> appUserData = (Map<String, Object>) appUserResultMap.get("data");
                accountList = (List<Map<String, String>>) appUserData.get("users");

                List<Object> transferList = new ArrayList<Object>();
                if( accountList != null && accountList.size() > 0 ){
                    for( Map<String, String>  map : accountList){
                        transferList.add( map );
                    }
                }

                //转json
                String transferJson = JsonUtils.pojoToJson(transferList);

                if(SystemPropertyHolder.getRoleDesignerName().equals( roleName ) ){
                    jedis.set(REDIS_CACHE_KEY_DESIGNER, transferJson,"NX","EX",CACHESECONDS);
                    //jedis.expire(REDIS_CACHE_KEY_DESIGNER, CACHESECONDS);
                }else{
                    jedis.set(REDIS_CACHE_KEY_AUDITOR, transferJson,"NX","EX",CACHESECONDS);
                    //jedis.expire(REDIS_CACHE_KEY_DESIGNER, CACHESECONDS);
                }
            }
           return accountList;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }
        return accountList;
    }
}
