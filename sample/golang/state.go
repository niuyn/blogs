package main

import (
	"fmt"
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

type StateChangeFunc func(modeCode int)

func main() {

	StateMap := [MOTOR_SIZE_EVENT][MOTOR_SIZE_MODE]int{
		/*state                0:MOTOR_MODE_STOP, 1:MOTOR_MODE_RUN,  2:MOTOR_MODE_ERROR*/
		/*event*/
		/*0 MOTOR_EVENT_STOP */ {MOTOR_MODE_STOP, MOTOR_MODE_STOP, MOTOR_MODE_ERROR},
		/*1 MOTOR_EVENT_RUN  */ {MOTOR_MODE_RUN, MOTOR_MODE_RUN, MOTOR_MODE_ERROR},
		/*2 MOTOR_EVENT_ERROR*/ {MOTOR_MODE_ERROR, MOTOR_MODE_ERROR, MOTOR_MODE_ERROR},
		/*3 MOTOR_EVENT_RESET*/ {MOTOR_MODE_STOP, MOTOR_MODE_ERROR, MOTOR_MODE_STOP}}

	EventMap := [MOTOR_SIZE_EVENT][MOTOR_SIZE_MODE]*StateChangeFunc{
		/*state                0:MOTOR_MODE_STOP, 1:MOTOR_MODE_RUN,  2:MOTOR_MODE_ERROR*/
		/*event*/
		/*0 MOTOR_EVENT_STOP */ {motor_none, motor_stop, motor_none},
		/*1 MOTOR_EVENT_RUN  */ {motor_run, motor_none, motor_none},
		/*2 MOTOR_EVENT_ERROR*/ {motor_error, motor_error, motor_none},
		/*3 MOTOR_EVENT_RESET*/ {motor_reset, motor_error, motor_reset}}

}
func motor_run(modeCode int) {
	fmt.Println("motor runs well")
}
func motor_stop(modeCode int) {
	fmt.Println("motor stop")
}
func motor_error(modeCode int) {
	fmt.Println("motor maybe has some problem")
}
func motor_none(modeCode int) {
	fmt.Println("motor do nothing")
}
func motor_reset(modeCode int) {
	fmt.Println("moter reset")
}
