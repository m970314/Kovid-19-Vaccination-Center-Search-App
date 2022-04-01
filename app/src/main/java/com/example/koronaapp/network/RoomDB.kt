package com.example.koronaapp.network

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.koronaapp.Model.HospitalInfoResponse
import com.google.gson.Gson
import dagger.Provides
import javax.inject.Singleton

@Database(entities = [HospitalInfoResponse::class], version = 1, exportSchema = false)
@TypeConverters
    ( value = [
    Converter::class
    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): RoomDAO

    object DatabaseModule {
        private const val DB_NAME = "haspital.db"
        @Singleton
        @Provides
        fun provideGson(): Gson {
            return Gson()
        }
        @Singleton
        @Provides
        fun provideDatabase(context: Context, gson: Gson): AppDatabase {
            return Room
                .databaseBuilder(context, AppDatabase::class.java, DB_NAME)
                .addTypeConverter(Converter(gson)) // 'List<String>' converter
                .build()
        }
        @Singleton
        @Provides
        fun provideUserDao(database: AppDatabase): RoomDAO {
            return database.roomDao()
        }
    }
}


