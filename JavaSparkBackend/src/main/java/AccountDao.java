import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class AccountDao {
    private static final ObjectMapper mapper = new ObjectMapper();//преобразует файлы.
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public synchronized List<Account> findAccount() {
        try {
            FileInputStream fis = new FileInputStream("account.json");// открытие потока, для чтения файла goods.json
            List<Account> account = mapper.readValue(fis, new TypeReference<List<Account>>() {
            });
            return account;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public synchronized Account findLogin(String name) {
        List<Account> clientList = findAccount();
        for (Account client : clientList) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }
}
