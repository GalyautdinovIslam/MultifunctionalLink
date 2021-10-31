package ru.itis.helpers;

import java.security.SecureRandom;
import java.util.Random;

public class CodeGeneratorImpl implements CodeGenerator {

    private final Random random = new SecureRandom();
    private final char[] standardAlphabet;

    public CodeGeneratorImpl() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = upper.toLowerCase();
        String digit = "0123456789";
        this.standardAlphabet = (upper + lower + digit).toCharArray();
    }

    @Override
    public String generateCode(int length) {
        return generateCode(length, standardAlphabet);
    }

    @Override
    public String generateCode(int length, char[] alphabet) {
        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            buf[i] = alphabet[random.nextInt(alphabet.length)];
        }
        return new String(buf);
    }
}
