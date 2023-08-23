package com.despance.salesapp.modal.CartItem;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.despance.salesapp.data.Converters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = {CartItem.class}, version = 1, exportSchema = false)
public abstract class CartItemDatabase extends RoomDatabase{

    public abstract CartItemDao cartItemDao();

    private static volatile CartItemDatabase INSTANCE;

    private static final int NUMBER_OF_THREADS = 1;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static CartItemDatabase getDatabase(final android.content.Context context) {
        if (INSTANCE == null) {
            synchronized (CartItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = androidx.room.Room.databaseBuilder(context.getApplicationContext(),
                            CartItemDatabase.class, "cartItem_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
