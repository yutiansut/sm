package cn.damei.business.controller.finance;

import cn.damei.business.constants.CommonStatusEnum;
import cn.damei.business.dto.finance.FinaPaymethodDto;
import cn.damei.business.entity.finance.FinaPaymethod;
import cn.damei.business.entity.finance.FinaPaymethodTree;
import cn.damei.business.entity.finance.PaymethodAttr;
import cn.damei.business.entity.finance.TreeNode;
import cn.damei.business.service.finance.FinaPaymethodService;
import cn.damei.business.service.finance.PaymethodAttrService;
import cn.mdni.commons.collection.MapUtils;
import cn.damei.core.dto.StatusDto;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/finance/paymethod")
public class PaymethodController {

    @Autowired
    private FinaPaymethodService finaPaymethodService;

    @Autowired
    private PaymethodAttrService paymethodAttrService;


    /**
     * 查询通用配置 (表名fina_paymethod)
     *
     * @param storeCode   前台传的门店ID
     * @param offset      分页的当前页
     * @param limit       每页多少条数据
     * @param orderColumn 排序的字段
     * @param orderSort   倒叙或者顺序排序
     * @return
     */
    @RequestMapping(value = "/list")
    public Object queryFinaPaymethodAll(@RequestParam(required = false) String storeCode,
                                        @RequestParam(defaultValue = "0") int offset,
                                        @RequestParam(defaultValue = "20") int limit,
                                        @RequestParam(defaultValue = "id") String orderColumn,
                                        @RequestParam(defaultValue = "DESC") String orderSort) {
        return StatusDto.buildDataSuccessStatusDto(finaPaymethodService.queryFinaPaymethodAll(storeCode, offset, limit, orderColumn, orderSort));
    }

    /**
     * 修改和删除方法
     *
     * @param entity
     * @return ztw
     */
    @RequestMapping(value = "/inputpaymethod")
    public Object inputFinaPaymethod(@RequestBody FinaPaymethod entity) {
        int resultInt = finaPaymethodService.getResult(entity);
        if (resultInt == 0) {
            return StatusDto.buildSuccessStatusDto("保存成功");
        } else if (resultInt == 2) {
            return StatusDto.buildFailureStatusDto("编码重复！");
        } else {
            return StatusDto.buildFailureStatusDto("保存失败");
        }
    }

    /**
     * 获取所有的门店
     *
     * @param
     * @return ztw
     */
    @RequestMapping(value = "/querystoreall")
    public Object queryStoreAll() {
        return StatusDto.buildDataSuccessStatusDto(finaPaymethodService.queryStoreAll());
    }

    /**
     * 获取类型的下拉框
     *
     * @param
     * @return ztw
     */
    @RequestMapping(value = "/fetchmethodtype")
    public Object fetchMethodType() {
        return StatusDto.buildDataSuccessStatusDto(finaPaymethodService.fetchMethodType());
    }

    /**
     * 获取状态下拉框
     *
     * @param
     * @return ztw
     */
    @RequestMapping(value = "/fetchmethodstatus")
    public Object fetchMethodStatus() {
        return StatusDto.buildDataSuccessStatusDto(finaPaymethodService.fetchMethodStatus());
    }

    /**
     * 获取特殊配置状态下拉框
     *
     * @param
     * @return ztw
     */
    @RequestMapping(value = "/getattrstatus")
    public Object getAttrStatus() {
        return StatusDto.buildDataSuccessStatusDto(finaPaymethodService.getAttrStatus());
    }

    /**
     * 回显 更id查询通用表(fina_paymethod)当前id下数据用于前台编辑的回显
     *
     * @param entity
     * @return
     */
    @RequestMapping(value = "/querybyid")
    public Object getSingleTagById(@RequestBody FinaPaymethod entity) {
        FinaPaymethod finaPaymethod = finaPaymethodService.getFinaPaymethodById(entity.getId());
        return StatusDto.buildDataSuccessStatusDto(finaPaymethod);
    }

