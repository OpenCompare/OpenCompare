package persistence.impl;

import persistence.BestBuyProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.BestBuyProduct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * JSON implementation of the BestBuyProductRepository interface
 * 
 * @author jmdavril
 */
public class JsonBestBuyProductRepositoryImpl
        implements BestBuyProductRepository {

    @Override
    public List<BestBuyProduct> findAllProductsForCategory(String categoryId) {
        byte[] encoded;
        try {
            Path path = Paths.get(
                    String.format("%s%s.txt", "data/datasets/",
                            categoryId));
            encoded = Files.readAllBytes(path);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        String json;
        try {
            json = new String(encoded, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return null;
        }

        return fromJSON(new TypeReference<List<BestBuyProduct>>() {
        }, json);
    }

    private static <T> T fromJSON(TypeReference<T> type, String jsonPacket) {
        T data = null;
        try {
            data = new ObjectMapper().readValue(jsonPacket, type);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
