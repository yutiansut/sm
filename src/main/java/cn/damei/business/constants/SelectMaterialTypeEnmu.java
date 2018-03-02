package cn.damei.business.constants;

public enum SelectMaterialTypeEnmu {

    //主分类
    PACKAGESTANDARD("套餐标配"), UPGRADEITEM("升级项"),
    ADDITEM("增项"), REDUCEITEM("减项"), OTHERMONEYADDORREDUCE("其他金额增减"),
    OTHERCOMPREHENSIVEFEE("其他综合费"),OLDHOUSEDEMOLITION("旧房拆改"),

    //子分类
    MAINMATERIAL("主材"), OTHERCATEGORIESOFSMALLFEES("其他综合费"),//子类
    BASEINSTALLQUOTA("基装定额"), BASEINSTALLCOMPREHENSIVEFEE("基装增项综合费"),
    DISMANTLEBASEINSTALLQUOTA("拆除基装定额"), DISMANTLEBASEINSTALLCOMPFEE("拆除基装增项综合费"),
    DISMANTLEOTHERCOMPFEE("拆除其他综合费");



    private String label;

    SelectMaterialTypeEnmu(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static SelectMaterialTypeEnmu getEnumFromString(String string){
        if(string != null){
            try{
                return Enum.valueOf(SelectMaterialTypeEnmu.class, string.trim());
            }
            catch(IllegalArgumentException ex){
            }
        }
        return null;
    }

}
