/**
 * Implementación del TAD AIFA como una interfaz.
 * Contiene las operaciones de agregar, eliminar mostrar materias de un 
 * estudiante mostrar estudiantes de una materia, la representación en 
 * String del árbol y un iterador del árbol de información académica.
 */
interface AIFA : Iterable<InfoAcad> {

    /** 
     * Agrega una nueva materia dado el carné de un estudiante y su calificación.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante
     * @param[m] Arreglo de Strings que representa una materia
     * @param[e] Entero que representa la calificación de una materia
     */
    fun agregar(e: String, m: String, c: Int)

    /** 
     * Elimina la información de una materia cursada por un estudiante.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante
     * @param[m] Arreglo de Strings que representa una materia
     */
    fun eliminar(e: String, m: String)

    /** 
     * Imprime por la salida estándar los materias que ha cursado 
     * un estudiante con sus respectivas calificaciones, ordenadas 
     * por materias.
     *
     * @param[e] Arreglo de Strings que representa el carnet del estudiante
     */
    fun mostrarMateriasDeUnEstudiante(e: String)

    /** 
     * Muestra por la salida estándar a todos los estudiantes que han cursado 
     * una materia, junto con su calificación, ordenados por el carné del estudiante. 
     *
     * @param[m] Arreglo de Strings que representa una materia
     */
    fun mostrarEstudiantesDeUnaMateria(m: String)

    /** Retorna un Iterador del Administrador de Información Académica. */
    override fun iterator(): Iterator<InfoAcad>

    /** Retorna la representación en String del Administrador de Información Académica.*/
    override fun toString(): String
}