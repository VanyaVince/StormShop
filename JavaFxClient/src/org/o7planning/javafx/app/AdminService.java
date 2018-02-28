package org.o7planning.javafx.app;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;


class AdminService {

    public void rootBuyGoods(FlowPane root) {
        root.setPadding(new Insets(ProportionsConfigs.PADDING));
        root.setHgap(ProportionsConfigs.ROOT_H_GAP);
        root.setVgap(ProportionsConfigs.ROOT_V_GAP);
        root.getChildren().add(AppClient.getGoodsInShop());
        root.getChildren().add(AppClient.getButtonUpdateListForBuyGoods());
        root.getChildren().add(AppClient.getAddToCartButton());
        root.getChildren().add(AppClient.getListCarts());
        root.getChildren().add(AppClient.getButtonCountUp());
        root.getChildren().add(AppClient.getButtonCountDown());
        root.getChildren().add(AppClient.getDeleteGood());
        root.getChildren().add(AppClient.getTotalAmount());
        root.getChildren().add(AppClient.getButtonOfBuy());
    }

    public void rootAddGoods(FlowPane root1) {
        root1.setPadding(new Insets(ProportionsConfigs.PADDING));
        root1.setVgap(ProportionsConfigs.ROOT1_V_GAP);
        root1.setHgap(ProportionsConfigs.ROOT1_H_GAP);

        //ввод данных для добавления товара
        root1.getChildren().add(AppClient.getLableNameOfGoods());
        root1.getChildren().add(AppClient.getNameOfGoodAddToShop());
        root1.getChildren().add(AppClient.getLableCountOfGoods());
        root1.getChildren().add(AppClient.getCountOfGoodAddToShop());
        root1.getChildren().add(AppClient.getLablePriceOfGoods());
        root1.getChildren().add(AppClient.getPriceOfGoodAddToShop());

        //состояние в магазине, кнопка добавить товар, обновить
        root1.getChildren().add(AppClient.getViewGoodsInShop());
        root1.getChildren().add(AppClient.getButtonAddGoods());
        root1.getChildren().add(AppClient.getButtonUpdateListForAddGoods());

    }

    public void rootAuthorization(GridPane root2) {
        root2.setPadding(new Insets(ProportionsConfigs.PADDING));
        root2.setHgap(ProportionsConfigs.ROOT2_H_GAP);
        root2.setVgap(ProportionsConfigs.ROOT2_V_GAP);
        root2.add(AppClient.getLabelNickNameAndPassword(), 0, 0, 2, 1);
        GridPane.setHalignment(AppClient.getLabelNickNameAndPassword(), HPos.CENTER);
        root2.add(AppClient.getLabelLogin(), 0, 1);
        root2.add(AppClient.getAuthorizationName(), 1, 1);
        root2.add(AppClient.getLabelPassword(), 0, 2);
        root2.add(AppClient.getAuthorizationPassword(), 1, 2);
        root2.add(AppClient.getButtonEnter(), 1, 3);
        GridPane.setHalignment(AppClient.getButtonEnter(), HPos.RIGHT);
    }

    public void rootUrlConnection(FlowPane root4) {
        root4.setPadding(new Insets(ProportionsConfigs.PADDING));
        root4.setHgap(ProportionsConfigs.ROOT4_H_GAP);
        root4.setVgap(ProportionsConfigs.ROOT4_V_GAP);
        root4.getChildren().add(AppClient.getLabelServerUrlResponse());
        root4.getChildren().add(AppClient.getUrl());
        root4.getChildren().add(AppClient.getConnectionUrlAddress());
    }
}
