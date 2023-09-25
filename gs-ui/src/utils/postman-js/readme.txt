在postman全局/局部变量中，增加对应关系：
myFunction-index.js(多合一)
然后通过eval写法引入
eval(postman.getGlobalVariable("myFunction"))


各js的方法：
index.js
	genPwd(pwd) 密码加密传输
	及以下all

sm2.js
	sm2Encrypt(data, publickey, cipherMode)

sm3.js
	sm3Digest(msg) sm3摘要，返回十六进制

base64.js
	hex2b64(h)
	b64tohex(s)
