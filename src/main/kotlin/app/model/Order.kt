package app.model

import support.OrderSession

data class Order(val orderSession: OrderSession, val customerGroup: CustomerGroup)