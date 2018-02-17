import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class ShopService {
    private Random random = new Random();
    private List<String> access_token = new ArrayList<>();

    private ShopDao dao = new ShopDao();

    public List<Good> getAll() {
        return dao.findAll();
    }

    public void addGoods(List<Good> goods, String access) throws IOException {

        for (String accessToken : access_token) {
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
                }
            } else {
                throw new IllegalArgumentException("access_Token не совпал");
            }
        }
    }

    public void buyGoods(List<Good> goods) throws IOException {
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
    }

    public String checkAccount(Account loginPaswword) throws IOException {

        String accessToken;

        Account accountJSON = dao.findLogin(loginPaswword.getName());
        if (Objects.equals(loginPaswword.getName(), accountJSON.getName()) &&
                Objects.equals(loginPaswword.getPassword(), accountJSON.getPassword())) {

            String randomNumbers = (String.valueOf(random.doubles()));
            String passordWithRandom = loginPaswword.getPassword().concat(randomNumbers);
            accessToken = DigestUtils.sha1Hex(passordWithRandom);
            access_token.add(accessToken);

            return accessToken;
            /*
            SHA1 shifr = new shifr();
            String random = (string)(Math.random);
            String input = pass.concat(random);
            String access_token = shifr.coding(input);
            return access_token;
             */
        } else {
            throw new IllegalArgumentException("Неверный логин или пароль.");
        }
    }
}