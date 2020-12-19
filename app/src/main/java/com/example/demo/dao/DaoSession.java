package com.example.demo.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.demo.entity.Course;
import com.example.demo.entity.JoinCoursesWithStudents;
import com.example.demo.entity.JoinCoursesWithTeachers;
import com.example.demo.entity.Message;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;

import com.example.demo.dao.CourseDao;
import com.example.demo.dao.JoinCoursesWithStudentsDao;
import com.example.demo.dao.JoinCoursesWithTeachersDao;
import com.example.demo.dao.MessageDao;
import com.example.demo.dao.NotificationDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.dao.TeacherDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig courseDaoConfig;
    private final DaoConfig joinCoursesWithStudentsDaoConfig;
    private final DaoConfig joinCoursesWithTeachersDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig notificationDaoConfig;
    private final DaoConfig studentDaoConfig;
    private final DaoConfig teacherDaoConfig;

    private final CourseDao courseDao;
    private final JoinCoursesWithStudentsDao joinCoursesWithStudentsDao;
    private final JoinCoursesWithTeachersDao joinCoursesWithTeachersDao;
    private final MessageDao messageDao;
    private final NotificationDao notificationDao;
    private final StudentDao studentDao;
    private final TeacherDao teacherDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        courseDaoConfig = daoConfigMap.get(CourseDao.class).clone();
        courseDaoConfig.initIdentityScope(type);

        joinCoursesWithStudentsDaoConfig = daoConfigMap.get(JoinCoursesWithStudentsDao.class).clone();
        joinCoursesWithStudentsDaoConfig.initIdentityScope(type);

        joinCoursesWithTeachersDaoConfig = daoConfigMap.get(JoinCoursesWithTeachersDao.class).clone();
        joinCoursesWithTeachersDaoConfig.initIdentityScope(type);

        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);

        notificationDaoConfig = daoConfigMap.get(NotificationDao.class).clone();
        notificationDaoConfig.initIdentityScope(type);

        studentDaoConfig = daoConfigMap.get(StudentDao.class).clone();
        studentDaoConfig.initIdentityScope(type);

        teacherDaoConfig = daoConfigMap.get(TeacherDao.class).clone();
        teacherDaoConfig.initIdentityScope(type);

        courseDao = new CourseDao(courseDaoConfig, this);
        joinCoursesWithStudentsDao = new JoinCoursesWithStudentsDao(joinCoursesWithStudentsDaoConfig, this);
        joinCoursesWithTeachersDao = new JoinCoursesWithTeachersDao(joinCoursesWithTeachersDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        notificationDao = new NotificationDao(notificationDaoConfig, this);
        studentDao = new StudentDao(studentDaoConfig, this);
        teacherDao = new TeacherDao(teacherDaoConfig, this);

        registerDao(Course.class, courseDao);
        registerDao(JoinCoursesWithStudents.class, joinCoursesWithStudentsDao);
        registerDao(JoinCoursesWithTeachers.class, joinCoursesWithTeachersDao);
        registerDao(Message.class, messageDao);
        registerDao(Notification.class, notificationDao);
        registerDao(Student.class, studentDao);
        registerDao(Teacher.class, teacherDao);
    }
    
    public void clear() {
        courseDaoConfig.clearIdentityScope();
        joinCoursesWithStudentsDaoConfig.clearIdentityScope();
        joinCoursesWithTeachersDaoConfig.clearIdentityScope();
        messageDaoConfig.clearIdentityScope();
        notificationDaoConfig.clearIdentityScope();
        studentDaoConfig.clearIdentityScope();
        teacherDaoConfig.clearIdentityScope();
    }

    public CourseDao getCourseDao() {
        return courseDao;
    }

    public JoinCoursesWithStudentsDao getJoinCoursesWithStudentsDao() {
        return joinCoursesWithStudentsDao;
    }

    public JoinCoursesWithTeachersDao getJoinCoursesWithTeachersDao() {
        return joinCoursesWithTeachersDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public NotificationDao getNotificationDao() {
        return notificationDao;
    }

    public StudentDao getStudentDao() {
        return studentDao;
    }

    public TeacherDao getTeacherDao() {
        return teacherDao;
    }

}
