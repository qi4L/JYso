# 🐦‍⬛ JYso

![](https://img.shields.io/badge/JDK-1.6+-orange)
![](https://img.shields.io/badge/gradle-8.7-blue)
![](https://img.shields.io/badge/SDL-Groovy-green)

JYso是一款可以同时当做 ysoserial 与 JNDIExploit 使用的工具，同时具备多种JNDI高版本、WAF、RASP的Bypass功能。

[English Readme](README_EN.md)

# 🦜 help！

具体的使用请查看[wiki](https://github.com/qi4L/JYso/wiki)。

可以从这里下载最新版本的[releases](https://github.com/qi4L/JYso/releases)。

# 🐲 特点

+ JNDI 账号密码启动
+ JNDI 路由隐藏或加密
+ JNDI 高版本Bypass
+ 12个可用回显类
+ 18个可用的内存马类，且支持修改内存马的路径、密码、验证的HTTP头与值
+ 75条可用 Gadget，并提供多种利用方式
+ 内存马支持[无文件落地Agent打入](https://xz.aliyun.com/t/10075?time__1311=mq%2BxBD9QDQe4yDBkPoN%2BuDAO%3DnB5x&alichlgref=https%3A%2F%2Fxz.aliyun.com%2Fsearch%3Fkeyword%3Drebeyond)
+ 内存马写入 JRE 或环境变量来隐藏
+ 序列化数据加脏数据
+ [序列化数据进行3字节对应的UTF-8编码](https://whoopsunix.com/docs/PPPYSO/advance/UTFMIX/)
+ SignedObject 二次反序列化，可用于绕过如 TemplatesImpl 黑名单，CTF 中常出现的 CC 无数组加黑名单等
+ 解决 Shiro Header 头部过长，从 request 中获取指定参数的值进行类加载
+ 动态生成混淆的类名
+ MSF/CS 上线


# ✨ 404StarLink 2.0 - Galaxy

JYso 是 404Team [404StarLink 2.0](https://github.com/knownsec/404StarLink) 中的一环，如果您有 关于JYso的问题或者想找伙伴交流，可以参考星链加群方式项目。

+ https://github.com/knownsec/404StarLink2.0-Galaxy#community

# 📷 参考

- https://github.com/veracode-research/rogue-jndi
- https://github.com/welk1n/JNDI-Injection-Exploit
- https://github.com/welk1n/JNDI-Injection-Bypass
- https://github.com/WhiteHSBG/JNDIExploit
- https://github.com/su18/ysoserial
- https://github.com/rebeyond/Behinder
- https://t.zsxq.com/17LkqCzk8
- https://mp.weixin.qq.com/s/fcuKNfLXiFxWrIYQPq7OCg
- https://xz.aliyun.com/t/11640?time__1311=mqmx0DBDuDnQ340vo4%2BxCwg%3DQai%3DYzaq4D&alichlgref=https%3A%2F%2Fxz.aliyun.com%2Fu%2F8697
- https://archive.conference.hitb.org/hitbsecconf2021sin/sessions/make-jdbc-attacks-brilliant-again/
- https://tttang.com/archive/1405/#toc_0x03-jdbc-rce
- https://xz.aliyun.com/t/10656?time__1311=mq%2BxBDy7G%3DLOD%2FD0DoYg0%3DDR0HG8KeD&alichlgref=https%3A%2F%2Ftttang.com%2F#toc-7
- https://whoopsunix.com/docs/PPPYSO/advance/UTFMIX/
- https://github.com/Whoopsunix/utf-8-overlong-encoding

