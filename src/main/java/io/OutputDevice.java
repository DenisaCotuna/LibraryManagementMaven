package io;

import java.io.*;

public class OutputDevice {
    private final OutputStream outputStream;

    public OutputDevice(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public <E> void print(E msg)
    {
        System.out.println(msg);
    }

    public void  writeBytes(byte[] bytes)
    {
        try {
            outputStream.write(bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void writetoFile(String content, String filename, Boolean append) throws IOException{
        try{
            FileWriter file = new FileWriter(filename,append);
            file.write(content);
            file.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
