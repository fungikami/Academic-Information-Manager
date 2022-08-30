/**
 * Función sorpresa implementada por el profesor de la materia.
 *
 * @param[its] Iterador de tipo InfoAcad del AIFA.
 */
fun sorpresita(its: Iterator<InfoAcad>) {
    var orden = true
    var n = 0

    if (its.hasNext()) {
        var anterior: InfoAcad = its.next()
        println(anterior.toString())
        n++
        while (its.hasNext()) {
            var actual = its.next()
            if (anterior > actual) orden = false
            println(actual.toString())
            anterior = actual
            n++
        }
    }

    println("Los datos del iterador se encuentran en orden: ${orden}. Número de datos: $n.")
}