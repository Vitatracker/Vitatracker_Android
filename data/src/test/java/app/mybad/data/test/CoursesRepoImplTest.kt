package app.mybad.data.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.mybad.data.db.models.CourseModel
import app.mybad.data.db.dao.RemedyDao
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
    private lateinit var medDao: RemedyDao
    private val now = getCurrentDateTime().toEpochSecond()
    private val userId = 0L
    private val testCoursesData = listOf(
        CourseModel(
            id = 1L,
            userId = userId,
            createdDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            remedyId = 1L,
            updateDate = now,
            interval = 86400 * 90
        ),
        CourseModel(
            id = 2L,
            userId = userId,
            createdDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            remedyId = 2L,
            updateDate = now,
            interval = 86400 * 90
        ),
        CourseModel(
            id = 3L,
            userId = userId,
            createdDate = now,
            startDate = now,
            endDate = now + 86400 * 30,
            remedyId = 3L,
            updateDate = now,
            interval = 86400 * 90
        ),
    )

    @Before
    fun setup() {
        hiltRule.inject()
        medDao = db.getRemedyDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAll_before_inserts_s_b_empty() {
        runTest {
            val r = medDao.getAllCourses(userId)
            Assert.assertEquals(emptyList<CourseModel>(), r)
        }
    }

    @Test fun getAll_with_inserts() {
        runTest {
            medDao.addCourse(testCoursesData[0])
            medDao.addCourse(testCoursesData[1])
            medDao.addCourse(testCoursesData[2])
            val r = medDao.getAllCourses(userId)
            Assert.assertEquals(testCoursesData, r)
        }
    }

    @Test
    fun getAllFlow_before_inserts_s_b_empty() {
        val r = medDao.getAllCoursesFlow(userId)
        runTest {
            Assert.assertEquals(emptyList<CourseModel>(), r.first())
        }
    }

    @Test
    fun getAllFlow_with_inserts() {
        medDao.addCourse(testCoursesData[0])
        medDao.addCourse(testCoursesData[1])
        medDao.addCourse(testCoursesData[2])
        val r = medDao.getAllCoursesFlow(userId)
        runTest {
            Assert.assertEquals(testCoursesData, r.first())
        }
    }

    @Test
    fun getSingle_before_inserts_s_b_empty() {
        val r = medDao.getCourseById(1L)
        Assert.assertEquals(null, r)
    }

    @Test
    fun getSingle_after_insert() {
        medDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], medDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun updateSingle_before_inserts_s_d_nothing() {
        val r = medDao.getCourseById(testCoursesData[0].id)
        Assert.assertEquals(null, r)
        medDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], medDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun add() {
        medDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], medDao.getCourseById(testCoursesData[0].id))
    }

    @Test
    fun deleteSingle() {
        medDao.addCourse(testCoursesData[0])
        Assert.assertEquals(testCoursesData[0], medDao.getCourseById(testCoursesData[0].id))
        medDao.deleteCourse(testCoursesData[0].id)
        Assert.assertEquals(null, medDao.getCourseById(testCoursesData[0].id))
    }
}
