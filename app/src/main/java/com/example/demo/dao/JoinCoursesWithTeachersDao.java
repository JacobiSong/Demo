package com.example.demo.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.demo.entity.JoinCoursesWithTeachers;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "JOIN_COURSES_WITH_TEACHERS".
*/
public class JoinCoursesWithTeachersDao extends AbstractDao<JoinCoursesWithTeachers, Integer> {

    public static final String TABLENAME = "JOIN_COURSES_WITH_TEACHERS";

    /**
     * Properties of entity JoinCoursesWithTeachers.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, int.class, "id", true, "ID");
        public final static Property UserId = new Property(1, String.class, "userId", false, "USER_ID");
        public final static Property CourseId = new Property(2, String.class, "courseId", false, "COURSE_ID");
    }


    public JoinCoursesWithTeachersDao(DaoConfig config) {
        super(config);
    }
    
    public JoinCoursesWithTeachersDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"JOIN_COURSES_WITH_TEACHERS\" (" + //
                "\"ID\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"USER_ID\" TEXT NOT NULL ," + // 1: userId
                "\"COURSE_ID\" TEXT NOT NULL );"); // 2: courseId
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"JOIN_COURSES_WITH_TEACHERS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, JoinCoursesWithTeachers entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindString(2, entity.getUserId());
        stmt.bindString(3, entity.getCourseId());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, JoinCoursesWithTeachers entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindString(2, entity.getUserId());
        stmt.bindString(3, entity.getCourseId());
    }

    @Override
    public Integer readKey(Cursor cursor, int offset) {
        return cursor.getInt(offset + 0);
    }    

    @Override
    public JoinCoursesWithTeachers readEntity(Cursor cursor, int offset) {
        JoinCoursesWithTeachers entity = new JoinCoursesWithTeachers( //
            cursor.getInt(offset + 0), // id
            cursor.getString(offset + 1), // userId
            cursor.getString(offset + 2) // courseId
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, JoinCoursesWithTeachers entity, int offset) {
        entity.setId(cursor.getInt(offset + 0));
        entity.setUserId(cursor.getString(offset + 1));
        entity.setCourseId(cursor.getString(offset + 2));
     }
    
    @Override
    protected final Integer updateKeyAfterInsert(JoinCoursesWithTeachers entity, long rowId) {
        return entity.getId();
    }
    
    @Override
    public Integer getKey(JoinCoursesWithTeachers entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(JoinCoursesWithTeachers entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
