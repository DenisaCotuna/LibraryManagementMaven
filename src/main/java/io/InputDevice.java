package io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class InputDevice {
    private final InputStream inputStream;

    public InputDevice(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    public String read(){
        try {
            String temp;
            if (inputStream != System.in) {
                temp = new String(inputStream.readAllBytes());
            }
            else {
                Scanner input = new Scanner(inputStream);
                temp = input.nextLine();
                return temp;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
