package com.framework.base.exception;

/**
 * SysCodeEnum
 */
public enum SysCodeEnum {

    SUCCESS("200", "成功"),

    SYS_ERROR("99999", "系统异常,请联系管理员!"),

    FUNCTIONAL_MAINTENANCE("88888", "此功能维护中"),
    REPEAT_REQUEST("7001", "重复请求,请稍后再试"),
    RECURSION_ERROR("7002", "递归垃圾数据,请检查"),

    RESULT_PARAM_ERROR("10001", "参数校验不通过"),
    DATA_EXIST_ERROR("10002", "数据已存在"),
    DATA_NOT_EXIST_ERROR("10003", "数据不存在"),
    SRC_DATA_EXIST_ERROR("10004", "上级数据不存在"),
    LEVEL_ERROR("10005", "上下级数据错误"),
    PARAMETER_ERROR("10006", "参数未配置"),

    ;
    private String code;

    private String desc;

    SysCodeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getByCode(String code) {
        for (SysCodeEnum item : SysCodeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item.getDesc();
            }
        }
        return null;
    }

}
