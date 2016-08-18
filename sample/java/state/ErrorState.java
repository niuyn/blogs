/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO 用一句话描述该文件做什么
 */

/**
 * @author niuyn
 *
 */
public class ErrorState extends MotorState {
	@Override
	public void motor_run() {
		getState();
		System.out.println("此时电机不能正常运行");
	}
	@Override
	public void motor_stop() {
		getState();
		System.out.println("此时电机不能正常停止");
	}
    //这里产生状态切换
	@Override
	public void motor_reset() {
		getState();
		System.out.println("此时电机重置");
		super.context.setCurrstate(Context.motor_stop);
		super.context.getCurrstate().motor_stop();
	}
	
   //这里电机出错时要调用的方法 
	@Override
	public void motor_error() {
		System.out.println("电机出错");

	}
	
	public void getState(){
		System.out.println("电机现处于错误状态下");
	}

}
