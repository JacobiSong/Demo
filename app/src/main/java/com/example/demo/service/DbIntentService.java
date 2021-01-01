package com.example.demo.service;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import com.example.demo.MyApplication;
import com.example.demo.dao.CourseDao;
import com.example.demo.dao.JoinCoursesWithStudentsDao;
import com.example.demo.dao.JoinCoursesWithTeachersDao;
import com.example.demo.dao.MessageDao;
import com.example.demo.dao.NotificationDao;
import com.example.demo.dao.StudentDao;
import com.example.demo.entity.Course;
import com.example.demo.entity.JoinCoursesWithStudents;
import com.example.demo.entity.JoinCoursesWithTeachers;
import com.example.demo.entity.Message;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.Join;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class DbIntentService extends IntentService {

    private static final String FIND_ALL_COURSES = "com.example.demo.action.find_all_courses";
    private static final String FIND_ALL_COURSE_GROUPS = "com.example.demo.action.find_all_groups";
    private static final String FIND_COURSES_BY_NAME = "com.example.demo.action.find_courses_by_name";
    private static final String FIND_ALL_NOTIFICATIONS = "com.example.demo.action.find_all_notifications";
    private static final String FIND_NOTIFICATIONS_BY_COURSE = "com.example.demo.action.find_notifications_by_courses";
    private static final String FIND_MESSAGES_BY_COURSE = "com.example.demo.action.find_messages_by_courses";
    private static final String FIND_STUDENTS_BY_COURSE = "com.example.demo.action.find_students_by_course";
    private static final String FIND_TEACHERS_BY_COURSE = "com.example.demo.action.find_teachers_by_course";

    private static final String INSERT_GROUP = "com.example.demo.action.insert_group";
    private static final String INSERT_MESSAGE = "com.example.demo.action.insert_message";
    private static final String INSERT_NOTIFICATION = "com.example.demo.action.insert_notification";

    private static final String EXTRA_MESSAGE = "com.example.demo.extra.MESSAGE";
    private static final String EXTRA_COURSE_ID = "com.example.demo.extra.COURSE_ID";
    private static final String EXTRA_COURSE_NAME = "com.example.demo.extra.COURSE_NAME";

    private final Query findAllCoursesQuery = ((MyApplication)getApplication()).getDaoSession().getCourseDao().queryBuilder().build();
    private final Query findAllCoursesGroupsQuery = ((MyApplication)getApplication()).getDaoSession().getCourseDao().queryBuilder().where(CourseDao.Properties.HasGroup.eq(true)).build();
    private final Query findAllNotificationsQuery = ((MyApplication)getApplication()).getDaoSession().getNotificationDao().queryBuilder().orderDesc(NotificationDao.Properties.Time).build();

    public DbIntentService() {
        super("DbIntentService");
    }

    public static void findAllCourses(Context context) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_ALL_COURSES);
        context.startService(intent);
    }

    public static void findAllCoursesGroups(Context context) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_ALL_COURSE_GROUPS);
        context.startService(intent);
    }

    public static void findCoursesByName(Context context, String courseName) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_COURSES_BY_NAME);
        intent.putExtra(EXTRA_COURSE_NAME, courseName);
        context.startService(intent);
    }

    public static void findAllNotifications(Context context) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_ALL_NOTIFICATIONS);
        context.startService(intent);
    }

    public static void findNotificationsByCourse(Context context, String courseId) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_NOTIFICATIONS_BY_COURSE);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        context.startService(intent);
    }

    public static void findMessagesByCourse(Context context, String courseId) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_MESSAGES_BY_COURSE);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        context.startService(intent);
    }

    public static void findStudentsByCourse(Context context, String courseId) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_STUDENTS_BY_COURSE);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        context.startService(intent);
    }

    public static void findTeachersByCourse(Context context, String courseId) {
        Intent intent = new Intent(context, DbIntentService.class);
        intent.setAction(FIND_TEACHERS_BY_COURSE);
        intent.putExtra(EXTRA_COURSE_ID, courseId);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case FIND_ALL_COURSES:
                    handleFindAllCourses();
                    break;
                case FIND_ALL_COURSE_GROUPS:
                    handleFindAllCourseGroups();
                    break;
                case FIND_COURSES_BY_NAME:
                    final String courseName = intent.getStringExtra(EXTRA_COURSE_NAME);
                    handleFindCoursesByName(courseName);
                    break;
                case FIND_ALL_NOTIFICATIONS:
                    handleFindAllNotifications();
                    break;
                case FIND_NOTIFICATIONS_BY_COURSE: {
                    final String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
                    handleFindNotificationsByCourse(courseId);
                    break;
                }
                case FIND_MESSAGES_BY_COURSE: {
                    final String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
                    handleFindMessagesByCourse(courseId);
                    break;
                }
                case FIND_STUDENTS_BY_COURSE: {
                    final String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
                    handleFindStudentsByCourse(courseId);
                    break;
                }
                case FIND_TEACHERS_BY_COURSE: {
                    final String courseId = intent.getStringExtra(EXTRA_COURSE_ID);
                    handleFindTeachersByCourse(courseId);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void handleFindAllCourses() {
        List<Course> list = findAllCoursesQuery.forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindAllCourseGroups() {
        List<Course> list = findAllCoursesGroupsQuery.forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindCoursesByName(String courseName) {
        if (courseName == null) {
            Log.w("DbIntentService", "call findCoursesByName, courseName is null.");
            return;
        }
        List<Course> list = ((MyApplication)getApplication()).getDaoSession().getCourseDao().queryBuilder().where(CourseDao.Properties.Name.like("%" + courseName + "%")).build().forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindAllNotifications() {
        List<Notification> list = findAllNotificationsQuery.forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindNotificationsByCourse(String courseId) {
        if (courseId == null) {
            Log.w("DbIntentService", "call findNotificationByCourse, courseId is null");
            return;
        }
        List<Notification> list = ((MyApplication)getApplication()).getDaoSession().getNotificationDao().queryBuilder().where(NotificationDao.Properties.ReceiverId.eq(courseId)).orderDesc(NotificationDao.Properties.Time).build().forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindMessagesByCourse(String courseId) {
        if (courseId == null) {
            Log.w("DbIntentService", "call findMessagesByCourse, courseId is null");
            return;
        }
        List<Message> list = ((MyApplication)getApplication()).getDaoSession().getMessageDao().queryBuilder().where(MessageDao.Properties.ReceiverId.eq(courseId)).orderDesc(MessageDao.Properties.Time).build().forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindStudentsByCourse(String courseId) {
        if (courseId == null) {
            Log.w("DbIntentService", "call findStudentsByCourse, courseId is null");
            return;
        }
        QueryBuilder<Student> queryBuilder = ((MyApplication)getApplication()).getDaoSession().getStudentDao().queryBuilder();
        Join join = queryBuilder.join(JoinCoursesWithStudents.class, JoinCoursesWithStudentsDao.Properties.UserId);
        Join course = queryBuilder.join(join, JoinCoursesWithStudentsDao.Properties.CourseId, Course.class, CourseDao.Properties.Id);
        course.where(CourseDao.Properties.Id.eq(courseId));
        List<Student> list = queryBuilder.build().forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }

    private void handleFindTeachersByCourse(String courseId) {
        if (courseId == null) {
            Log.w("DbIntentService", "call findTeachersByCourse, courseId is null");
            return;
        }
        QueryBuilder<Teacher> queryBuilder = ((MyApplication)getApplication()).getDaoSession().getTeacherDao().queryBuilder();
        Join join = queryBuilder.join(JoinCoursesWithTeachers.class, JoinCoursesWithTeachersDao.Properties.UserId);
        Join course = queryBuilder.join(join, JoinCoursesWithTeachersDao.Properties.CourseId, Course.class, CourseDao.Properties.Id);
        course.where(CourseDao.Properties.Id.eq(courseId));
        List<Teacher> list = queryBuilder.build().forCurrentThread().list();
        EventBus.getDefault().post(new EventBusMessage(list));
    }
}