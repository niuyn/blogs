/**
 * Copyright 2015-2016, QunShuo Electronics Co.,Ltd. All rights reserved
 * @Description: TODO ��һ�仰�������ļ���ʲô
 */

/**
 * @author niuyn
 *
 */
public abstract class MotorState {
	//����һ��������ɫ��Ҳ���Ƿ�װ״̬�仯����Ĺ��ܱ仯
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
