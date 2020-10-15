call docker-compose up -d
timeout 45 /nobreak
call java -jar app-deadline.jar -P:jdbc.url=jdbc:mysql://192.168.99.100:3306/app -P:jdbc.user=app -P:jdbc.password=pass
call docker-compose down