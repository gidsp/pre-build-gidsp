# 构建编译环境 JAVA 17
# sudo docker build -t gidsp-gradlew8-3-java .
# 挂载主机源码文件夹
# sudo docker run -it -v /home/ubuntu/final:/app gidsp-gradlew8-3-java /bin/bash
# 再次进入
# sudo docker stop
# sudo docker start
# sudo docker exec -it xx /bin/bash

FROM gradle:8.3

# 设置维护者信息（可选）
LABEL maintainer="gidisp@fastable"

# 更新包管理器，并安装 Maven、zip、unzip 和 sudo
RUN apt-get update && \
    apt-get install -y \
    maven \
    zip \
    nodejs \
    npm \
    unzip \
    sudo && \
    # 清理缓存以减小镜像体积
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# 设置工作目录
WORKDIR /app

# 默认命令（可根据需要修改）
CMD ["bash"]