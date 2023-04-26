package app.mybad.network.repos.impl

import app.mybad.network.api.CoursesApiRepo
import app.mybad.network.repos.repo.CoursesNetworkRepo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CoursesNetworkRepoImpl @Inject constructor(
    private val coursesApiRepo: CoursesApiRepo
) : CoursesNetworkRepo {

}