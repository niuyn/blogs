/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO 用一句话描述该文件做什么
 */

/**
 * @author niuyn
 *
 */
public abstract class MotorState {
	//定义一个环境角色，也就是封装状态变化引起的功能变化
	protected Context context;
	public void setContext(Context _context){
		this.context=_context;
	}
	public abstract void motor_run();
	public abstract void motor_stop();
	public abstract void motor_none();
	public abstract void motor_reset();
	public abstract void motor_error();

}
