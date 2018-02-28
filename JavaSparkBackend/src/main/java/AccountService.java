import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class AccountService {

    private Random random = new Random();
    private List<String> accessT = new ArrayList<>();
    private AccountDao daoAccount = new AccountDao();

    public String checkAccount(Account loginPaswword) throws IOException {

        String accessToken;

        Account accountJSON = daoAccount.findLogin(loginPaswword.getName());
        if (Objects.equals(loginPaswword.getName(), accountJSON.getName()) &&
                Objects.equals(loginPaswword.getPassword(), accountJSON.getPassword())) {

            String randomNumbers = (String.valueOf(random.doubles()));
            String passordWithRandom = loginPaswword.getPassword().concat(randomNumbers);
            accessToken = DigestUtils.sha1Hex(passordWithRandom);
            accessT.add(accessToken);

            return accessToken;

        } else {
            throw new IllegalArgumentException("Неверный логин или пароль.");
        }
    }
    public List<String> list (){
        return accessT;
    }
}