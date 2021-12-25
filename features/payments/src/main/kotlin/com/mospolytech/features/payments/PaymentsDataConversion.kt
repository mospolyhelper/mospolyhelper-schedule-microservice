package com.mospolytech.features.payments

import com.mospolytech.domain.payments.model.PaymentType
import com.mospolytech.features.base.setEnumConverter
import io.ktor.util.converters.*


fun DataConversion.Configuration.paymentsDataConversion() {
    setEnumConverter(PaymentType::values)
}

