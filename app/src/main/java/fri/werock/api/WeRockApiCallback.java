package fri.werock.api;

public interface WeRockApiCallback<T> {
    void onResponse(T t);
    void onError(WeRockApiError error);
    void onFailure();
}
