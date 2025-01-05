# 先授权
chmod +x gradlew
#再构建
./gradlew clean build

##注意
删除此文件将构建失败。

用 gradle wrapper 命令生成的 gradlew 可能会有问题。
如果要用getGidspPreBuild.py（夸克）升级新版本，务必保留 build 文件夹和 gradle 文件夹中的所有内容 
#可以直接在根目录执行 sh build.sh 批处理
