package org.o7planning.javafx.listview;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;
import org.o7planning.javafx.model.*;
import org.o7planning.javafx.service.ConnectionHTTP;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppClient extends Application {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final fromService update = new fromService();
    private static final ConnectionHTTP http = new ConnectionHTTP();
    private Label totalAmount = new Label();
    private List<Good> serverGoods;

    //    картинка java
    private FileInputStream inputstream = new FileInputStream("C:\\java\\Java1.gif");
    private Image image = new javafx.scene.image.Image(inputstream);
    private ImageView javaImage = new ImageView(image);
    public AppClient() throws FileNotFoundException {
    }

    //лист товаров при покупки
    private ObservableList<Good> goodsCollection;
    private TableView<Good> goodsInShop;

    //лист товаров при добавлении товара
    private TableView<Good> viewGoodsInShop;

    //корзина
    private ObservableList<Good> cartOfProduct = FXCollections.observableArrayList(new ArrayList<>());
    private TableView<Good> listCarts = new TableView<>(cartOfProduct);

    //имя товара при добавлении магазина
    private TextField nameOfGoodAddToShop;
    //количество продукта при добавлении товара в магазин
    private TextField countOfGoodAddToShop;
    //цена товара при добавлении в магазин
    private TextField priceOfGoodAddToShop;
    //авторизация логин
    private TextField authorizationName;
    //авторизация пароль
    private PasswordField authorizationPassword;
    //textFieldAccessToken

    private Stage windowForAddGoods = new Stage();
    private Stage authorization = new Stage();

    @Override
    public void start(Stage primaryStage) throws Exception {

        //запрос getAll на добавление и приобретения товаров в магазин
        serverGoods = http.sendGet();

        //лист товаров в магазине
        initializeListOfGoods();

        //корзина
        intializeListCart();

        //  кнопка добавления товара в корзину
        Button addToCartButton = new Button();
        addToCartButton.setText("Добавить товар в корзину");
        addToCartButton.setOnAction(addGoodToCartEvent);

        //удалить товар из корзины
        Button deleteGood = new Button();
        deleteGood.setText("x");
        deleteGood.setOnAction(deleteGoodToCartEvent);

        //увеличить количество товара в корзине
        Button buttonCountUp = new Button();
        buttonCountUp.setText("+");
        buttonCountUp.setMinWidth(Proportions.BUTTON_UP);
        buttonCountUp.setOnAction(upCountGoodToCartEvent);

        // уменьшить количество товара в корзине
        Button buttonCountDown = new Button();
        buttonCountDown.setText("-");
        buttonCountDown.setMinWidth(Proportions.BUTTON_DOWN);
        buttonCountDown.setOnAction(downCountGoodToCartEvent);

        // кнопка покупки
        Button buttonOfBuy = new Button();
        buttonOfBuy.setText("Купить товар");
        buttonOfBuy.setOnAction(buyEvent);

        //обновить состояние
        Button buttonUpdateListForAddGoods = new Button();
        buttonUpdateListForAddGoods.setText("Обновить");
        buttonUpdateListForAddGoods.setOnAction(updateEvent);

        ///////////////////////////////////////////////// добавить товар в магазин ///////////////////////////////////////

        initializeListOfGoodsAddGoods();

        //окна, для добавления товара в магазин
        Label lableNameOfGoods = new Label("Имя товара");
        Label lableCountOfGoods = new Label("Количество");
        Label lablePriceOfGoods = new Label("Цена");

        //имя продукта при добавления товара в магазин
        nameOfGoodAddToShop = new TextField();
        nameOfGoodAddToShop.setMaxWidth(Proportions.TEXT_FIELS_ADD_GOOD_NAME);
        //количество продукта при добавлении товара в магазин
        countOfGoodAddToShop = new TextField();
        countOfGoodAddToShop.setMaxWidth(Proportions.TEXT_FIELS_ADD_GOOD_COUNTandPRICE);
        //цена товара при добавлении в магазин
        priceOfGoodAddToShop = new TextField();
        priceOfGoodAddToShop.setMaxWidth(Proportions.TEXT_FIELS_ADD_GOOD_COUNTandPRICE);

        //добавить товар в магазин
        Button buttonAddGoods = new Button();
        buttonAddGoods.setText("Добавить товар");
        buttonAddGoods.setOnAction(addGoodEvent);

        //обновление состояние листов
        Button buttonUpdateListForBuyGoods = new Button();
        buttonUpdateListForBuyGoods.setText("Обновить");
        buttonUpdateListForBuyGoods.setOnAction(updateEvent);

        /////////////////////////////////////////////////////авторизация///////////////////////////////////////////////////
        Label labelNickNameAndPassword = new Label("Введите ваш логин и пароль");
        Label labelLogin = new Label("User_Name");
        authorizationName = new TextField();
        Label labelPassword = new Label("Password");
        authorizationPassword = new PasswordField();
        Button buttonEnter = new Button();
        buttonEnter.setText("Login");
        buttonEnter.setOnAction(authorizationEvent);

        ////////////////////////////// корневой узел getAll, addToCart, buy///////////////////////////////
        FlowPane root = new FlowPane();
        root.setPadding(new Insets(Proportions.PADDING));
        root.setHgap(Proportions.ROOT_H_GAP);
        root.setVgap(Proportions.ROOT_V_GAP);
        root.getChildren().add(goodsInShop);
        root.getChildren().add(javaImage);
        javaImage.setFitHeight(Proportions.IMAGE_HEIGHT);
        javaImage.setFitWidth(Proportions.IMAGE_WIDTH);
        root.getChildren().add(buttonUpdateListForBuyGoods);
        root.getChildren().add(addToCartButton);
        root.getChildren().add(listCarts);
        root.getChildren().add(buttonCountUp);
        root.getChildren().add(buttonCountDown);
        root.getChildren().add(deleteGood);
        root.getChildren().add(totalAmount);
        root.getChildren().add(buttonOfBuy);

        ///////////////////////////////корневой узел addGoods///////////////////////////////////////

        FlowPane root1 = new FlowPane();
        root1.setPadding(new Insets(Proportions.PADDING));
        root1.setVgap(Proportions.ROOT1_V_GAP);
        root1.setHgap(Proportions.ROOT1_H_GAP);

        //ввод данных для добавления товара
        root1.getChildren().add(lableNameOfGoods);
        root1.getChildren().add(nameOfGoodAddToShop);
        root1.getChildren().add(lableCountOfGoods);
        root1.getChildren().add(countOfGoodAddToShop);
        root1.getChildren().add(lablePriceOfGoods);
        root1.getChildren().add(priceOfGoodAddToShop);

        //состояние в магазине, кнопка добавить товар, обновить
        root1.getChildren().add(viewGoodsInShop);
        root1.getChildren().add(buttonAddGoods);
        root1.getChildren().add(buttonUpdateListForAddGoods);

        /////////////////////////////корневой узел авторизации//////////////////////////////////////

        GridPane root2 = new GridPane();
        root2.setPadding(new Insets(Proportions.PADDING));
        root2.setHgap(Proportions.ROOT2_H_GAP);
        root2.setVgap(Proportions.ROOT2_V_GAP);
        root2.add(labelNickNameAndPassword, 0, 0, 2, 1);
        GridPane.setHalignment(labelNickNameAndPassword, HPos.CENTER);
        root2.add(labelLogin, 0, 1);
        root2.add(authorizationName, 1, 1);
        root2.add(labelPassword, 0, 2);
        root2.add(authorizationPassword, 1, 2);
        root2.add(buttonEnter, 1, 3);
        GridPane.setHalignment(buttonEnter, HPos.RIGHT);

        //окно для добавления товара в корзину, покупка товара
        primaryStage.setTitle("Shop(Product)");
        primaryStage.setScene(new Scene(root, Proportions.SCENE_BUY_GOODS_WIDHT, Proportions.SCENE_BUY_GOODS_HEIGHT));
        primaryStage.show();

        //окно для добавления товара в магазин
        windowForAddGoods.setTitle("AddGoods");
        windowForAddGoods.setScene(new Scene(root1, Proportions.SCENE_ADD_GOODS_WIDHT, Proportions.SCENE_ADD_GOODS_HEIGHT));

        //окно для регистрации
        authorization.setTitle("Login_Password");
        authorization.setScene(new Scene(root2, Proportions.SCENE_LOGIN_GOODS_WIDHT, Proportions.SCENE_LOGIN_GOODS_HEIGHT));
        authorization.show();
    }

    private void initializeListOfGoods() {

        //лист товаров
        goodsCollection = FXCollections.observableArrayList(serverGoods);
        goodsInShop = new TableView<>(goodsCollection);

        TableColumn<Good, String> titleColumn = new TableColumn<>("Название");
        titleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn.setMinWidth(Proportions.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> QuantityColumn = new TableColumn<>("Количество");
        QuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        QuantityColumn.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> priceColumn = new TableColumn<>("Цена");
        priceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        //размер
        goodsInShop.setMaxWidth(Proportions.LIST_GOODS_TO_BUY_WIDHT);
        goodsInShop.setMaxHeight(Proportions.LIST_GOODS_TO_BUY_HEIGHT);

        // добавление в колонн
        goodsInShop.getColumns().addAll(titleColumn, QuantityColumn, priceColumn);
    }

    private void intializeListCart() {
        TableColumn<Good, String> cartTitleColumn = new TableColumn<>("Название");
        cartTitleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        cartTitleColumn.setMinWidth(Proportions.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> cartQuantityColumn = new TableColumn<>("Количество");
        cartQuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        cartQuantityColumn.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> cartPriceColumn = new TableColumn<>("Цена");
        cartPriceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        cartPriceColumn.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        // добавление в колонн
        listCarts.getColumns().addAll(cartTitleColumn, cartQuantityColumn, cartPriceColumn);

        //размер
        listCarts.setMaxWidth(Proportions.LIST_GOODS_TO_BUY_WIDHT);
        listCarts.setMaxHeight(Proportions.LIST_GOODS_TO_BUY_HEIGHT);
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
                    http.sendPost("http://localhost:4567/buyGoods", json, http.accessToken);
                    windowInformation("Товар успешно приобретен");
                    cartOfProduct.removeAll(cartOfProduct);
                    update.afterBuyOfGoods(totalAmount);
                    update.updateOfGoods(goodsCollection, productInShop);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void initializeListOfGoodsAddGoods() {

        //лист товаров
        viewGoodsInShop = new TableView<>(goodsCollection);

        TableColumn<Good, String> titleColumn1 = new TableColumn<Good, String>("Название");
        titleColumn1.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn1.setMinWidth(Proportions.COLUMN_WIDTH_NAME);

        TableColumn<Good, String> countColumn2 = new TableColumn<Good, String>("Количество");
        countColumn2.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        countColumn2.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        TableColumn<Good, String> priceColumn3 = new TableColumn<Good, String>("Цена");
        priceColumn3.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn3.setMaxWidth(Proportions.COLUMN_WIDTH_COUNTandPRICE);

        // добавление в колонны
        viewGoodsInShop.getColumns().addAll(titleColumn1, countColumn2, priceColumn3);

        viewGoodsInShop.setMaxWidth(Proportions.LIST_GOODS_TO_ADD_WIDHT);
        viewGoodsInShop.setMaxHeight(Proportions.LIST_GOODS_TO_ADD_HEIGHT);
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
                http.sendPost("http://localhost:4567/addGoods", json, http.accessToken);
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
            String nameAccount = authorizationName.getText();
            String passwordAccount = authorizationPassword.getText();
            Account account = new Account(nameAccount, passwordAccount);

            try {
                String json = mapper.writeValueAsString(account);
                http.sendPost("http://localhost:4567/authorization", json, http.accessToken);
            } catch (IOException e) {
                windowInformation("Неверный логин или пароль");
            }
            if (!(http.accessToken == null)) {
                authorization.close();
                windowForAddGoods.show();
            }
        }
    };

    public void windowInformation(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}