.
├── .gradle                    Gradle 构建缓存
│   ├── 7.6.6                  Gradle 版本缓存
│   │   ├── checksums
│   │   ├── dependencies-accessors
│   │   ├── executionHistory
│   │   ├── fileChanges
│   │   ├── fileHashes
│   │   └── gc.properties
│   ├── buildOutputCleanup
│   └── vcs-1
├── .idea                     IDEA 配置
│   ├── artifacts
│   ├── codeStyles
│   ├── inspectionProfiles
│   └── libraries
├── docs                      项目文档
│   └── directory_structure.md
├── gradle
│   └── wrapper                Gradle Wrapper (7.6.6)
├── libs                      第三方 JAR
├── src
│   ├── main
│   │   ├── frontend           前端 React 项目
│   │   │   ├── public
│   │   │   └── src
│   │   ├── java
│   │   │   └── com/qi4l/JYso
│   │   │       ├── HTTPServer.java
│   │   │       ├── LdapServer.java
│   │   │       ├── LdapsServer.java
│   │   │       ├── RMIServer.java
│   │   │       ├── Starter.java
│   │   │       ├── controllers          JNDI/LDAP 控制器
│   │   │       │   ├── BaseLdapController.java   公共基类
│   │   │       │   ├── BasicController.java
│   │   │       │   ├── ELProcessorController.java
│   │   │       │   ├── GroovyController.java
│   │   │       │   ├── LdapController.java       控制器接口
│   │   │       │   ├── LdapMapping.java
│   │   │       │   ├── MemoryXXEController.java
│   │   │       │   ├── SerializedDataController.java
│   │   │       │   ├── SnakeYamlController.java
│   │   │       │   ├── XStreamController.java
│   │   │       │   ├── jdbcController1.java
│   │   │       │   ├── jdbcController2.java
│   │   │       │   ├── ldap2rmiController.java
│   │   │       │   ├── rmi                      RMI 工具类
│   │   │       │   │   ├── Basic.java
│   │   │       │   │   └── ELProcessor.java
│   │   │       │   └── utils
│   │   │       │       └── JNDIUtils.java
│   │   │       ├── enumtypes
│   │   │       │   ├── GadgetType.java
│   │   │       │   ├── PayloadType.java
│   │   │       │   └── WebsphereActionType.java
│   │   │       ├── exceptions
│   │   │       │   ├── IncorrectParamsException.java
│   │   │       │   ├── UnSupportedActionTypeException.java
│   │   │       │   ├── UnSupportedGadgetTypeException.java
│   │   │       │   └── UnSupportedPayloadTypeException.java
│   │   │       ├── exploit               -cp 模式漏洞利用
│   │   │       │   ├── JBoss.java
│   │   │       │   ├── JMXInvokeMBean.java
│   │   │       │   ├── JRMPClassLoadingListener.java
│   │   │       │   ├── JRMPClient.java
│   │   │       │   ├── JRMPListener.java
│   │   │       │   ├── JSF.java
│   │   │       │   ├── JenkinsCLI.java
│   │   │       │   ├── JenkinsListener.java
│   │   │       │   ├── JenkinsReverse.java
│   │   │       │   └── RMIBindExploit.java
│   │   │       ├── gadgets               反序列化 Gadget
│   │   │       │   ├── ObjectPayload.java
│   │   │       │   ├── ReleaseableObjectPayload.java
│   │   │       │   ├── SignedObject.java
│   │   │       │   ├── annotation          注解
│   │   │       │   │   ├── Authors.java
│   │   │       │   │   └── Dependencies.java
│   │   │       │   ├── Config              全局配置
│   │   │       │   │   ├── Config.java
│   │   │       │   │   ├── MemShellPayloads.java
│   │   │       │   │   └── ysoserial.java
│   │   │       │   ├── utils               工具类
│   │   │       │   │   ├── Gadgets.java
│   │   │       │   │   ├── Reflections.java
│   │   │       │   │   ├── Serializer.java
│   │   │       │   │   ├── Utils.java
│   │   │       │   │   ├── ... (ByteUtil, ClassFiles, HexUtils, InjShell 等)
│   │   │       │   │   ├── beanshell/
│   │   │       │   │   ├── cc/
│   │   │       │   │   ├── clojure/
│   │   │       │   │   ├── dirty/
│   │   │       │   │   ├── handle/
│   │   │       │   │   ├── jre/
│   │   │       │   │   └── utf8OverlongEncoding/
│   │   │       │   └── *.java              各 Gadget 链实现
│   │   │       ├── template             模板类
│   │   │       │   ├── ClassLoaderTemplate.java
│   │   │       │   ├── DefineClassFromParameter.java
│   │   │       │   ├── HideMemShellTemplate.java
│   │   │       │   ├── Meterpreter.java
│   │   │       │   ├── ReverseShellTemplate.java
│   │   │       │   └── Template.java
│   │   │       └── web                  Web 控制台
│   │   │           ├── JYsoWebApplication.java
│   │   │           ├── ApiAuthFilter.java
│   │   │           ├── AuthServlet.java
│   │   │           ├── JettyApiServlet.java
│   │   │           ├── RequestLogCollector.java
│   │   │           ├── SpaFallbackFilter.java
│   │   │           ├── StaticResourceServlet.java
│   │   │           └── config/
│   │   │               ├── JYsoWebPasswordProvider.java
│   │   │               └── WebPasswordGenerator.java
│   │   └── resources                   资源文件
│   │       ├── application.properties
│   │       ├── log4j2.xml
│   │       └── static/                  前端打包产出
│   └── test                            测试代码
│       └── java
│           ├── Main.java
│           └── com/example/demo/demos/web
│               ├── Test.java
│               └── secCig.java
├── build.gradle               Gradle 构建脚本
├── settings.gradle            Gradle 项目设置
├── README.md
└── README.en.md
