package com.mospolytech.data.applications

import com.mospolytech.domain.applications.model.Application
import com.mospolytech.domain.applications.repository.ApplicationsRepository
import java.time.LocalDateTime

class ApplicationsRepositoryImpl() : ApplicationsRepository {

    override fun getApplications(): List<Application> {
        return listOf(
            Application(
                LocalDateTime.now(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
            Application(
                LocalDateTime.now(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
            Application(
                LocalDateTime.now(),
                "MR24092112553",
                "Заявка на материальную помощь",
                "Готово",
                LocalDateTime.now(),
                "Профсоюзная организация работников и обучающихся\n" +
                    "107023, г. Москва, ул. Б. Семеновская, д. 38, аудитория В-202. Тел. 495 223-05-31",
                "Ваше заявление получено. Необходимо подойти в ауд. В-202 для подписания заявления с копией паспорта стр 2-5",
            ),
        )
    }
}
