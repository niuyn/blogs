package main

import (
	"fmt"
	"time"
)

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

type StateChangeFunc func(modeCode int) int

func main() {

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
		//引发动作
		fmt.Println("motor is in " + getMode(currMode))
		currMode = EventTable[event][currMode](StateTable[event][currMode])
		time.Sleep(5 * time.Second)
	}

}
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
