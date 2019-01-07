# StormShop

StormShop - проект содержащий в себе серверную(java) и клиентскую часть(javaFX), для платформ JDK.

Особенности:
- Написан под Desktop приложение.
- В проекте используется фреймворк Spark: все запросы осуществляются по протоколу - http.

В прокте используются следующие библиотеки:
- jackson - сериализация и десериализация объектов из json файла и обратно;
- apache commons code - хеширование пароля пользователя, на основании алгоритма SHA1;

Для запуска приложения необходимо:
- Скачать java 8;
- Clean instaill - осуществляется через инструмент для сбори java проекта - maven;

Фунционал программы:

Запуская серверную за тем клиентскую часть, вы имеете окно, где должне быть прописан адрес сервера;
Адрес для подключения к серверу - http://localhost:4567 (прописан изначально, в поле ввода);

В случае удачного подключения появляются новые два окна, BuyGoods и Authorization;
BuyGoods - окно для добавления товара в корзину c последующим приобретением: 
- Выбрав товар, вы можете увеличивать а также уменьшать количества.
- В случае, выбрав товар по ошибке или нежелание приобретение имеющегося товара в корзине, вы всегда можете его удалить из корзины.
  
Authorization - окно для получения прав администра магазина. Для авторизации необходимо ввести Login и Password:
- authorization (login - Vanya, password - tarakan123), "в корневой папке проекта JavaSparkBackend имеется файл со списком аккаунтов - "account.json""; 

После авторизации вам становится доступно окно AddGoods - где вы можете выполнять следующий фунционал:
- Изменять количество уже существующих товаров 

	Для этого необходимо: 
- ввести имя уже существующего товара с указание нужного вам количества;
- уменшение количество товара, используется также логика с указание нужным количеством отнимаемого товара; 
- для добавления нового товара в магазин - прописываются новый товар со своим количеством и ценой;
- в случае необходимости изменить цену товара пункт номер один, с нужным указание цены товара;

Файл с содержимом товаров в магазине можно посмотреть в корневой папке проекта JavaSparkBackend с наименованием goods.json;

StormShop - функиональное приложение, которое позволяет вам осуществялть простые манипуляции:  приобретения и добавление товара не выходя из дома).
