/**
 * Tipo de dato que representa la información académica.
 *
 * @constructor Crea un nuevo tipo InfoAcad que contiene los valores del
 * carnet, materia y calificación de un estudiante.
 */
class InfoAcad(val carnet: String, val materia: String, val calificacion: Int) : Comparable<InfoAcad> {
    /** 
     * Representación en String de la tripleta (carne, materia, calificación).
     */
    override fun toString(): String = "Carné: $carnet \nMateria: $materia \nCalificación: $calificacion\n"

    /** 
     * Compara dos elementos de tipo InfoAcad. Compara primero por el carne
     * Si ambos carne son iguales, se compara según la materia
     *
     * @return zero, si ambos son iguales, un entero negativo si el 
     * objeto [other] es mayor, en cambio retorna un entero positivo.
     */
    override fun compareTo(other: InfoAcad): Int {
        val thisCarne = this.carnet
        val otherCarne = other.carnet

        if (thisCarne > otherCarne) {
            return 1 
        } else if (thisCarne < otherCarne) {
            return -1
        } else {
            val thisMat = this.materia
            val otherMat = other.materia

            if (thisMat == otherMat) {
                return 0
            } else if (thisMat > otherMat) {
                return 1
            } else {
                return -1
            }
        }
    }

    /** 
     * Verifica si dos elementos de tipo InfoAcad son iguales. 
     *
     * @return true si la materia y el carne de ambos elementos coinciden.
     */
    override fun equals(other: Any?): Boolean {
        if ((other == null) || (other !is InfoAcad) || 
            (this.carnet != other.carnet) || 
            (this.materia != other.materia)) return false
        return true
    }
}