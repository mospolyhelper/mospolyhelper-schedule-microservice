## Gradle
Автоформаттирование кода:
```shell
./gradlew formatKotlin
```

## Docker
Создания образа:
```shell
docker build -t edugma .
```
Сохранение образа:
```shell
docker save -o .\edugma.tar edugma
```
Удалить все контейнеры и образы:
```shell
sudo docker rm -vf $(sudo docker ps -aq)
sudo docker rmi -f $(sudo docker images -aq)
```
Загрузка образа:
```shell
sudo docker load < edugma.tar
```
Запуск образа:
```shell
sudo docker run -p 8003:8003 -d --name edugma --env-file env.list edugma
```
Остановка контейнера:
```shell
sudo docker stop edugma
```
Чтение логов контейнера:
```shell
sudo docker logs edugma -f
```
