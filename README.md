## Gradle
Автоформаттирование кода:
```shell
./gradlew formatKotlin
```

## Docker
Удаление старого образа. Создание и сохранение образа:
```shell
docker rmi $(docker images 'edugma' -a -q)
docker build -t edugma .
docker save -o .\edugma.tar edugma
```
Удалить все старные контейнеры и образы:
```shell
docker rm -vf $(docker ps -aq)
docker rmi $(docker images 'edugma' -a -q)
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
