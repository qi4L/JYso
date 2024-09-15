# 🐦‍⬛ JYso

![](https://img.shields.io/badge/JDK-1.6+-orange)
![](https://img.shields.io/badge/gradle-8.7-blue)
![](https://img.shields.io/badge/SDL-Groovy-green)
[![License](https://img.shields.io/github/license/shmilylty/OneForAll)](https://github.com/shmilylty/OneForAll/tree/master/LICENSE)

👊**JYso**是一款可以同时当做 ysoserial 与 JNDIExploit 使用的工具，同时具备多种JNDI高版本、WAF、RASP的Bypass功能。📝[English Document](README_EN.md)

## 🚀 上手指南

📢 请务必花一点时间阅读此文档，有助于你快速熟悉JYso！

🧐 使用文档[Wiki](https://github.com/qi4L/JYso/wiki)。

✔ 下载最新版本的[Releases](https://github.com/qi4L/JYso/releases)。

## 👍 特点

+ JNDI 账号密码启动
+ JNDI 路由隐藏或加密
+ JNDI 高版本Bypass
+ 自定义修改内存马的路径、密码、验证的HTTP头与值
+ 内存马支持[无文件落地Agent打入](https://xz.aliyun.com/t/10075?time__1311=mq%2BxBD9QDQe4yDBkPoN%2BuDAO%3DnB5x&alichlgref=https%3A%2F%2Fxz.aliyun.com%2Fsearch%3Fkeyword%3Drebeyond)
+ 内存马写入 JRE 或环境变量来隐藏
+ 序列化数据加脏数据
+ [序列化数据进行3字节对应的UTF-8编码](https://whoopsunix.com/docs/PPPYSO/advance/UTFMIX/)
+ TemplatesImpl 的 _bytecodes 特征消除且做了大小缩减
+ SignedObject 二次反序列化，可用于绕过如 TemplatesImpl 黑名单，CTF 中常出现的 CC 无数组加黑名单等
+ 解决 Shiro Header 头部过长，从 request 中获取指定参数的值进行类加载
+ 动态生成混淆的类名
+ MSF/CS 上线
+ 通过JDBC来进行代码执行

如果你有其他很棒的想法请务必告诉我！😎

## 🐯 编译

下载 gradle8.7+ 并配置到全局环境变量中，在项目根目录下执行

```shell
./gradlew shadowJar
```

## 🌲目录结构

更多信息请参阅[目录结构说明](docs/directory_structure.md)。

## 🙏贡献

非常热烈欢迎各位大佬一起完善本项目！

## ✨ 404StarLink 2.0 - Galaxy

JYso 是 404Team [404StarLink 2.0](https://github.com/knownsec/404StarLink) 中的一环，如果您有 关于JYso的问题或者想找伙伴交流，可以参考星链加群方式项目。

+ https://github.com/knownsec/404StarLink2.0-Galaxy#community

## 📜 免责声明

本工具仅能在取得足够合法授权的企业安全建设中使用，在使用本工具过程中，您应确保自己所有行为符合当地的法律法规。
如您在使用本工具的过程中存在任何非法行为，您将自行承担所有后果，本工具所有开发者和所有贡献者不承担任何法律及连带责任。
除非您已充分阅读、完全理解并接受本协议所有条款，否则，请您不要安装并使用本工具。
您的使用行为或者您以其他任何明示或者默示方式表示接受本协议的，即视为您已阅读并同意本协议的约束。

## 📷 参考

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
- https://tttang.com/archive/1405/#toc_groovyclassloader
- https://xz.aliyun.com/t/10656?time__1311=mq%2BxBDy7G%3DLOD%2FD0DoY4AKqiKD%3DOQjqx&alichlgref=https%3A%2F%2Ftttang.com%2F
- https://www.leavesongs.com/PENETRATION/use-tls-proxy-to-exploit-ldaps.html

