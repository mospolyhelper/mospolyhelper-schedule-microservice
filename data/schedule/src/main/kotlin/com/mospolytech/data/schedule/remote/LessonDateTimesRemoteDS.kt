package com.mospolytech.data.schedule.remote

import com.mospolytech.data.base.insertIfNotExist
import com.mospolytech.data.common.db.MosPolyDb
import com.mospolytech.data.schedule.model.db.LessonDateTimesDb
import com.mospolytech.data.schedule.model.entity.LessonDateTimeEntity
import com.mospolytech.domain.schedule.model.lesson.LessonDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import java.util.*

class LessonDateTimesRemoteDS {
    suspend fun add(lessonDateTimes: List<LessonDateTime>): List<String> {
        return MosPolyDb.transaction {
            lessonDateTimes.map { lessonDateTime ->
                LessonDateTimeEntity.insertIfNotExist(
                    op = {
                        LessonDateTimesDb.startDate eq lessonDateTime.startDate and
                            (LessonDateTimesDb.endDate eq lessonDateTime.endDate) and
                            (LessonDateTimesDb.startTime eq lessonDateTime.time.start) and
                            (LessonDateTimesDb.endTime eq lessonDateTime.time.end)
                    }
                ) {
                    startDate = lessonDateTime.startDate
                    endDate = lessonDateTime.endDate
                    startTime = lessonDateTime.time.start
                    endTime = lessonDateTime.time.end
                }.id.value.toString()
            }
        }
    }

    suspend fun add(lessonDateTime: LessonDateTime): String {
        return MosPolyDb.transaction {
            LessonDateTimeEntity.insertIfNotExist(
                op = {
                    LessonDateTimesDb.startDate eq lessonDateTime.startDate and
                        (LessonDateTimesDb.endDate eq lessonDateTime.endDate) and
                        (LessonDateTimesDb.startTime eq lessonDateTime.time.start) and
                        (LessonDateTimesDb.endTime eq lessonDateTime.time.end)
                }
            ) {
                startDate = lessonDateTime.startDate
                endDate = lessonDateTime.endDate
                startTime = lessonDateTime.time.start
                endTime = lessonDateTime.time.end
            }.id.value.toString()
        }
    }
}
