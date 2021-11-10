package ru.itis.helpers;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptHelperImpl implements EncryptHelper {

    @Override
    public String encryptPassword(String password) {
        char[] chars = password.toCharArray();
        int cycle = 0;
        for (int i = 0; i < chars.length; i++) {
            switch (cycle) {
                case 0:
                    chars[i] += 'O';
                    cycle++;
                    break;
                case 1:
                    chars[i] += 'L';
                    cycle++;
                    break;
                case 2:
                    chars[i] += 'G';
                    cycle++;
                    break;
                case 3:
                    chars[i] += 'A';
                    cycle = 0;
                    break;
            }
        }
        password = new String(chars);
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return concatenateSequentially(md, password, 2, 2);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public String encryptCode(String code) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return concatenateSequentially(md, code, 8, 1);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    private String concatenateSequentially(MessageDigest md, String toEncrypt, int times, int depth) {
        byte[] bytes = toEncrypt.getBytes();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            for (int j = 0; j < depth; j++) {
                md.update(bytes);
                bytes = md.digest();
            }
            sb.append(DatatypeConverter.printHexBinary(bytes).toUpperCase());
        }
        return sb.toString();
    }
}
