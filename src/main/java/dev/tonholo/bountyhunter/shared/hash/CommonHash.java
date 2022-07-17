package dev.tonholo.bountyhunter.shared.hash;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class CommonHash {

    public String sha256(String...values) {
        final var listAsString =
                "[\"" + String.join("\",\"", values) + "\"]";
        return DigestUtils
                .sha256Hex(listAsString)
                .toLowerCase();
    }
}
