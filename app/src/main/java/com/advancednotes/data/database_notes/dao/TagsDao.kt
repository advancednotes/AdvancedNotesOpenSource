package com.advancednotes.data.database_notes.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.advancednotes.data.database_notes.entities.TagEntity

@Dao
interface TagsDao {

    /**
     * GET
     * */
    @Query("SELECT * FROM tags ORDER BY id ASC")
    fun getAllTags(): List<TagEntity>

    @Query("SELECT * FROM tags WHERE deleted_permanently = 0 ORDER BY order_number ASC")
    fun getTags(): List<TagEntity>

    @Query("SELECT * FROM tags WHERE uuid = :uuid LIMIT 1")
    fun getTagByUUID(uuid: String): TagEntity?

    @Query("SELECT MAX(order_number) FROM tags")
    fun getMaxOrder(): Int?

    @Query("SELECT modification_date FROM tags")
    fun getTagsModificationDates(): List<String>

    /**
     * INSERT
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTag(tagEntity: TagEntity)

    /**
     * UPDATE
     * */
    @Update
    fun updateTag(tagEntity: TagEntity)

    @Query("UPDATE tags SET deleted_permanently = 1, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateTagDeletedPermanently(uuid: String, modificationDate: String)

    @Query("UPDATE tags SET order_number = :orderNumber, modification_date = :modificationDate WHERE uuid = :uuid")
    fun updateTagOrderNumber(uuid: String, orderNumber: Int, modificationDate: String)

    /**
     * DELETE
     * */
    @Query("DELETE FROM tags")
    fun deleteAllTags() // CAUTION

    @Query("DELETE FROM tags WHERE deleted_permanently = 1")
    fun deleteTagsDeletedPermanently()
}