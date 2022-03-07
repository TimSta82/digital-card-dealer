package de.bornholdtlee.defaultprojectkotlin.data.api

import de.bornholdtlee.defaultprojectkotlin.data.model.QuestionListDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    /**
     * Beispielhafter Aufbau eines Endpoints
     */
    @GET("/2.2/questions?order=desc&sort=creation&site=stackoverflow")
    suspend fun loadQuestions(@Query("tagged") tags: String): Response<QuestionListDto>
}
