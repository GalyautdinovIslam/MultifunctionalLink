package ru.itis.helpers;

public interface CodeGenerator {

    String generateCode(int length);

    String generateCode(int length, char[] alphabet);
}
