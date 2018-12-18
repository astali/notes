
初始化Mysql连接
```go
package lib

import (
	"fmt"

	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
)

var (
	dbMaxidle, dbMaxconn = 30, 50
)

func init() {
	Setlog()
	if err := SetdbConnection(); err != nil {
		panic(err.Error())
	}
	if err := SetCache(); err != nil {
		panic(err.Error())
	}
}

func Setlog() {
	beego.SetLogger(beego.AppConfig.String("log.type"),
		beego.AppConfig.String("log.path"))
	beego.SetLevel(beego.LevelDebug)
	beego.Info("set log config success")
}

func SetdbConnection() error {
	dbMaxidle, _ = beego.AppConfig.Int("db.maxidle")
	dbMaxconn, _ = beego.AppConfig.Int("db.maxconn")
	conn := fmt.Sprintf("%s:%s@tcp(%s:%s)/%s?charset=utf8",
		beego.AppConfig.String("db.default.user"),
		beego.AppConfig.String("db.default.password"),
		beego.AppConfig.String("db.default.addr"),
		beego.AppConfig.String("db.default.port"),
		beego.AppConfig.String("db.default.name"))
	orm.RegisterDriver("mysql", orm.DRMySQL)
	if err := orm.RegisterDataBase("default", "mysql", conn, dbMaxidle, dbMaxconn); err != nil {
		beego.Error("set db connection error:", err)
		return err
	}
	orm.RunCommand()
	beego.Info("set db connection success")
	return nil
}
```

Beego使用
```go
package dao

import (
	"JumpWechat/model"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)
func BindJumpwAccountDao(msg *model.MyForm) (error, int) {
	o := orm.NewOrm()
	o.Using("defalut") // 默认使用 default，你可以指定为其他数据库
	var r orm.RawSeter
	r = o.Raw("select id baseinfotab("+
		"AccountName,"+
		"Password"+
		") VALUES(?,?);",
		msg.AccountName,
		msg.Password)
	if _, err := r.Exec(); err != nil {
		beego.Error(err)
		return err, model.UnknowError
	}
	return nil, model.Success
}
```
GetDB
从已注册的数据库返回 *sql.DB 对象，默认返回别名为 default 的数据库。
```go
db, err := orm.GetDB()if err != nil {
    fmt.Println("get default DataBase")
}
db, err := orm.GetDB("alias")if err != nil {
    fmt.Println("get alias DataBase")
}
```
