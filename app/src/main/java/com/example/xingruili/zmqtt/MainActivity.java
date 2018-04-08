package com.example.xingruili.zmqtt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.xingruili.zmqtt.Util.ServiceUtil;
import com.example.xingruili.zmqtt.Util.ToastUtil;
import com.example.xingruili.zmqtt.service.MQTTMessage;
import com.example.xingruili.zmqtt.service.MQTTService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

/**
 * Created by XingruiLi on 2018/3/20.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //EventBus注册
        EventBus.getDefault().register(this);
        //moitor the button
        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.connect).setOnClickListener(this);
        findViewById(R.id.disconnect).setOnClickListener(this);
        findViewById(R.id.send).setOnClickListener(this);
    }
    //设置button监听选择
    @Override
    public void onClick(View v) {
        boolean isRunning = isServiceRunning();
        switch (v.getId()){
            case R.id.start:
                if (isRunning){
                    ToastUtil.showToast(this, "MQTT Service started");
                } else {
                    ToastUtil.showToast(this, "MQTT Service starting");
                    startService(new Intent(this, MQTTService.class));
                }
                break;

            case R.id.stop:
                if (!isRunning){
                    ToastUtil.showToast(this, "MQTT Service stopped");
                } else {
                    ToastUtil.showToast(this, "MQTT Service stopping");
                    stopService(new Intent(this, MQTTService.class));
                }
                break;

            case R.id.connect:
                if (!isRunning){
                    ToastUtil.showToast(this, "please start MQTT Service");
                    break;
                }

                if (MQTTService.isConnect()){
                    ToastUtil.showToast(this, "MQTT Service connected");
                    break;
                }
                ToastUtil.showToast(this, "MQTT Service connecting");
                MQTTService.connect();
                break;

            case R.id.disconnect:
                if (!isRunning){
                    ToastUtil.showToast(this, "please start MQTT Service");
                    break;
                }

                if (!MQTTService.isConnect()){
                    ToastUtil.showToast(this, "MQTT Service disconnected");
                    break;
                }
                ToastUtil.showToast(this, "MQTT Service disconnecting");
                MQTTService.disconnect();
                break;

            case R.id.send:
                if (!isRunning){
                    ToastUtil.showToast(this, "please start MQTT Service");
                    break;
                }

                if (!MQTTService.isConnect()){
                    ToastUtil.showToast(this, "please connect");
                    break;
                }

                MQTTService.publish("测试Z");
                break;
        }
    }

    private boolean isServiceRunning() {
        return ServiceUtil.isServiceWork(this, MQTTService.class.getName());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMqttMessage(MQTTMessage mqttMessage){
        Log.i(MQTTService.TAG,"get message:"+mqttMessage.getMessage());
        Toast.makeText(this,mqttMessage.getMessage(),Toast.LENGTH_SHORT).show();
    }

    //解除EventBus的注册
    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

   /* //服务器套接头

    JSONObject data = new JSONObject();
    //data.put("Attribute_M","Value_M");
    //data.put("Attribute_N","Value_N");
    data.put("属性u","值u");
    data.put("属性n","值n");
    MqttPacketModel xx = MqttMsgHeader.MqttMsgHeaderMake("Topic/MachineNum",data.toJSONString().getBytes(),false);
    System.out.println(new String(xx.mPayload));
    String datay = new String(xx.mPayload);
    MqttPacketModel yy = MqttMsgHeader.MqttMsgHeaderPasre("Topic/MachineNum",datay.getBytes());
    System.out.println(new String(yy.mPayload));*/

}
