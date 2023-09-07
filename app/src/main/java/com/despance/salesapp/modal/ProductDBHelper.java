package com.despance.salesapp.modal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.despance.salesapp.modal.Product.Product;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ProductDBHelper extends SQLiteOpenHelper {

    //!!!!!!!!!!!!!!!!!!!!DEPRECATED!!!!!!!!!!!!!!!!!!!!!!!!

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productManager";
    private static final String TABLE_NAME = "products";
    private static final String KEY_ID = "id";
    private static final String KEY_PRODUCT_NAME = "productName";
    private static final String KEY_PRICE = "price";
    private static final String KEY_VAT_RATE = "vatRate";
    private static final String KEY_BARCODE = "barcode";



    public ProductDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void addProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProductName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_VAT_RATE, product.getVatRate());
        values.put(KEY_BARCODE, product.getBarcode());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void deleteProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
        db.close();
    }

    public void updateProduct(Product product){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PRODUCT_NAME, product.getProductName());
        values.put(KEY_PRICE, product.getPrice());
        values.put(KEY_VAT_RATE, product.getVatRate());
        values.put(KEY_BARCODE, product.getBarcode());

        db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(product.getId()) });
        db.close();
    }

    public Product findProductById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID,
                        KEY_PRODUCT_NAME, KEY_PRICE, KEY_VAT_RATE, KEY_BARCODE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if(cursor != null)
            cursor.moveToFirst();

        Product product = new Product(cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getString(4));
        product.setId(Integer.parseInt(cursor.getString(0)));
        return product;
    }

    public List<Product> getAllProducts(){
        List<Product> productList = new ArrayList<Product>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            do {
                Product currentProduct= new Product(cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3), cursor.getString(4));
                currentProduct.setId(Integer.parseInt(cursor.getString(0)));
                productList.add(currentProduct);
            }while (cursor.moveToNext());
        }
        return productList;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PRODUCT_NAME + " TEXT,"
                + KEY_PRICE + " REAL,"
                + KEY_VAT_RATE + " REAL,"
                + KEY_BARCODE + " TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS products");
        onCreate(sqLiteDatabase);
    }
}
