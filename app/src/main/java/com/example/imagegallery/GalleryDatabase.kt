package com.example.imagegallery

import android.content.Context
import androidx.room.*

@Entity
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "filename") val filename: String
)

@Dao
interface ImageDao {
    @Query("SELECT filename FROM image")
    fun getFilenames(): List<String>

    @Query("SELECT COUNT(*) FROM image")
    fun count(): Int

    @Query("SELECT filename FROM image WHERE id = :id")
    fun getFilename(id: Int): String

    @Query("DELETE FROM image")
    fun deleteAll()

    @Query("DELETE FROM image WHERE id = :id")
    fun deletePhoto(id: Int)

    @Insert
    fun insertAll(vararg images: Image)
}

@Database(entities = [Image::class], version = 10)
abstract class GalleryDatabase : RoomDatabase() {
    abstract fun userDao(): ImageDao

    companion object {

        @Volatile private var INSTANCE: GalleryDatabase? = null

        fun getInstance(context: Context): GalleryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                GalleryDatabase::class.java, "images.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}
