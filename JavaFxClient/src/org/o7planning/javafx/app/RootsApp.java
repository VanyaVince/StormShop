package org.o7planning.javafx.app;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import org.o7planning.javafx.app.AppClient;
import org.o7planning.javafx.app.ProportionsConfigs;

public class RootsApp {

    public static void rootBuyGoods(FlowPane root) {
        root.setPadding(new Insets(ProportionsConfigs.PADDING));
        root.setHgap(ProportionsConfigs.ROOT_H_GAP);
        root.setVgap(ProportionsConfigs.ROOT_V_GAP);
        root.getChildren().add(AppClient.goodsInShop);
//        root.getChildren().add(javaImage);
//        AppClient.javaImage.setFitHeight(ProportionsConfigs.IMAGE_HEIGHT);
//        javaImage.setFitWidth(ProportionsConfigs.IMAGE_WIDTH);
        root.getChildren().add(AppClient.buttonUpdateListForBuyGoods);
        root.getChildren().add(AppClient.addToCartButton);
        root.getChildren().add(AppClient.listCarts);
        root.getChildren().add(AppClient.buttonCountUp);
        root.getChildren().add(AppClient.buttonCountDown);
        root.getChildren().add(AppClient.deleteGood);
        root.getChildren().add(AppClient.totalAmount);
        root.getChildren().add(AppClient.buttonOfBuy);
    }

    public static void rootAddGoods(FlowPane root1) {
        root1.setPadding(new Insets(ProportionsConfigs.PADDING));
        root1.setVgap(ProportionsConfigs.ROOT1_V_GAP);
        root1.setHgap(ProportionsConfigs.ROOT1_H_GAP);

        //ввод данных для добавления товара
        root1.getChildren().add(AppClient.lableNameOfGoods);
        root1.getChildren().add(AppClient.nameOfGoodAddToShop);
        root1.getChildren().add(AppClient.lableCountOfGoods);
        root1.getChildren().add(AppClient.countOfGoodAddToShop);
        root1.getChildren().add(AppClient.lablePriceOfGoods);
        root1.getChildren().add(AppClient.priceOfGoodAddToShop);

        //состояние в магазине, кнопка добавить товар, обновить
        root1.getChildren().add(AppClient.viewGoodsInShop);
        root1.getChildren().add(AppClient.buttonAddGoods);
        root1.getChildren().add(AppClient.buttonUpdateListForAddGoods);

    }

    public static void rootAuthorization(GridPane root2) {
        root2.setPadding(new Insets(ProportionsConfigs.PADDING));
        root2.setHgap(ProportionsConfigs.ROOT2_H_GAP);
        root2.setVgap(ProportionsConfigs.ROOT2_V_GAP);
        root2.add(AppClient.labelNickNameAndPassword, 0, 0, 2, 1);
        GridPane.setHalignment(AppClient.labelNickNameAndPassword, HPos.CENTER);
        root2.add(AppClient.labelLogin, 0, 1);
        root2.add(AppClient.authorizationName, 1, 1);
        root2.add(AppClient.labelPassword, 0, 2);
        root2.add(AppClient.authorizationPassword, 1, 2);
        root2.add(AppClient.buttonEnter, 1, 3);
        GridPane.setHalignment(AppClient.buttonEnter, HPos.RIGHT);
    }

    public static void rootUrlConnection(FlowPane root4) {
        root4.getChildren().add(AppClient.url);
        root4.getChildren().add(AppClient.connectionUrlAddress);
    }
}
