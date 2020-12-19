package com.example.demo.entity;

public class File {

    private final String filename; //文件名
    private final String author; //上传者
    private final double size; //文件大小

    public File(String filename, String author, double size) {
        this.filename = filename;
        this.author = author;
        this.size = size;
    }

    public String getFilename() {
        return filename;
    }

    public String getAuthor() {
        return author;
    }

    public double getSize() {
        return size;
    }
}
