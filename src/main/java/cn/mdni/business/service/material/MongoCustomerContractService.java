package cn.mdni.business.service.material;

import cn.mdni.business.constants.SelectMaterialTypeEnmu;
import cn.mdni.business.entity.orderflow.CustomerContract;
import cn.mdni.business.entity.material.ProjectChangeMaterial;
import cn.mdni.business.entity.material.ProjectMaterial;
import cn.mdni.business.entity.material.SmSkuChangeDosage;
import cn.mdni.business.entity.material.SmSkuDosage;
import cn.mdni.business.entity.OtherAddReduceAmount;
import cn.mdni.commons.db.mongodb.BaseMongoDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description: MongoDB使用Service
 * @Company: 美得你智装科技有限公司
 * @Author Chaos
 * @Date: 2017/11/21.
 */
@Service
public class MongoCustomerContractService extends BaseMongoDaoImpl<CustomerContract> {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public MongoTemplate getMongoTemplate() {
        return this.mongoTemplate;
    }

    public void insertProjectMaterial(ProjectMaterial projectMaterial) {
        this.mongoTemplate.insert(projectMaterial);
    }

    public void insertSmSkuDosage(ProjectMaterial projectMaterial) {
        this.mongoTemplate.insert(projectMaterial);
    }

    /**
     * 批量插入
     *
     * @param obj
     */
    public <T> void insertBatch(List<T> obj) {
        this.mongoTemplate.insertAll(obj);
    }

    /**
     * 查询主材
     */
    public List<ProjectChangeMaterial> findProMatrlByContrCode(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is("1");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<ProjectChangeMaterial> projectMaterialList = this.mongoTemplate.find(query, ProjectChangeMaterial.class);
        return projectMaterialList;

    }

