# 同学们,QuartzWeb的第一个可运行版本已经出炉了 #




# Web应用搭建步骤 #


  * 从SVN将代码check到本地
  * 执行建库和建表的脚本,脚本在工程的dbscript目录下。进入mysql命令行，输入"source 脚本路径" 就可以了
  * 修改WEB-INF下的applicationContext.xml文件,修改fileUploadHelper Bean里的路径属性为你本机的实际配置，修改jdbc.properties文件，把数据库的链接信息改为你自己的
> ## 到此web应用就搭建完成了，下面开始介绍如何将一个java程序安装到我们的定时调度平台 ##
    * 启动应用，在浏览器里打开index.jsp
    * 在工程的目录下有个app\_for\_test目录，里面有个test\_jobs.zip,这是一个用于测试的java程序,通过页面把这个zip包上传到我们的服务器，然后点击安装
    * 输入任务名，所属组，主类、主函数参数、java虚拟机参数、描述。点击提交，这样一个 任务就家到我们的系统里了
    * 任务加到了系统里它自己并不会自动执行，需要我们给它关联个Trigger.点击任务名上的超链接，进入添加trigger的页面。我们这里用的是cronTrigger，几乎可以满足所有的调度需求，但是比较难懂。比如我们输入0/10  ?   