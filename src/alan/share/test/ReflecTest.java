package alan.share.test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import alan.share.user.model.TripUser;

public class ReflecTest {

	@Test
	public void getParams() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException{
		TripUser user=new TripUser();
		user.setUserid("sdsdsdsd");
		user.setSex("女");
		user.setUsername("小狐仙");
		Class userClass=(Class)user.getClass();
		
		Field[] fs=userClass.getDeclaredFields();
		for(int i=0;i<fs.length;i++){
			Field f=fs[i];
			f.setAccessible(true);//社会属性是可访问的
			Object val=f.get(user);//得到此属性的值
			System.out.println("name:"+f.getName()+"\t value="+val);
		}
		
		Method[] methods=userClass.getMethods();
		for(int i=0;i<methods.length;i++){
			Method method=methods[i];
			if(method.getName().startsWith("get")){
				System.out.println("methodName:"+method.getName()+"\t");
				System.out.println("value:"+method.invoke(user));
			}
			
		}

	}
	
	@Test
	public void test2() throws NoSuchMethodException, SecurityException{
		TripUser user=new TripUser();
		user.setHeaderimage("dedede");
		System.out.println(user.getClass().getMethod("headerimage"));
	}
}
