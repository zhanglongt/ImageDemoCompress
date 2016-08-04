# ImageDemo
android 打开相机/打开相册(版本兼容)-获取照片，压缩100K一下保存到本地


流程：
1、打开相机/相册，获取照片的返回路径
2、如果是相册的路径，通过ImageUtil.getPhotoPath() 来得到图片的路径
3、图片的压缩
4、在图片压缩完成保存到的ByteArrayOutputStream里面写入到本地，并返回图片的路径
5、得到返回的图片的路径，上传到服务器。

