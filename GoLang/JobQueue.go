package main

import (
	"fmt"
	"reflect"
	"testing"
	"time"
)
func TestPayDB(t *testing.T) {
	fmt.Println("kkkkkkkkkkk")
	//JobQueue_ = make(chan Job_, 10)
	//dispatcher := NewDisPatcher_(MaxWorker)
	//dispatcher.Run()
	//time.Sleep(1 * time.Second)
	//go addQueue()
	//time.Sleep(1000 * time.Second)
}
var (
	MaxWorker = 10
)

type Payload struct {
	Num int
}

//待执行的工作
type Job_ struct {
	Payload Payload
}

//任务channal
var JobQueue_ chan Job_

//执行任务的工作单元
type Worker_ struct {
	WorkerPool chan chan Job_ //工作者池--每个元素是一个工作者私有任务channal
	JobChannel chan Job_      //每个工作者单元包含一个任务管道，用于获取任务
	Quit       chan bool     //退出信号
}

//创建一个工作单元
func NewWorker_(WorkerPool chan chan Job_) Worker_ {
	fmt.Print("新建一个工作单元")
	return Worker_{
		WorkerPool: WorkerPool,
		JobChannel: make(chan Job_),
		Quit:       make(chan bool),
	}
}

// 循环 监听任务和结束信号
func (w Worker_) Start() {
	go func() {
		for {
			w.WorkerPool <- w.JobChannel
			fmt.Println("w.Workpool <- w.JobChannel", w)
			select {
			case job := <-w.JobChannel:
				fmt.Println("job:= <- w.Jobchannel")
				fmt.Println(job)
				time.Sleep(10 * time.Second)
			case <-w.Quit:
				//收到退出信号
				return
			}
		}
	}()
}

//停止信号
func (w Worker_) Stop() {
	go func() {
		w.Quit <- true
	}()
}

//调度中心
type Dispatcher_ struct {
	//工作者池
	WorkerPool chan chan Job_
	//工作者数量
	MaxWorkers int
}

//创建调度中心
func NewDisPatcher_(maxWorker int) *Dispatcher_ {
	pool := make(chan chan Job_, maxWorker)
	return &Dispatcher_{WorkerPool: pool, MaxWorkers: maxWorker}
}

//工作者池初始化
func (d *Dispatcher_) Run() {
	worker := NewWorker_(d.WorkerPool)
	worker.Start()
	go d.dispatch()
}

//调度
func (d *Dispatcher_) dispatch() {
	for {
		select {
		case job := <-JobQueue_:
			fmt.Println("job:=<-JobQueue")
			go func(job Job_) {
				fmt.Println("等待空闲worker,(任务多会阻塞在这里)")
				//等待空闲worker
				jobChannel := <-d.WorkerPool
				fmt.Println("jobChannel := <- d.WorkPol", reflect.TypeOf(jobChannel))
				//将任务放到上述woker的私有任务channal中
				jobChannel <- job
				fmt.Println("jobChannel <- job")
			}(job)
		}
	}
}


func addQueue() {
	for i:=0; i<100 ; i++  {
		payLoad := Payload{Num:i}
		work:= Job_{Payload: payLoad}
		JobQueue_ <- work
		fmt.Println("JobQuere <- work ",i)
		//fmt.Println("当前协程数",runtime.NumGoroutine())
		time.Sleep(100 * time.Microsecond)

	}
}

