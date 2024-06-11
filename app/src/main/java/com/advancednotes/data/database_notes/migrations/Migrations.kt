package com.advancednotes.data.database_notes.migrations

import android.annotation.SuppressLint
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.advancednotes.domain.models.AdvancedContentItem
import com.advancednotes.domain.models.AdvancedContentType
import com.advancednotes.utils.type_adapters.AdvancedContentTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE notes ADD COLUMN reminder TEXT DEFAULT NULL")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Crear una nueva tabla temporal sin la columna que deseas eliminar
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS notes_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "uuid TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "content TEXT," +
                    "tags_uuids TEXT," +
                    "location TEXT," +
                    "reminder TEXT," +
                    "fixed INTEGER NOT NULL," +
                    "archived INTEGER NOT NULL," +
                    "creation_date TEXT NOT NULL," +
                    "modification_date TEXT NOT NULL," +
                    "trash_date TEXT," +
                    "deleted_permanently INTEGER NOT NULL)"
        )

        // Copiar los datos de la tabla original a la nueva tabla temporal
        db.execSQL(
            "INSERT INTO notes_temp (id, uuid, name, content, tags_uuids, location, " +
                    "fixed, archived, creation_date, modification_date, " +
                    "trash_date, deleted_permanently) " +
                    "SELECT id, uuid, name, content, tags_uuids, location, " +
                    "fixed, archived, creation_date, modification_date, " +
                    "trash_date, deleted_permanently FROM notes"
        )

        // Eliminar la tabla original
        db.execSQL("DROP TABLE notes")

        // Renombrar la nueva tabla temporal al nombre original
        db.execSQL("ALTER TABLE notes_temp RENAME TO notes")
    }
}

val MIGRATION_3_4 = object : Migration(3, 4) {
    @SuppressLint("Range")
    override fun migrate(db: SupportSQLiteDatabase) {
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(AdvancedContentType::class.java, AdvancedContentTypeAdapter())
            .serializeNulls()
            .create()

        // Agregar la nueva columna de tipo texto para la advanced_content
        db.execSQL("ALTER TABLE notes ADD COLUMN advanced_content TEXT")

        // Obtener los datos de la columna "content"
        val cursor = db.query("SELECT id, content FROM notes")
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val id: Long = cursor.getLong(cursor.getColumnIndex("id"))
            val content: String? = cursor.getString(cursor.getColumnIndex("content"))

            if (content != null) {
                // Convertir el contenido de String a un objeto AdvancedContentItem con type 'text'
                val advancedContentList: List<AdvancedContentItem> = listOf(
                    AdvancedContentItem(
                        type = AdvancedContentType.TEXT,
                        content = content
                    )
                )

                // Convertir la lista a JSON
                val json = gson.toJson(advancedContentList)

                // Actualizar la nueva columna "advanced_content" con los datos
                db.execSQL(
                    "UPDATE notes SET advanced_content = ? WHERE id = ?",
                    arrayOf(json, id.toString())
                )
            }

            cursor.moveToNext()
        }

        cursor.close()
    }
}

val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Crear una nueva tabla temporal sin la columna que deseas eliminar
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS notes_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "uuid TEXT NOT NULL," +
                    "name TEXT NOT NULL," +
                    "advanced_content TEXT," +
                    "tags_uuids TEXT," +
                    "location TEXT," +
                    "reminder TEXT," +
                    "fixed INTEGER NOT NULL," +
                    "archived INTEGER NOT NULL," +
                    "creation_date TEXT NOT NULL," +
                    "modification_date TEXT NOT NULL," +
                    "trash_date TEXT," +
                    "deleted_permanently INTEGER NOT NULL)"
        )

        // Copiar los datos de la tabla original a la nueva tabla temporal
        db.execSQL(
            "INSERT INTO notes_temp (id, uuid, name, advanced_content, tags_uuids, location, " +
                    "fixed, archived, creation_date, modification_date, " +
                    "trash_date, deleted_permanently) " +
                    "SELECT id, uuid, name, advanced_content, tags_uuids, location, " +
                    "fixed, archived, creation_date, modification_date, " +
                    "trash_date, deleted_permanently FROM notes"
        )

        // Eliminar la tabla original
        db.execSQL("DROP TABLE notes")

        // Renombrar la nueva tabla temporal al nombre original
        db.execSQL("ALTER TABLE notes_temp RENAME TO notes")
    }
}

val MIGRATION_5_6 = object : Migration(5, 6) {
    @SuppressLint("Range")
    override fun migrate(db: SupportSQLiteDatabase) {
        // Agregar la nueva columna de tipo texto not null para la file_uuid
        db.execSQL("ALTER TABLE notes_files ADD COLUMN file_uuid TEXT NOT NULL DEFAULT ''")

        // Obtener los datos de la columna "file_identifier"
        val cursor = db.query("SELECT id, file_identifier FROM notes_files")
        cursor.moveToFirst()

        while (!cursor.isAfterLast) {
            val id: Long = cursor.getLong(cursor.getColumnIndex("id"))
            val fileIdentifier: String = cursor.getString(cursor.getColumnIndex("file_identifier"))

            // Actualizar la nueva columna "file_uuid" con los datos
            db.execSQL(
                "UPDATE notes_files SET file_uuid = ? WHERE id = ?",
                arrayOf(fileIdentifier, id.toString())
            )

            cursor.moveToNext()
        }

        cursor.close()
    }
}

val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Crear una nueva tabla temporal sin la columna que deseas eliminar
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS notes_files_temp (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "uuid TEXT NOT NULL," +
                    "note_uuid TEXT NOT NULL," +
                    "file_uuid TEXT NOT NULL," +
                    "file_name TEXT NOT NULL," +
                    "file_extension TEXT NOT NULL," +
                    "directory TEXT NOT NULL," +
                    "creation_date TEXT NOT NULL," +
                    "modification_date TEXT NOT NULL," +
                    "deleted_permanently INTEGER NOT NULL)"
        )

        // Copiar los datos de la tabla original a la nueva tabla temporal
        db.execSQL(
            "INSERT INTO notes_files_temp (id, uuid, note_uuid, file_uuid, file_name, file_extension, " +
                    "directory, creation_date, modification_date, deleted_permanently) " +
                    "SELECT id, uuid, note_uuid, file_uuid, file_name, file_extension, " +
                    "directory, creation_date, modification_date, " +
                    "deleted_permanently FROM notes_files"
        )

        // Eliminar la tabla original
        db.execSQL("DROP TABLE notes_files")

        // Renombrar la nueva tabla temporal al nombre original
        db.execSQL("ALTER TABLE notes_files_temp RENAME TO notes_files")
    }
}

val MIGRATION_7_8 = object : Migration(7, 8) {
    @SuppressLint("Range")
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE tags ADD COLUMN order_number INTEGER NOT NULL DEFAULT 0")

        val cursor = db.query("SELECT id FROM tags ORDER BY id ASC")
        cursor.moveToFirst()

        var order: Int = 0
        while (!cursor.isAfterLast) {
            val id: Long = cursor.getLong(cursor.getColumnIndex("id"))

            // Actualizar el campo "order" para la fila actual
            val updateQuery = "UPDATE tags SET order_number = $order WHERE id = $id"
            db.execSQL(updateQuery)

            order++
            cursor.moveToNext()
        }

        cursor.close()
    }
}