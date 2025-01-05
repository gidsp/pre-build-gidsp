#!/bin/sh

# 检查文件是否存在
if [ ! -f ./gidsp-rule-engine/build/libs/rule-engine-jvm-3.2.2.jar ]; then
    cd ./gidsp-rule-engine/
    chmod +x ./gradlew
    chown -R $(whoami) ./build
    # 使用 gradle wrapper 构建
    ./gradlew clean build
    cd ../
fi

# 执行 maven 安装
mvn clean install --batch-mode --no-transfer-progress -DskipTests=true -Dgpg.skip=true