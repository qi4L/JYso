# 🐦‍⬛JYso

![](https://img.shields.io/badge/JDK-1.6+-orange)
![](https://img.shields.io/badge/gradle-8.7-blue)
![](https://img.shields.io/badge/SDL-Groovy-green)

👊**JYso** is a tool that can be used as both ysoserial and JNDIExploit. It also has bypass functions of multiple JNDI high versions, WAF, and RASP.

# 🚀 Getting Started Guide

📢 Please be sure to take a moment to read this document, which will help you quickly become familiar with JYso!

🧐 Use documentation [Wiki](https://github.com/qi4L/JYso/wiki).

✔ Download the latest version of [Releases](https://github.com/qi4L/JYso/releases).

# 🐲Features

+ JNDI account activation
+ JNDI routing hidden or encrypted
+ JNDI high version Bypass
+ 12 available echo classes
+ 18 available memshell classes, and supports modifying the memshell path, password, authentication HTTP header and value
+ 76 available Gadgets and provide multiple ways to use them
+ Memory horse supports [No file landing Agent entry](https://xz.aliyun.com/t/10075?time__1311=mq%2BxBD9QDQe4yDBkPoN%2BuDAO%3DnB5x&alichlgref=https%3A%2F%2Fxz.aliyun.com%2Fsearch%3Fkeyword%3Drebeyond)
+ write the memshell to JRE or environment variable to hide
+ Serialized data plus dirty data
+ [Serialized data is encoded in UTF-8 corresponding to 3 bytes](https://whoopsunix.com/docs/PPPYSO/advance/UTFMIX/)
+ Secondary deserialization of SignedObject, which can be used to bypass TemplatesImpl blacklist, CC countless groups and blacklists that often appear in CTF, etc.
+ To solve the problem that the Shiro Header header is too long, obtain the value of the specified parameter from the request for class loading.
+ Dynamically generate obfuscated class names
+ MSF/CS online
+ Code execution via JDBC

If you have any other great ideas be sure to let me know! 😎

# 🐯Compile

Download gradle8.7+ and configure it in the global environment variable, execute it in the project root directory

```shell
./gradlew shadowJar
```

# 🌲Directory structure

For more information, see [Directory Structure Description](docs/directory_structure.md).

# ⌛ Follow-up plan

- [ ] Continuous optimization and improvement of each module
- [ ] Add more Bypass functions
- [ ] Optimize the traversal effect through machine learning and graph theory

# 🙏Contribute

We warmly welcome all of you to work together to improve this project!

# ✨404StarLink 2.0 - Galaxy

JYso is a part of 404Team [404StarLink 2.0](https://github.com/knownsec/404StarLink). If you have questions about JYso or want to find a partner to communicate, you can refer to the Starlink group project.

+ https://github.com/knownsec/404StarLink2.0-Galaxy#community

# 📜 Disclaimer

This tool can only be used in the security construction of enterprises that have obtained sufficient legal authorization. When using this tool, you should ensure that all your actions comply with local laws and regulations.
If you commit any illegal behavior while using this tool, you will bear all the consequences yourself. All developers and contributors of this tool do not assume any legal and joint liability.
Please do not install and use this tool unless you have fully read, fully understood and accepted all the terms of this agreement.
Your usage behavior or your acceptance of this Agreement in any other express or implicit manner shall be deemed to have read and agreed to be bound by this Agreement.

# 📷reference project

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

