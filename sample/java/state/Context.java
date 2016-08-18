/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO 用一句话描述该文件做什么
 */

/**
 * @author niuyn
 *
 */
public class Context {
	//定义出所有的电梯状态
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
	//行为代理
	public void motor_run() {
		this.currstate.motor_run();

	}
	public void motor_stop() {
		this.currstate.motor_stop();
	}

	public void motor_reset() {
		this.currstate.motor_reset();
	}
	
	public void motor_error() {
		this.currstate.motor_error();
	}
	
}
