/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO 用一句话描述该文件做什么
 */

/**
 * @author niuyn
 *
 */
public class StopState extends MotorState {

	@Override
	public void motor_run() {
		getState();
		System.out.println("电机遇到运行事件");
		super.context.setCurrstate(Context.motor_run);
		super.context.getCurrstate().motor_run();

	}

	@Override
	public void motor_stop() {
		System.out.println("电机停止");

	}

	@Override
	public void motor_reset() {
		getState();
		System.out.println("电机遇到重置事件");
		super.context.setCurrstate(Context.motor_stop);
		super.context.getCurrstate().motor_stop();

	}

	@Override
	public void motor_error() {
		getState();
		System.out.println("电机遇到出错事件");
		super.context.setCurrstate(Context.motor_error);
		super.context.getCurrstate().motor_error();
	}

	public void getState() {
		System.out.println("此时电机处于stop状态");
	}

}
