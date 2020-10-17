 # SQL
 
 Автотесты для проверки входа в личный кабинет интернет-банка
 
 ## Начало работы
 
 Откройте на своем компьютере Git Bash и выполните команду ```git clone https://github.com/NAleshina/SQL```
 Запустите клонированный проект в IntelliJ IDEA
 
 ### Установка и запуск
 
 -  ```docker-machine start default``` запуск Docker;
 -  ```docker-compose up -d``` запуск контейнера;
 -  ```cd artifacts``` переход в папку с приложением;
 -  ```java -jar app-deadline.jar -P:jdbc.url=jdbc:mysql://192.168.99.100:3306/app -P:jdbc.user=app -P:jdbc.password=pass``` запуск приложения;
 -  ```gradlew test``` запуск тестов;
 -  ```docker-compose down``` завершение работы контейнера.
 