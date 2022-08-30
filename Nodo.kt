/**
 * Implementación del nodo de un árbol binario de búsquedad, con 
 * referencia al hijo izquierdo, al hijo derecho y al padre del nodo.
 *
 * @property valor contiene la información académica de tipo InfoAcad
 */
class Nodo(val valor: InfoAcad) {
    var hijoDer: Nodo? = null
    var hijoIzq: Nodo? = null
    var padre: Nodo? = null

    /** 
     * Representación en String del valor contenido en el nodo.
     */
    override fun toString(): String {
        return "Carné: ${valor.carnet} \nMateria: ${valor.materia} \nCalificación: ${valor.calificacion}\n"
    }
}