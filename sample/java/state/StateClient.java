import java.util.Random;

/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO ��һ�仰�������ļ���ʲô
 */

/**
 * @author niuyn
 *
 */
public class StateClient {
	public static void main(String[] args) {
		Context context = new Context();
		// ���ó�ʼ״̬Ϊֹͣ״̬
		context.setCurrstate(new StopState());

		Random rand = new Random();
        while(true){
    		switch (rand.nextInt(4)) {
    		case 0:
    			context.motor_stop();
    			break;
    		case 1:
    			context.motor_run();
    			break;
    		case 2:
    			context.motor_reset();
    			break;
    		default:
    			context.motor_error();

    		}
    		
    		try {
				Thread.sleep(3*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	
        }

	}
}
