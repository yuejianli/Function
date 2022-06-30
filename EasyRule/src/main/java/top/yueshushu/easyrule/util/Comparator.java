package top.yueshushu.easyrule.util;

/**
 *
 */
public enum Comparator {
    /**
     *
     */
    equal("equal"),
    notEqual("notEqual"),
    greater("greater"),
    greaterOrEqual("greaterOrEqual"),
    less("less"),
    lessOrEqual("lessOrEqual"),
    contain("contain"),
    notContain("notContain"),
    isNull("isNull"),
    isNotNull("isNotNull");
    
    private String value;
    
    Comparator(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getOperator() {
        String reV = "";
        
        if (value.equals("equal")) {
            reV = "==";
        } else if (value.equals("notEqual")) {
            reV = "!=";
        } else if (value.equals("greater")) {
            reV = ">";
        } else if (value.equals("greaterOrEqual")) {
            reV = ">=";
        } else if (value.equals("less")) {
            reV = "<";
        } else if (value.equals("lessOrEqual")) {
            reV = "<=";
        }
        
        return reV;
    }
    
}
