--日期时间 2017.11.02
--修改人：Chaos
--描  述：选材系统主要表结构
--执行状态 未执行


DROP TABLE IF EXISTS `sm_operate_log`;
CREATE TABLE `sm_operate_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(20) DEFAULT NULL COMMENT '项目编号',
  `system_type` varchar(10) DEFAULT NULL COMMENT '系统类型:1.订单流转 2.选材',
  `operate_type` varchar(50) DEFAULT NULL COMMENT '操作类型(操作时间节点)',
  `appoint_org_code` varchar(20) DEFAULT NULL COMMENT '被指定人工号',
  `appoint_name` varchar(50) DEFAULT NULL COMMENT '被指定人姓名',
  `appoint_phone` varchar(20) DEFAULT NULL COMMENT '被指定人手机号',
  `operate_description` varchar(50) DEFAULT NULL COMMENT '操作描述',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `sm_contract_operate_time_summary` (
  `id` int(11) not null auto_increment comment 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `material_submit_audit_time` datetime DEFAULT NULL COMMENT '选材提交审计时间',
  `material_assign_audit_time` datetime DEFAULT NULL COMMENT '选材指派审计时间',
  `material_audit_pass_time` datetime DEFAULT NULL COMMENT '选材审计通过时间',
  `material_audit_refuse_time` datetime DEFAULT NULL COMMENT '选材审计未通过时间',
  `material_sign_time` datetime DEFAULT NULL COMMENT '选材签约时间',
  `start_change_time` datetime DEFAULT NULL COMMENT '发起变更时间',
  `change_submit_audit_time` datetime DEFAULT NULL COMMENT '变更提交审计时间',
  `change_audit_pass_time` datetime DEFAULT NULL COMMENT '变更审计通过时间',
  `change_audit_refuse_time` datetime DEFAULT NULL COMMENT '变更审计未通过时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目操作时间汇总';

-- ----------------------------
-- Table structure for sm_change_amount
-- ----------------------------
DROP TABLE IF EXISTS `sm_change_amount`;
CREATE TABLE `sm_change_amount` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `change_detail_id` int(11) DEFAULT NULL COMMENT '变更单id',
  `category_code` varchar(32) DEFAULT NULL COMMENT '大类code',
  `category_detail_code` varchar(32) DEFAULT NULL COMMENT '小类code',
  `original_amount` decimal(18,3) DEFAULT NULL COMMENT '原金额',
  `cash_amount` decimal(18,3) DEFAULT NULL COMMENT '现金额',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变更单金额结果明细';

-- ----------------------------
-- Table structure for sm_change_audit
-- ----------------------------
DROP TABLE IF EXISTS `sm_change_audit`;
CREATE TABLE `sm_change_audit` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `change_id` int(11) DEFAULT NULL COMMENT '变更单id',
  `audit_user` varchar(20) DEFAULT NULL COMMENT '审核人',
  `audit_dep` varchar(50) DEFAULT NULL COMMENT '审核部门',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_result` varchar(50) DEFAULT NULL COMMENT '审核结果',
  `audit_remark` varchar(100) DEFAULT NULL COMMENT '审核备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选材变更单审计记录';

-- ----------------------------
-- Table structure for sm_change_detail
-- ----------------------------
DROP TABLE IF EXISTS `sm_change_detail`;
CREATE TABLE `sm_change_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `change_log_id` int(11) DEFAULT NULL COMMENT '变更日志主键',
  `change_category_url` varchar(50) DEFAULT NULL COMMENT '变更类目url(一级类目)',
  `change_no` varchar(50) DEFAULT NULL COMMENT '变更单号',
  `change_category_name` varchar(50) DEFAULT NULL COMMENT '变更类目名称',
  `current_status` varchar(32) DEFAULT NULL COMMENT '当前状态(材料部审核中、设计总监审核中、审计审核中、审核通过、审核未通过)',
  `current_aduit_user` varchar(32) DEFAULT NULL COMMENT '当前审计人',
  `current_audit_time` datetime DEFAULT NULL COMMENT '当前审计时间',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `pass_materials_department` char(1) DEFAULT '0' COMMENT '是否过材料部',
  `material_remarks` varchar(1000) DEFAULT NULL COMMENT '材料审核备注',
  `design_director_remarks` varchar(1000) DEFAULT NULL COMMENT '设计总监备注',
  `download_status` char(1) DEFAULT '0' COMMENT '是否下载',
  `download_times` int(11) DEFAULT 0 COMMENT '下载次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='变更信息';

-- ----------------------------
-- Table structure for sm_change_log
-- ----------------------------
DROP TABLE IF EXISTS `sm_change_log`;
CREATE TABLE `sm_change_log` (
  `id` int(11) not null auto_increment comment 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `change_version_no` varchar(50) DEFAULT NULL COMMENT '变更版本',
  `change_status` varchar(32) DEFAULT NULL COMMENT '变更状态',
  `operat_user` varchar(32) DEFAULT NULL COMMENT '操作人',
  `operat_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目变更日志';

