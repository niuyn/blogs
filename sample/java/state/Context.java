/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO ��һ�仰�������ļ���ʲô
 */

/**
 * @author niuyn
 *
 */
public class Context {
	//��������еĵ���״̬
	public final static MotorState motor_stop = new StopState();
	public final static MotorState motor_run = new RunState();
	public final static MotorState motor_error = new ErrorState();
	private MotorState currstate;
	public MotorState getCurrstate() {
		return currstate;
	}
	public void setCurrstate(MotorState currstate) {
		this.currstate = currstate;
		this.currstate.setContext(this);
	}
	//��Ϊ����
	public void motor_run() {
		// TODO Auto-generated method stub
		this.currstate.motor_run();

	}

	public void motor_stop() {
		// TODO Auto-generated method stub

	}

	public void motor_none() {
		// TODO Auto-generated method stub

	}

	public void motor_reset() {
		// TODO Auto-generated method stub

	}


	
	public void motor_error() {
		// TODO Auto-generated method stub

	}
	
}
