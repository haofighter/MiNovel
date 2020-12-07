package com.hao.plug;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理测试
 */
public class ProxyTest {
    interface Option {
        void doIn();

        void doOut();
    }
    Option u = new User();

    class User implements Option {
        public void doIn() {
            Log.i("日志", "进入");
        }

        public void doOut() {
            Log.i("日志", "出来");
        }
    }

    public void testProxy(Context context) {
        Option proxyInstance = (Option) Proxy.newProxyInstance(context.getClassLoader(), new Class[]{Option.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.i("日志", "代理执行前");
                method.invoke(u, args);
                Log.i("日志", "代理执行后");
                return null;
            }
        });

        proxyInstance.doIn();
        proxyInstance.doOut();
    }
}
