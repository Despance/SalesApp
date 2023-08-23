package com.despance.salesapp.data;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.despance.salesapp.modal.Product.Product;
import com.google.gson.Gson;



public class Converters {

    @TypeConverter
    public Product fromStringtoProduct(String source){
        Gson gson = new Gson();
        Product product = gson.fromJson(source, Product.class);

        return product;
    }

    @TypeConverter
    public String fromProductToString(Product product){
        Gson gson = new Gson();
        String json = gson.toJson(product);
        return json;
    }

}
