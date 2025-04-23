# Запуск

## У себя
```bash
./gradlew runClient
./gradlew runServer
```

## На клиенте
```bash
#Сервер
./gradlew runServer

#Создаем jar и запускаем
./gradlew clientJar
java -jar build/libs/lab6-client.jar helios.cs.ifmo.ru 22222

#P.S. Почему-то именно клиент через build не работает, поэтому запускаю через jar
```
Подключение у себя - сервер не работает т.к. у нас он просто пытается бесконечно подключиться и не может. 
Но если есть вайб попробовать то команды теже что и в запуске на клиенте