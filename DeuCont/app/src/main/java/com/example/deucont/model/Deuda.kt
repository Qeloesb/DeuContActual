package com.example.deucont.model

data class Deuda(
    val userid: String?,
    var id: String? = null,
    var nombreDeuda: String? = null,
    var valorDeuda: String? = null,
    var valorMensual: String? = null,
    var cantidadCuotas: String? = null,
    var fechaPago: String? = null,
){
    constructor() : this(null, null, null, null, null, null,null)
}