    /**
     * 查询当前支付类型下的特殊类型全量
     *
     * @param id 通用表的Id(fina_paymethod)
     * @return
     */
    @RequestMapping("/querytree")
    public Object queyTree(Long id) {
        List<FinaPaymethodDto> childrenList = finaPaymethodService.queyTree(id);
        return StatusDto.buildDataSuccessStatusDto(childrenList);
    }

    /**
     * @param finaPaymethodTree
     * @Author ZTW
     * @Description 修改或者插入特殊配置(表名:fina_paymethod_attr)
     * @Date:16:02 2017/12/8
     */
    @RequestMapping(value = "/saveorupdate")
    public Object saveOrUpdate(@RequestBody FinaPaymethodTree finaPaymethodTree) {
        //插入之前先校验编码是否重复
        if (finaPaymethodService.validateCodeAvailable(finaPaymethodTree)) {
            return StatusDto.buildFailureStatusDto("编码重复！");
        }
        finaPaymethodService.saveOrUpdate(finaPaymethodTree);
        FinaPaymethodDto dto = new FinaPaymethodDto(finaPaymethodTree.getId(), finaPaymethodTree.getName());
        return StatusDto.buildDataSuccessStatusDto(dto);
    }

    /**
     * @param id 特殊配置表的id(fina_paymethod_attr 的id)
     * @Author ZTW
     * @Description 获取单个节点 用于回显
     * @Date:16:03 2017/12/8
     */
    @RequestMapping(value = "/getbyidtree", method = RequestMethod.GET)
    public Object getByIdTree(Long id) {
        FinaPaymethodTree dic = finaPaymethodService.getByIdTree(id);
        if (dic == null) {
            return StatusDto.buildFailureStatusDto("获取节点失败！");
        }
        return StatusDto.buildDataSuccessStatusDto(dic);
    }

    /**
     * @param finaPaymethodTree
     * @Author ZTW
     * @Description 删除单个接单
     * @Date:16:04 2017/12/8
     */
    @RequestMapping(value = "/deletebyid", method = RequestMethod.GET)
    public Object deleteById(FinaPaymethodTree finaPaymethodTree) {
        //名称和id不能为空
        if (finaPaymethodTree == null || finaPaymethodTree.getId() == null || StringUtils.isBlank(finaPaymethodTree.getName())) {
            return StatusDto.buildFailureStatusDto("自定义名称id或名称为空!");
        }
        //判断该节点下是否还有子节点
        boolean result = finaPaymethodService.checkHasChildNode(finaPaymethodTree.getId());
        if (result) {
            //有,提示不能删除
            return StatusDto.buildFailureStatusDto("该数据下还有子节点数据,请先删除子节点数据后,再尝试删除该数据!");
        }
        //根据ID删除该节点的数据
        int flag = finaPaymethodService.deleteFinaPaymethod(finaPaymethodTree);
        if (flag > 0) {
            return StatusDto.buildDataSuccessStatusDto("删除成功!");
        }
        return StatusDto.buildFailureStatusDto("删除失败!");
    }


    /**
     * 异步查询树
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/querytreebak")
    @ResponseBody
    public Object queryTreebak(String id, String sid) throws JSONException {
        Map<String, Object> mapResult = new HashedMap();
        List<TreeNode> treeNodeList = finaPaymethodService.getTreeNodes(id);
        return treeNodeList;
    }


    /**
     * 某个特殊属性的子属性
     *
     * @param methoId 支付方式的id
     * @param attrPid 父级属性的id
     * @return
     */

    @RequestMapping(value = "/querymethodattr")
    public Object queryMethodAttr(@RequestParam("methoId") String methoId, @RequestParam(value = "attrPid", defaultValue = "1") int attrPid) {
        Map<String, Object> queryMap = new HashMap<String, Object>();
        MapUtils.putNotNull(queryMap, "methodId", methoId);
        MapUtils.putNotNull(queryMap, "parentAttrId", attrPid);
        MapUtils.putNotNull(queryMap, "attrStatus", CommonStatusEnum.NORMAL.toString());
        List<PaymethodAttr> attrList = paymethodAttrService.queryPayMethodAttrWithCondition(queryMap);
        return StatusDto.buildDataSuccessStatusDto(attrList);
    }

}