    /**
     * 查询主材
     */
    public List<ProjectChangeMaterial> findProMatrlByContrCode(String contractCode, String changeNo, String flag) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is(flag);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<ProjectChangeMaterial> projectMaterialList = this.mongoTemplate.find(query, ProjectChangeMaterial.class);
        return projectMaterialList;

    }

    /**
     * 查询用量
     */
    public List<SmSkuChangeDosage> findDosageByContrCode(String contractCode, String changeNo, String flag) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is(flag);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<SmSkuChangeDosage> smSkuChangeDosageList = this.mongoTemplate.find(query, SmSkuChangeDosage.class);
        return smSkuChangeDosageList;

    }

    /**
     * 查询用量
     */
    public List<SmSkuChangeDosage> findDosageByContrCode(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is("1");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<SmSkuChangeDosage> smSkuChangeDosageList = this.mongoTemplate.find(query, SmSkuChangeDosage.class);
        return smSkuChangeDosageList;

    }

    /**
     * 查询其他金额
     */
    public  List<OtherAddReduceAmount> findOtherAddReduceAmountByContrCode(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2));
        List<OtherAddReduceAmount> otherAddReduceAmountList = this.mongoTemplate.find(query, OtherAddReduceAmount.class);
        return otherAddReduceAmountList;

    }
    /**
     * 查询其他金额根据合同号
     */
    public List<OtherAddReduceAmount> findOtherAddReduceAmountOnlyByContrCode(String contractCode) {
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c2));
        List<OtherAddReduceAmount> otherAddReduceAmountList = this.mongoTemplate.find(query, OtherAddReduceAmount.class);
        return otherAddReduceAmountList;

    }
    /**
     * 查询原始其他金额
     */
    public List<OtherAddReduceAmount> findOtherAddReduceAmountByChangeVersion(String contractCode) {
        Criteria c = Criteria.where("changeVersionNo").is(contractCode + "00");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c));
        List<OtherAddReduceAmount> otherAddReduceAmountList = this.mongoTemplate.find(query, OtherAddReduceAmount.class);
        return otherAddReduceAmountList;

    }

    /**
     * 根据合同编号和选材标识查询选材
     */
    public List<ProjectMaterial> findMaterialByCodeAndFlag(String contractCode, String flag) {
        Criteria versionNo = Criteria.where("changeVersionNo").is(contractCode + "00");
        Criteria code = Criteria.where("contractCode").is(contractCode);
        Criteria materialFlag = Criteria.where("materialFlag").is(flag);
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(versionNo, code, materialFlag));
        List<ProjectMaterial> projectMaterials = this.mongoTemplate.find(query, ProjectMaterial.class);
        return projectMaterials;
    }

    /**
     * 根据合同编号和选材标识查询原始用量
     *
     * @param contractCode 合同编号
     */
    public List<SmSkuDosage> findSmSkuDosageByCodeAndFlag(String contractCode) {
        Criteria changeVersionNo = Criteria.where("changeVersionNo").is(contractCode + "00");
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(criteria.andOperator(changeVersionNo));
        List<SmSkuDosage> SmSkuDosages = this.mongoTemplate.find(query, SmSkuDosage.class);
        return SmSkuDosages;
    }

    public List<ProjectMaterial> findMaterialByConcodeAndChno(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c6 = Criteria.where("changeFlag").is("1");
        Criteria c3 = Criteria.where("categoryDetailCode").is(SelectMaterialTypeEnmu.PACKAGESTANDARD.toString());
        Criteria c4 = Criteria.where("categoryDetailCode").is(SelectMaterialTypeEnmu.UPGRADEITEM.toString());
        Criteria c5 = Criteria.where("categoryDetailCode").is(SelectMaterialTypeEnmu.MAINMATERIAL.toString());
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c6).orOperator(c3, c4, c5));
        List<ProjectMaterial> projectMaterialList = this.mongoTemplate.find(query, ProjectMaterial.class);
        return projectMaterialList;
    }

    public List<OtherAddReduceAmount> findOtherAmountByChanVerNo(String contractCode, String changeVersionNo) {
        Criteria c1 = Criteria.where("changeVersionNo").is(changeVersionNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is("1");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2,c3));
        List<OtherAddReduceAmount> otherAddReduceAmountList = this.mongoTemplate.find(query, OtherAddReduceAmount.class);
        return otherAddReduceAmountList;
    }

    public List<ProjectChangeMaterial> getMaterialCustomization(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is("0");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<ProjectChangeMaterial> projectChangeMaterialList = this.mongoTemplate.find(query, ProjectChangeMaterial.class);
        return projectChangeMaterialList;
    }

    public List<SmSkuChangeDosage> getDosageCustomization(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("changeNo").is(changeNo);
        Criteria c2 = Criteria.where("contractCode").is(contractCode);
        Criteria c3 = Criteria.where("changeFlag").is("0");
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2, c3));
        List<SmSkuChangeDosage> smSkuChangeDosageList = this.mongoTemplate.find(query, SmSkuChangeDosage.class);
        return smSkuChangeDosageList;
    }

    public List<ProjectChangeMaterial> findAllMaterial(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("contractCode").is(contractCode);
        Criteria c2 = Criteria.where("changeNo").is(changeNo);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2));
        List<ProjectChangeMaterial> projectChangeMaterialList = this.mongoTemplate.find(query, ProjectChangeMaterial.class);
        return projectChangeMaterialList;
    }

    public List<SmSkuChangeDosage> findAllDosage(String contractCode, String changeNo) {
        Criteria c1 = Criteria.where("contractCode").is(contractCode);
        Criteria c2 = Criteria.where("changeNo").is(changeNo);
        Query query = new Query();
        Criteria cr = new Criteria();
        query.addCriteria(cr.andOperator(c1, c2));
        List<SmSkuChangeDosage> smSkuChangeDosageList = this.mongoTemplate.find(query, SmSkuChangeDosage.class);
        return smSkuChangeDosageList;
    }
}
