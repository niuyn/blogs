/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO ��һ�仰�������ļ���ʲô
 */

/**
 * @author niuyn
 *
 */
public class RunState extends MotorState {

    //������RunStateʱҪ���� �ķ���
	@Override
	public void motor_run() {
		System.out.println("�������");
	}

    //���﷢��״̬�л�
	@Override
	public void motor_stop() {
		getState();
		System.out.println("�������ֹͣ�¼�");
		super.context.setCurrstate(Context.motor_stop);
		super.context.getCurrstate().motor_stop();
		
	}
	@Override
	public void motor_reset() {
		getState();
		System.out.println("������������¼�");
		super.context.setCurrstate(Context.motor_error);
		super.context.getCurrstate().motor_error();
	}

	@Override
	public void motor_error() {
		getState();
		System.out.println("������������¼�");
		super.context.setCurrstate(Context.motor_error);
		super.context.getCurrstate().motor_error();

	}
	public void getState(){
		System.out.println("��ʱ�������run״̬");
	}

}
