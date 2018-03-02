package cn.damei.business.service.material;

import cn.damei.business.dao.material.DomainInfoDao;
import cn.damei.business.entity.material.DomainInfo;
import cn.damei.core.WebUtils;
import cn.damei.core.base.service.CrudService;
import cn.damei.core.dto.StatusDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class DomainInfoService extends CrudService<DomainInfoDao,DomainInfo>{

    @Autowired
    private DomainInfoDao domainInfoDao;

    @Transactional(rollbackFor = Exception.class)
    public Object insertOrUpdate(DomainInfo domainInfo) {
        try {
            Date date = new Date();
            Map map = new HashMap();
            map.put("domainName",domainInfo.getDomainName());
            map.put("includeDomainType",domainInfo.getIncludeDomainType());
            if ( domainInfo.getId() == null ) {
                DomainInfo entitys = this.entityDao.getByName(map);
                if ( entitys != null ) {
                    return StatusDto.buildFailureStatusDto("您输入的类型已存在，请重新输入！");
                }else {
                    domainInfo.setCreateTime(date);
                    domainInfo.setCreateUser(WebUtils.getLoggedUser().getName());
                    this.entityDao.insert(domainInfo);
                }
            } else {
                domainInfo.setUpdateTime(date);
                domainInfo.setUpdateUser(WebUtils.getLoggedUser().getName());
                this.entityDao.update(domainInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return StatusDto.buildDataSuccessStatusDto("保存成功");
    }

    @Transactional(rollbackFor = Exception.class)
    public void openById(Long id) {
        this.domainInfoDao.openById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public void offById(Long id) {
        this.domainInfoDao.offById(id);
    }
}
