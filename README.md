

# 安装构建环境（ubuntu 20.04 ~24.04）

```bash
sudo apt update
```

## 一、 Install JDK 17

You can install OpenJDK 17 by running:

```bash
sudo apt install openjdk-17-jdk
```

## 二、Install Maven

```bash
sudo apt install maven
```

### Configure Environment Variables 

编辑 `.bashrc` or `.bash_profile` 文件:

```bash
nano ~/.bashrc
```

Add the following lines to the end of the file:

```bash
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export MAVEN_HOME=/usr/share/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

Save the file and exit. Then, apply the changes:

```bash
source ~/.bashrc
```



## 三、安装 SDK MAN

```
curl -s "https://get.sdkman.io" | bash
```

！！！安装后，要重开一个新的 terminal ！！！

## 四、安装 Gradle

```
sdk install gradle 8.12
```

## 五、批量构建

```
sudo sh build.sh
```
——————
## 在Docker 中构建项目
另一种方案是在Docker中构建项目，
### 构建编译环境 
下载源代码根目录中的 Dockerfile
`sudo docker build -t gidsp-java7 .`
### 挂载主机源码文件夹
`sudo docker run -it -v /home/ubuntu/gidsp-core:/app gidsp-java7 /bin/bash`
### 再次进入
```
sudo docker stop
sudo docker start
sudo docker exec -it <生成的docker id> /bin/bash
```

