package cn.damei.business.service.orderflow;

import cn.damei.business.constants.NumberingTypeEnum;
import cn.damei.business.dao.orderflow.NumberingRuleDao;
import cn.damei.business.entity.orderflow.NumberingRule;
import cn.damei.core.base.service.CrudService;
import cn.mdni.commons.date.DateUtils;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class NumberingRuleService extends CrudService<NumberingRuleDao,NumberingRule> {

    /**
     * 获取单条编号规则
     *
     * @param storeCode 门店编码
     * @param numberType 编号类型
     *
     */
    public synchronized String getNumber(String storeCode,NumberingTypeEnum numberType)
    {
        //编号规则表增加字段 尾号格式化类型
        String numbering = "";
        Map<String,String> paramsMap=new HashMap<String,String>();
        paramsMap.put("storeCode",storeCode);
        paramsMap.put("numberType",numberType.toString());
        try{
            NumberingRule numberingRule = this.entityDao.getByStoreCodeAndNumType(paramsMap);
            //获取当天的日期 生成编号中部信息
            String midNum= DateUtils.parseStr(new Date(),"yyMMdd");
            if(numberingRule != null)
            {
                if(numberingRule.getMidNumber().equals(midNum))
                {
                    numberingRule.setTailNumber(numberingRule.getTailNumber()+1);
                }
                else
                {
                    numberingRule.setMidNumber(midNum);
                    numberingRule.setTailNumber(1);
                }
                this.update(numberingRule);
                //尾号格式化 “001” “0001”
                Format tailFormat = new DecimalFormat(numberingRule.getTailFormatType());
                numbering = numberingRule.getPrefix() + numberingRule.getMidNumber();
                numbering+=tailFormat.format(numberingRule.getTailNumber());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return numbering;
    }
}
