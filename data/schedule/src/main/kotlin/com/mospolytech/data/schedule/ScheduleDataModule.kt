package com.mospolytech.data.schedule

import com.mospolytech.data.schedule.converters.*
import com.mospolytech.data.schedule.converters.dateTime.LessonDateTimeConverter
import com.mospolytech.data.schedule.converters.groups.LessonGroupsConverter
import com.mospolytech.data.schedule.converters.lessons.LessonConverter
import com.mospolytech.data.schedule.converters.places.LessonPlacesConverter
import com.mospolytech.data.schedule.converters.subjects.LessonSubjectConverter
import com.mospolytech.data.schedule.converters.teachers.LessonTeachersConverter
import com.mospolytech.data.schedule.converters.types.LessonTypeConverter
import com.mospolytech.data.schedule.repository.*
import com.mospolytech.data.schedule.service.ScheduleService
import com.mospolytech.domain.schedule.repository.FreePlacesRepository
import com.mospolytech.domain.schedule.repository.GroupsRepository
import com.mospolytech.domain.schedule.repository.LessonSubjectsRepository
import com.mospolytech.domain.schedule.repository.LessonsRepository
import com.mospolytech.domain.schedule.repository.PlacesRepository
import com.mospolytech.domain.schedule.repository.ScheduleInfoRepository
import com.mospolytech.domain.schedule.repository.ScheduleRepository
import com.mospolytech.domain.schedule.repository.TeachersRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val scheduleDataModule =
    module {
        singleOf(::ScheduleService)

        singleOf(::ApiScheduleConverter)
        singleOf(::LessonSubjectConverter)
        singleOf(::LessonTypeConverter)
        singleOf(::LessonTeachersConverter)
        singleOf(::LessonGroupsConverter)
        singleOf(::LessonPlacesConverter)
        singleOf(::LessonDateTimeConverter)
        singleOf(::LessonConverter)
        singleOf(::LessonsRepositoryImpl) { bind<LessonsRepository>() }
        singleOf(::ScheduleRepositoryImpl) { bind<ScheduleRepository>() }
        singleOf(::ScheduleInfoRepositoryImpl) { bind<ScheduleInfoRepository>() }
        singleOf(::FreePlacesRepositoryImpl) { bind<FreePlacesRepository>() }

        singleOf(::LessonSubjectsRepositoryImpl) { bind<LessonSubjectsRepository>() }
        singleOf(::TeachersRepositoryImpl) { bind<TeachersRepository>() }
        singleOf(::GroupsRepositoryImpl) { bind<GroupsRepository>() }
        singleOf(::PlacesRepositoryImpl) { bind<PlacesRepository>() }
    }
