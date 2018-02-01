import java.io.IOException;
import java.util.List;

public class ShopService {

    ShopDao dao = new ShopDao();

    public List<Good> getAll() {

        return dao.findAll();
    }

    public void addGoods(List<Good> goods) throws IOException {
        // List<Good> goodsInShop = dao.findAll();
        for (Good good : goods) {
            Good goodInFile = dao.findByName(good.name);

            if (goodInFile == null) {
                dao.save(good);
            } else {
                dao.deleteByName(good.name);
                Good newGood = new Good(goodInFile.name, goodInFile.count + good.count, good.price);
                dao.save(newGood);
            }
        }
    }

    public void buyGoods(List<Good> goods) throws IOException {
        for (Good good : goods) {
            Good goodInFile = dao.findByName(good.name);

            if (goodInFile == null || goodInFile.count < good.count) {
                throw new IllegalArgumentException("Нету такого количесвта");
            } else {
                Good newGood = new Good(goodInFile.name, goodInFile.count - good.count, good.price);
                dao.deleteByName(good.name);
                dao.save(newGood);
//        goodsInFile.stream()
//                .filter(el -> el.count <= 0)
//                .findAny()
//                .ifPresent(el -> goodsInFile.remove(el));

            }
        }
    }
}
