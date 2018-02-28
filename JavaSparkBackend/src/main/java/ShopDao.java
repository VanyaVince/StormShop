import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.List;


public class ShopDao {
    private static final ObjectMapper mapper = new ObjectMapper();//преобразует файлы.
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public synchronized List<Good> findAll() {
        try {
            FileInputStream fis = new FileInputStream("goods.json");// открытие потока, для чтения файла goods.json

            List<Good> goods = mapper.readValue(fis, new TypeReference<List<Good>>() {
            });
            return goods;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    public synchronized Good findByName(String name) {
        List<Good> goods = findAll();
        for (Good good : goods) {
            if (good.name.equals(name)) {
                return good;
            }
        }
        return null;
    }

    public synchronized void save(Good good) throws IOException {
        List<Good> goods = findAll();
        goods.add(good);

        String listOfGoods = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(goods);
        FileOutputStream fos = new FileOutputStream("goods.json");//открытие потока, для чтения файла.
        fos.write(listOfGoods.getBytes());
        fos.flush();
        fos.close();//закрытие потока
    }

    public synchronized void deleteByName(String name) throws IOException {
        List<Good> goods = findAll();
        Iterator<Good> it = goods.iterator();
        while (it.hasNext()) {
            Good el = it.next();
            if (el.name.equals(name))
                it.remove();
        }
        String listOfGoods = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(goods);
        FileOutputStream fos = new FileOutputStream("goods.json");//открытие потока, для чтения файла.
        fos.write(listOfGoods.getBytes());
        fos.flush();
        fos.close();
    }
}