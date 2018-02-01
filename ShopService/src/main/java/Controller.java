import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;

public class Controller {
    private static ShopService service = new ShopService();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    public static void main(String[] args) throws IOException {

        staticFileLocation("/");
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");

            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));


        get("/getAll", (request, response) -> {
            String json = mapper.writeValueAsString(service.getAll());
            return json;
        });
        post("/addGoods", (request, response) -> {

            try {
                String body = request.body();
                List<Good> goods = mapper.readValue(body, new TypeReference<List<Good>>() {
                });
                service.addGoods(goods);
                return "ok";
            } catch (JsonParseException | JsonMappingException e) {
                LOGGER.error(e.getMessage(), e);
            }

            return "error";
        });
        post("/buyGoods", (request, response) -> {

            try {
                String body = request.body();
                List<Good> goods = mapper.readValue(body, new TypeReference<List<Good>>() {
                });
                service.buyGoods(goods);
                return "ok";
            } catch (JsonParseException | JsonMappingException e) {
                e.printStackTrace();
            }

            return "error";
        });
    }
}
