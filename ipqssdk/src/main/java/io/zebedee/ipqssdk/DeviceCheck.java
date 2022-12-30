package io.zebedee.ipqssdk;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ipqualityscore.FraudEngine.IPQualityScore;
import com.ipqualityscore.FraudEngine.Interfaces.OnMobileTrackerResult;
import com.ipqualityscore.FraudEngine.MobileTracker;
import com.ipqualityscore.FraudEngine.Results.MobileTrackerResult;
import com.ipqualityscore.FraudEngine.Utilities.IPQualityScoreException;

public class DeviceCheck {
    public int Test(int i, int j) {
        return i+j;
    }

    public void Init(Activity currentActivity, String apiKey, io.zebedee.ipqssdk.PluginCallback callback){
        IPQualityScore api = IPQualityScore.getInstance();
        try {
            api.setAppKey(apiKey);
        } catch (IPQualityScoreException e) {
            e.printStackTrace();
        }
        api.setActivity(currentActivity);

        try {
            /*
             * Set custom variables if you have them before calling performFraudCheck()
             * MobileTracker.addCustomVariable("userID", "1234");
             */
            MobileTracker.performFraudCheck(new OnMobileTrackerResult(){
                @Override
                public void onResult(MobileTrackerResult result){
                    // Display a result via a toast.
                    if(result.getSuccess()) {
                        try {
                            DeviceInfo dI = new DeviceInfo();

                            if (result.getDeviceSuspicious() == null) {
                                dI.isSuspicious = false;
                            } else {
                                dI.isSuspicious = result.getDeviceSuspicious();
                            }

                            if (result.getDeviceEmulated() == null) {
                                dI.isEmulator = false;
                            } else {
                                dI.isEmulator = result.getDeviceEmulated();
                            }

                            dI.deviceId = result.getDeviceID();
                            dI.fraudScore = result.getFraudScore();
                            Gson gson = new Gson();
                            String res = gson.toJson(dI).toString();
                            Log.d("IPQSS", res);
                            callback.onSuccess(res);
                        }catch (Exception e){
                            Log.e("IPQSF", e.toString());
                            callback.onError(e.getMessage());

                        }
                    }else{
                        callback.onError("error");
                    }
                }
            });
        } catch(IPQualityScoreException e){
            // Display the error via toast.
            callback.onError(e.getMessage());

        }
    }
}
