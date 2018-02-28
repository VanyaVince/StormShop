import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ShopService {

    private ShopDao daoShop = new ShopDao();
    private AccountService accountService = new AccountService();

    public List<Good> getAll() {
        return daoShop.findAll();
    }

    public String addGoods(List<Good> goods, String access) throws IOException {
        boolean check = false;
        List<String> accessT = accountService.list();
        for (String accessToken : accessT) {
            if (Objects.equals(access, accessToken)) {
                for (Good good : goods) {
                    Good goodInFile = daoShop.findByName(good.name);

                    if (goodInFile == null) {
                        daoShop.save(good);
                    } else {
                        daoShop.deleteByName(good.name);
                        Good newGood = new Good(goodInFile.name, goodInFile.count + good.count, good.price);
                        daoShop.save(newGood);
                    }
                    return accessToken;
                }
                check = true;
            }
        }
        if (!check) {
            throw new IllegalArgumentException("access_Token не совпал");
        }
        return "";
    }

    public String buyGoods(List<Good> goods, String access_token) throws IOException {
        for (Good good : goods) {
            Good goodInFile = daoShop.findByName(good.name);

            if (goodInFile == null || goodInFile.count < good.count) {
                throw new IllegalArgumentException("Нету такого количества.");
            } else {
                Good newGood = new Good(goodInFile.name, goodInFile.count - good.count, good.price);
                daoShop.deleteByName(good.name);
                daoShop.save(newGood);
            }
        }
        return access_token;
    }
}