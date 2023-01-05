package fri.werock.api;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class WeRockApiError {
    public static final int UNKNOWN               =  0;
    //Local
    public static final int BODY_MISSING          = -1;
    public static final int FAILED_PARSING_JSON   = -2;
    //Authenticate
    public static final int AUTH_MISSING_HEADER   = 10;
    public static final int AUTH_MISSING_TOKEN    = 11;
    public static final int AUTH_UNVERIFIED_TOKEN = 12;
    //Register
    public static final int REG_MISSING           = 20;
    public static final int REG_USER_EXISTS       = 21;
    public static final int REG_HASH_FAILED       = 22;
    //Login
    public static final int LOGIN_MISSING         = 30;
    public static final int LOGIN_WRONG           = 31;

    public static final WeRockApiError UNKNOWN_ERROR = new WeRockApiError(
            WeRockApiError.UNKNOWN, 0, "Unknown local error"
    );
    public static final WeRockApiError BODY_MISSING_ERROR = new WeRockApiError(
            WeRockApiError.BODY_MISSING, 0, "Response error body missing"
    );
    public static final WeRockApiError FAILED_PARSING_JSON_ERROR = new WeRockApiError(
            WeRockApiError.FAILED_PARSING_JSON, 0, "Failed to parse response error body json"
    );

    private int id;
    private int code;
    private String message;

    public WeRockApiError(int id, int code, String message) {
        this.id = id;
        this.code = code;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static WeRockApiError fromJson(String json){
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, WeRockApiError.class);
        } catch (JsonSyntaxException exception) {
            return null;
        }
    }
}
