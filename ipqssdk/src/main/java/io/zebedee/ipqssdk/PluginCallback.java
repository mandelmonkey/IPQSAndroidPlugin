package io.zebedee.ipqssdk;

public interface PluginCallback {
    public void onSuccess(String result);
    public void onError(String errorMessage);
}
