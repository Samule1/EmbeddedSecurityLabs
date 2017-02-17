
import java.math.BigInteger;

public class Main {


	public static void main(String[] args) {
        
        BigInteger check_key = new BigInteger("0000000000000000");
        Secret s = new Secret();
        boolean loop = true;
        long timeDelay = 9;

        BigInteger increment_key = new BigInteger("1000000000000000");
        

        while(loop){
            check_key = check_key.add(increment_key);
            long t0 = System.currentTimeMillis();
            try{
                s.verifyPassword(check_key.toString());
            }catch(IllegalArgumentException e){
                System.out.println(e);
            }
            long t1 = System.currentTimeMillis();
            if(t1-t0 > timeDelay){
                timeDelay = timeDelay + 10;
                System.out.println(t1-t0);
                System.out.println("Current key for test : " + check_key.toString());
                increment_key = increment_key.divide(new BigInteger("10"));
            }

            if(s.getSecret() != null){
                System.out.println("Secret is : " + s.getSecret());
                loop = false;
            }
        }
	}

}
