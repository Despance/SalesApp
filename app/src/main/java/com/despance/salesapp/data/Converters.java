package com.despance.salesapp.data;

import androidx.room.TypeConverter;

import com.despance.salesapp.modal.Product.Product;
import com.google.gson.Gson;


public class Converters {

    @TypeConverter
    public Product fromStringtoProduct(String source){
        return new Gson().fromJson(source, Product.class);
    }

    @TypeConverter
    public String fromProductToString(Product product){
        return new Gson().toJson(product);
    }

}
