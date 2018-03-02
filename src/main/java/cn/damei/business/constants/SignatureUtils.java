package cn.damei.business.constants;


import cn.mdni.commons.codec.MD5Utils;
import cn.mdni.commons.json.JsonUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static cn.damei.business.constants.PropertyHolder.getMpsKey;

public class SignatureUtils {

    /**
     * 签名比对
     * @param jsonData 请求的json 数据
     * @return
     */
    public static boolean signAuth(String jsonData)
    {
        Map<String,Object> map= JsonUtils.fromJson(jsonData, HashMap.class);
        String key = "";
        if(map.containsKey("key"))
        {
            key = map.get("key").toString();
            map.remove("key");
        }
        String mykey=getKey(map,"");
        if(!key.equals(mykey))
        {
            return false;
        }
        return true;
    }



    /**
     * 获取参数加密后的加密key 大写
     * @param map
     * @param separator 分隔符
     * @return
     */
    public static  String getKey(Map<String,Object> map,String separator){
        Map<String,Object> sortMap=sortMapByKey(map);
        String sign= new String();
        for (Map.Entry<String, Object> entry : sortMap.entrySet()) {
            if(null != entry.getValue() && !isArray(entry.getValue()))
            {
                //sign += entry.getValue().toString() + separator;
                sign += entry.getValue().toString().replace("null","") + separator;
            }
        }
        sign += getMpsKey();
        return MD5Utils.encode(sign).toUpperCase();
    }

    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return new HashedMap();
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * 判断json 值是否为数组
     * @param obj  json 值
     * @return
     */
    public static boolean isArray(Object obj){
        boolean result = false;
        String strJson = obj.toString();
        if(StringUtils.isBlank(strJson))
        {
            return  result;
        }
        final char[] strChar = strJson.toCharArray();
        final char firstChar = strChar[0];
        final char lastChar = strChar[strChar.length-1];

        if(firstChar == '[' && lastChar == ']'){
            result = true;
        }
        return  result;
    }
    public static class MapKeyComparator implements Comparator<String> {

        @Override
        public int compare(String str1, String str2) {

            return str1.compareTo(str2);
        }
    }
}

