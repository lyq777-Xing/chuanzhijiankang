package net.zjitc.common;

public enum ResponseCode {
    SUCCESS(200,"请求成功"),
    CREATED(201,"创建成功"),
    DELETED(204,"删除成功"),
    BAD_REQUEST(400,"请求的地址不存在或者包含不支持的参数"),
    UNAUTHORIZED(401,"未授权"),
    FORBIDDEN(403,"被禁止访问"),
    NOT_FOUND(404,"请求的资源不存在"),
    Unprocesable_entity(422,"[POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误"),
    INTERNAL_SERVER_ERROR(500,"内部错误");
    private Integer code;
    private String message;

    ResponseCode() {
    }

    ResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
