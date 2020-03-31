# uploader 大文件、断点续传、分片、妙传、普通文件上传样例
工具类包封装了一些关于分片md5验证、断点续传、分片上传、等方法
前端样例使用百度插件 <code>WebUploader</code> , 插件的源码还是有一定的问题的
样例包对插件源码做了轻微修改
新增了<code>dialogOpen</code>事件、文件新增<code>file.uploadRate</code>(上传速率)属性<br>
[doc文档](https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/script/javascript/uploader/web-uploader/uploader-doc.html)

## 后端实现功能

* 断点续传: 本次最主要的基础功能，在断网或者在暂停的情况下，能够在上传断点中继续上传。<br>
* 分块上传: 也是归属于断点续传的基础功能之一，前端大文件分块后端组合，断点续传也是重传出错的这个分块<br>
* 文件秒传: 上传前验证MD5 ，服务端返回一个代表这个文件已经上传了的状态，前端跳过这个文件<br>
* 普通上传: 顺带着写了一个

## 基础原理

前端在上传前 发送一条带有MD5信息的GET请求 来验证文件的状态，前端根据后端返回的状态进行上传
如果后端返回文件已经上传的状态码，前端跳过该文件的上传，这样就实现了秒传.
当后端收到分片数据的时候
使用 RandomAccessFile(随机访问文件) 类随机指定位置写入文件,类似于其他方案的合成分片
检验分片文件是否全部上传完成，重命名缓存文件

## 基于工具类包1.1版本

```xml
<dependency>
  <groupId>cn.novelweb</groupId>
  <artifactId>tool-core</artifactId>
  <version>1.3.3</version>
</dependency>
```

## 上传中:
![](https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/wp-content/uploads/2019/11/20191105092506.png)<br>

## 部分源码:
![](https://qcloud-1256166828.cos.ap-shanghai.myqcloud.com/wp-content/uploads/2019/11/20191105001842.png)<br>