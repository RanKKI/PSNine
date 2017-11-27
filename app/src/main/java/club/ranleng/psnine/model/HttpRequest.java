package club.ranleng.psnine.model;

public class HttpRequest {

    private String message;
    private int code;

    public HttpRequest(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
