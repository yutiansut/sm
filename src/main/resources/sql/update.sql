
--日期时间 2017/11/2
--修改人：Ryze
--描  述： prod_sku_price 价格类型枚举值 添加
--执行状态 未执行
ALTER TABLE prod_sku_price MODIFY COLUMN price_type enum (
	'SUPPLY',
	'STORE',
	'SALE',
	'UPGRADE',
	'INCREASED',
	'MINUS'
) COMMENT 'SUPPLY("网真采购价"), STORE("门店采购价"), SALE("门店销售价"),UPGRADE("升级项价"),INCREASED("增项"),MINUS("减项")';

alter table  sm_material_change_audit_record add COLUMN  audit_user_type varchar(50) DEFAULT null COMMENT '审核人类型' ;

ALTER TABLE `prod_sku` ADD INDEX idx_prod_sku_product_id ( `product_id` ) ;
ALTER TABLE `prod_catalog` ADD INDEX idx_prod_catalog_url ( `url` ) ;
ALTER TABLE `prod_product_image` ADD INDEX idx_prod_product_image_sku_id ( `sku_id` ) ;

ALTER TABLE sm_project_material ADD COLUMN brand_id int (11) DEFAULT NULL COMMENT '品牌Id';

ALTER TABLE sm_project_change_material ADD COLUMN brand_id int (11) DEFAULT NULL COMMENT '品牌Id';