-- ----------------------------
-- Table structure for sm_domain_category
-- ----------------------------
DROP TABLE IF EXISTS `sm_domain_category`;
CREATE TABLE `sm_domain_category` (
  `id` int(11) not null auto_increment comment 'id',
  `domain_info_id` int(11) DEFAULT NULL COMMENT '功能区信息',
  `product_category_id` int(11) DEFAULT NULL COMMENT '商品类目',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能区类目关系';

-- ----------------------------
-- Table structure for sm_domain_info
-- ----------------------------
DROP TABLE IF EXISTS `sm_domain_info`;
CREATE TABLE `sm_domain_info` (
  `id` int(11) not null auto_increment comment 'id',
  `domain_name` varchar(50) DEFAULT NULL COMMENT '功能区名称',
  `include_domain_type` varchar(32) DEFAULT NULL COMMENT '包涵功能区类型',
  `domain_status` varchar(20) DEFAULT NULL COMMENT '状态',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='功能区信息（新增）';

-- ----------------------------
-- Table structure for sm_meal_info
-- ----------------------------
DROP TABLE IF EXISTS `sm_meal_info`;
CREATE TABLE `sm_meal_info` (
  `id` int(11) not null auto_increment comment 'id',
  `store_code` varchar(32) DEFAULT NULL COMMENT '所属门店id',
  `meal_name` varchar(50) DEFAULT NULL COMMENT '套餐名称',
  `meal_sale_price` decimal(18,3) DEFAULT NULL COMMENT '套餐售价',
  `validity_date` datetime DEFAULT NULL COMMENT '有效期起始时间',
  `expiration_date` datetime DEFAULT NULL COMMENT '有效期截止时间',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `meal_status` varchar(32) DEFAULT NULL COMMENT '状态',
  `no_deadline` int(11) DEFAULT NULL COMMENT '是否无期限',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='套餐信息表（新增）';

-- ----------------------------
-- Table structure for sm_project_material
-- ----------------------------
DROP TABLE IF EXISTS `sm_project_material`;
CREATE TABLE `sm_project_material` (
  `id`  varchar(50) not null comment 'id',
  `product_category_url` varchar(50) DEFAULT NULL COMMENT '商品类目url',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目code',
  `category_code` varchar(50) DEFAULT NULL COMMENT '选材类型（套餐标配、增项、减项）',
  `category_detail_code` varchar(32) DEFAULT NULL COMMENT '主材，基装增项',
  `sku_code` varchar(50) DEFAULT NULL COMMENT 'sku编码',
  `sku_image_path` varchar(200) DEFAULT NULL COMMENT 'sku图片路径',
  `store_sale_price` decimal(18,3) DEFAULT NULL COMMENT '门店销售价',
  `design_remark` varchar(100) DEFAULT NULL COMMENT '设计备注',
  `audit_remark` varchar(100) DEFAULT NULL COMMENT '审计备注',
  `sku_sqec` varchar(20) DEFAULT NULL COMMENT '规格',
  `sku_model` varchar(20) DEFAULT NULL COMMENT '型号',
  `brand_id` int(11) DEFAULT NULL COMMENT '品牌id',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `material_unit` varchar(20) DEFAULT NULL COMMENT '材料单位',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `quota_describe` varchar(1000) DEFAULT NULL COMMENT '',
  `check_scale` int(1) DEFAULT NULL COMMENT '是否复尺',
  `attribute1` varchar(100) DEFAULT NULL COMMENT '颜色：白色',
  `attribute2` varchar(100) DEFAULT NULL COMMENT '属性值2',
  `attribute3` varchar(100) DEFAULT NULL COMMENT '属性值3',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目主材sku';

-- ----------------------------
-- Table structure for sm_project_material_temporary
-- ----------------------------
DROP TABLE IF EXISTS `sm_project_change_material`;
CREATE TABLE `sm_project_change_material` (
  `id`  varchar(50) not null comment 'id',
  `product_category_url` varchar(50) DEFAULT NULL COMMENT '商品类目url',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目code',
  `category_code` varchar(50) DEFAULT NULL COMMENT '选材类型（套餐标配、增项、减项）',
  `category_detail_code` varchar(32) DEFAULT NULL COMMENT '主材，基装增项',
  `sku_code` varchar(50) DEFAULT NULL COMMENT 'sku编码',
  `sku_image_path` varchar(200) DEFAULT NULL COMMENT 'sku图片路径',
  `store_sale_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店销售价',
  `design_remark` varchar(100) DEFAULT NULL COMMENT '设计备注',
  `audit_remark` varchar(100) DEFAULT NULL COMMENT '审计备注',
  `sku_sqec` varchar(20) DEFAULT NULL COMMENT '规格',
  `sku_model` varchar(20) DEFAULT NULL COMMENT '型号',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `material_unit` varchar(20) DEFAULT NULL COMMENT '材料单位',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `quota_describe` varchar(1000) DEFAULT NULL COMMENT '',
  `check_scale` int(11) DEFAULT NULL COMMENT '是否复尺',
  `attribute1` varchar(100) DEFAULT NULL COMMENT '颜色：白色',
  `attribute2` varchar(100) DEFAULT NULL COMMENT '属性值2',
  `attribute3` varchar(100) DEFAULT NULL COMMENT '属性值3',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `materials_status` varchar(50) DEFAULT NULL COMMENT '材料状态',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目选材变更材料表';

-- ----------------------------
-- Table structure for sm_sku_dosage
-- ----------------------------
DROP TABLE IF EXISTS `sm_sku_dosage`;
CREATE TABLE `sm_sku_dosage` (
  `id` varchar(50) not null comment 'id',
  `project_material_id` varchar(50) not null COMMENT '项目主材skuid',
  `domain_name` varchar(50) DEFAULT NULL COMMENT '功能区名称',
  `store_purchase_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店采购价',
  `store_upgrade_difference_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店升级差价',
  `store_reduce_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店减项价',
  `store_increase_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店增项价',
  `store_sale_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店销售价',
  `budget_dosage` decimal(18,3) DEFAULT NULL COMMENT '85 m2/850 片 ',
  `convert_unit` varchar(50) DEFAULT NULL COMMENT '㎡"转为"片',
  `loss_factor` decimal(18,3) DEFAULT NULL COMMENT '1.08',
  `no_loss_dosage` decimal(18,3) DEFAULT NULL COMMENT '850',
  `loss_dosage` decimal(18,3) DEFAULT NULL COMMENT '900',
  `project_proportion` decimal(18,3) DEFAULT NULL COMMENT '工程占比',
  `dosage_pricing_mode` varchar(1000) DEFAULT NULL COMMENT '单价/基装增项总价占比/工程总价占比',
  `dosage_remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sku用量信息';

-- ----------------------------
-- Table structure for sm_sku_dosage_temporary
-- ----------------------------
DROP TABLE IF EXISTS `sm_sku_change_dosage`;
CREATE TABLE `sm_sku_change_dosage` (
  `id` varchar(50) not null comment 'id',
  `project_material_id` varchar(50) not null COMMENT '项目主材skuid',
  `domain_name` varchar(200) DEFAULT NULL COMMENT '功能区名称',
  `store_purchase_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店采购价',
  `store_sale_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店销售价',
  `store_upgrade_difference_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店升级差价',
  `store_reduce_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店减项价',
  `store_increase_price` decimal(18,3) DEFAULT NULL COMMENT 'sku门店增项价',
  `budget_dosage` decimal(18,3) DEFAULT NULL COMMENT '预算用量85（地砖存平米数） ',
  `convert_unit` varchar(50) DEFAULT NULL COMMENT '㎡"转为"片',
  `loss_factor` decimal(18,3) DEFAULT NULL COMMENT '1.08',
  `no_loss_dosage` decimal(18,3) DEFAULT NULL COMMENT '850',
  `loss_dosage` decimal(18,3) DEFAULT NULL COMMENT '900',
  `project_proportion` decimal(18,3) DEFAULT NULL COMMENT '工程占比',
  `dosage_pricing_mode` varchar(1000) DEFAULT NULL COMMENT '单价/基装增项总价占比/工程总价占比',
  `dosage_remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `original_dosage` decimal(18,3) DEFAULT 0.000 COMMENT '原含损耗用量',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COMMENT='sku变更用量信息';

-- ----------------------------
-- Table structure for sm_project_intem_type
-- ----------------------------
DROP TABLE IF EXISTS sm_project_intem_type;
CREATE TABLE sm_project_intem_type (
  id int(11) NOT NULL COMMENT 'id -- ''',
  project_intem_type_name varchar(100) DEFAULT NULL COMMENT '分类名称',
  status char(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='施工项类型';

-- ----------------------------
-- Table structure for sm_project_intem
-- ----------------------------
DROP TABLE IF EXISTS sm_project_intem;
CREATE TABLE sm_project_intem (
  id int(11) NOT NULL COMMENT 'id',
  project_intem_type_id int(11) DEFAULT NULL COMMENT '施工项分类id',
  project_intem_mold varchar(100) DEFAULT NULL COMMENT '施工项类型 1.增项；2.减项',
  project_intem_code varchar(64) DEFAULT NULL COMMENT '施工项编码',
  project_intem_name varchar(100) DEFAULT NULL COMMENT '施工项名称',
  project_intem_unit varchar(64) DEFAULT NULL COMMENT '施工项计量单位',
  project_type varchar(100) DEFAULT NULL COMMENT '施工类型',
  group_type varchar(10) DEFAULT NULL COMMENT '套餐类型 1.套餐内；2套餐外',
  status varchar(64) DEFAULT NULL COMMENT '状态',
  project_intem_detail varchar(2000) DEFAULT NULL COMMENT '施工项详情',
  using_platform varchar(50) DEFAULT NULL COMMENT '使用平台',
  valuation_method varchar(50) DEFAULT NULL COMMENT '计价方式',
  subordinate_category varchar(50) DEFAULT NULL COMMENT '所属类别',
  is_default char(1) DEFAULT NULL COMMENT '是否默认项',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='施工项';

-- ----------------------------
-- Table structure for sm_project_intem_price
-- ----------------------------
DROP TABLE IF EXISTS sm_project_intem_price;
CREATE TABLE sm_project_intem_price (
  id int(11) NOT NULL COMMENT 'id',
  store_code int(11) DEFAULT NULL COMMENT '门店code',
  project_intem_id int(11) DEFAULT NULL COMMENT '施工项id',
  project_intem_price decimal(10,2) DEFAULT NULL COMMENT '价格',
  project_intem_version int(11) DEFAULT NULL COMMENT '版本号',
  effect_date date DEFAULT NULL COMMENT '生效日期',
  project_intem_price_remarks varchar(255) DEFAULT NULL COMMENT '价格备注',
  project_intem_cost_price decimal(10,2) DEFAULT NULL COMMENT '成本单价/成本占比',
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='施工项价格';

-- ----------------------------
-- Table structure for sm_store_relation_mapping
-- ----------------------------
DROP TABLE IF EXISTS `sm_store_relation_mapping`;
CREATE TABLE `sm_store_relation_mapping` (
  `id` int(11) NOT NULL,
  `mps_store_id` int(11) DEFAULT NULL COMMENT '--产业工人门店id',
  `mps_store_name` varchar(50) DEFAULT NULL COMMENT '--产业工人门店名称',
  `sm_store_name` varchar(50) DEFAULT NULL COMMENT '--选材门店名称',
  `sm_store_code` varchar(30) DEFAULT NULL COMMENT '--选材门店code',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `sm_other_add_reduce_amount`;
create table `sm_other_add_reduce_amount`
(
  id                   int(11) not null auto_increment comment 'id',
  contract_code        varchar(50) DEFAULT NULL comment '项目编码',
  item_name            varchar(50) DEFAULT NULL comment '增减项（优惠税，优惠管理费。。）',
  add_reduce_reason    varchar(200) DEFAULT NULL comment '增加原因',
  add_reduce_type      varchar(20) DEFAULT NULL comment '增减类型（增加、减少）',
  is_taxed_amount      char(1) DEFAULT '0' comment '1是，0否',
  quota                decimal(18,3) DEFAULT NULL comment '额度',
  approver             varchar(50) DEFAULT NULL comment '批准人',
  change_flag          char(1) DEFAULT '0' comment '1是，0否',
  primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='其他增减项金额';

-- ----------------------------
-- Table structure for sm_order_bespeak_contract
-- ----------------------------
DROP TABLE IF EXISTS `sm_order_bespeak_contract`;
create table `sm_order_bespeak_contract`
(
  id                   int(11) not null auto_increment comment 'id',
  contract_code        varchar(50) DEFAULT NULL comment '项目编码',
  complete_sign        char(1) DEFAULT '0' comment '是否完成签约，1是、0否',
  schedule_sign_time   datetime DEFAULT NULL comment '预定签约时间',
  executor             varchar(50) DEFAULT NULL comment '执行人',
  package_type         varchar(50) DEFAULT NULL comment '套餐类型',
  is_taxed_amount      char(1) DEFAULT '0' comment '1是，0否',
  dismantle_repair_cost decimal(18,3) DEFAULT NULL comment '拆除修复费',
  long_range_cost      decimal(18,3) DEFAULT NULL comment '远程费',
  carry_cost           decimal(18,3) DEFAULT NULL comment '搬运费',
  total_budget_amount  decimal(18,3) DEFAULT NULL comment '预算总金额',
  advance_payment      decimal(18,3) DEFAULT NULL comment '预付款（定金）',
  primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单预约合同';

CREATE TABLE `sm_construction_change` (
  `id` int(11) not null auto_increment comment 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `change_no` varchar(50) DEFAULT NULL COMMENT '基装变更单号',
  `change_apply_date` datetime DEFAULT NULL COMMENT '变更申请时间',
  `change_reason` varchar(32) DEFAULT NULL COMMENT '变更原因',
  `add_project_total_price` decimal(18,2) DEFAULT NULL COMMENT '增项金额合计',
  `cut_project_total_price` decimal(18,2) DEFAULT NULL COMMENT '减项金额合计',
  `change_list_total_price` decimal(18,2) DEFAULT NULL COMMENT '变更金额合计',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='施工变更信息（基装变更）';

CREATE TABLE `sm_construction_change_detail` (
  `id` int(11) not null auto_increment comment 'id',
  `change_no` varchar(50) DEFAULT NULL COMMENT '基装变更单号',
  `project_intem_code` varchar(50) DEFAULT NULL COMMENT '施工项code',
  `change_project_name` varchar(50) DEFAULT NULL COMMENT '变更项目名称',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `amount` int(11) DEFAULT NULL COMMENT '数量',
  `loss` decimal(18,2) DEFAULT NULL COMMENT '损耗',
  `labor_costs` decimal(18,2) DEFAULT NULL COMMENT '人工费',
  `total_unit_price` decimal(18,2) DEFAULT NULL COMMENT '综合单价',
  `unit_project_total_price` decimal(18,2) DEFAULT NULL COMMENT '总价',
  `explain` varchar(1000) DEFAULT NULL COMMENT '说明',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='施工变更明细（基装变更）';

/*==============================================================*/
/* Table: sm_material_change_audit_record                       */
/*==============================================================*/
drop table if exists `sm_material_change_audit_record`;
create table `sm_material_change_audit_record`
(
  id                   int not null auto_increment comment 'id',
  change_no            varchar(50) comment '变更单编码',
  audit_user           varchar(20) comment '审核人',
  audit_dep            varchar(50) comment '审核部门',
  audit_time           datetime comment '审核时间',
  audit_result         varchar(50) comment '审核结果',
  audit_remark         varchar(100) comment '审核备注',
  change_version       varchar(50) comment '变更版本号',
  primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选材变更单审计记录';


drop table if exists sm_order_quota_bill;
create table sm_order_quota_bill
(
  id                   int not null auto_increment comment 'id',
  contract_code        varchar(50) comment '项目编码',
  category_code        varchar(32) comment '大类code',
  category_detail_code varchar(32) comment '小类code',
  amount               decimal(18,3) comment '金额',
  creater              varchar(50) comment '创建人',
  create_time          datetime comment '创建时间',
  primary key (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选材定额明细';

DROP TABLE IF EXISTS `sm_contract_single`;
CREATE TABLE `sm_contract_single` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `single_tag_id` int(11) DEFAULT NULL COMMENT '串单标签id',
  `contract_code` varchar(20) DEFAULT NULL COMMENT '订单编号',
  `deleted` varchar(10) DEFAULT NULL COMMENT '删除状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单串单关联表';


DROP TABLE IF EXISTS `sm_numbering_rule`;
CREATE TABLE `sm_numbering_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `store_code` varchar(50) DEFAULT NULL COMMENT '门店编码',
  `number_type` varchar(20) DEFAULT NULL COMMENT '编号类型',
  `prefix` varchar(20) DEFAULT NULL COMMENT '编号前缀',
  `mid_number` varchar(20) DEFAULT NULL COMMENT '编号中间信息',
  `tail_number` varchar(20) DEFAULT NULL COMMENT '编号尾部信息',
  `tail_format_type` varchar(20) DEFAULT NULL COMMENT '编号尾号格式化类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单流转系统订单编号生成规则';

DROP TABLE IF EXISTS `sm_project_design`;
CREATE TABLE `sm_project_design` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(20) DEFAULT NULL COMMENT '项目编号',
  `design_type` varchar(20) DEFAULT NULL COMMENT '设计类型',
  `complete` varchar(10) DEFAULT NULL COMMENT '是否完成',
  `complete_time` date DEFAULT NULL COMMENT '完成时间',
  `executor` varchar(20) DEFAULT NULL COMMENT '执行人',
  `cad_url` varchar(100) DEFAULT NULL COMMENT 'CAD路径',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `operate_time` date DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='项目设计表';

DROP TABLE IF EXISTS `sm_project_sign`;
CREATE TABLE `sm_project_sign` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(20) DEFAULT NULL COMMENT '项目编号',
  `complete` varchar(10) DEFAULT NULL COMMENT '是否完成',
  `complete_time` date DEFAULT NULL COMMENT '完成时间',
  `sign_executor` varchar(20) DEFAULT NULL COMMENT '签约执行人',
  `contract_sign_trem` varchar(10) DEFAULT NULL COMMENT '合同签订工期',
  `contract_start_time` date DEFAULT NULL COMMENT '合同签订开工时间',
  `contract_complete_time` date DEFAULT NULL COMMENT '合同签订竣工日期',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  `operate_time` date DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='项目签约信息';

DROP TABLE IF EXISTS `sm_single_tag`;
CREATE TABLE `sm_single_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(100) DEFAULT NULL COMMENT '标签名称',
  `operator` varchar(20) DEFAULT NULL COMMENT '操作人',
  `operate_time` date DEFAULT NULL COMMENT '操作时间',
  `describtion` varchar(500) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='串单标签表';

DROP TABLE IF EXISTS `sm_store_relation_numrule`;
CREATE TABLE `sm_store_relation_numrule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `store_code` varchar(50) DEFAULT NULL COMMENT '门店编码',
	`rule_id` int(11)  COMMENT '门店编码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单流转门店和编号规则对应表';


DROP TABLE IF EXISTS `sm_proportion_money`;
CREATE TABLE `sm_proportion_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `baseloadrating1` decimal(10,3) DEFAULT NULL COMMENT '集装定额',
  `renovationAmount` decimal(10,3) DEFAULT NULL COMMENT '装修工程总价',
  `baseloadrating3` decimal(10,3) DEFAULT NULL COMMENT '拆改基桩',
  `comprehensivefee4` decimal(10,3) DEFAULT NULL COMMENT '拆改总金额',
  `contract_code` varchar(255) DEFAULT NULL COMMENT '合同编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='选材基桩金额';

DROP TABLE IF EXISTS `sm_request_outapi_log`;
CREATE TABLE `sm_request_outapi_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `push_type` VARCHAR(30) DEFAULT NULL COMMENT '推送类型',
  `need_again_push` TINYINT DEFAULT NULL COMMENT '是否需要再次推送',
  `push_content` text DEFAULT NULL COMMENT '推送内容',
  `response_content` VARCHAR(500) DEFAULT NULL COMMENT '响应内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='sm系统请求外部接口调用记录';

DROP TABLE IF EXISTS `sm_assistinfo_sync`;
CREATE TABLE `sm_assistinfo_sync` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编号',
  `assist_item_name` varchar(50) DEFAULT NULL COMMENT '辅材名称',
  `unit` varchar(50) DEFAULT NULL COMMENT '单位',
  `unit_price` decimal(18,2) DEFAULT 0 COMMENT '门店销售价',
  `worker_price` decimal(18,2) DEFAULT 0 COMMENT '工人结算价',
  `supplier_price` decimal(18,2) DEFAULT 0 COMMENT '供货价',
  `last_count` decimal(18,2) DEFAULT 0 COMMENT '用量',
  `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `supplier_name` varchar(200) DEFAULT NULL COMMENT '供应商名称',
  `material_cate_name` varchar(50) DEFAULT NULL COMMENT '材料分类名称',
  `acceptance_date` datetime DEFAULT NULL COMMENT '验收时间',
  `brand` varchar(100) DEFAULT NULL COMMENT '品牌',
  `assist_item_no` varchar(100) DEFAULT NULL COMMENT '辅料编码',
	`create_time` datetime DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='同步辅料信息';

-- ----------------------------
-- 以下为更新原有表，增加字段或者更改类型等
-- ----------------------------

ALTER TABLE prod_sku_price modify price_type ENUM('SUPPLY','STORE','SALE','UPGRADE','INCREASED','MINUS') comment 'SUPPLY("网真采购价"), STORE("门店采购价"), SALE("门店销售价"),UPGRADE("升级项价"),INCREASED("增项"),MINUS("减项")';

<!--商品类目表增加字段-->
alter table prod_catalog ADD column catalog_type varchar(100) comment '主材|基、辅材|软饰|家具|家电';
alter table prod_catalog ADD column loss_factor DECIMAL(18,3) comment '损耗系数';
alter table prod_catalog ADD column use_decimal INT comment '是否允许为小数';
alter table prod_catalog ADD column meal_category INT comment '是否套餐类目';
<!--客户合同表增加字段-->
alter table customer_contract ADD column store_code varchar(20) comment '门店编码';
alter table customer_contract ADD column second_contact varchar(20) comment '第二联系人';
alter table customer_contract ADD column second_contact_mobile varchar(11) comment '第二联系人电话';
alter table customer_contract ADD column meal_id INT comment '选择套餐id';
alter table customer_contract ADD column meal_price decimal(18,3) comment '套餐单价';
alter table customer_contract ADD column layout varchar(50) comment '户型';
alter table customer_contract ADD column build_area decimal(18,3) comment '房屋面积';
alter table customer_contract ADD column valuate_area decimal(18,3) comment '计价面积';
alter table customer_contract ADD column house_condition int comment '房屋状况';
alter table customer_contract ADD column elevator INT comment '有无电梯';
alter table customer_contract ADD column house_type varchar(50) comment '房屋类型';
alter table customer_contract ADD column address_province varchar(50) comment '工程地址-省份';
alter table customer_contract ADD column address_city varchar(50) comment '工程地址-市州';
alter table customer_contract ADD column address_area varchar(50) comment '工程地址-区县';
alter table customer_contract ADD column activity_name varchar(500) comment '活动名称';
alter table customer_contract ADD column other_install_info varchar(500) comment '是否有吊顶,石膏线,电视背景墙,以上三项皆无';
alter table customer_contract ADD column design_remark varchar(500) comment '设计备注';
alter table customer_contract ADD column audit_remark varchar(500) comment '审计备注';
alter table customer_contract ADD column material_status varchar(50) comment '未选材,选材中...';
alter table customer_contract ADD column payment_time datetime comment '交款时间';
alter table customer_contract ADD column contract_amount decimal(18,2) comment '原始合同金额';
alter table customer_contract ADD column modify_amount decimal(18,2) comment '拆改费';
alter table customer_contract ADD column single_order_info varchar(50) comment '串单信息';
alter table customer_contract ADD column designer_org_code varchar(20) comment '设计师员工编号';
alter table customer_contract ADD column auditor varchar(20) comment '审计员';
alter table customer_contract ADD column auditor_mobile varchar(20) comment '审计员手机号';
alter table customer_contract ADD column auditor_org_code varchar(20) comment '审计员员工编号';
alter table customer_contract ADD column contract_status varchar(50) comment '项目状态';
alter table customer_contract ADD column finance_status varchar(50) comment '财务状态';
alter table customer_contract ADD column current_change_version varchar(50) comment '当前变更版本号';
alter table customer_contract ADD column book_house_complete varchar(10) comment '量房是否完成';
alter table customer_contract ADD column book_house_complete_time date comment '量房完成时间';
alter table customer_contract ADD column book_house_executor varchar(20) comment '量房执行人';
alter table customer_contract ADD column book_house_cadurl varchar(100) comment '量房CAD路径';
alter table customer_contract ADD column out_map_complete varchar(10) comment '出图是否完成';
alter table customer_contract ADD column out_map_complete_time date comment '出图完成时间';
alter table customer_contract ADD column out_map_executor varchar(20) comment '出图执行人';
alter table customer_contract ADD column out_map_cadurl varchar(100) comment '出图CAD路径';
alter table customer_contract ADD column forward_delivery_housing INT comment '是否期房';
alter table customer_contract ADD column is_sign_deposit int comment '是否签定金合同';
alter table customer_contract ADD column is_return_word int comment '是否有可退字样';
alter table customer_contract ADD column create_user varchar(50) comment '创建人';
alter table customer_contract ADD column create_time datetime comment '创建时间';
alter table customer_contract ADD column discount_name varchar(100) comment '折扣名称';
alter table customer_contract ADD column payment_status varchar(20) comment '付款状态';
alter table customer_contract ADD column order_flow_status varchar(50) comment '订单流转状态';
alter table customer_contract ADD column plan_decorate_time date comment '计划装修时间';
alter table customer_contract ADD column book_house_time date comment '预约量房时间';
alter table customer_contract ADD column plan_house_time date comment '计划量房时间';
alter table customer_contract ADD column designer_dep_name varchar(50) comment '设计师部门名称';
alter table customer_contract ADD column designer_dep_code varchar(20) comment '设计师部门号';
alter table customer_contract ADD column designer_code varchar(20) comment '设计师工号';
alter table customer_contract ADD column service_mobile varchar(20) comment '客服手机号';
alter table customer_contract ADD column service_name varchar(50) comment '客服姓名';
alter table customer_contract ADD column service_code varchar(20) comment '客服工号';
alter table customer_contract ADD column contract_uuid varchar(50) comment '合同UUID';
alter table customer_contract  ADD column basic_construction_time datetime comment '基装完成时间';
alter table customer_contract  ADD column mps_back_remark varchar(100) comment '产业工人拒绝接单备注';
alter table customer_contract  ADD column province_code VARCHAR(10) comment '省份标识';
alter table customer_contract  ADD column city_code VARCHAR(10) comment '市标识';
alter table customer_contract  ADD column area_code VARCHAR(10) comment '区标识';
<!-- 客户表增加字段 -->
alter table customer ADD column customer_level varchar(10) comment '客户级别';
alter table customer ADD column customer_tag varchar(50) comment '客户标签';
alter table customer ADD column income_source varchar(50) comment '进店来源';
alter table customer ADD column income_time date comment '进店日期';
<!-- 删除编号规则表门店字段 -->
alter table sm_numbering_rule drop column store_code;
alter table customer_contract  ADD column start_construction_time datetime comment '开工时间';
alter table customer_contract  ADD column complete_construction_time datetime comment '竣工时间';
alter table sm_construction_change MODIFY COLUMN  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ;
alter table sm_construction_change_detail MODIFY COLUMN  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' ;
alter table sm_construction_change_detail  ADD column change_type tinyint(1) comment '变更类型 1 增 2 减';

--日期时间 2017/12/11
--修改人：Paul
--描  述：
-- 1:sm_construction_change 新增 打印次数
--执行状态 未执行
alter table sm_construction_change  ADD column print_count int(5) DEFAULT 0 comment '打印次数' AFTER change_list_total_price;
--日期时间 2017/12/11
--修改人：刘铎
--描  述：
-- 1:sm_change_detail 新增
--执行状态 未执行
alter table sm_change_detail  ADD column print_count int(5) DEFAULT 0 comment '打印次数' ;
alter table fina_projectchange_money MODIFY COLUMN  change_status VARCHAR(20)  NULL DEFAULT 'VALID' COMMENT '变更款状态' ;
alter table sm_construction_change MODIFY COLUMN  add_project_total_price decimal NULL DEFAULT 0 COMMENT '增项金额合计' ;
alter table sm_construction_change MODIFY COLUMN  cut_project_total_price decimal NULL DEFAULT 0 COMMENT '减项金额合计' ;
alter table sm_construction_change MODIFY COLUMN  change_list_total_price decimal NULL DEFAULT 0 COMMENT '变更金额合计' ;
alter table sm_construction_change_detail MODIFY COLUMN  amount decimal(18,2) NULL DEFAULT 0 COMMENT '数量' ;
alter table sm_construction_change_detail MODIFY COLUMN  loss decimal NULL DEFAULT 0 COMMENT '损耗' ;
alter table sm_construction_change_detail MODIFY COLUMN  labor_costs decimal NULL DEFAULT 0 COMMENT '人工费' ;
alter table sm_construction_change_detail MODIFY COLUMN  total_unit_price decimal NULL DEFAULT 0 COMMENT '综合单价' ;
alter table sm_construction_change_detail MODIFY COLUMN  unit_project_total_price decimal NULL DEFAULT 0 COMMENT '总价' ;
alter table sm_order_bespeak_contract ADD COLUMN  remark varchar(500)  COMMENT '备注' ;
alter table fina_projectchange_money ADD COLUMN  change_type varchar(20)  COMMENT '变更类型 material主材 basic 基装' ;
alter table sm_operate_log modify column operate_description text comment '操作描述';
--日期时间 2018/01/05
--修改人：巢帅
--描  述：
-- 1:customer_contract 新增
--执行状态 未执行
alter table customer_contract  ADD column return_reason VARCHAR(50) comment '退回原因';
alter table customer_contract  ADD column return_reason_describe VARCHAR(200) comment '退回原因描述';
alter table customer_contract  ADD column single_tag_id int comment '串单id';
--日期时间 2018/01/10
--修改人：Paul
--描  述：新建预备单/item表及异常表
--执行状态 未执行
DROP TABLE IF EXISTS `indent_prepare_order`;
CREATE TABLE `indent_prepare_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contract_code` varchar(30) NOT NULL COMMENT '项目编码',
  `data_source` varchar(30) NOT NULL COMMENT '数据来源，（选材、变更）',
  `brand_id` int(11) DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(20) DEFAULT NULL COMMENT '品牌名称',
  `status` varchar(30) DEFAULT NULL COMMENT '预备单状态:待转单、已转单、已作废',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_account` varchar(50) DEFAULT NULL COMMENT '更新人',
  `switch_time` datetime DEFAULT NULL COMMENT '转单时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='订货预备单';

DROP TABLE IF EXISTS `indent_prepare_order_item`;
CREATE TABLE `indent_prepare_order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prepare_order_id` int(11) NOT NULL COMMENT '预备单id',
  `sku_id` int(11) NOT NULL COMMENT 'skuid',
  `sku_name` varchar(100) NOT NULL COMMENT 'sku名称',
  `model` varchar(120) DEFAULT NULL COMMENT '型号',
  `spec` varchar(100) DEFAULT NULL COMMENT '规格',
  `attribute1` varchar(1000) DEFAULT NULL COMMENT '属性值1',
  `attribute2` varchar(1000) DEFAULT NULL COMMENT '属性值2',
  `attribute3` varchar(1000) DEFAULT NULL COMMENT '属性值3',
  `supply_price` decimal(9,2) DEFAULT NULL COMMENT '供货商的供货价',
  `quantity` decimal(9,2) NOT NULL COMMENT '订货数量',
  `installation_location` varchar(50) DEFAULT NULL COMMENT '安装位置',
  `spec_unit` varchar(20) DEFAULT NULL COMMENT '商品单位',
  `tablet_num` int(11) DEFAULT NULL COMMENT '片数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COMMENT='订货预备单sku';

DROP TABLE IF EXISTS `sm_prepare_order_abnormal_log`;
CREATE TABLE `sm_prepare_order_abnormal_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `contract_code` varchar(50) DEFAULT NULL COMMENT '项目编码',
  `data_source` varchar(30) NOT NULL COMMENT '数据来源，（选材、变更）',
  `deal_status` char(1) DEFAULT '0' COMMENT '是否处理',
  `deal_user` varchar(32) DEFAULT NULL COMMENT '操作人',
  `deal_time` datetime DEFAULT NULL COMMENT '操作时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `abnormal_content` varchar(4000) DEFAULT NULL COMMENT '异常日志内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='选材数据转换预备订单异常日志表';