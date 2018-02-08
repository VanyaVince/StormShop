package org.o7planning.javafx.listview;

import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;
import org.o7planning.javafx.model.ConnectionHTTP;
import org.o7planning.javafx.model.Good;
import org.o7planning.javafx.model.Update;

import java.util.ArrayList;

import java.util.List;

public class AppClient extends Application {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Update update = new Update();
    private static final ConnectionHTTP HTTP = new ConnectionHTTP();

    private Label totalAmount = new Label();
    private List<Good> serverGoods;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage windowForAddGoods = new Stage();

        //запрос getAll на добавление и приобретения товаров в магазин
        serverGoods = HTTP.getAll();

        //лист товаров
        ObservableList<Good> goodsCollection = FXCollections.observableArrayList(serverGoods);
        final TableView<Good> goodsInShop = new TableView<>(goodsCollection);

        TableColumn<Good, String> titleColumn = new TableColumn<Good, String>("Название");
        titleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn.setMinWidth(200);

        TableColumn<Good, String> priceColumn = new TableColumn<Good, String>("Цена");
        priceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn.setMaxWidth(50);

        //размер
        goodsInShop.setMinWidth(250);
        goodsInShop.setMaxHeight(90);

        // добавление в колонны
        goodsInShop.getColumns().addAll(titleColumn, priceColumn);

        // количество товаров для добавления в корзину
        TextField quantityInput = new TextField("1");
        quantityInput.setMaxWidth(40);

        //корзина
        ObservableList<Good> cartOfProduct = FXCollections.observableArrayList(new ArrayList<>());
        final TableView<Good> listCarts = new TableView<>(cartOfProduct);



        TableColumn<Good, String> cartTitleColumn = new TableColumn<Good, String>("Название");
        cartTitleColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        cartTitleColumn.setMinWidth(150);

        TableColumn<Good, String> cartQuantityColumn = new TableColumn<Good, String>("Количество");
        cartQuantityColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        cartQuantityColumn.setMaxWidth(50);

        TableColumn<Good, String> cartPriceColumn = new TableColumn<Good, String>("Цена");
        cartPriceColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        cartPriceColumn.setMaxWidth(50);

        //размер
        listCarts.setMaxWidth(282.5);
        listCarts.setMaxHeight(90);

        // добавление в колонны
        listCarts.getColumns().addAll(cartTitleColumn, cartQuantityColumn, cartPriceColumn);

        //  кнопка добавления товара в корзину //
        Button addToCartButton = new Button();
        addToCartButton.setText("Добавить товар в корзину");

        addToCartButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println(listGood.getSelectionModel().getSelectedItem());
                if (goodsInShop.getSelectionModel().getSelectedItem() == null) {
                    windowAfterActBuy("Не выбран товар для изменения количества");

                } else {
                    //    int quantity = Integer.parseInt(quantityInput.getText());
                    //    GoodInCart good = new GoodInCart(goodsInShop.getSelectionModel().getSelectedItem(), quantity);
                    Good good = goodsInShop.getSelectionModel().getSelectedItem();
                    Good newGood = new Good(good.name, 1, good.price);
                    boolean flag = false;
                    for (Good good1 : listCarts.getItems()) {
                        if (good1.name == newGood.name) {
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
        });
        //увеличить количество товара в корзине
        Button buttonCountUp = new Button();
        buttonCountUp.setText("+");
        buttonCountUp.setMinWidth(20);

        buttonCountUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (listCarts.getSelectionModel().getSelectedItem() == null) {
                    windowAfterActBuy("Не выбран товар для изменения количества");
                } else {
                    listCarts.getSelectionModel().getSelectedItem().count++;
                    update.totalSum(totalAmount, listCarts);
                    listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                }
            }
        });
        // уменьшить количество товара в корзине
        Button buttonCountDown = new Button();
        buttonCountDown.setText("-");
        buttonCountDown.setMinWidth(24);

        buttonCountDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean checkCount = false;
                if (listCarts.getSelectionModel().getSelectedItem() == null) {
                    windowAfterActBuy("Не выбран товар для изменения количества");
                    checkCount = true;
                }
