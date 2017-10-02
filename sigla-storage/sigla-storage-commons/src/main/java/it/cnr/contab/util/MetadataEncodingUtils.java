package it.cnr.contab.util;

import it.cnr.contab.spring.storage.StorageException;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by francesco on 19/07/17.
 */
public class MetadataEncodingUtils {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MetadataEncodingUtils.class);
    private static final String UTF_8 = "UTF-8";
    private static final String PREFIX = "CNR_";

    /**
     * Encode metadata value
     *
     * @param value metadata to encode
     * @return encoded metadata (i.e. ${md5(value)}|${b64(value)})
     */
    public static String encodeValue(String value) {
        byte[] bytes;
        try {
            bytes = value.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(StorageException.Type.GENERIC, "cannot encode value " + value, e);
        }
        String md5digest = DigestUtils
                .md5Hex(bytes)
                .toUpperCase();
        String b64encoded = Base64
                .getEncoder()
                .encodeToString(bytes)
                .replaceAll("=+$", "");

        LOGGER.info("value {} having md5 {} has been encoded to {}", value, md5digest, b64encoded);
        String output = String.format("%s|%s", md5digest, b64encoded);
        LOGGER.info("value {} encoded to {}", value, output);
        return output;
    }

    /**
     * Decode metadata value
     *
     * @param input encoded string (${md5(value)}|${b64(value)}) to decode
     * @return decoded value
     */
    public static String decodeValue(String input) {
        String[] a = input.split("\\|");
        Assert.isTrue(2 == a.length, "invalid metadata: " + input);
        byte[] decoded = Base64.getDecoder().decode(a[1]);
        try {
            String decodedValue = new String(decoded, UTF_8);
            LOGGER.info("{} decoded to {}", input, decodedValue);
            Assert.isTrue(a[0].equals(DigestUtils.md5Hex(decodedValue).toUpperCase()), "integrity issue with input " + input);
            return decodedValue;
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(StorageException.Type.GENERIC, "cannot decode " + input, e);
        }
    }


    /**
     * Encode a list of metadata values
     *
     * @param entryValue list of metadata values
     * @return an encoded string such as ${md5(e1)}|${b64(e1)},${md5(e2)}|${b64(e2)},${md5(e3)}|${b64(e3)}
     */
    public static String encodeValues(List<String> entryValue) {

        String base64EncodedValues = entryValue
                .stream()
                .map(MetadataEncodingUtils::encodeValue)
                .collect(Collectors.joining(","));

        LOGGER.info("{} encoded to {}", entryValue, base64EncodedValues);

        return base64EncodedValues;

    }

    /**
     * Decode a multi-valued metadata field
     *
     * @param input encoded string (made by a concatenation of encoded strings)
     * @return a list of decoded values
     */
    public static List<String> decodeValues(String input) {

        return Optional.ofNullable(input)
                .map(s -> Arrays
                        .stream(s.split(","))
                        .map(MetadataEncodingUtils::decodeValue)
                        .collect(Collectors.toList())
                )
                .orElse(Collections.emptyList());
    }


    /**
     * Encode metadata key
     *
     * @param input metadata key
     * @return encoded metadata key (i.e. ${PREFIX}${base64(key)})
     */
    public static String encodeKey(String input) {
        byte[] bytes;
        try {
            bytes = input.getBytes(UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(StorageException.Type.GENERIC, "cannot encode key " + input, e);
        }
        String suffix = Base64
                .getEncoder()
                .encodeToString(bytes)
                .replaceAll("=+$", "");
        String b64encodedKey = String.format("%s%s", PREFIX, suffix);
        LOGGER.info("key {} encoded to {}", input, b64encodedKey);
        return b64encodedKey;

    }


    /**
     * Decode a metadata key
     *
     * @param input metadata key such as ${PREFIX}${base64(key)}
     * @return decoded metadata key
     */
    public static String decodeKey(String input) {
        Assert.isTrue(input.startsWith(PREFIX));
        String suffix = input.replaceFirst(PREFIX, "");
        try {
            String output = new String(Base64.getDecoder().decode(suffix), UTF_8);
            LOGGER.info("{} decoded to {}", input, output);
            return output;
        } catch (UnsupportedEncodingException e) {
            throw new StorageException(StorageException.Type.GENERIC, "cannot decode " + input, e);
        }
    }

}
