import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface ProxyTest {
    public static void main(String[] args) {
        A a = new A();
        P b = (P)Proxy.newProxyInstance(a.getClass().getClassLoader(), a.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("aaa");
                        return new A();

                    }

                });
                b.doSomething();;
            System.out.println(b.getClass().getName());

    }

}

class A implements P {
    private int i;
    @Override 
    public void doSomething() {
        System.out.println("do something");
    }

}

interface P {
    void doSomething();
}

