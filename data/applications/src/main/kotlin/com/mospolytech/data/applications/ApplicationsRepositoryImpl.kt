package com.mospolytech.data.applications

import com.mospolytech.domain.services.applications.Application
import com.mospolytech.domain.services.applications.ApplicationsRepository
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class ApplicationsRepositoryImpl : ApplicationsRepository {
    override fun getApplications(): List<Application> {
        return listOf(
            Application(
                LocalDateTime.now().toKotlinLocalDateTime(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now().toKotlinLocalDateTime(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
            Application(
                LocalDateTime.now().toKotlinLocalDateTime(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now().toKotlinLocalDateTime(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
            Application(
                LocalDateTime.now().toKotlinLocalDateTime(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now().toKotlinLocalDateTime(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
        )
    }
}
