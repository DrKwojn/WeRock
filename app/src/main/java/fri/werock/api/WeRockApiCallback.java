package fri.werock.api;

public interface WeRockApiCallback<T> {
    void onResponse(T t);
    void onError(ApiError error);
    void onFailure();
}
