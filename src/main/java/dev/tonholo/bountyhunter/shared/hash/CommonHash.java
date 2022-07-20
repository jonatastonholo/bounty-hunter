package dev.tonholo.bountyhunter.shared.hash;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CommonHash {

    public String sha256(List<String> values) {
        return sha256(values.toArray(new String[0]));
    }

    public String sha256(String...values) {
        final var listAsString =
                "[\"" + String.join("\",\"", values) + "\"]";
        return DigestUtils
                .sha256Hex(listAsString)
                .toLowerCase();
    }
}
