package fri.werock.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ApiError {
    private int code;
    private String message;

    public ApiError() {
        this.code = 0;
        this.message = "Unknown error";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static ApiError fromJson(String json){
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, ApiError.class);
        } catch (JsonSyntaxException exception) {
            return null;
        }
    }
}
