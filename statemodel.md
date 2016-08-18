# <center> 状态模式初体验
　　　在最近的项目中看到了大量的switch case，而且被认为状态模式，特此查了些资料，并记录下来，领导说的有总结才有提高还是要相信的。

先看一下状态模式的定义 Allow an object to alter its behavior when its internal state changes. The object will appear to change to change its class (当一个对象内在状态改变时充许改变其行为，这个对象看起来像改变了其类)，在例出相关例子之前看看其使用场景，状态模式适用于当某个对象在它的状态发生改变时，它的行为也随着发生较大的变化，也就是说在行为状态约束的情况下可以使用状态模式，而且使用时对象的状态最好不超过5个。

下面我们来例证，我只想到我最初工作时的一个例子，是关于电机运行的，当时还不知道状态模式这个词，用C实现的，没有封装，在这里在适合不过了，现在我将其简化用golang重写一遍。场景如下

电机运行简化为3个状态，4个事件
###### 事件

* 电机停止
* 电机运行
* 电机重置
* 电机出错
##### 状态

* 电机停止状态
* 电机运行状态
* 电机错误状态

电机在各个状态碰到各个事件，会切换到不同状态和产生不同的形为，状态切换如下图


|   |停止|运行|错误
|---|----|----|----|
停止|停止|停止|错误|
运行|运行|运行|错误|
出错|错误|错误|错误
重置|停止|错误|停止

产生的形为如下图 空出单元格表示do nothing

|   |停止|运行|错误
|---|----|----|----|
停止|    |停止|    |
运行|运行|    |    |
出错|出错|出错|
重置|重置|出错|重置

按面向过程的思想来实现我们可以用两个二维表来表示上述状态切换

```
    const (
        MOTOR_MODE_STOP   = 0
        MOTOR_MODE_RUN    = 1
        MOTOR_MODE_ERROR  = 2
        MOTOR_EVENT_RESET = 3
        MOTOR_EVENT_STOP  = 0
        MOTOR_EVENT_RUN   = 1
        MOTOR_EVENT_ERROR = 2
        MOTOR_SIZE_EVENT  = 4
        MOTOR_SIZE_MODE   = 3
    )
    StateTable := [MOTOR_SIZE_EVENT][MOTOR_SIZE_MODE]int{
        /*state                0:MOTOR_MODE_STOP, 1:MOTOR_MODE_RUN,  2:MOTOR_MODE_ERROR*/
        /*event*/
        /*0 MOTOR_EVENT_STOP */ {MOTOR_MODE_STOP, MOTOR_MODE_STOP, MOTOR_MODE_ERROR},
        /*1 MOTOR_EVENT_RUN  */ {MOTOR_MODE_RUN, MOTOR_MODE_RUN, MOTOR_MODE_ERROR},
        /*2 MOTOR_EVENT_ERROR*/ {MOTOR_MODE_ERROR, MOTOR_MODE_ERROR, MOTOR_MODE_ERROR},
        /*3 MOTOR_EVENT_RESET*/ {MOTOR_MODE_STOP, MOTOR_MODE_ERROR, MOTOR_MODE_STOP}}

    EventTable := [MOTOR_SIZE_EVENT][MOTOR_SIZE_MODE]StateChangeFunc{
        /*state                0:MOTOR_MODE_STOP, 1:MOTOR_MODE_RUN,  2:MOTOR_MODE_ERROR*/
        /*event*/
        /*0 MOTOR_EVENT_STOP */ {motor_none, motor_stop, motor_none},
        /*1 MOTOR_EVENT_RUN  */ {motor_run, motor_none, motor_none},
        /*2 MOTOR_EVENT_ERROR*/ {motor_error, motor_error, motor_none},
        /*3 MOTOR_EVENT_RESET*/ {motor_reset, motor_error, motor_reset}}
```

不同状态的形为定义如下

