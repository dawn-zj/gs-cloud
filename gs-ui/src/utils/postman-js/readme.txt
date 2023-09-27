在postman全局/局部变量中，增加对应关系：
myFunction-all.js(多合一)
然后通过eval写法引入
eval(postman.getGlobalVariable("myFunction"))


各js的方法：(vue项目下建议用sm-crypto)
all.js
	以下all

sm2.js
	sm2Encrypt(data, publickey, cipherMode)
	暂未找到解密方法

sm3.js
	sm3Digest(msg) sm3摘要，返回十六进制

base64.js
	hex2b64(h)
	b64tohex(s)
