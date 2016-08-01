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