Приложение для бронирования комнат в хоспитале
Стек технологий Java-8, Postgre-11, JPA, SpringBoot-2, Vaadin-12, Maven.
Инструкция по запуску
- создать базу в постгре jdbc:postgresql://localhost/postgres
- ввести логин и пароль а так же настроить его в файле application.properties
- лучше всего запустить через среду разработки, но можно и через консоль
- таблицы в базе инициализируются самостоятельно при запуске без дополнительных настроек и DDL скриптов, Spring их генерирует сам
Краткий гайд по приложению
- есть 3 страницы /login /booking(бронирование комнат) /register(регистрация юзера)
- в нем можно регистрировать комнаты, изменять и удалять, вбивать в поиск Так же как и юзеров
- Если текущая дата превышает дату окончания бронирования автоматом UI обновляется посредством Vaadin-push(as WebSocket), Scheduler проверяет базу(а точнее мини кэш)
- Текущий Юзер не может бронировать два помещения, комната не содержит 2 манипуляции
- в случае ошибки выскакивает alert notification
- CommandRunner загружает 2х пользователей
- Полной валидации на поля нету, есть только частичная
-Пользователи
1.login-user password-user role-USER
2.login-admin password-admin role-ADMIN
админ может вносить изменения пользователей, обычный юзер нет
