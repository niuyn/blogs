/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO 用一句话描述该文件做什么
 */

/**
 * @author niuyn
 *
 */
public class RunState extends MotorState {

    //这里是RunState时要调用 的方法
	@Override
	public void motor_run() {
		System.out.println("电机运行");
	}

    //这里发生状态切换
	@Override
	public void motor_stop() {
		getState();
		System.out.println("电机遇到停止事件");
		super.context.setCurrstate(Context.motor_stop);
		super.context.getCurrstate().motor_stop();
		
	}
	@Override
	public void motor_reset() {
		getState();
		System.out.println("电机遇到重置事件");
		super.context.setCurrstate(Context.motor_error);
		super.context.getCurrstate().motor_error();
	}

	@Override
	public void motor_error() {
		getState();
		System.out.println("电机遇到出错事件");
		super.context.setCurrstate(Context.motor_error);
		super.context.getCurrstate().motor_error();

	}
	public void getState(){
		System.out.println("此时电机处于run状态");
	}

}
