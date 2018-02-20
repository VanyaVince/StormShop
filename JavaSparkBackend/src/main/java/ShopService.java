import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ShopService {
    private Random random = new Random();
    private List<String> accessT = new ArrayList<>();

    private ShopDao dao = new ShopDao();

    public List<Good> getAll() {
        return dao.findAll();
    }

    public String addGoods(List<Good> goods, String access) throws IOException {
        boolean check = false;
        for (String accessToken : accessT) {
            if (Objects.equals(access, accessToken)) {
                for (Good good : goods) {
                    Good goodInFile = dao.findByName(good.name);

                    if (goodInFile == null) {
                        dao.save(good);
                    } else {
                        dao.deleteByName(good.name);
                        Good newGood = new Good(goodInFile.name, goodInFile.count + good.count, good.price);
                        dao.save(newGood);
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
            Good goodInFile = dao.findByName(good.name);

            if (goodInFile == null || goodInFile.count < good.count) {
                throw new IllegalArgumentException("Нету такого количества.");
            } else {
                Good newGood = new Good(goodInFile.name, goodInFile.count - good.count, good.price);
                dao.deleteByName(good.name);
                dao.save(newGood);
            }
        }

        return access_token;
    }

    public String checkAccount(Account loginPaswword) throws IOException {

        String accessToken;

        Account accountJSON = dao.findLogin(loginPaswword.getName());
        if (Objects.equals(loginPaswword.getName(), accountJSON.getName()) &&
                Objects.equals(loginPaswword.getPassword(), accountJSON.getPassword())) {

            String randomNumbers = (String.valueOf(random.doubles()));
            String passordWithRandom = loginPaswword.getPassword().concat(randomNumbers);
            accessToken = DigestUtils.sha1Hex(passordWithRandom);
            accessT.add(accessToken);

            return accessToken;
            /*
            SHA1 shifr = new shifr();
            String random = (string)(Math.random);
            String input = pass.concat(random);
            String accessT = shifr.coding(input);
            return accessT;
             */
        } else {
            throw new IllegalArgumentException("Неверный логин или пароль.");
        }
    }
}