看到公众号发了一个工具，GitHub又取消了，然后好多朋友又求这个工具，就自己开发一个

其实就是个按钮集合工具

 

下载impacket：https://github.com/fortra/impacket

使用python3，版本不要高于3.10，否则impacket有bug，我用的Python 3.9.13

进入目录执行pip install -r requirements.txt，pip install impacket

 

## **2023.8.31**

初步完成功能按钮的绑定

遇到问题

如果有中文出现乱码，添加编码按钮，使用GBK编码解决乱码

**
**

## **2023.9.1**

完成完整的交互式shell，除了zerologon没测试都测试正常

![image-20230902223647578](D:\Record\TyporaImages\image-20230902223647578.png)

![image-20230902223851344](D:\Record\TyporaImages\image-20230902223851344.png)

![image-20230902223910448](D:\Record\TyporaImages\image-20230902223910448.png)
