package org.o7planning.javafx.app;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.o7planning.javafx.model.Good;
import org.o7planning.javafx.service.HttpConnectionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopService {

    public void totalSum(Label viewSum, TableView list) {
        List<Good> product = changeList(list);
        int totalSumByGoods = totalSumOfGoods(product);
        viewSum.setText("Общая сумма: " + ":" + totalSumByGoods + "$");
    }

    private int totalSumOfGoods(List<Good> product) {
        int total = 0;
        for (Good good : product) {
            int countSum = good.price * good.count;
            total = countSum + total;
        }
        return total;
    }

    public List<Good> changeList(TableView list) {
        List<Good> product = new ArrayList<>();
        for (Object good : list.getItems()) {
            product.add((Good) good);
        }
        return product;
    }

    public void afterBuyOfGoods(Label viewSum) {

        viewSum.setText("Общая сумма: " + ":" + "0$");
    }

    public Good findByName(List<Good> listOfGood, String name) {
        for (Good good : listOfGood) {
            if (good.name.equals(name)) {
                return good;
            }
        }
        return null;
    }

    public void updateOfGoods(ObservableList list) throws IOException {
        list.clear();
        list.addAll(AppClient.getServerGoods());
    }
}