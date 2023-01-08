package fri.werock.api;

import java.io.IOException;

public interface WeRockApiCallback<T> {
    void onResponse(T t) throws IOException;
    void onError(WeRockApiError error);
    void onFailure();
}