//                if ((!checkCount) && listCarts.getSelectionModel().getSelectedItem().count == 0) {
//                    cartOfProduct.remove(listCarts.getSelectionModel().getSelectedItem());
//                    listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
//                    checkCount = true;
//                }
                if (!checkCount) {
                    listCarts.getSelectionModel().getSelectedItem().count--;
                    listCarts.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                    update.totalSum(totalAmount, listCarts);
                }
                if ((!checkCount) && listCarts.getSelectionModel().getSelectedItem().count == 0) {
                    cartOfProduct.remove(listCarts.getSelectionModel().getSelectedItem());
                    update.totalSum(totalAmount, listCarts);
                }
                //listCarts.getColumns().get(1).setVisible(false);
                //listCarts.getColumns().get(1).setVisible(true);
            }
        });

        // кнопка покупки //
        Button buttonOfBuy = new Button();
        buttonOfBuy.setText("Купить товар");

        buttonOfBuy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean check = false;

                List<Good> productInShop = update.changeList(goodsInShop);
                List<Good> productInCart = update.changeList(listCarts);

                for (Good goodInCart : productInCart) {
                    Good good = update.findByName(productInShop, goodInCart.name);
                    if (good.count < goodInCart.count) {
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    try {
                        HTTP.sendPost(productInCart);
                        windowAfterActBuy("Товар успешно приобретен");
                        cartOfProduct.removeAll(cartOfProduct);
                        update.afterBuyOfGoods(totalAmount);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    windowAfterActBuy("Нет такого количества");
                }
            }
        });
        ////////////////////////////// добавить товар в магазин ///////////////////////////////////////
        // запрос по getAll, для добавления товара в магазин

        //лист товаров
        ObservableList<Good> productCollection = FXCollections.observableArrayList(serverGoods);
        final TableView<Good> viewGoodsInShop = new TableView<>(productCollection);

        TableColumn<Good, String> titleColumn1 = new TableColumn<Good, String>("Название");
        titleColumn1.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().nameString()));
        titleColumn1.setMinWidth(130);

        TableColumn<Good, String> countColumn2 = new TableColumn<Good, String>("Количество");
        countColumn2.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().countString()));
        countColumn2.setMaxWidth(50);

        TableColumn<Good, String> priceColumn3 = new TableColumn<Good, String>("Цена");
        priceColumn3.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().priceString()));
        priceColumn3.setMaxWidth(50);

        // добавление в колонны
        viewGoodsInShop.getColumns().addAll(titleColumn1, countColumn2, priceColumn3);

        viewGoodsInShop.setMaxWidth(250);
        viewGoodsInShop.setMaxHeight(130);

        //название текст окн, для добавления товара в магазин
        Label lableNameOfGoods = new Label("Имя товара");
        //lableNameOfGoods.setMinHeight(60);
        Label lableCountOfGoods = new Label("Количество");
        //lableCountOfGoods.setMaxHeight(30);
        Label lablePriceOfGoods = new Label("Цена");
        //lablePriceOfGoods.setMaxHeight(30);

        //имя продукта при добавления товара в магазин
        TextField nameOfGoodAddToShop = new TextField("");
        nameOfGoodAddToShop.setMaxWidth(90);
        //количество продукта при добавлении товара в магазин
        TextField countOfGoodAddToShop = new TextField("");
        countOfGoodAddToShop.setMaxWidth(40);
        //цена товара при добавлении в магазин
        TextField priceOfGoodAddToShop = new TextField("");
        priceOfGoodAddToShop.setMaxWidth(50);

        //добавить товар в магазин
        Button buttonAddGoods = new Button();
        buttonAddGoods.setText("Добавить товар");

        buttonAddGoods.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nameOfGood = nameOfGoodAddToShop.getText();
                int countOfGood = Integer.parseInt(countOfGoodAddToShop.getText());
                int priceOfGood = Integer.parseInt(priceOfGoodAddToShop.getText());

                Good addGood = new Good(nameOfGood, countOfGood, priceOfGood);
                List<Good> addProduct = new ArrayList<>();
                addProduct.add(addGood);
                try {
                    HTTP.sendPost(addProduct);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ///////////// не работает ///////////////////
        Button buttonUpdateList = new Button();
        buttonUpdateList.setText("Обновить");

        buttonUpdateList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    serverGoods = HTTP.getAll();
//                    ObservableList<Good> productCollection = FXCollections.observableArrayList(serverGoods);
//                    final TableView<Good> viewGoodsInShop = new TableView<>(productCollection);
                    viewGoodsInShop.getProperties().put(TableViewSkinBase.RECREATE, Boolean.TRUE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        ////////////////////////////// корневой узел getAll, addToCart, buy///////////////////////////////
        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));

        root.getChildren().add(goodsInShop);
        root.getChildren().add(addToCartButton);
        root.getChildren().add(listCarts);
        root.getChildren().add(buttonCountUp);
        root.getChildren().add(buttonCountDown);
        root.getChildren().add(totalAmount);
        root.getChildren().add(buttonOfBuy);

        ///////////////////////////////корневой узел addGoods///////////////////////////////////////
        FlowPane root1 = new FlowPane();
        root1.setPadding(new Insets(10));
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
        root1.getChildren().add(buttonUpdateList);

        //окно для добавления товара в магазин, покупка товара
        primaryStage.setTitle("Shop(Product)");
        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
        //окно для добавления товара в магазин
        windowForAddGoods.setTitle("AddGoods");
        windowForAddGoods.setScene(new Scene(root1, 400, 250));
        //windowForAddGoods.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    private void windowAfterActBuy(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");

        // alert.setHeaderText("Results:");
        alert.setContentText(message);

        alert.showAndWait();
    }
}
