URL进行encode 
```go
userName_encode:= url.QueryEscape(userName)
```
md5
```go
    h := md5.New()
	io.WriteString(h, "123456")
   serverSign := fmt.Sprintf("%x", h.Sum(nil)) 
```
	