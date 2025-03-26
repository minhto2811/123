package com.example.apphuongdancongthuc;

import androidx.annotation.NonNull;

public class MessageModel {
    private int id;

    private String userId;
    private String message;
    private String role;
    private String productId;

    private String images;
    public MessageModel( String message, String userId, String productId, String role,String images) {
        this.userId = userId;
        this.message = message;
        this.role = role;
        this.productId = productId;
        this.images = images;
    }

    public MessageModel(int id, String message, String userId, String productId, String role,String images) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.role = role;
        this.productId = productId;
        this.images = images;
    }

    public Boolean isUser() {
        return role.equals("user");
    }

    public String getMessage() {
        return message;
    }

    public String getImages() {
        return images;
    }



    @NonNull
    @Override
    public String toString() {
        String m1 = message.replaceAll("'","");
        return "'" + m1 + "', " + "'" + userId + "', " + "'" + productId + "', '"  + role + "', '"  + images + "'";
    }
}
