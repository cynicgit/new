package ip.cynic.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * @author cynic
 *
 * 2015-12-2
 */
public class LocationService extends Service{

    private LocationManager lm;
    private SharedPreferences mPref;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);//允许付费(使用流量)
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 设置为最大精度
        String bestProvider = lm.getBestProvider(criteria, true);//获取最佳的位置提供者
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        lm.requestLocationUpdates(bestProvider, 0, 0, new LocationListener(){

            //位置变化
            @Override
            public void onLocationChanged(Location location) {
                String longitude = "经度"+location.getLongitude();//经度
                String latitude = "纬度"+location.getLatitude();//纬度
                String accuracy = "精确度"+location.getAccuracy();//纬度
                mPref.edit().putString("locationX", longitude).putString("locationY", latitude).commit();
            }

            //状态变化（信号）
            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {

            }
            //GPS开启
            @Override
            public void onProviderEnabled(String provider) {

            }
            //GPS关闭
            @Override
            public void onProviderDisabled(String provider) {

            }

        });
    }


}
