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
Загрузка образа:
```shell
docker load < edugma.tar
```
Запуск образа:
```shell
docker run -p 8003:8003 -d --name edugma --env-file env.list edugma
```
Остановка контейнера:
```shell
docker stop edugma
```
Чтение логов контейнера:
```shell
docker logs edugma -f
```
