/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO ��һ�仰�������ļ���ʲô
 */

/**
 * @author niuyn
 *
 */
public class ErrorState extends MotorState {
	@Override
	public void motor_run() {
		getState();
		System.out.println("��ʱ���������������");
	}
	@Override
	public void motor_stop() {
		getState();
		System.out.println("��ʱ�����������ֹͣ");
	}
    //�������״̬�л�
	@Override
	public void motor_reset() {
		getState();
		System.out.println("��ʱ�������");
		super.context.setCurrstate(Context.motor_stop);
		super.context.getCurrstate().motor_stop();
	}
	
   //����������ʱҪ���õķ��� 
	@Override
	public void motor_error() {
		System.out.println("�������");

	}
	
	public void getState(){
		System.out.println("����ִ��ڴ���״̬��");
	}

}
