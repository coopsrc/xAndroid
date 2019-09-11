package com.coopsrc.xandroid.downloader.db

/**
 * Created by tingkuo.
 * Date: 2018-07-24
 * Time: 09:42
 */
internal object TaskInfoColumns {
    const val TABLE_NAME = "tasks"

    const val COLUMN_NAME_ID = "id"
    const val COLUMN_NAME_URL = "url"
    const val COLUMN_NAME_PATH = "path"
    const val COLUMN_NAME_NAME = "name"
    const val COLUMN_NAME_TYPE = "type"
    const val COLUMN_NAME_DOWNLOAD = "download_size"
    const val COLUMN_NAME_TOTAL_SIZE = "total_size"
    const val COLUMN_NAME_TAG = "tag"
    const val COLUMN_NAME_STATUS = "status"
    const val COLUMN_NAME_MODIFIED = "last_modified"
    private const val ITEM_COUNT = "item_count"

    const val READ_QUERY = "SELECT * FROM " + TABLE_NAME +
            " WHERE " + COLUMN_NAME_TAG + " = ?"
    const val UD_CLAUSE = COLUMN_NAME_TAG + " = ?"

}