package org.o7planning.javafx.app;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;
import org.o7planning.javafx.model.*;
import org.o7planning.javafx.service.HttpConnectionService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppClient extends Application {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final AppService update = new AppService();
    private static final HttpConnectionService http = new HttpConnectionService();
    private static List<Good> serverGoods;

    //фунционал покупки товара
    public static Button addToCartButton = new Button();
    public static Button deleteGood = new Button();
    public static Button buttonCountUp = new Button();
    public static Button buttonCountDown = new Button();
    public static Button buttonOfBuy = new Button();
    public static Button buttonUpdateListForAddGoods = new Button();
    public static Button buttonUpdateListForBuyGoods = new Button();
    public static Label totalAmount = new Label();

    //имя товара при добавлении магазина
    public static Label lableNameOfGoods = new Label("Имя товара");
    public static TextField nameOfGoodAddToShop = new TextField();
    //количество продукта при добавлении товара в магазин
    public static Label lableCountOfGoods = new Label("Количество");
    public static TextField countOfGoodAddToShop = new TextField();
    //цена товара при добавлении в магазин
    public static Label lablePriceOfGoods = new Label("Цена");
    public static TextField priceOfGoodAddToShop = new TextField();
    // добавить товар в магазин
    public static Button buttonAddGoods = new Button();

    //авторизация логин
    public static Label labelNickNameAndPassword = new Label("Введите ваш логин и пароль");
    public static Label labelLogin = new Label("User_Name");
    public static TextField authorizationName = new TextField();
    //авторизация пароль
    public static Label labelPassword = new Label("Password");
    public static PasswordField authorizationPassword = new PasswordField();
    public static Button buttonEnter = new Button();

    //urlResponse
    public static Button connectionUrlAddress = new Button("OK");
    public static TextField url = new TextField();

    //лист товаров при покупки
    private static ObservableList<Good> goodsCollection;
    public static TableView<Good> goodsInShop;
    //лист товаров при добавлении товара
    public static TableView<Good> viewGoodsInShop;
    //корзина
    private static ObservableList<Good> cartOfProduct = FXCollections.observableArrayList(new ArrayList<>());
    public static TableView<Good> listCarts = new TableView<>(cartOfProduct);

    private Stage windowForAddGoods = new Stage();
    private Stage authorization = new Stage();
    private Stage primaryStage = new Stage();
    private Stage urlAddress = new Stage();
    private FlowPane rootBuyGood = new FlowPane();
    private FlowPane rootAddGood = new FlowPane();
    private GridPane rootAuthorization = new GridPane();
    private FlowPane rootUrlConnection = new FlowPane();

    //    картинка java
//    private FileInputStream inputstream = new FileInputStream("C:\\java\\Java1.gif");
//    private Image image = new javafx.scene.image.Image(inputstream);
//    private ImageView javaImage = new ImageView(image);

    public AppClient() throws FileNotFoundException {
    }

    @Override
    public void start(Stage app) throws Exception {
                                        //соединение с сервером
        //urlAddress соединение, корневой узел
        connectionUrlAddress.setOnAction(localhost);
        url.setMinWidth(ProportionsConfigs.URL_WIDHT);
        RootsApp.rootUrlConnection(rootUrlConnection);

                                        //логин пароль
        //авторизация, корневой узел
        buttonEnter.setText("Login");
        buttonEnter.setOnAction(authorizationEvent);
        RootsApp.rootAuthorization(rootAuthorization);

                                       //покапка товара
        //  кнопка добавления товара в корзину
        addToCartButton.setText("Добавить товар в корзину");
        addToCartButton.setOnAction(addGoodToCartEvent);

        //удалить товар из корзины
        deleteGood.setText("x");
        deleteGood.setOnAction(deleteGoodToCartEvent);

        //увеличить количество товара в корзине
        buttonCountUp.setText("+");
        buttonCountUp.setMinWidth(ProportionsConfigs.BUTTON_UP);
        buttonCountUp.setOnAction(upCountGoodToCartEvent);

        // уменьшить количество товара в корзине
        buttonCountDown.setText("-");
        buttonCountDown.setMinWidth(ProportionsConfigs.BUTTON_DOWN);
        buttonCountDown.setOnAction(downCountGoodToCartEvent);

        // кнопка покупки
        buttonOfBuy.setText("Купить товар");
        buttonOfBuy.setOnAction(buyEvent);

        //обновить состояние
        buttonUpdateListForAddGoods.setText("Обновить");
        buttonUpdateListForAddGoods.setOnAction(updateEvent);

                                        //добавить товар в магазин
        //имя продукта при добавления товара в магазин
        nameOfGoodAddToShop.setMaxWidth(ProportionsConfigs.TEXT_FIELS_ADD_GOOD_NAME);
        //количество продукта при добавлении товара в магазин
        countOfGoodAddToShop.setMaxWidth(ProportionsConfigs.TEXT_FIELS_ADD_GOOD_COUNTandPRICE);
        //цена товара при добавлении в магазин
        priceOfGoodAddToShop.setMaxWidth(ProportionsConfigs.TEXT_FIELS_ADD_GOOD_COUNTandPRICE);
        //добавить товар в магазин
        buttonAddGoods.setText("Добавить товар");
        buttonAddGoods.setOnAction(addGoodEvent);
        //обновление состояние листов
        buttonUpdateListForBuyGoods.setText("Обновить");
        buttonUpdateListForBuyGoods.setOnAction(updateEvent);

                                        //окна приложения
        //окно для добавления товара в корзину, покупка товара
        primaryStage.setTitle("Shop(Product)");
        primaryStage.setScene(new Scene(rootBuyGood, ProportionsConfigs.SCENE_BUY_GOODS_WIDHT, ProportionsConfigs.SCENE_BUY_GOODS_HEIGHT));
        //окно для добавления товара в магазин
        windowForAddGoods.setTitle("AddGoods");
        windowForAddGoods.setScene(new Scene(rootAddGood, ProportionsConfigs.SCENE_ADD_GOODS_WIDHT, ProportionsConfigs.SCENE_ADD_GOODS_HEIGHT));
        //окно для регистрации
        authorization.setTitle("Login_Password");
        authorization.setScene(new Scene(rootAuthorization, ProportionsConfigs.SCENE_LOGIN_GOODS_WIDHT, ProportionsConfigs.SCENE_LOGIN_GOODS_HEIGHT));
        //окно для urlConnection
        urlAddress.setTitle("Put Service");
        urlAddress.setScene(new Scene(rootUrlConnection, ProportionsConfigs.SCENE_URL_CONNECTION_WIDHT, ProportionsConfigs.SCENE_URL_CONNECTION_HEIGHT));
        urlAddress.show();
    }

    private EventHandler<ActionEvent> localhost = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                HttpConnectionService.urlConnection = url.getText();
                //запрос getAll на добавление и приобретения товаров в магазин
                serverGoods = http.sendGet();
                //лист товаров в магазине
                initializeListOfGoods();
                initializeListOfGoodsAddGoods();
                //корзина
                intializeListCart();
                //открытие окон
                primaryStage.show();
                authorization.show();
                //закрытие окна urlConnection
                urlAddress.close();
                //корневой узел buyGoods
                RootsApp.rootBuyGoods(rootBuyGood);
                //корневой узел addGoods
                RootsApp.rootAddGoods(rootAddGood);

            } catch (
                    IOException e) {
                windowInformation("Неверный запрос");
            }
        }
    };

    private void initializeListOfGoods() {

        //лист товаров
        goodsCollection = FXCollections.observableArrayList(serverGoods);
        goodsInShop = new TableView<>(goodsCollection);

        TableColumn<Good, String> titleColumn = new TableColumn<>("Название");
        titleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn.setMinWidth(ProportionsConfigs.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> QuantityColumn = new TableColumn<>("Количество");
        QuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        QuantityColumn.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        //размер
        goodsInShop.setMaxWidth(ProportionsConfigs.LIST_GOODS_TO_BUY_WIDHT);
        goodsInShop.setMaxHeight(ProportionsConfigs.LIST_GOODS_TO_BUY_HEIGHT);

        // добавление в колонн
        goodsInShop.getColumns().addAll(titleColumn, QuantityColumn, priceColumn);
    }

    private void intializeListCart() {
        TableColumn<Good, String> cartTitleColumn = new TableColumn<>("Название");
        cartTitleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        cartTitleColumn.setMinWidth(ProportionsConfigs.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> cartQuantityColumn = new TableColumn<>("Количество");
        cartQuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        cartQuantityColumn.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> cartPriceColumn = new TableColumn<>("Цена");
        cartPriceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        cartPriceColumn.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        // добавление в колонн
        listCarts.getColumns().addAll(cartTitleColumn, cartQuantityColumn, cartPriceColumn);

        //размер
        listCarts.setMaxWidth(ProportionsConfigs.LIST_GOODS_TO_BUY_WIDHT);
        listCarts.setMaxHeight(ProportionsConfigs.LIST_GOODS_TO_BUY_HEIGHT);
    }

    private EventHandler<ActionEvent> addGoodToCartEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            if (goodsInShop.getSelectionModel().getSelectedItem() == null) {
                windowInformation("Выберите товар");

            } else {
                Good good = goodsInShop.getSelectionModel().getSelectedItem();
                Good newGood = new Good(good.name, 1, good.price);
                boolean flag = false;
                for (Good good1 : listCarts.getItems()) {
                    if (Objects.equals(good1.name, newGood.name)) {
                        good1.count++;
                        update.totalSum(totalAmount, listCarts);
                        listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                        flag = true;
                    }
                }
                if (!flag) {
                    cartOfProduct.add(newGood);
                    update.totalSum(totalAmount, listCarts);
                }
            }
        }
    };

    private EventHandler<ActionEvent> upCountGoodToCartEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (listCarts.getSelectionModel().getSelectedItem() == null) {
                windowInformation("Не выбран товар для изменения количества");
            } else {
                listCarts.getSelectionModel().getSelectedItem().count++;
                update.totalSum(totalAmount, listCarts);
                listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
            }
        }
    };

    private EventHandler<ActionEvent> downCountGoodToCartEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            boolean checkCount = false;
            if (listCarts.getSelectionModel().getSelectedItem() == null) {
                windowInformation("Не выбран товар для изменения количества");
                checkCount = true;
            }
            if (!checkCount) {
                listCarts.getSelectionModel().getSelectedItem().count--;
                listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                update.totalSum(totalAmount, listCarts);
            }
            if ((!checkCount) && listCarts.getSelectionModel().getSelectedItem().count == 0) {
                cartOfProduct.remove(listCarts.getSelectionModel().getSelectedItem());
                update.totalSum(totalAmount, listCarts);
            }
        }
    };

    private EventHandler<ActionEvent> deleteGoodToCartEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (listCarts.getSelectionModel().getSelectedItem() == null) {
                cartOfProduct.removeAll(cartOfProduct);
            } else {
                int row = listCarts.getSelectionModel().getSelectedIndex();
                listCarts.getItems().remove(row);
                update.totalSum(totalAmount, listCarts);
                listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
            }
        }
    };

    private EventHandler<ActionEvent> buyEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            boolean check = false;

            List<Good> productInShop = update.changeList(goodsInShop);
            List<Good> productInCart = update.changeList(listCarts);

            if (cartOfProduct.isEmpty()) {
                windowInformation("В корзине нет товара");
                check = true;
            }

            for (Good goodInCart : productInCart) {
                Good good = update.findByName(productInShop, goodInCart.name);
                if (good.count < goodInCart.count) {
                    check = true;
                    windowInformation("Нет такого количества");
                    try {
                        update.updateOfGoods(goodsCollection, productInShop);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (!check) {
                try {
                    String json = mapper.writeValueAsString(productInCart);
                    http.sendPost(HttpConnectionService.urlConnection.concat("buyGoods"), json, http.accessToken);
                    windowInformation("Товар успешно приобретен");
                    cartOfProduct.removeAll(cartOfProduct);
                    update.afterBuyOfGoods(totalAmount);
                    update.updateOfGoods(goodsCollection, productInShop);

                } catch (IOException e) {
                    windowInformation("Нет такого количества");
                }
                try {
                    update.updateOfGoods(goodsCollection, productInShop);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    private void initializeListOfGoodsAddGoods() {

        //лист товаров
        viewGoodsInShop = new TableView<>(goodsCollection);

        TableColumn<Good, String> titleColumn1 = new TableColumn<Good, String>("Название");
        titleColumn1.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn1.setMinWidth(ProportionsConfigs.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> countColumn2 = new TableColumn<Good, String>("Количество");
        countColumn2.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        countColumn2.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> priceColumn3 = new TableColumn<Good, String>("Цена");
        priceColumn3.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn3.setMaxWidth(ProportionsConfigs.COLUMN_WIDTH_COUNTandPRICE);

        // добавление в колонны
        viewGoodsInShop.getColumns().addAll(titleColumn1, countColumn2, priceColumn3);

        viewGoodsInShop.setMaxWidth(ProportionsConfigs.LIST_GOODS_TO_ADD_WIDHT);
        viewGoodsInShop.setMaxHeight(ProportionsConfigs.LIST_GOODS_TO_ADD_HEIGHT);
    }

    private EventHandler<ActionEvent> addGoodEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {

            String nameOfGood = nameOfGoodAddToShop.getText();
            int countOfGood = Integer.parseInt(countOfGoodAddToShop.getText());
            int priceOfGood = Integer.parseInt(priceOfGoodAddToShop.getText());

            Good addGood = new Good(nameOfGood, countOfGood, priceOfGood);

            List<Good> addProduct = new ArrayList<>();
            addProduct.add(addGood);

            try {
                String json = mapper.writeValueAsString(addProduct);
                http.sendPost(HttpConnectionService.urlConnection.concat("addGoods"), json, http.accessToken);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private EventHandler<ActionEvent> updateEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                update.updateOfGoods(goodsCollection, serverGoods);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private EventHandler<ActionEvent> authorizationEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            boolean check = true;
            String nameAccount = authorizationName.getText();
            String passwordAccount = authorizationPassword.getText();
            Account account = new Account(nameAccount, passwordAccount);

            try {
                String json = mapper.writeValueAsString(account);
                http.sendPost(HttpConnectionService.urlConnection.concat("authorization"), json, http.accessToken);
            } catch (IOException e) {
                windowInformation("Неверный логин или пароль");
                check = false;
            }
            if (check) {
                authorization.close();
                windowForAddGoods.show();
            }
        }
    };

    private void windowInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}