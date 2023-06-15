package app.mybad.data.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.mybad.data.db.entity.CourseDataModel
import app.mybad.data.db.dao.MedDao
import app.mybad.data.db.MedDbImpl
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Instant
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    sdk = [26]
)
class CoursesRepoImplTest {

    @get:Rule var hiltRule = HiltAndroidRule(this)

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var db: MedDbImpl
    lateinit var userDao: MedDao
    private val now = Instant.now().epochSecond
    private val testCoursesData = listOf(
        CourseDataModel(
            id = 1L,
            userId = 0L,
            creationDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            medId = 1L,
            updateDate = now,
            interval = 86400 * 90
        ),
        CourseDataModel(
            id = 2L,
            userId = 0L,
            creationDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            medId = 2L,
            updateDate = now,
            interval = 86400 * 90
        ),
        CourseDataModel(
            id = 3L,
            userId = 0L,
            creationDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            medId = 3L,
            updateDate = now,
            interval = 86400 * 90
        ),
    )

    @Before
    fun setup() {
        hiltRule.inject()
        userDao = db.dao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAll_before_inserts_s_b_empty() {
        runTest {
            val r = userDao.getAllCourses()
            Assert.assertEquals(emptyList<CourseDataModel>(), r)
        }
    }

    @Test fun getAll_with_inserts() {
        runTest {
            userDao.addCourse(testCoursesData[0])
            userDao.addCourse(testCoursesData[1])
            userDao.addCourse(testCoursesData[2])
            val r = userDao.getAllCourses()
            Assert.assertEquals(testCoursesData, r)
        }
    }

    @Test
    fun getAllFlow_before_inserts_s_b_empty() {
        val r = userDao.getAllCoursesFlow()
        runTest {
            Assert.assertEquals(emptyList<CourseDataModel>(), r.first())
        }
    }

    @Test
    fun getAllFlow_with_inserts() {
        userDao.addCourse(testCoursesData[0])
        userDao.addCourse(testCoursesData[1])
        userDao.addCourse(testCoursesData[2])
        val r = userDao.getAllCoursesFlow()
        runTest {
            Assert.assertEquals(testCoursesData, r.first())
        }
    }

    @Test
    fun getSingle_before_inserts_s_b_empty() {
        val r = userDao.getCourseById(1L)
        Assert.assertEquals(null, r)
    }

    @Test
    fun getSingle_after_insert() {
        userDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], userDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun updateSingle_before_inserts_s_d_nothing() {
        val r = userDao.getCourseById(testCoursesData[0].id)
        Assert.assertEquals(null, r)
        userDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], userDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun add() {
        userDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], userDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun deleteSingle() {
        userDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], userDao.getCourseById(testCoursesData[0].id))
        userDao.deleteCourse(testCoursesData[0].id)
        Assert.assertEquals(null, userDao.getCourseById(testCoursesData[0].id))
    }
}
