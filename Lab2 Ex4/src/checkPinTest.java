import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 Expression: A || B && C 
 access || (tryCounter > 0 && pin == securePin
  A  B   C   R
+---+---+---+---+
| T | T | T | T |
+---+---+---+---+
| T | T | F | T |
+---+---+---+---+
| T | F | T | T |
+---+---+---+---+
| T | F | F | T |
+---+---+---+---+
| F | T | T | T |
+---+---+---+---+
| F | T | F | F |
+---+---+---+---+
| F | F | T | F |
+---+---+---+---+
| F | F | F | F |
+---+---+---+---+ --> This was my proudest moment // Mac. 
 * */

public class checkPinTest {

	public PIN p0;
	public PIN p1;
	public PIN p2;
	public PIN p3;
	public int pin = 1337;


	//this is a bit naughty.
	Field accessField;
	Field tryCounterField;

	@Before
	public void setUp() throws Exception {
		p0 = new PIN(pin);
		p1 = new PIN(pin);
		p2 = new PIN(pin);
		p3 = new PIN(pin);

		accessField = p0.getClass().getDeclaredField("access");
		accessField.setAccessible(true);

		tryCounterField = p0.getClass().getDeclaredField("tryCounter");
		tryCounterField.setAccessible(true);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//Set access to true.
		p0.checkPin(pin);
		boolean access = (boolean) accessField.get(p0);
		assertEquals(true, access);

		//Make sure that  tryCounter > 0
		int tc = (int)tryCounterField.get(p0);
		assertEquals(true, (tc > 0));

		//Test with wrong pass. 
		p0.checkPin(7);
		access = (boolean) accessField.get(p0);
		assertEquals(true, access);
	}

	@Test
	public void test4() throws IllegalArgumentException, IllegalAccessException{
		//Check that access was initially false.
		boolean access = (boolean) accessField.get(p1);
		assertEquals(false, access);

		//Make sure that  tryCounter > 0
		int tc = (int)tryCounterField.get(p1);
		assertEquals(true, (tc > 0));

		//Login with correct pin and then check access. 
		p1.checkPin(pin);
		access = (boolean) accessField.get(p1);
		assertEquals(true, access);

	}
	@Test
	public void test5() throws IllegalArgumentException, IllegalAccessException{
		//Check that access was initially false.
		boolean access = (boolean) accessField.get(p2);
		assertEquals(false, access);

		//Make sure that  tryCounter > 0
		int tc = (int)tryCounterField.get(p2);
		assertEquals(true, (tc > 0));

		//Login with correct pin and then check access. 
		p1.checkPin(7);
		access = (boolean) accessField.get(p2);
		assertEquals(false, access);

	}

	@Test
	public void test6() throws IllegalArgumentException, IllegalAccessException{
		//Check that access was initially false.
		boolean access = (boolean) accessField.get(p3);
		assertEquals(false, access);
		
		//Check that try counter was initally 3.
		int tc = (int)tryCounterField.get(p3);
		assertEquals(true, (tc == 3));
		
		//Try with wrong pin n times
		for(int i = 0; i < 3; i++ ){
			p3.checkPin(000000000);
		}
		
		//Try to login with correct pin and check access. 
		p0.checkPin(pin);
		access = (boolean) accessField.get(p3);
		assertEquals(false, access);
		


	}

}
