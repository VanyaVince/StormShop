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
    private static final ShopService update = new ShopService();
    private static final AdminService rootsPanel = new AdminService();
    private static final HttpConnectionService http = new HttpConnectionService();
    private static List<Good> serverGoods = new ArrayList<>();

    //фунционал покупки товара
    private static Button addToCartButton = new Button();
    private static Button deleteGood = new Button();
    private static Button buttonCountUp = new Button();
    private static Button buttonCountDown = new Button();
    private static Button buttonOfBuy = new Button();
    private static Button buttonUpdateListForAddGoods = new Button();
    private static Button buttonUpdateListForBuyGoods = new Button();
    private static Label totalAmount = new Label();

    //функционал добавления товара
    private static Label lableNameOfGoods = new Label("Имя товара");
    private static TextField nameOfGoodAddToShop = new TextField();
    private static Label lableCountOfGoods = new Label("Количество");
    private static TextField countOfGoodAddToShop = new TextField();
    private static Label lablePriceOfGoods = new Label("Цена");
    private static TextField priceOfGoodAddToShop = new TextField();
    private static Button buttonAddGoods = new Button();

    //авторизация
    private static Label labelNickNameAndPassword = new Label("Введите ваш логин и пароль");
    private static Label labelLogin = new Label("User Name");
    private static TextField authorizationName = new TextField();
    private static Label labelPassword = new Label("Password");
    private static PasswordField authorizationPassword = new PasswordField();
    private static Button buttonEnter = new Button();

    //urlResponse
    private static Button connectionUrlAddress = new Button("OK");
    private static TextField url = new TextField("http://localhost:4567");
    private static Label labelServerUrlResponse = new Label("Server");

    //лист товаров при покупки
    private static ObservableList<Good> goodsCollection;
    private static TableView<Good> goodsInShop;
    //лист товаров при добавлении товара
    private static TableView<Good> viewGoodsInShop;
    //корзина
    private static ObservableList<Good> cartOfProduct = FXCollections.observableArrayList(new ArrayList<>());
    private static TableView<Good> listCarts = new TableView<>(cartOfProduct);

    private Stage windowForAddGoods = new Stage();
    private Stage authorization = new Stage();
    private Stage primaryStage = new Stage();
    private Stage urlAddress = new Stage();
    private FlowPane rootBuyGood = new FlowPane();
    private FlowPane rootAddGood = new FlowPane();
    private GridPane rootAuthorization = new GridPane();
    private FlowPane rootUrlConnection = new FlowPane();

    public AppClient() throws FileNotFoundException {
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage app) throws Exception {
        connectToServer();
        setAuthRootPane();
        buyButtons();
        addGoods();
        windowsApplication();
    }

    private void connectToServer() {
        //urlAddress соединение, корневой узел
        connectionUrlAddress.setOnAction(localhost);
        url.setMinWidth(ProportionsConfigs.URL_WIDHT);
        rootsPanel.rootUrlConnection(rootUrlConnection);
    }

    private void setAuthRootPane() {
        //логин пароль
        //авторизация, корневой узел
        buttonEnter.setText("Login");
        buttonEnter.setOnAction(authorizationEvent);
        rootsPanel.rootAuthorization(rootAuthorization);
    }

    private void buyButtons() {

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
    }

    private void addGoods() {
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
    }

    private void windowsApplication() {
        //окна приложения
        //окно для добавления товара в корзину, покупка товара
        primaryStage.setTitle("Buy goods");
        primaryStage.setScene(new Scene(rootBuyGood, ProportionsConfigs.SCENE_BUY_GOODS_WIDHT, ProportionsConfigs.SCENE_BUY_GOODS_HEIGHT));
        //окно для добавления товара в магазин
        windowForAddGoods.setTitle("Add goods");
        windowForAddGoods.setScene(new Scene(rootAddGood, ProportionsConfigs.SCENE_ADD_GOODS_WIDHT, ProportionsConfigs.SCENE_ADD_GOODS_HEIGHT));
        //окно для регистрации
        authorization.setTitle("Authorization");
        authorization.setScene(new Scene(rootAuthorization, ProportionsConfigs.SCENE_LOGIN_GOODS_WIDHT, ProportionsConfigs.SCENE_LOGIN_GOODS_HEIGHT));
        //окно для urlConnection
        urlAddress.setTitle("Url response");
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
                rootsPanel.rootBuyGoods(rootBuyGood);
                //корневой узел addGoods
                rootsPanel.rootAddGoods(rootAddGood);

            } catch (
                    IOException e) {
                windowError("Неверный запрос");
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
                windowWaring("Выберите товар");

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
                windowWaring("Не выбран товар для изменения количества");
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
                windowWaring("Не выбран товар для изменения количества");
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
                cartOfProduct.clear();
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
                windowWaring("В корзине нет товара");
                check = true;
            }

            for (Good goodInCart : productInCart) {
                Good good = update.findByName(productInShop, goodInCart.name);
                if (good.count < goodInCart.count) {
                    check = true;
                    windowError("Нет такого количества");
                    try {
                        serverGoods = http.sendGet();
                        update.updateOfGoods(goodsCollection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }

            if (!check) {
                try {
                    String json = mapper.writeValueAsString(productInCart);
                    http.sendPost(HttpConnectionService.urlConnection.concat("/buyGoods"), json, http.accessToken);
                    windowInformation("Товар успешно приобретен");
                    cartOfProduct.clear();
                    update.afterBuyOfGoods(totalAmount);
                    serverGoods = http.sendGet();
                    update.updateOfGoods(goodsCollection);

                } catch (IOException e) {
                    windowError("Нет такого количества");
                }
                try {
                    serverGoods = http.sendGet();
                    update.updateOfGoods(goodsCollection);
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

            try {
                String nameOfGood = nameOfGoodAddToShop.getText();
                int countOfGood = Integer.parseInt(countOfGoodAddToShop.getText());
                int priceOfGood = Integer.parseInt(priceOfGoodAddToShop.getText());

                Good addGood = new Good(nameOfGood, countOfGood, priceOfGood);

                List<Good> addProduct = new ArrayList<>();
                addProduct.add(addGood);

                String json = mapper.writeValueAsString(addProduct);
                http.sendPost(HttpConnectionService.urlConnection.concat("/addGoods"), json, http.accessToken);
                windowInformation("Товар успешно добавлен");
                serverGoods = http.sendGet();
                update.updateOfGoods(goodsCollection);
            } catch (Exception e) {
                windowError("Введен неверный формат данных");
            }
        }
    };

    private EventHandler<ActionEvent> updateEvent = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            try {
                serverGoods = http.sendGet();
                update.updateOfGoods(goodsCollection);
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
                http.sendPost(HttpConnectionService.urlConnection.concat("/authorization"), json, http.accessToken);
            } catch (IOException e) {
                windowError("Неверный логин или пароль");
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
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void windowError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void windowWaring(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static Button getAddToCartButton() {
        return addToCartButton;
    }

    public static Button getDeleteGood() {
        return deleteGood;
    }

    public static Button getButtonCountUp() {
        return buttonCountUp;
    }

    public static Button getButtonCountDown() {
        return buttonCountDown;
    }

    public static Button getButtonOfBuy() {
        return buttonOfBuy;
    }

    public static Button getButtonUpdateListForAddGoods() {
        return buttonUpdateListForAddGoods;
    }

    public static Button getButtonUpdateListForBuyGoods() {
        return buttonUpdateListForBuyGoods;
    }

    public static Label getTotalAmount() {
        return totalAmount;
    }

    public static Label getLableNameOfGoods() {
        return lableNameOfGoods;
    }

    public static TextField getNameOfGoodAddToShop() {
        return nameOfGoodAddToShop;
    }

    public static Label getLableCountOfGoods() {
        return lableCountOfGoods;
    }

    public static TextField getCountOfGoodAddToShop() {
        return countOfGoodAddToShop;
    }

    public static Label getLablePriceOfGoods() {
        return lablePriceOfGoods;
    }

    public static TextField getPriceOfGoodAddToShop() {
        return priceOfGoodAddToShop;
    }

    public static Button getButtonAddGoods() {
        return buttonAddGoods;
    }

    public static Label getLabelNickNameAndPassword() {
        return labelNickNameAndPassword;
    }

    public static Label getLabelLogin() {
        return labelLogin;
    }

    public static TextField getAuthorizationName() {
        return authorizationName;
    }

    public static Label getLabelPassword() {
        return labelPassword;
    }

    public static PasswordField getAuthorizationPassword() {
        return authorizationPassword;
    }

    public static Button getButtonEnter() {
        return buttonEnter;
    }

    public static Button getConnectionUrlAddress() {
        return connectionUrlAddress;
    }

    public static TextField getUrl() {
        return url;
    }

    public static Label getLabelServerUrlResponse() {
        return labelServerUrlResponse;
    }

    public static List<Good> getServerGoods() {
        return serverGoods;
    }

    public static TableView<Good> getGoodsInShop() {
        return goodsInShop;
    }

    public static TableView<Good> getViewGoodsInShop() {
        return viewGoodsInShop;
    }

    public static TableView<Good> getListCarts() {
        return listCarts;
    }
}