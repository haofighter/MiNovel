package com.hao.plug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button load_date;
    private Button load_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.text);
        load_date = findViewById(R.id.load_date);
        load_test = findViewById(R.id.load_test);
        initView();
        new ProxyTest().testProxy(this);
        proxyStartActtivity();
        startActivity(new Intent(this, SecondActivity.class));
    }

    private void proxyStartActtivity() {
/**
 * Activity的跳转
 * startActivity
 * ->startActivityForResult
 * ->mInstrumentation.execStartActivity
 * ->ActivityTaskManager.getService().startActivity
 *
 * ActivityTaskManager.getService()是一个实现了IActivityTaskManager的静态对象
 * 可以使用动态代理及反射 将这个对象替换为代理对象
 */
        try {
            Class ActivityTaskManager = Class.forName("android.app.ActivityTaskManager");
            Field IActivityTaskManagerSingleton = ActivityTaskManager.getField("IActivityTaskManagerSingleton");
            IActivityTaskManagerSingleton.setAccessible(true);
            //获取到系统中的单例对象
            Object singleton = IActivityTaskManagerSingleton.get(null);

            //从单例中获取到IActivityTaskManager对象
            Class Singleton = Class.forName("android.util.Singleton");
            Field IActivityTaskManager = Singleton.getDeclaredField("mInstance");
            IActivityTaskManager.setAccessible(true);
            Object iActivityTaskManagerInstance = IActivityTaskManager.get(singleton);

            Object iActivityTaskManagerInstanceProxy = Proxy.newProxyInstance(getClassLoader(), new Class[]{Class.forName("android.app.IActivityTaskManager")}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            Log.i("日志", "代理对象替换成功");
                            return method.invoke(iActivityTaskManagerInstance, args);
                        }
                    }
            );

            IActivityTaskManagerSingleton.set(singleton, iActivityTaskManagerInstanceProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initView() {
        load_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DexClassLoader dexClassLoader=new DexClassLoader("","","",getClassLoader());

                //1.反射获取到系统中的BaseDexClassLoader
                try {
                    Class appuserInfo = Class.forName("com.hao.minovel.moudle.entity.AppUseInfo");
                    Method method = appuserInfo.getMethod("toString");
                    //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的
                    method.setAccessible(true);//值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查

                    method.invoke(appuserInfo.newInstance());


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        load_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DexClassLoader dexClassLoader=new DexClassLoader("","","",getClassLoader());

                try {
                    Class<?> clazz = Class.forName("com.enjoy.plugin.Test");
                    Method print = clazz.getMethod("print");
                    print.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }
}