```
func motor_run(modeCode int) int {
    fmt.Println("motor runs")
    fmt.Println("motor change into " + getMode(modeCode))
    return modeCode
}
func motor_stop(modeCode int) int {
    fmt.Println("motor stop")
    fmt.Println("motor change into " + getMode(modeCode))
    return modeCode
}
func motor_error(modeCode int) int {
    fmt.Println("motor error")
    fmt.Println("motor change into " + getMode(modeCode))
    return modeCode
}
func motor_none(modeCode int) int {
    fmt.Println("motor do nothing")
    fmt.Println("motor change into " + getMode(modeCode))
    return modeCode
}
func motor_reset(modeCode int) int {
    fmt.Println("moter reset")
    fmt.Println("motor change into " + getMode(modeCode))
    return modeCode
}
func getMode(modeCode int) string {
    switch modeCode {
    case MOTOR_MODE_STOP:
        return "stop mode"
    case MOTOR_MODE_RUN:
        return "run mode"
    case MOTOR_MODE_ERROR:
        return "error mode"
    }
    return ""
}
```
然后就是模拟下状态切换的过程，电机初始状态为停止，然后碰到随机产生的事件发生状态切换，随机我们用golang 的select 来实现代码如下：

```
    //当前状态
    currMode := MOTOR_MODE_STOP
    eventSwith := make(chan int, 1)
    for {
        //产生事件
        select {
        case eventSwith <- MOTOR_EVENT_STOP:
        case eventSwith <- MOTOR_EVENT_RUN:
        case eventSwith <- MOTOR_EVENT_ERROR:
        case eventSwith <- MOTOR_EVENT_RESET:
        }
        event := <-eventSwith
        //引发动作和状态切换
        fmt.Println("motor is in " + getMode(currMode))
        currMode = EventTable[event][currMode](StateTable[event][currMode])
        time.Sleep(5 * time.Second)
    }
```
运行一下结果如下：

![image](https://github.com/niuyn/blogs/blob/master/sample/results/go_state_ret.png)

这样写个人感觉还不错，但是现在java当道，各种封装继承，还要扩展，扩展方面上面的方法就不行了，总不能每次都更新一次二维表吧，下面再用java改进一下

状态模式中有3个角色

State--抽象状态角色

接口或者抽象类，负责对象状态定义，并且封装环境角色（用来实现状态切换）在本例也就是电机的三个状态和三个事件代码如下：

```
public abstract class MotorState {
    //定义一个环境角色，也就是封装状态变化引起的功能变化
    protected Context context;
    public void setContext(Context _context){
        this.context=_context;
    }
    //电机运行事件
    public abstract void motor_run();
    //电机停止事件
    public abstract void motor_stop();
    //电机重置事件
    public abstract void motor_reset();
    //电机出错事件
    public abstract void motor_error();

}
```

Context 类就是环境角色下面会有说明 

ConcreteState -- 具体状态角色

也就是上面的抽象状态角色state的实现类，每个状态必须完成两个功能：本状态下能做的事情，和本状态和其他状态的功能，功能和开头介绍的两个二维表的功能类似，上代码：

```
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
```


最后一个Context -- 环境角色
定义客户端需要的接口，并且负责状态的具体切换。这个类有点像代理模式中的类。该角色书中说有两个不成文的约束

* 把状态对象声明为静态常量，有几个状态就声明几个常量，本例有三个
* 环境角色有抽象状态角色定义的所有形为，具体执行使用委托方式，本例有四个、

代码如下：

```
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
```

状态模式的通用类图如下：

![image](https://github.com/niuyn/blogs/blob/master/sample/results/stateUML.png)

现在写个Main方法测试一下我们写的类：

```
public class StateClient {
    public static void main(String[] args) {
        Context context = new Context();
        // 设置初始状态为停止状态
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
```

执行结果：

```
电机停止
此时电机处于stop状态
电机遇到重置事件
电机停止
此时电机处于stop状态
电机遇到重置事件
电机停止
此时电机处于stop状态
电机遇到重置事件
电机停止
电机停止
此时电机处于stop状态
电机遇到出错事件
电机出错
电机出错
电机出错
电机现处于错误状态下
此时电机不能正常运行
电机现处于错误状态下
此时电机不能正常运行
电机现处于错误状态下
此时电机不能正常停止
电机出错
电机现处于错误状态下
此时电机不能正常停止
....

```



至此，我们用两种方式模拟了简化的电机运行过程，不过还是感觉第一种直观啊，第二种好处显而易见，不过反正也没用过，写文留点痕迹。