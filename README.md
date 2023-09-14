# GS-Cloud

项目基于SpringCloud、maven等技术。

# 在线浏览地址
https://101.43.242.145:8443/tool

## 功能：
1. 系统监控：获取系统、CPU、内存、硬盘等信息；
2. socket通信测试； 
3. 线程池线程复用测试；

### 基础工具类
1. 日期工具类；
2. 字符串工具类；
3. 十六进制工具类；
4. 操作文件工具类，文件压缩解压缩；
5. Base64编解码工具类；
6. 获取网卡信息，执行Sh脚本工具类，发送http/https的post请求；

### 图片处理
1. 制作图片验证码：支持指定宽高、验证码个数、干扰线个数；
2. 制作二维码；
3. 图片去底色；
4. 图片加水印(不成熟)；
5. 圆形图章；

### PDF操作
1. PDF转Word(不成熟)；
2. PDF模板获取文本域；
3. PDF模板填充内容；
4. PDF添加图片； 
5. PDF文件RSA签章、验章、撤章；(PDF的RSA签章 = 图片+签名，没有印章和签章结构)
   1. 验签支持ETSI.CAdES.detached、adbe.pkcs7.detached、adbe.x509.rsa_sha1等;


### 信息安全
1. 签名验签支持SHA1_RSA、SHA256_RSA、SM3_SM2； 
2. 加密解密支持RSA、SM2(Bouncycastle、GMT0009)；
3. SM2加解密密文支持C1C2C3、C1C3C2模式；
4. 解析密钥文件（PFX、JKS），获取私钥；
5. 解析PFX文件，获取证书；
6. JKS转PFX；
7. 解析证书实体；
8. 获取Provider；
9. 制作和解析P7B；
10. 制作和验证p10，暂只支持RSA算法；
11. 制作和验证数字信封；
12. 支持制作PKCS1规范的hash值；

### 报文生成
1. 采用japidocs工具类，通过规范的注释来生成报文结构。

### 打包问题
1. 为了Jenkins打包，前后端全都属于父项目中的模块：
    - 前端pom中定义编译后存入NetTool里的tomcat中，
    - 后端pom中定义编译后的war存入NetTool里的tomcat中。
    - 在Jenkins里，执行根pom，然后将NetTool中所需信息传到腾讯云服务里，重启服务。
2. 研究assembly自动打包，争取打出基线包和补丁包。

### 错误码设计
[系统]-[模块]-[功能]-[错误类型]
[xxx]-[xxx]-[x参数/增删改查]-[为空/超限/不存在]
xxx模块-xxx参数-不能为空

### MVC架构设计
1. 分为controller、service、mapper三层；
2. controller调service；
3. service调service、mapper；
4. controller接收用户to类、转为po后传给service
5. service和mapper为一体，只与po有关；